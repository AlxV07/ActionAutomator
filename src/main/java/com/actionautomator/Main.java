package com.actionautomator;

import com.actionautomator.Gui.GuiMain;

import java.awt.*;

public class Main {
    /*
    TODO:
        - Binding management cleanup
        - CodeTextArea IDE functionality
            - Waypoint saving, Action interruption, variables, math, looping, etc.
     */
    public static void main(String[] args) {
        GuiMain guiMain = new GuiMain();
        guiMain.start(true, Color.GREEN, Color.GRAY);
    }
}