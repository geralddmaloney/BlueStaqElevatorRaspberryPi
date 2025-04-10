package com.geraldmaloney.elevator;

import com.geraldmaloney.elevator.input.KeypadInputProvider;
import com.geraldmaloney.elevator.input.InputProvider;
import com.geraldmaloney.elevator.display.LcdDisplayAdapter;

public class Main {
    public static void main(String[] args) {
        LcdDisplayAdapter display = new LcdDisplayAdapter();
        InputProvider inputProvider = new KeypadInputProvider();
        PassengerElevator elevator = new PassengerElevator(inputProvider, display);

        boolean running = true;
        long tickRateMs = 1000;

        while (running) {
            elevator.goToNextFloor();
            elevator.printStatus();

            String input = inputProvider.getNextInput();
            if (input != null) {
                System.out.println("KEYPAD INPUT: " + input);

                switch (input) {
                    case "Q", "QUIT", "EXIT" -> running = false;
                    case "B" -> elevator.pressFloor(PassengerElevator.MIN_FLOOR_NUMBER);
                    case "L" -> elevator.pressFloor(PassengerElevator.LOBBY_FLOOR_NUMBER);
                    case "R" -> elevator.pressFloor(PassengerElevator.MAX_FLOOR_NUMBER);
                    case "OPEN" -> elevator.openDoor();
                    case "CLOSE" -> elevator.closeDoor();
                    case "BELL" -> elevator.ringBell();
                    case "STOP" -> elevator.emergencyStopPressed();
                    default -> {
                        try {
                            int floor = Integer.parseInt(input);
                            elevator.pressFloor(floor);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input: " + input);
                        }
                    }
                }
            }

            try {
                Thread.sleep(tickRateMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
