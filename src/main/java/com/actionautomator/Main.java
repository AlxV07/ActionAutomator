package com.actionautomator;

import com.actionautomator.Gui.GuiMainFrame;

import java.awt.*;

public class Main {
    /*
    TODO:
        - CodeTextArea IDE functionality
            - variables, math, looping, etc.
            - highlight errors
        - clean up GUI
        - README & Help page
     */
    public static void main(String[] args) {
        GuiMainFrame guiMainFrame = new GuiMainFrame();
        guiMainFrame.start(true, Color.GREEN, Color.GRAY);
    }
}