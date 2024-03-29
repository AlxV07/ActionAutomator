package com.actionautomator.Gui.ThemedComponents;

import com.actionautomator.Gui.ActionAutomatorResources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ThemedPanel extends JPanel implements ThemedComponent {
    protected boolean darkMode;
    protected Color primaryColor;
    protected Color secondaryColor;
    protected final List<ThemedComponent> themedComponents;

    public ThemedPanel() {
        super();
        super.setFont(ActionAutomatorResources.defaultFont);
        super.setFocusable(false);
        this.themedComponents = new ArrayList<>();
    }

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
        for (ThemedComponent themedComponent : themedComponents) {
            themedComponent.updateColorTheme(darkMode, primaryColor, secondaryColor);
        }
    }

    @Override
    public Component add(Component component) {
        try {
            themedComponents.add((ThemedComponent) component);
        } catch (ClassCastException ignored) {
        }
        return super.add(component);
    }

    @Override
    public void remove(Component component) {
        try {
            themedComponents.remove((ThemedComponent) component);
        } catch (ClassCastException ignored) {
        }
        super.remove(component);
    }
}
