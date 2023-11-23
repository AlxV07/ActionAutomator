package com.actionautomator.Gui.Components;

import com.actionautomator.Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class ThemedTextArea extends JTextArea implements ThemedComponent {
    protected boolean darkMode;
    protected Color primaryColor;
    protected Color secondaryColor;

    public ThemedTextArea() {
        super();
        super.setFont(GuiResources.smallerFont);
        super.setFocusable(false);
        super.setOpaque(true);
        super.setWrapStyleWord(true);
        super.setLineWrap(true);
        super.setMargin(GuiResources.defaultMargin);
    }

    @Override
    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
        this.darkMode = darkMode;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        setForeground(primaryColor);
        if (darkMode) {
            setBackground(GuiResources.darkThemeColor);
            setBorder(GuiResources.darkThemeBorder);
        } else {
            setBackground(GuiResources.lightThemeColor);
            setBorder(GuiResources.lightThemeBorder);
        }
    }
}
