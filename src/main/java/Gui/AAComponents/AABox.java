package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AABox extends Box implements AAComponent{
    protected final List<AAComponent> aaComponents;

    public AABox() {
        super(BoxLayout.Y_AXIS);
        super.setFont(GuiResources.defaultFont);
        super.setFocusable(false);
        aaComponents = new ArrayList<>();
    }

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
