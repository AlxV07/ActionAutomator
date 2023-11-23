package Gui.Components;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ThemedMenuBar extends JMenuBar implements ThemedComponent {
    protected boolean darkMode;
    protected Color primaryColor;
    protected Color secondaryColor;
    protected final List<ThemedComponent> themedComponents;

    public ThemedMenuBar() {
        super();
        super.setFont(GuiResources.defaultFont);
        super.setMargin(GuiResources.defaultMargin);
        super.setFocusable(false);
        themedComponents = new ArrayList<>();
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
