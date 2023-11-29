package com.actionautomator;

import com.actionautomator.Gui.GuiFrame;

import java.awt.*;

public class Main {
    /*
    TODO:
        - CodeTextArea IDE functionality
            - variables, math, looping, etc.
            - highlight errors
     */
    public static void main(String[] args) {
        GuiFrame guiFrame = new GuiFrame();
        guiFrame.start(true, Color.GREEN, Color.GRAY);
    }
}