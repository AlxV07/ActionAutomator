package Gui.LegacyGui;

import javax.swing.*;

public class PresetLabel extends JLabel {
    public PresetLabel() {
        setupLabel();
    }

    public PresetLabel(String text) {
        super(text);
        setupLabel();
    }

    private void setupLabel() {
        super.setFont(Resources.labelFont);
    }
}
