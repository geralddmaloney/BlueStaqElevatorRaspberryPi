package com.geraldmaloney.elevator;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

import java.util.*;

public class KeypadToLCD {

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

        // Initialize LCD
        LCDController lcd = new LCDController(pi4j);

        // Setup keypad columns
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

        // Setup keypad rows
        List<DigitalInput> rowIn = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            var config = DigitalInput.newConfigBuilder(pi4j)
                    .id("ROW" + i)
                    .address(ROWS[i])
                    .pull(PullResistance.PULL_DOWN)
                    .build();
            rowIn.add(pi4j.create(config));
        }

        lcd.print("Ready for input");

        while (true) {
            for (int c = 0; c < 4; c++) {
                // Activate one column at a time
                for (int j = 0; j < 4; j++) {
                    colOut.get(j).state(j == c ? DigitalState.HIGH : DigitalState.LOW);
                }

                for (int r = 0; r < 4; r++) {
                    if (rowIn.get(r).state() == DigitalState.HIGH) {
                        char key = KEYS[r][c];
                        lcd.print("Pressed: " + key);
                        sleep(300); // Debounce and display delay
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
