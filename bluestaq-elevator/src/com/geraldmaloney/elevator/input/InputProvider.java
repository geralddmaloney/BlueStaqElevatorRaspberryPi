package com.geraldmaloney.elevator.input;

public interface InputProvider {
    /**
     * Interfce Implementation of the User Input provider. Input methods may vary per elevator implementation type.
     * So form an interface to make a data contract for the user input provider...
     * All it has is a method to wait for input and act.
     * Gets the next User input string if available.
     * @return User input string, or null if nothing is typed
     */
    String getNextInput();
}

