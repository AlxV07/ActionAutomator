package com.actionautomator;

import com.actionautomator.Gui.GuiMain;

import java.awt.*;

public class Main {
    /*
    TODO:
        - Binding management cleanup
        - Waypoint saving, Action interruption
        - CodeTextArea IDE functionality
            - variables, math, looping, etc.
     */
    public static void main(String[] args) {
        GuiMain guiMain = new GuiMain();
        guiMain.start(true, Color.GREEN, Color.GRAY);
    }
}