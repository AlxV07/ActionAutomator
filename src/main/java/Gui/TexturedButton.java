package Gui;

import javax.swing.*;

public class TexturedButton extends JButton {
    public TexturedButton() {
        super();
        super.setFont(GuiResources.labelFont);
        super.setMargin(GuiResources.noMargin);
        super.setFocusable(false);
    }

    public TexturedButton(String title) {
        super(title);
        super.setFont(GuiResources.labelFont);
        super.setMargin(GuiResources.noMargin);
    }
}
