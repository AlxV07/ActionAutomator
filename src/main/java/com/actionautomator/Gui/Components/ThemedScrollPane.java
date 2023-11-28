package com.actionautomator.Gui.Components;

import com.actionautomator.Gui.GuiResources;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ThemedScrollPane extends JScrollPane implements ThemedComponent {
    protected boolean darkMode;
    protected Color primaryColor;
    protected Color secondaryColor;
    protected final List<ThemedComponent> themedComponents;

    public ThemedScrollPane(JComponent component) {
        super(component);
        super.setFont(GuiResources.defaultFont);
        super.setFocusable(false);
        this.themedComponents = new ArrayList<>();
    }

    private class CustomScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            thumbColor = secondaryColor; // Set thumb color
            if (!darkMode) {  // Set thumb dark shadow color
                trackColor = Color.WHITE; // Set track color
                thumbDarkShadowColor = Color.WHITE;
                thumbHighlightColor = Color.WHITE; // Set thumb highlight color
            } else {
                trackColor = Color.BLACK;
                thumbDarkShadowColor = Color.BLACK;
                thumbHighlightColor = Color.BLACK; // Set thumb highlight color
            }
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            JButton button = super.createDecreaseButton(orientation);
            button.setBackground(primaryColor); // Set decrease button background color
            return button;
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            JButton button = super.createIncreaseButton(orientation);
            button.setBackground(primaryColor); // Set increase button background color
            return button;
        }
    }

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
        for (ThemedComponent themedComponent : themedComponents) {
            themedComponent.updateColorTheme(darkMode, primaryColor, secondaryColor);
        }
        super.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        super.getVerticalScrollBar().setUnitIncrement(5); // Example value, adjust as needed
        super.getVerticalScrollBar().setBlockIncrement(60); // Example value, adjust as needed
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
