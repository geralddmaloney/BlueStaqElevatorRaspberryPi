package com.geraldmaloney.elevator.display;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

/**
 * Controls a 16x2 LCD display using GPIO pins through Pi4J.
 * This class writes characters, clears the display, and positions the cursor.
 */
public class LCDController {

    private final DigitalOutput rs, e, d4, d5, d6, d7;

    /**
     * Constructor — maps each GPIO pin to a named LCD control pin.
     * @param pi4j the Pi4J Context used to create and manage GPIO pins
     */
    public LCDController(Context pi4j) {
        this.rs = createOut(pi4j, 26, "LCD_RS");
        this.e  = createOut(pi4j, 19, "LCD_E");
        this.d4 = createOut(pi4j, 13, "LCD_D4");
        this.d5 = createOut(pi4j, 6,  "LCD_D5");
        this.d6 = createOut(pi4j, 5,  "LCD_D6");
        this.d7 = createOut(pi4j, 11, "LCD_D7");

        initializeLCD();
    }

    /**
     * Helper method to create and configure each digital output pin.
     */
    private DigitalOutput createOut(Context pi4j, int bcm, String id) {
        var config = DigitalOutput.newConfigBuilder(pi4j)
                .id(id)
                .name(id)
                .address(bcm)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.LOW)
                .build();

        return pi4j.create(config);
    }

    /**
     * Clears the LCD display and resets the cursor.
     */
    public void clear() {
        command(0x01); // LCD clear display command
        sleep(2);      // Give time for command to complete
    }

    /**
     * Prints a string of characters to the LCD at the current cursor position.
     */
    public void print(String text) {
        for (char c : text.toCharArray()) {
            writeChar(c);
        }
    }

    /**
     * Positions the LCD cursor.
     * @param row Line number (0 = top line, 1 = bottom line)
     * @param col Character position on that line
     */
    public void setCursor(int row, int col) {
        int[] rowOffsets = {0x00, 0x40};  // Address offsets for 16x2 LCD
        command(0x80 | (col + rowOffsets[row])); // 0x80 = Set DDRAM address command
    }

    /**
     * Initializes the LCD in 4-bit, 2-line mode.
     */
    private void initializeLCD() {
        sleep(50); // Wait after power-up

        // Initialization sequence per HD44780 datasheet
        write4Bits(0x03); sleep(5);
        write4Bits(0x03); sleep(5);
        write4Bits(0x03); sleep(1);
        write4Bits(0x02); // Switch to 4-bit mode

        command(0x28); // Function Set: 2 line, 5x8 dots, 4-bit
        command(0x0C); // Display ON, Cursor OFF, Blink OFF
        command(0x06); // Entry Mode: move right, no display shift
        clear();       // Clear the screen
    }

    /**
     * Sends a single character to the LCD.
     */
    private void writeChar(char c) {
        send(c, true);
    }

    /**
     * Sends a command to the LCD (e.g. clear, set cursor).
     */
    private void command(int cmd) {
        send(cmd, false);
    }

    /**
     * Sends data or command bytes to the LCD.
     * @param data The 8-bit value to send
     * @param mode True = character, False = command
     */
    private void send(int data, boolean mode) {
        rs.state(mode ? DigitalState.HIGH : DigitalState.LOW);
        write4Bits(data >> 4);      // Send high nibble
        write4Bits(data & 0x0F);    // Send low nibble
    }

    /**
     * Sends a 4-bit nibble to the LCD over data pins.
     */
    private void write4Bits(int data) {
        d4.state((data & 0x01) != 0 ? DigitalState.HIGH : DigitalState.LOW);
        d5.state((data & 0x02) != 0 ? DigitalState.HIGH : DigitalState.LOW);
        d6.state((data & 0x04) != 0 ? DigitalState.HIGH : DigitalState.LOW);
        d7.state((data & 0x08) != 0 ? DigitalState.HIGH : DigitalState.LOW);
        pulseEnable();
    }

    /**
     * Generates the enable pulse to latch data into the LCD.
     */
    private void pulseEnable() {
        e.state(DigitalState.HIGH);
        sleep(1);
        e.state(DigitalState.LOW);
    }

    /**
     * Simple sleep wrapper with interrupt safety.
     */
    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
