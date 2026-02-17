package com.concordia.robot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RobotTest {
    
    private Floor floor;
    private Robot robot;
    
    @BeforeEach
    public void setUp() {
        floor = new Floor(10);
        robot = new Robot(floor);
    }
    
    @Test
    public void testInitialState() {
        assertEquals(0, robot.getX());
        assertEquals(0, robot.getY());
        assertEquals(Direction.NORTH, robot.getDirection());
        assertFalse(robot.isPenDown());
    }
    
    @Test
    public void testPenUp() {
        robot.penDown();
        robot.penUp();
        assertFalse(robot.isPenDown());
    }
    
    @Test
    public void testPenDown() {
        robot.penDown();
        assertTrue(robot.isPenDown());
    }
    
    @Test
    public void testTurnRight() {
        robot.turnRight();
        assertEquals(Direction.EAST, robot.getDirection());
        robot.turnRight();
        assertEquals(Direction.SOUTH, robot.getDirection());
    }
    
    @Test
    public void testTurnLeft() {
        robot.turnLeft();
        assertEquals(Direction.WEST, robot.getDirection());
        robot.turnLeft();
        assertEquals(Direction.SOUTH, robot.getDirection());
    }
    
    @Test
    public void testMoveNorth() {
        boolean success = robot.move(5);
        assertTrue(success);
        assertEquals(0, robot.getX());
        assertEquals(5, robot.getY());
    }
    
    @Test
    public void testMoveSouth() {
        robot.move(5);
        robot.turnRight();
        robot.turnRight();
        boolean success = robot.move(3);
        assertTrue(success);
        assertEquals(0, robot.getX());
        assertEquals(2, robot.getY());
    }
    
    @Test
    public void testMoveEast() {
        robot.turnRight();
        boolean success = robot.move(7);
        assertTrue(success);
        assertEquals(7, robot.getX());
        assertEquals(0, robot.getY());
    }
    
    @Test
    public void testMoveWest() {
        robot.turnRight();
        robot.move(5);
        robot.turnRight();
        robot.turnRight();
        boolean success = robot.move(3);
        assertTrue(success);
        assertEquals(2, robot.getX());
        assertEquals(0, robot.getY());
    }
    
    @Test
    public void testMoveOutOfBounds() {
        boolean success = robot.move(15);
        assertFalse(success);
    }
    
    @Test
    public void testMoveNegativeSpaces() {
        boolean success = robot.move(-5);
        assertFalse(success);
    }
    
    @Test
    public void testDrawWithPenDown() {
        robot.penDown();
        robot.move(3);
        assertEquals(1, floor.getCell(0, 0));
        assertEquals(1, floor.getCell(0, 1));
        assertEquals(1, floor.getCell(0, 2));
        assertEquals(1, floor.getCell(0, 3));
    }
    
    @Test
    public void testNoDrawWithPenUp() {
        robot.move(3);
        assertEquals(0, floor.getCell(0, 1));
        assertEquals(0, floor.getCell(0, 2));
    }
    
    @Test
    public void testReset() {
        robot.move(5);
        robot.turnRight();
        robot.penDown();
        robot.reset();
        assertEquals(0, robot.getX());
        assertEquals(0, robot.getY());
        assertEquals(Direction.NORTH, robot.getDirection());
        assertFalse(robot.isPenDown());
    }
    
    @Test
    public void testDrawSquare() {
        robot.penDown();
        robot.move(3);
        robot.turnRight();
        robot.move(3);
        robot.turnRight();
        robot.move(3);
        robot.turnRight();
        robot.move(3);
        assertEquals(1, floor.getCell(0, 0));
        assertEquals(1, floor.getCell(0, 3));
        assertEquals(1, floor.getCell(3, 3));
        assertEquals(1, floor.getCell(3, 0));
    }
}