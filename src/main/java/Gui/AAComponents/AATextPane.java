package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class AATextPane extends JTextPane implements AAComponent {

    private Color alternateColor;

    public AATextPane() {
        super();
        super.setFont(Font.getFont("Source Code Pro"));
        super.setMargin(new Insets(10, 10, 10, 10));
        super.setBorder(GuiResources.defaultBorder);
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
