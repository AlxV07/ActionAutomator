package com.actionautomator;

import com.actionautomator.Gui.GuiMainFrame;

import java.awt.*;

public class Main {
    /*
    TODO:
        - ProgInterface IDE functionality, Code
            - variables, math, looping, etc.
            - Binding code "burning"
        - README, Logo
     */
    public static void main(String[] args) {
        GuiMainFrame guiMainFrame = new GuiMainFrame();
        guiMainFrame.start(true, Color.GREEN, Color.GRAY);
    }
}