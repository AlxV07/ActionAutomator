package Gui.Components;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class ThemedLabel extends JLabel implements ThemedComponent {
    protected boolean darkMode;
    protected Color primaryColor;
    protected Color secondaryColor;

    public ThemedLabel(String title) {
        super(title);
        super.setOpaque(true);
        super.setFont(GuiResources.defaultFont);
        super.setFocusable(false);
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
