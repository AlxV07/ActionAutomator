package Gui.AAComponents;

import javax.swing.*;
import java.awt.*;

public class AALabel extends JLabel implements AAComponent {
    @Override
    public void updateColorTheme(Color primaryColor, Color secondaryColor, Color alternateColor) {
        super.setForeground(primaryColor);
        super.setBackground(secondaryColor);
    }
}
