package com.concordia.robot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class FloorTest {
    
    private Floor floor;
    
    @BeforeEach
    public void setUp() {
        floor = new Floor(10);
    }
    
    @Test
    public void testFloorInitialization() {
        assertEquals(10, floor.getSize());
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                assertEquals(0, floor.getCell(x, y));
            }
        }
    }
    
    @Test
    public void testInvalidFloorSize() {
        assertThrows(IllegalArgumentException.class, () -> new Floor(0));
        assertThrows(IllegalArgumentException.class, () -> new Floor(-5));
    }
    
    @Test
    public void testMarkCell() {
        floor.mark(5, 5);
        assertEquals(1, floor.getCell(5, 5));
    }
    
    @Test
    public void testMarkMultipleCells() {
        floor.mark(0, 0);
        floor.mark(5, 5);
        floor.mark(9, 9);
        assertEquals(1, floor.getCell(0, 0));
        assertEquals(1, floor.getCell(5, 5));
        assertEquals(1, floor.getCell(9, 9));
        assertEquals(0, floor.getCell(3, 3));
    }
    
    @Test
    public void testValidPosition() {
        assertTrue(floor.isValidPosition(0, 0));
        assertTrue(floor.isValidPosition(9, 9));
        assertTrue(floor.isValidPosition(5, 5));
        assertFalse(floor.isValidPosition(-1, 0));
        assertFalse(floor.isValidPosition(0, -1));
        assertFalse(floor.isValidPosition(10, 0));
        assertFalse(floor.isValidPosition(0, 10));
    }
    
    @Test
    public void testGetCellOutOfBounds() {
        assertEquals(-1, floor.getCell(-1, 0));
        assertEquals(-1, floor.getCell(0, -1));
        assertEquals(-1, floor.getCell(10, 0));
        assertEquals(-1, floor.getCell(0, 10));
    }
    
    @Test
    public void testClear() {
        floor.mark(5, 5);
        floor.mark(3, 3);
        floor.clear();
        assertEquals(0, floor.getCell(5, 5));
        assertEquals(0, floor.getCell(3, 3));
    }
    
    @Test
    public void testMarkSameCellMultipleTimes() {
        floor.mark(5, 5);
        floor.mark(5, 5);
        floor.mark(5, 5);
        assertEquals(1, floor.getCell(5, 5));
    }
}