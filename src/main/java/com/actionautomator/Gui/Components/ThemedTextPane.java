package com.actionautomator.Gui.Components;

import com.actionautomator.Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class ThemedTextPane extends JTextPane implements ThemedComponent {
    protected boolean darkMode;
    protected Color primaryColor;
    protected Color secondaryColor;

    @Override
    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
        this.darkMode = darkMode;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        if (darkMode) {
            setForeground(primaryColor);
            setBackground(GuiResources.darkThemeColor);
            setBorder(GuiResources.darkThemeBorder);
        } else {
            setForeground(secondaryColor);
            setBackground(GuiResources.lightThemeColor);
            setBorder(GuiResources.lightThemeBorder);
        }
    }
}
