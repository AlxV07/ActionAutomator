package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class AAButton extends JButton implements AAComponent {
    protected Color alternateColor;
    protected Color primaryColor;
    protected Color secondaryColor;

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

    @Override
    public void updateColorTheme(Color primaryColor, Color secondaryColor, Color alternateColor) {
        super.setForeground(primaryColor);
        super.setBackground(secondaryColor);
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.alternateColor = alternateColor;
    }
}
