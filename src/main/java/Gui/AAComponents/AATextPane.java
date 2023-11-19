package Gui.AAComponents;

import javax.swing.*;
import java.awt.*;

public class AATextPane extends JTextPane implements AAComponent {

    private Color alternateColor;

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
