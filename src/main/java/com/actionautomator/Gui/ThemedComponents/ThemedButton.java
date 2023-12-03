package com.actionautomator.Gui.ThemedComponents;

import com.actionautomator.Gui.ActionAutomatorResources;

import javax.swing.*;
import java.awt.*;

public class ThemedButton extends JButton implements ThemedComponent {
    protected Color primaryColor;
    protected Color secondaryColor;
    protected boolean darkMode;

    public ThemedButton(String title) {
        super(title);
        super.setFont(ActionAutomatorResources.defaultFont);
        super.setMargin(ActionAutomatorResources.defaultMargin);
        super.setFocusable(false);
    }

    @Override
    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.darkMode = darkMode;
        setForeground(primaryColor);
        if (darkMode) {
            setBackground(ActionAutomatorResources.darkThemeColor);
            setBorder(ActionAutomatorResources.darkThemeBorder);
        } else {
            setBackground(ActionAutomatorResources.lightThemeColor);
            setBorder(ActionAutomatorResources.lightThemeBorder);
        }
    }
}
