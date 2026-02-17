package com.concordia.robot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RobotControllerTest {
    
    private RobotController controller;
    
    @BeforeEach
    public void setUp() {
        controller = new RobotController(10);
    }
    
    @Test
    public void testInitialization() {
        assertNotNull(controller.getRobot());
        assertNotNull(controller.getFloor());
        assertEquals(10, controller.getFloor().getSize());
    }
    
    @Test
    public void testPenUpCommand() {
        assertTrue(controller.processCommand("U"));
        assertTrue(controller.processCommand("u"));
    }
    
    @Test
    public void testPenDownCommand() {
        assertTrue(controller.processCommand("D"));
        assertTrue(controller.processCommand("d"));
        assertTrue(controller.getRobot().isPenDown());
    }
    
    @Test
    public void testTurnRightCommand() {
        assertTrue(controller.processCommand("R"));
        assertEquals(Direction.EAST, controller.getRobot().getDirection());
    }
    
    @Test
    public void testTurnLeftCommand() {
        assertTrue(controller.processCommand("L"));
        assertEquals(Direction.WEST, controller.getRobot().getDirection());
    }
    
    @Test
    public void testMoveCommand() {
        assertTrue(controller.processCommand("M 5"));
        assertEquals(5, controller.getRobot().getY());
        assertTrue(controller.processCommand("m 3"));
        assertEquals(8, controller.getRobot().getY());
    }
    
    @Test
    public void testInitializeCommand() {
        assertTrue(controller.processCommand("I 15"));
        assertEquals(15, controller.getFloor().getSize());
        assertTrue(controller.processCommand("i 20"));
        assertEquals(20, controller.getFloor().getSize());
    }
    
    @Test
    public void testInvalidCommands() {
        assertFalse(controller.processCommand("X"));
        assertFalse(controller.processCommand(""));
        assertFalse(controller.processCommand(null));
        assertFalse(controller.processCommand("M"));
        assertFalse(controller.processCommand("M abc"));
    }
    
    @Test
    public void testCommandHistory() {
        controller.processCommand("D");
        controller.processCommand("M 5");
        controller.processCommand("R");
        assertEquals(3, controller.getCommandHistory().size());
    }
    
    @Test
    public void testQuitNotInHistory() {
        controller.processCommand("D");
        controller.processCommand("Q");
        assertEquals(1, controller.getCommandHistory().size());
    }
    
    @Test
    public void testReplayNotInHistory() {
        controller.processCommand("D");
        controller.processCommand("H");
        assertEquals(1, controller.getCommandHistory().size());
    }
    
    @Test
    public void testNegativeMoveCommand() {
        assertFalse(controller.processCommand("M -5"));
    }
    
    @Test
    public void testInvalidInitializeCommand() {
        assertFalse(controller.processCommand("I 0"));
        assertFalse(controller.processCommand("I -10"));
    }
    
    @Test
    public void testCaseInsensitiveCommands() {
        assertTrue(controller.processCommand("d"));
        assertTrue(controller.processCommand("D"));
        assertTrue(controller.processCommand("m 3"));
        assertTrue(controller.processCommand("M 3"));
    }
}