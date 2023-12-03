package com.actionautomator.Gui.ThemedComponents;

import com.actionautomator.Gui.ActionAutomatorResources;

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
            setBackground(ActionAutomatorResources.darkThemeColor);
            setBorder(ActionAutomatorResources.darkThemeBorder);
        } else {
            setForeground(secondaryColor);
            setBackground(ActionAutomatorResources.lightThemeColor);
            setBorder(ActionAutomatorResources.lightThemeBorder);
        }
    }
}
