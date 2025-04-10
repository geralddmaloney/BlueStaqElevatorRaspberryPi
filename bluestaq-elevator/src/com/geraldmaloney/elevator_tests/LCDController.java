package com.geraldmaloney.elevator;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.*;

public class LCDController {

    private final DigitalOutput rs, e, d4, d5, d6, d7;

    public LCDController(Context pi4j) {
        this.rs = createOut(pi4j, 26, "LCD_RS");
        this.e  = createOut(pi4j, 19, "LCD_E");
        this.d4 = createOut(pi4j, 13, "LCD_D4");
        this.d5 = createOut(pi4j, 6,  "LCD_D5");
        this.d6 = createOut(pi4j, 5,  "LCD_D6");
        this.d7 = createOut(pi4j, 11, "LCD_D7");

        initializeLCD();
    }

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

    private void pulseEnable() {
        e.state(DigitalState.HIGH);
        sleep(1);
        e.state(DigitalState.LOW);
    }

    private void write4Bits(int data) {
        d4.state((data & 0x01) != 0 ? DigitalState.HIGH : DigitalState.LOW);
        d5.state((data & 0x02) != 0 ? DigitalState.HIGH : DigitalState.LOW);
        d6.state((data & 0x04) != 0 ? DigitalState.HIGH : DigitalState.LOW);
        d7.state((data & 0x08) != 0 ? DigitalState.HIGH : DigitalState.LOW);
        pulseEnable();
    }

    private void send(int data, boolean mode) {
        rs.state(mode ? DigitalState.HIGH : DigitalState.LOW);
        write4Bits(data >> 4);
        write4Bits(data & 0x0F);
    }

    private void command(int cmd) {
        send(cmd, false);
    }

    private void writeChar(char c) {
        send(c, true);
    }

    public void clear() {
        command(0x01);
        sleep(2);
    }

    public void print(String text) {
        clear();
        for (char c : text.toCharArray()) {
            writeChar(c);
        }
    }

    private void initializeLCD() {
        sleep(50);
        write4Bits(0x03); sleep(5);
        write4Bits(0x03); sleep(5);
        write4Bits(0x03); sleep(1);
        write4Bits(0x02);

        command(0x28); // 4-bit mode, 2-line
        command(0x0C); // Display ON, cursor OFF
        command(0x06); // Entry Mode Set
        clear();
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
