package com.geraldmaloney.elevator;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class LCDMain {

    public static void main(String[] args) {
        // Create Pi4J context using auto-detection of Pi4J I/O providers
        Context pi4j = Pi4J.newAutoContext();

        // Instantiate the LCD controller with GPIO pin mapping
        LCDController lcd = new LCDController(pi4j);

        // Print Hello Bluestaq to the LCD
        lcd.print("Hello Bluestaq!");
    }
}
