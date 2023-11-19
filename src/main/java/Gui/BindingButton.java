package Gui;

import BindingManagement.BindingManager;
import Gui.AAComponents.AAButton;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;

public class BindingButton extends AAButton {
    private final BindingPanel bindingPanel;
    private final int idx;

    public BindingButton(BindingManager bindingManager, BindingPanel bindingPanel, int idx) {
        super();
        super.setFocusable(false);
        super.setFont(GuiResources.bindButtonFont);
        this.bindingPanel = bindingPanel;
        this.idx = idx;
        addActionListener(e -> {
            bindingManager.startBindingButton(this);
            super.setText("Binding...");
            super.requestFocusInWindow();
        });
    }

    /**
     * Set key text to be displayed on button, updates background color if marker position
     * @param key The key whose text is to be displayed (NativeKeyEvent VC)
     */
    public void setKeyText(int key) {
        if (key == -1) {
            if (super.isEnabled()) {
                super.setBackground(getAlternateColor());
            } else {
                super.setBackground(getBackground());
            }
            super.setText("null");
        } else {
            super.setBackground(getBackground());
            super.setText(NativeKeyEvent.getKeyText(key));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            if (super.getText().equals("null")) {
                super.setBackground(getAlternateColor());
            } else {
                super.setBackground(getBackground());
            }
        }
        super.setEnabled(enabled);
    }

    /**
     * Complete a binding process with the binding panel
     * @param key The resulting key of the binding process (NativeKeyEvent VC)
     */
    public void completeBind(int key) {
        bindingPanel.completeBind(this.idx, key);
    }
}