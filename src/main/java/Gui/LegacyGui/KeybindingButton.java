package Gui.LegacyGui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeybindingButton extends PresetButton implements KeyListener {
    boolean waitingForKey = false;
    int keyPressed;
    final JLabel label;

    public KeybindingButton(String text, JLabel label) {
        super(text);
        this.label = label;
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
                keyPressed = 0;
                label.setText("Default (Escape)");
            } else {
                keyPressed = e.getKeyCode();
                label.setText(KeyEvent.getKeyText(keyCode));
            }
            waitingForKey = false;
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
}
