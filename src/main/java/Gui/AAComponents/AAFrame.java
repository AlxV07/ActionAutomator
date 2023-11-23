package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AAFrame extends JFrame implements AAComponent {
    protected final List<AAComponent> aaComponents;

    public AAFrame() {
        super();
        super.setFont(GuiResources.defaultFont);
        super.setFocusable(false);
        super.setTitle("ActionAutomator");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setLayout(null);
        super.setSize(500, 500);
        super.setResizable(false);
        aaComponents = new ArrayList<>();
    }

    protected Color primaryColor;
    protected Color secondaryColor;

    @Override
    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        if (darkMode) {
            setForeground(primaryColor);
            super.getContentPane().setBackground(GuiResources.darkThemeColor);
        } else {
            setForeground(secondaryColor);
            super.getContentPane().setBackground(GuiResources.lightThemeColor);
        }
        for (AAComponent aaComponent : aaComponents) {
            aaComponent.updateColorTheme(darkMode, primaryColor, secondaryColor);
        }
    }

    @Override
    public Component add(Component component) {
        try {
            aaComponents.add((AAComponent) component);
        } catch (ClassCastException ignored) {
        }
        return super.add(component);
    }

    @Override
    public void remove(Component component) {
        try {
            aaComponents.remove((AAComponent) component);
        } catch (ClassCastException ignored) {
        }
        super.remove(component);
    }
}
