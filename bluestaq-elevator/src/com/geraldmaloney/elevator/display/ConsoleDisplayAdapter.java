package com.geraldmaloney.elevator.display;

/**
 * Console-based implementation of DisplayAdapter.
 * Used during development before integrating physical LCD.
 */
public class ConsoleDisplayAdapter implements DisplayAdapter {

    @Override
    public void updateDisplay(String line1, String line2) {
        System.out.println("[LCD Display]");
        System.out.println("Line 1: " + line1);
        System.out.println("Line 2: " + line2);
        System.out.println("-------------------------");
    }
}
