package com.concordia.robot;

import java.util.Scanner;

/**
 * Main class for Robot Motion Simulator
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("===========================================");
        System.out.println("   Robot Motion Simulator - COEN 448");
        System.out.println("===========================================");
        System.out.println();
        
        printHelp();
        
        RobotController controller = new RobotController(10);
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nSystem initialized with 10x10 floor");
        System.out.println("Robot at position [0, 0], pen up, facing north");
        System.out.println();
        
        boolean running = true;
        
        while (running) {
            System.out.print(">Enter command: ");
            String command = scanner.nextLine();
            
            if (command.trim().isEmpty()) {
                continue;
            }
            
            String upperCommand = command.trim().toUpperCase();
            
            // Check for quit command
            if (upperCommand.equals("Q")) {
                System.out.println("Exiting program. Goodbye!");
                running = false;
                continue;
            }
            
            // Process the command
            boolean success = controller.processCommand(command);
            
            if (!success) {
                System.out.println("Error: Invalid command. Type 'H' to see available commands.");
            }
            
            System.out.println();
        }
        
        scanner.close();
    }
    
    /**
     * Print help information about available commands
     */
    private static void printHelp() {
        System.out.println("Available Commands:");
        System.out.println("-------------------");
        System.out.println("U or u       - Pen up");
        System.out.println("D or d       - Pen down");
        System.out.println("R or r       - Turn right");
        System.out.println("L or l       - Turn left");
        System.out.println("M s or m s   - Move forward s spaces");
        System.out.println("P or p       - Print the floor");
        System.out.println("C or c       - Print current position and status");
        System.out.println("I n or i n   - Initialize with n x n floor");
        System.out.println("H or h       - Replay command history");
        System.out.println("Q or q       - Quit");
        System.out.println();
    }
}