package com.geraldmaloney.elevator.display;

/**
 * Interface for updating elevator status displays.
 * Can be implemented for console, LCD, or any custom display system.
 */
public interface DisplayAdapter {
    /**
     * Updates the display with the current elevator state.
     * @param line1 The first line (e.g., floor or position).
     * @param line2 The second line (e.g., direction, door, or mode).
     */
    void updateDisplay(String line1, String line2);
}
