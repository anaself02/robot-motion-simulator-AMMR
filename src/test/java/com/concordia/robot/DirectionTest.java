package com.concordia.robot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {
    
    @Test
    public void testTurnRight() {
        assertEquals(Direction.EAST, Direction.NORTH.turnRight());
        assertEquals(Direction.SOUTH, Direction.EAST.turnRight());
        assertEquals(Direction.WEST, Direction.SOUTH.turnRight());
        assertEquals(Direction.NORTH, Direction.WEST.turnRight());
    }
    
    @Test
    public void testTurnLeft() {
        assertEquals(Direction.WEST, Direction.NORTH.turnLeft());
        assertEquals(Direction.NORTH, Direction.EAST.turnLeft());
        assertEquals(Direction.EAST, Direction.SOUTH.turnLeft());
        assertEquals(Direction.SOUTH, Direction.WEST.turnLeft());
    }
    
    @Test
    public void testToString() {
        assertEquals("north", Direction.NORTH.toString());
        assertEquals("east", Direction.EAST.toString());
        assertEquals("south", Direction.SOUTH.toString());
        assertEquals("west", Direction.WEST.toString());
    }
    
    @Test
    public void testFullCircleRight() {
        Direction dir = Direction.NORTH;
        dir = dir.turnRight();
        dir = dir.turnRight();
        dir = dir.turnRight();
        dir = dir.turnRight();
        assertEquals(Direction.NORTH, dir);
    }
    
    @Test
    public void testFullCircleLeft() {
        Direction dir = Direction.NORTH;
        dir = dir.turnLeft();
        dir = dir.turnLeft();
        dir = dir.turnLeft();
        dir = dir.turnLeft();
        assertEquals(Direction.NORTH, dir);
    }
}