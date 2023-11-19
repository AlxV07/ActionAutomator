package Gui.AAComponents;

import javax.swing.*;
import java.awt.*;

public class AAPanel extends JPanel implements AAComponent {
    @Override
    public void updateColorTheme(Color primaryColor, Color secondaryColor, Color alternateColor) {
        super.setForeground(primaryColor);
        super.setBackground(secondaryColor);
    }
}
