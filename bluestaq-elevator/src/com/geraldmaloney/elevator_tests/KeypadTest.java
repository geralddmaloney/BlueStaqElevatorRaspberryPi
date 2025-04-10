package com.geraldmaloney.elevator;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

import java.util.*;

public class KeypadTest {

    private static final int[] ROWS = {21, 20, 16, 12}; // BCM GPIOs
    private static final int[] COLS = {25, 24, 23, 18}; // BCM GPIOs
    private static final char[][] KEYS = {
            {'D','C','B','A'},
            {'#','9','6','3'},
            {'0','8','5','2'},
            {'*','7','4','1'}
    };

    public static void main(String[] args) {
        Context pi4j = Pi4J.newAutoContext();

        // Setup column outputs
        List<DigitalOutput> colOut = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            var config = DigitalOutput.newConfigBuilder(pi4j)
                    .id("COL" + i)
                    .address(COLS[i])
                    .shutdown(DigitalState.LOW)
                    .initial(DigitalState.LOW)
                    .build();
            colOut.add(pi4j.create(config));
        }

        // Setup row inputs with pull-down
        List<DigitalInput> rowIn = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            var config = DigitalInput.newConfigBuilder(pi4j)
                    .id("ROW" + i)
                    .address(ROWS[i])
                    .pull(PullResistance.PULL_DOWN)
                    .build();
            rowIn.add(pi4j.create(config));
        }

        System.out.println("Keypad ready. Press buttons...");

        while (true) {
            for (int c = 0; c < 4; c++) {
                // Set current column high, others low
                for (int j = 0; j < 4; j++) {
                    colOut.get(j).state(j == c ? DigitalState.HIGH : DigitalState.LOW);
                }

                // Read each row
                for (int r = 0; r < 4; r++) {
                    if (rowIn.get(r).state() == DigitalState.HIGH) {
                        System.out.println("Key Pressed: " + KEYS[r][c]);
                        sleep(200); // debounce delay
                    }
                }
            }
        }
    }

    private static void sleep(int ms) {
        try { Thread.sleep(ms); }
        catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
