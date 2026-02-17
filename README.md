# Robot Motion Simulator - COEN 448 Project D1

## Team AMMR

- Anas
- Mikelange
- Rahil
- Jazib

## Project Overview

A Java application that simulates a robot moving on an N×N grid floor. The robot can draw patterns by moving with its pen down, and supports various commands for control and visualization.

## Features

- 10 robot commands (U, D, R, L, M, P, C, Q, I, H)
- N×N configurable grid floor
- Pen up/down drawing capability
- Command history with replay
- Boundary checking and input validation

## Requirements

- Java JDK 8 or higher
- Maven 3.x
- Git

## Quick Start
```bash
# Clone the repository
git clone https://github.com/anaself02/robot-motion-simulator-AMMR.git
cd robot-motion-simulator-AMMR

# Compile
mvn clean compile

# Run tests
mvn test

# Run the application
java -cp target/classes com.concordia.robot.Main
```

## Commands

| Command | Description |
|---------|-------------|
| `U` | Pen up |
| `D` | Pen down |
| `R` | Turn right |
| `L` | Turn left |
| `M s` | Move forward s spaces |
| `P` | Print the floor |
| `C` | Show current position and status |
| `Q` | Quit |
| `I n` | Initialize n×n floor |
| `H` | Replay command history |

## Example
```
>Enter command: I 10
System initialized with 10x10 floor

>Enter command: D
>Enter command: M 4
>Enter command: R
>Enter command: M 3
>Enter command: P
   0  1  2  3  4  5  6  7  8  9
4  *  *  *  *
3  *
2  *
1  *
0  *
```

## Testing

- **42 unit tests** covering all functionality
- **100% test pass rate**
- JUnit 5 testing framework

Run tests with: `mvn test`

## Project Structure
```
src/
├── main/java/com/concordia/robot/
│   ├── Direction.java          # Direction enum
│   ├── Floor.java              # Grid management
│   ├── Robot.java              # Robot state and movement
│   ├── RobotController.java    # Command processing
│   └── Main.java               # User interface
└── test/java/com/concordia/robot/
    ├── DirectionTest.java
    ├── FloorTest.java
    ├── RobotTest.java
    └── RobotControllerTest.java
```

## License

Educational project for COEN 448 - Concordia University