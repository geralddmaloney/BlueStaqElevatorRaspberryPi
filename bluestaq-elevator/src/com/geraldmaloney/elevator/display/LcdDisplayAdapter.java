package com.geraldmaloney.elevator.display;

import com.geraldmaloney.elevator.display.LCDController;
import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class LcdDisplayAdapter implements DisplayAdapter {

    private final LCDController lcd;

    public LcdDisplayAdapter() {
        Context pi4j = Pi4J.newAutoContext();
        this.lcd = new LCDController(pi4j);
    }

    @Override
    public void updateDisplay(String line1, String line2) {
        lcd.clear();
        lcd.setCursor(0,0);
        lcd.print(pad(line1));
        lcd.setCursor(1,0);   // Line 2
        lcd.print(pad(line2));
    }
    
    private String pad(String line) {
        return String.format("%-16s", line.length() > 16 ? line.substring(0,16) : line);
    }
    
}
