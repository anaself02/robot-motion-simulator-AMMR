package com.concordia.robot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QC_test {

	private RobotController controller;

	@BeforeEach
	void setUp() {
		controller = new RobotController(10);
	}

	@Test
	void testDirectionToStringContract() {
		assertEquals("north", Direction.NORTH.toString());
		assertEquals("east", Direction.EAST.toString());
		assertEquals("south", Direction.SOUTH.toString());
		assertEquals("west", Direction.WEST.toString());
	}


	@Test
	void testDirectionTurnMappings() {
		assertEquals(Direction.EAST, Direction.NORTH.turnRight());
		assertEquals(Direction.SOUTH, Direction.EAST.turnRight());
		assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
		assertEquals(Direction.NORTH, Direction.EAST.turnLeft());
	}

	@Test
	void testFloorInitializationAndClear() {
		Floor floor = new Floor(5);
		floor.mark(0, 0);
		floor.mark(4, 4);
		assertEquals(1, floor.getCell(0, 0));
		assertEquals(1, floor.getCell(4, 4));
		floor.clear();
		assertEquals(0, floor.getCell(0, 0));
		assertEquals(0, floor.getCell(4, 4));
	}

	@Test
	void testFloorPositionValidation() {
		Floor floor = new Floor(5);
		assertTrue(floor.isValidPosition(0, 0));
		assertTrue(floor.isValidPosition(4, 4));
		assertFalse(floor.isValidPosition(-1, 0));
		assertFalse(floor.isValidPosition(5, 0));
	}

	@Test
	void testRobotPenDownMarksCurrentCell() {
		Floor floor = new Floor(6);
		Robot robot = new Robot(floor);
		robot.penDown();
		assertEquals(1, floor.getCell(0, 0));
	}

	@Test
	void testRobotMovementByDirection() {
		Floor floor = new Floor(10);
		Robot robot = new Robot(floor);
		assertTrue(robot.move(3));
		assertEquals(0, robot.getX());
		assertEquals(3, robot.getY());

		robot.turnRight();
		assertTrue(robot.move(2));
		assertEquals(2, robot.getX());
		assertEquals(3, robot.getY());
	}

	@ParameterizedTest(name = "Invalid command should fail: {0}")
	@CsvSource({
		"X,false",
		"M abc,false",
		"M -2,false",
		"M 99,false",
		"I 0,false"
	})
	void testInvalidAndBoundaryCommandsAreRejected(String command, boolean expectedResult) {
		boolean result = controller.processCommand(command);
		assertEquals(expectedResult, result);
		assertFalse(result);
	}

	@ParameterizedTest(name = "Console output check for command: {0}")
	@CsvSource({
		"M abc,Invalid number format",
		"M -2,non-negative",
		"M 99,outside floor boundaries",
		"I 0,greater than 0"
	})
	void testErrorMessagesForInvalidInputs(String command, String expectedMessageFragment) {
		String output = captureOutput(() -> assertFalse(controller.processCommand(command)));
		assertTrue(output.contains(expectedMessageFragment));
	}

	@Test
	void testCommandCombinationFlow() {
		List<String> commands = List.of("I 8", "D", "M 2", "R", "M 2", "L", "U", "M 1", "P", "C", "H", "Q");
		String output = captureOutput(() -> {
			for (String command : commands) {
				assertTrue(controller.processCommand(command));
			}
		});

		assertTrue(output.contains("System initialized with 8x8 floor"));
		assertTrue(output.contains("Replaying command history"));
		assertTrue(output.contains("Replay complete"));
	}

	@Test
	void testInvalidCommandGuidanceMessageIsAccurate() {
		String output = captureMainOutput("X\nQ\n");

		assertFalse(
			output.contains("Type 'H' to see available commands."),
			"Expected guidance should not point to H for available commands."
		);
	}

	@Test
	void testNegativeMoveShowsSingleUserFacingError() {
		String output = captureMainOutput("M -2\nQ\n");

		assertEquals(
			1,
			countOccurrences(output, "Error:"),
			"Expected one consolidated error line for a single invalid action."
		);
	}

	@ParameterizedTest(name = "Out-of-bounds/invalid move should have one user error line: {0}")
	@CsvSource({
		"M -2,1",
		"M 99,1"
	})
	void testInvalidMoveErrorLineConsistency(String command, int expectedErrorLines) {
		String output = captureMainOutput(command + "\nQ\n");
		assertEquals(expectedErrorLines, countOccurrences(output, "Error:"));
	}

	@Test
	void testReplayIntegrityScenario() {
		executeScenario(List.of("D", "M 2", "R", "H"));
		String statusOutput = captureOutput(() -> assertTrue(controller.processCommand("C")));
		assertTrue(statusOutput.contains("Position: 0, 2"));
		assertTrue(statusOutput.contains("Pen: down"));
		assertTrue(statusOutput.contains("Facing: east"));
	}

	@Test
	void testCommandHistoryContract() {
		controller.processCommand("D");
		controller.processCommand("M 2");
		controller.processCommand("Q");
		controller.processCommand("H");
		controller.processCommand("R");
		assertEquals(3, controller.getCommandHistory().size());
	}

	@Test
	void testCommandParserRobustness() {
		assertTrue(controller.processCommand("M    3"));
		assertTrue(controller.processCommand("   D   "));
		assertTrue(controller.processCommand("M\t2"));
		assertFalse(controller.processCommand("M 3 4"));
		assertFalse(controller.processCommand("M"));
		assertFalse(controller.processCommand("ZZ 1"));
	}

	@ParameterizedTest(name = "Initialize boundary size {0} should return {1}")
	@CsvSource({
		"1,true",
		"2,true",
		"50,true",
		"0,false",
		"-1,false"
	})
	void testInitializeBoundarySizes(int size, boolean expectedResult) {
		boolean result = controller.processCommand("I " + size);
		assertEquals(expectedResult, result);

		if (expectedResult) {
			String output = captureOutput(() -> assertTrue(controller.processCommand("C")));
			assertTrue(output.contains("Position: 0, 0"));
			assertTrue(output.contains("Pen: up"));
			assertTrue(output.contains("Facing: north"));
		}
	}

	@Test
	void testFloorPrintContract() {
		executeScenario(List.of("D", "M 3", "R", "M 2"));
		String output = captureOutput(() -> assertTrue(controller.processCommand("P")));

		assertTrue(output.contains("0"), "Expected column header to include 0");
		assertTrue(output.contains("*"), "Expected printed floor to include drawn marks");
		int printedRows = countGridRows(output);
		assertTrue(printedRows >= 10, "Expected at least one printed row per floor row");
	}

	@Test
	void testStateInvariantsAfterSequence() {
		executeScenario(List.of("D", "M 3", "R", "M 2"));
		String statusOutput = captureOutput(() -> assertTrue(controller.processCommand("C")));

		int[] position = parsePosition(statusOutput);
		assertTrue(position[0] >= 0 && position[0] < 10);
		assertTrue(position[1] >= 0 && position[1] < 10);

		String facing = parseFacing(statusOutput);
		assertTrue(List.of("north", "east", "south", "west").contains(facing));
	}

	@Test
	void testLongCommandJourney() {
		String output = captureOutput(() -> executeScenario(
			List.of("I 10", "D", "M 3", "R", "M 2", "L", "U", "M 1", "D", "M 2", "P", "C", "H", "Q")));
		assertTrue(output.contains("System initialized with 10x10 floor"));
		assertTrue(output.contains("Replaying command history"));
		assertTrue(output.contains("Replay complete"));

		String status = captureOutput(() -> assertTrue(controller.processCommand("C")));
		assertTrue(status.contains("Position:"));
		assertTrue(status.contains("Facing:"));
	}

	@Test
	void testRandomizedCommandSmokeInvariants() {
		Random random = new Random(448);
		List<String> commandPool = List.of("U", "D", "R", "L", "M 1", "M 2", "M 5", "P", "C", "H", "I 10");

		String output = captureOutput(() -> {
			for (int i = 0; i < 120; i++) {
				String command = commandPool.get(random.nextInt(commandPool.size()));
				controller.processCommand(command);
			}
		});

		assertNotNull(output);
		String statusOutput = captureOutput(() -> assertTrue(controller.processCommand("C")));
		int[] position = parsePosition(statusOutput);
		assertTrue(position[0] >= 0 && position[0] < controller.getFloor().getSize());
		assertTrue(position[1] >= 0 && position[1] < controller.getFloor().getSize());
	}

	private void executeScenario(List<String> commands) {
		for (String command : commands) {
			assertTrue(controller.processCommand(command), "Expected command to succeed: " + command);
		}
	}


	private String captureOutput(Runnable action) {
		PrintStream originalOut = System.out;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		try {
			action.run();
		} finally {
			System.setOut(originalOut);
		}
		return outputStream.toString();
	}

	private String captureMainOutput(String simulatedInput) {
		PrintStream originalOut = System.out;
		java.io.InputStream originalIn = System.in;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outputStream));
		System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));
		try {
			Main.main(new String[0]);
		} finally {
			System.setOut(originalOut);
			System.setIn(originalIn);
		}
		return outputStream.toString();
	}

	private int countOccurrences(String source, String token) {
		int count = 0;
		int index = 0;
		while ((index = source.indexOf(token, index)) != -1) {
			count++;
			index += token.length();
		}
		return count;
	}

	private int[] parsePosition(String statusOutput) {
		Pattern pattern = Pattern.compile("Position:\\s*(\\d+),\\s*(\\d+)");
		Matcher matcher = pattern.matcher(statusOutput);
		assertTrue(matcher.find(), "Expected a parseable position in status output");
		return new int[] {
			Integer.parseInt(matcher.group(1)),
			Integer.parseInt(matcher.group(2))
		};
	}

	private String parseFacing(String statusOutput) {
		Pattern pattern = Pattern.compile("Facing:\\s*(north|east|south|west)");
		Matcher matcher = pattern.matcher(statusOutput);
		assertTrue(matcher.find(), "Expected a parseable facing value in status output");
		return matcher.group(1);
	}

	private int countGridRows(String printOutput) {
		int rows = 0;
		for (String line : printOutput.split("\\R")) {
			if (line.matches("^\\d+.*")) {
				rows++;
			}
		}
		return rows;
	}
}
