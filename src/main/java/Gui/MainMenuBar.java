package Gui;

import javax.swing.*;
import java.awt.*;

public class MainMenuBar extends JMenuBar {
    public MainMenuBar() {
        super();
        super.setFont(Font.getFont("Source Code Pro"));
        super.setMargin(GuiResources.noMargin);
        super.setBorder(GuiResources.areaBorder);
    }
}
