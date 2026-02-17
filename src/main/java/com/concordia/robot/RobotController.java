package com.concordia.robot;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller to process robot commands
 */
public class RobotController {
    private Robot robot;
    private Floor floor;
    private List<String> commandHistory;
    
    /**
     * Create a new controller with default 10x10 floor
     */
    public RobotController() {
        this(10);
    }
    
    /**
     * Create a new controller with specified floor size
     * @param size floor size
     */
    public RobotController(int size) {
        this.floor = new Floor(size);
        this.robot = new Robot(floor);
        this.commandHistory = new ArrayList<>();
    }
    
    /**
     * Process a command string
     * @param command the command to process
     * @return true if command was valid and executed
     */
    public boolean processCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }
        
        command = command.trim();
        String upperCommand = command.toUpperCase();
        
        // Store command in history (except Q and H)
        if (!upperCommand.equals("Q") && !upperCommand.equals("H")) {
            commandHistory.add(command);
        }
        
        // Single character commands
        if (command.length() == 1) {
            char cmd = upperCommand.charAt(0);
            switch (cmd) {
                case 'U':
                    robot.penUp();
                    return true;
                case 'D':
                    robot.penDown();
                    return true;
                case 'R':
                    robot.turnRight();
                    return true;
                case 'L':
                    robot.turnLeft();
                    return true;
                case 'P':
                    floor.print();
                    return true;
                case 'C':
                    robot.printStatus();
                    return true;
                case 'Q':
                    return true;
                case 'H':
                    replayHistory();
                    return true;
                default:
                    return false;
            }
        }
        
        // Multi-character commands (M s, I n)
        String[] parts = command.split("\\s+");
        if (parts.length == 2) {
            char cmd = parts[0].toUpperCase().charAt(0);
            try {
                int value = Integer.parseInt(parts[1]);
                
                switch (cmd) {
                    case 'M':
                        if (value < 0) {
                            System.out.println("Error: Move distance must be non-negative");
                            return false;
                        }
                        boolean moved = robot.move(value);
                        if (!moved) {
                            System.out.println("Error: Cannot move outside floor boundaries");
                        }
                        return moved;
                        
                    case 'I':
                        if (value <= 0) {
                            System.out.println("Error: Floor size must be greater than 0");
                            return false;
                        }
                        initialize(value);
                        return true;
                        
                    default:
                        return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid number format");
                return false;
            }
        }
        
        return false;
    }
    
    /**
     * Initialize the system with a new floor size
     * @param size new floor size
     */
    public void initialize(int size) {
        this.floor = new Floor(size);
        this.robot = new Robot(floor);
        this.commandHistory.clear();
        System.out.println("System initialized with " + size + "x" + size + " floor");
    }
    
    /**
     * Replay all commands in history
     */
    public void replayHistory() {
        System.out.println("Replaying command history:");
        System.out.println("---------------------------");
        
        if (commandHistory.isEmpty()) {
            System.out.println("No commands in history");
            return;
        }
        
        // Store current state
        List<String> history = new ArrayList<>(commandHistory);
        
        // Reinitialize
        int currentSize = floor.getSize();
        initialize(currentSize);
        
        // Replay each command
        for (String cmd : history) {
            System.out.println("> " + cmd);
            processCommand(cmd);
        }
        
        System.out.println("---------------------------");
        System.out.println("Replay complete");
    }
    
    /**
     * Get the robot
     * @return the robot
     */
    public Robot getRobot() {
        return robot;
    }
    
    /**
     * Get the floor
     * @return the floor
     */
    public Floor getFloor() {
        return floor;
    }
    
    /**
     * Get command history
     * @return list of commands
     */
    public List<String> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
}