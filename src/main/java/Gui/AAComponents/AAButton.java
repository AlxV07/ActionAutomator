package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class AAButton extends JButton implements AAComponent {
    private Color alternateColor;

    public AAButton() {
        super();
        super.setFont(GuiResources.labelFont);
        super.setMargin(GuiResources.noMargin);
        super.setFocusable(false);
    }

    public AAButton(String title) {
        super(title);
        super.setFont(GuiResources.labelFont);
        super.setMargin(GuiResources.noMargin);
    }

    public Color getAlternateColor() {
        return this.alternateColor;
    }

    @Override
    public void updateColorTheme(Color primaryColor, Color secondaryColor, Color alternateColor) {
        super.setForeground(primaryColor);
        super.setBackground(secondaryColor);
        this.alternateColor = alternateColor;
    }
}
