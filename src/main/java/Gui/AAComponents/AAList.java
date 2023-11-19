package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class AAList <E> extends JList<E> implements AAComponent {

    public Color alternateColor;

    public AAList() {
        super(new DefaultListModel<>());
        super.setFont(new Font("Arial", Font.PLAIN, 12));
        super.setBorder(GuiResources.defaultBorder);
    }

    public Color getAlternateColor() {
        return alternateColor;
    }

    @Override
    public void updateColorTheme(Color primaryColor, Color secondaryColor, Color alternateColor) {
        super.setForeground(primaryColor);
        super.setBackground(secondaryColor);
        this.alternateColor = alternateColor;
    }
}
