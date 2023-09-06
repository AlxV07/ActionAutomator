package Gui.TextEditorGui;

import Gui.LegacyGui.PresetButton;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeybindingButton extends PresetButton implements KeyListener {
    final String defaultText;
    boolean waitingForKey = false;
    int key;
    final JLabel label;

    public KeybindingButton(String defaultText, String buttonText, JLabel label) {
        super(buttonText);
        this.defaultText = defaultText;
        this.label = label;
        this.label.setText(defaultText);
        addActionListener(e -> {
            this.label.setText("Press a key... (Backspace For Default)");
            requestFocusInWindow();
            waitingForKey = true;
        });
        addKeyListener(this);
        setFocusPainted(false);
    }

    public void keyPressed(KeyEvent e) {
        if (waitingForKey) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_BACK_SPACE) {
                key = -1;
                label.setText(defaultText);
            } else {
                key = e.getKeyCode();
                label.setText(KeyEvent.getKeyText(keyCode));
            }
            waitingForKey = false;
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}
