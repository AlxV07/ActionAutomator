package Gui;

import javax.swing.*;

public class PresetButton extends JButton {
    public PresetButton() {
        super();
        setupButton();
    }

    public PresetButton(String text) {
        super(text);
        setupButton();
    }

    private void setupButton() {
        super.setMargin(Resources.buttonInsets);
        super.setFont(Resources.buttonFont);

        super.setFocusPainted(false);
    }
}
