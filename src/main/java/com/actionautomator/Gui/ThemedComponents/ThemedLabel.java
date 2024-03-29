package com.actionautomator.Gui.ThemedComponents;

import com.actionautomator.Gui.ActionAutomatorResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ThemedLabel extends JLabel implements ThemedComponent {
    protected boolean darkMode;
    protected Color primaryColor;
    protected Color secondaryColor;

    public ThemedLabel(String title) {
        super(title);
        super.setOpaque(true);
        super.setFont(ActionAutomatorResources.defaultFont);
        super.setFocusable(false);
    }

    public ThemedLabel(String title, int x) {
        super(title, x);
        super.setOpaque(true);
        super.setFont(ActionAutomatorResources.defaultFont);
        super.setFocusable(false);
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
