package com.actionautomator;

import com.actionautomator.Gui.GuiMainFrame;

import java.awt.*;

public class Main {
    /*
    TODO:
        - ProgInterface IDE highlighting
        - Code Documentation (README)
        - Logo
     */
    public static void main(String[] args) {
        GuiMainFrame guiMainFrame = new GuiMainFrame();
        guiMainFrame.start(true, Color.GREEN, Color.GRAY);
    }
}