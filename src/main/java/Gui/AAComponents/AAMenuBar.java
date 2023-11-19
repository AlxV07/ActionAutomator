package Gui.AAComponents;

import Gui.GuiResources;

import javax.swing.*;
import java.awt.*;

public class AAMenuBar extends JMenuBar implements AAComponent {
    public AAMenuBar() {
        super();
        super.setFont(Font.getFont("Source Code Pro"));
        super.setMargin(GuiResources.noMargin);
        super.setBorder(GuiResources.areaBorder);
    }

    @Override
    public void updateColorTheme(Color primaryColor, Color secondaryColor, Color alternateColor) {
        super.setForeground(primaryColor);
        super.setBackground(secondaryColor);
    }
}
