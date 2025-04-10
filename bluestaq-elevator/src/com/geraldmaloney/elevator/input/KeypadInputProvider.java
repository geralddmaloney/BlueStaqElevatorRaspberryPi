package com.geraldmaloney.elevator.input;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

import java.util.*;

public class KeypadInputProvider implements InputProvider {

    private final Context pi4j;
    private final List<DigitalOutput> colOut;
    private final List<DigitalInput> rowIn;

    private static final int[] ROWS = {21, 20, 16, 12}; // BCM GPIO
    private static final int[] COLS = {25, 24, 23, 18}; // BCM GPIO

    // Key layout - I know it's messed up. I suspect the pin label on the button pad connector is backwards.
    private static final char[][] KEYS = {
            {'D','C','B','A'},
            {'#','9','6','3'},
            {'0','8','5','2'},
            {'*','7','4','1'}
    };

    private final StringBuilder inputBuffer = new StringBuilder();
    private String queuedCommand = null;

    public KeypadInputProvider() {
        this.pi4j = Pi4J.newAutoContext();
        this.colOut = new ArrayList<>();
        this.rowIn = new ArrayList<>();

        setupPins();
    }

    private void setupPins() {
        // Setup output columns
        for (int i = 0; i < 4; i++) {
            var config = DigitalOutput.newConfigBuilder(pi4j)
                    .id("COL" + i)
                    .address(COLS[i])
                    .shutdown(DigitalState.LOW)
                    .initial(DigitalState.LOW)
                    .build();
            colOut.add(pi4j.create(config));
        }

        // Setup input rows
        for (int i = 0; i < 4; i++) {
            var config = DigitalInput.newConfigBuilder(pi4j)
                    .id("ROW" + i)
                    .address(ROWS[i])
                    .pull(PullResistance.PULL_DOWN)
                    .build();
            rowIn.add(pi4j.create(config));
        }
    }

    @Override
    public String getNextInput() {
        //System.out.println("Polling Keypad...");
        if (queuedCommand != null) {
            String out = queuedCommand;
            queuedCommand = null;
            return out;
        }

        // Scan keypad
        for (int c = 0; c < 4; c++) {
            // Activate one column
            for (int j = 0; j < 4; j++) {
                colOut.get(j).state(j == c ? DigitalState.HIGH : DigitalState.LOW);
            }

            for (int r = 0; r < 4; r++) {
                if (rowIn.get(r).state() == DigitalState.HIGH) {
                    char key = KEYS[r][c];
                    System.out.println("DETECTED KEY PRESS: " + key);       //Keypad debug
                    handleKeyPress(key);
                    sleep(250); // delay
                    return null; // wait for submit key
                }
            }
        }

        return null;
    }

    private void handleKeyPress(char key) {
        switch (key) {
            case '#' -> {
                if (inputBuffer.length() > 0) {
                    queuedCommand = inputBuffer.toString();
                    inputBuffer.setLength(0);
                }
            }
            case '*' -> inputBuffer.setLength(0); // Clear
            case 'A' -> queuedCommand = "OPEN";
            case 'B' -> queuedCommand = "CLOSE";
            case 'C' -> queuedCommand = "BELL";
            case 'D' -> queuedCommand = "STOP";
            default -> {
                if (Character.isDigit(key)) {
                    inputBuffer.append(key);
                }
            }
        }
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
