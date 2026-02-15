package com.concordia.robot;

/**
 * Represents the N x N floor grid where the robot moves
 */
public class Floor {
    private int[][] grid;
    private int size;
    
    /**
     * Initialize floor with given size
     * @param n size of the floor (n x n)
     */
    public Floor(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Floor size must be greater than 0");
        }
        this.size = n;
        this.grid = new int[n][n];
        // Grid is automatically initialized to 0s in Java
    }
    
    /**
     * Mark a position on the floor
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void mark(int x, int y) {
        if (isValidPosition(x, y)) {
            grid[y][x] = 1;
        }
    }
    
    /**
     * Check if a position is valid (within bounds)
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if position is valid
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }
    
    /**
     * Get the value at a position
     * @param x x-coordinate
     * @param y y-coordinate
     * @return 0 or 1
     */
    public int getCell(int x, int y) {
        if (isValidPosition(x, y)) {
            return grid[y][x];
        }
        return -1; // Invalid position
    }
    
    /**
     * Get floor size
     * @return size of the floor
     */
    public int getSize() {
        return size;
    }
    
    /**
     * Print the floor grid
     * Displays asterisks (*) where robot has drawn (value = 1)
     * Displays spaces where robot hasn't drawn (value = 0)
     */
    public void print() {
        // Print column indices
        System.out.print("   ");
        for (int x = 0; x < size; x++) {
            System.out.printf("%-3d", x);
        }
        System.out.println();
        
        // Print floor from top to bottom (highest y first)
        for (int y = size - 1; y >= 0; y--) {
            System.out.printf("%-3d", y);
            for (int x = 0; x < size; x++) {
                if (grid[y][x] == 1) {
                    System.out.print("*  ");
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Clear the floor (reset all cells to 0)
     */
    public void clear() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                grid[y][x] = 0;
            }
        }
    }
}