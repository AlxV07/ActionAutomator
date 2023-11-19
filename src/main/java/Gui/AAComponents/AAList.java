package Gui.AAComponents;

import javax.swing.*;
import java.awt.*;

public class AAList <E> extends JList<E> implements AAComponent {

    public Color alternateColor;

    public AAList() {
        super(new DefaultListModel<>());
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
