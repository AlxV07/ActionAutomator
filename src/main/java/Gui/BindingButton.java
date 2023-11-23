package Gui;

import BindingManagement.BindingManager;
import Gui.AAComponents.AAButton;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

public class BindingButton extends AAButton {
    private final BindingPanel bindingPanel;
    private final int idx;

    public BindingButton(BindingManager bindingManager, BindingPanel bindingPanel, int idx) {
        super("null");
        super.setFont(GuiResources.smallerFont);
        this.bindingPanel = bindingPanel;
        this.idx = idx;
        addActionListener(e -> {
            bindingManager.startBindingButton(this);
            super.setText("Binding...");
            super.setForeground(secondaryColor);
            super.setBackground(primaryColor);
            super.requestFocusInWindow();
        });
    }

    /**
     * Set key text to be displayed on button, updates background color if marker position
     * @param key The key whose text is to be displayed (NativeKeyEvent VC)
     */
    public void setKeyText(int key) {
        if (key == -1) {
            super.setText("null");
        } else {
            super.setText(NativeKeyEvent.getKeyText(key));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    /**
     * Complete a binding process with the binding panel
     * @param key The resulting key of the binding process (NativeKeyEvent VC)
     */
    public void completeBind(int key) {
        super.setForeground(primaryColor);
        if (darkMode) {
            super.setBackground(GuiResources.darkThemeColor);
        } else {
            super.setBackground(GuiResources.lightThemeColor);
        }
        bindingPanel.completeBind(this.idx, key);
    }
}
