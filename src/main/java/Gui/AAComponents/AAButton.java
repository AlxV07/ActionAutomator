package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class AAButton extends JButton implements AAComponent {

    public AAButton(String title) {
        super(title);
        super.setFont(GuiResources.defaultFont);
        super.setMargin(GuiResources.defaultMargin);
        super.setFocusable(false);
    }

    protected Color primaryColor;
    protected Color secondaryColor;
    protected boolean darkMode;

    @Override
    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.darkMode = darkMode;
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
