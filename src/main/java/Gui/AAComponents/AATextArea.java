package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class AATextArea extends JTextArea implements AAComponent {

    public AATextArea() {
        super();
        super.setFont(GuiResources.smallerFont);
        super.setFocusable(false);
        super.setOpaque(true);
        super.setWrapStyleWord(true);
        super.setLineWrap(true);
        super.setMargin(GuiResources.defaultMargin);
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
