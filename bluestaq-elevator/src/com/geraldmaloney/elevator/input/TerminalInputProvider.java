package com.geraldmaloney.elevator.input;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Interface Implementation.
 *  Reads the Buffered Reader which users System.in
 */
public class TerminalInputProvider implements InputProvider {
    private final BufferedReader reader;

    public TerminalInputProvider() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Reads User Input from reader
     * @return User input string
     */
    @Override
    public String getNextInput() {
        try {
            if (reader.ready()) {
                return reader.readLine().trim().toUpperCase();
            }
        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }
        return null;
    }
}
