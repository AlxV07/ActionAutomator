package com.actionautomator.Gui.ThemedComponents;

import com.actionautomator.Gui.ActionAutomatorResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemedTextArea extends JTextArea implements ThemedComponent {
    protected boolean darkMode;
    protected Color primaryColor;
    protected Color secondaryColor;

    public ThemedTextArea() {
        super();
        super.setFont(ActionAutomatorResources.smallerFont);
        super.setFocusable(false);
        super.setOpaque(true);
        super.setWrapStyleWord(true);
        super.setLineWrap(true);
        super.setMargin(ActionAutomatorResources.defaultMargin);
    }

    @Override
    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
        this.darkMode = darkMode;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        setForeground(primaryColor);
        if (darkMode) {
            setBackground(ActionAutomatorResources.darkThemeColor);
            setBorder(ActionAutomatorResources.darkThemeBorder);
        } else {
            setBackground(ActionAutomatorResources.lightThemeColor);
            setBorder(ActionAutomatorResources.lightThemeBorder);
        }
    }


    public void setHelp(ThemedTextArea helpLabel, String doc) {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                helpLabel.setText(doc);
            }
        });
    }
}
