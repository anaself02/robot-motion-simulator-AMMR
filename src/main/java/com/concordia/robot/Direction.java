package com.concordia.robot;

/**
 * Enum representing the four cardinal directions
 */
public enum Direction {
    NORTH, EAST, SOUTH, WEST;
    
    /**
     * Get the direction after turning right
     * @return the new direction
     */
    public Direction turnRight() {
        switch (this) {
            case NORTH: return EAST;
            case EAST: return SOUTH;
            case SOUTH: return WEST;
            case WEST: return NORTH;
            default: return this;
        }
    }
    
    /**
     * Get the direction after turning left
     * @return the new direction
     */
    public Direction turnLeft() {
        switch (this) {
            case NORTH: return WEST;
            case WEST: return SOUTH;
            case SOUTH: return EAST;
            case EAST: return NORTH;
            default: return this;
        }
    }
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}