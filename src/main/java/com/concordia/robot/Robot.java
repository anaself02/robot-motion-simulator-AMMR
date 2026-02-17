package com.concordia.robot;

public class Robot {
    private int x;
    private int y;
    private boolean penDown;
    private Direction direction;
    private Floor floor;


    /**
     * Create a new Robot instance with the specified floor, and initial position (0, 0), pen up, and facing north.
     * @param floor the floor on which the robot will operate
     */
    public Robot(Floor floor) {
        this.x = 0;
        this.y = 0;
        this.penDown = false;
        this.direction = Direction.NORTH;
        this.floor = floor;
    }

    // Raise the pen
    public void penUp() {
        this.penDown = false;
    }

    // Lower the pen
    public void penDown() {
        this.penDown = true;
    }

    // check if pen is down, return true if pen is down, false otherwise
    public boolean isPenDown() {
        return this.penDown;
    }

    /** 
     * Move robot forward by the specified number of steps
     * @param steps the number of steps to move forward
     * @return true if the move was successful, false if the move would go out of bounds
     */

    public boolean move(int steps) {
        if (steps < 0) {
            System.out.println("Steps must be a non-negative integer.");
            return false;
        }

        int newX = this.x;
        int newY = this.y;

        switch (this.direction) {
            case NORTH -> newY += steps;
            case EAST -> newX += steps;
            case SOUTH -> newY -= steps;
            case WEST -> newX -= steps;
        }

        if (!floor.isValidPosition(newX, newY)) {
            System.out.println("Move would go out of bounds. Move cancelled.");
            return false;
        }

        for (int i=0; i < steps; i++) {
            switch (this.direction) {
                case NORTH -> this.y++;
                case SOUTH -> this.y--;
                case EAST -> this.x++;
                case WEST -> this.x--;
            }

            if (penDown) {
                floor.mark(x, y);
            }   

        }

        return true;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Direction getDirection() {
        return this.direction;
    } 

    // Print the current status of the robot, including position, pen status, and facing direction
    public void printStatus() {
        String penStatus = penDown ? "down" : "up";
        System.out.printf("Position: %d, %d - Pen: %s - Facing: %s%n", 
                         x, y, penStatus, direction);
    }

    // Turn 90° to the right (clockwise)
    public void turnRight() {
        this.direction = this.direction.turnRight();
    }

    // Turn 90° to the left (counter‑clockwise)
    public void turnLeft() {
        this.direction = this.direction.turnLeft();
    }

    
    // Reset robot to initial state
    public void reset() {
        this.x = 0;
        this.y = 0;
        this.penDown = false;
        this.direction = Direction.NORTH;
    }


}
