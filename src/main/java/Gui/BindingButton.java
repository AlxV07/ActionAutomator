package Gui;

import BindingManagement.BindingManager;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.Arrays;

public class BindingButton extends TexturedButton {
    private final BindingPanel parentBindingPanel;
    private final int idx;
    public int key;

    public BindingButton(BindingManager bindingManager, BindingPanel parentBindingPanel, int idx) {
        super();
        super.setFocusable(false);
        this.parentBindingPanel = parentBindingPanel;
        this.idx = idx;
        super.setFont(GuiResources.bindButtonFont);
        addActionListener(e -> {
            bindingManager.startBindingButton(this);
            super.setText("Binding...");
            requestFocusInWindow();
        });
        super.setAlignmentX(0);
    }

    public void completeBind(int key) {
        this.key = key;
        if (parentBindingPanel.getBinding().containsKey(key)) {
            this.key = -1;
        }
        parentBindingPanel.getBinding().setKey(idx, this.key);
        String k;
        if (this.key == -1) {
            if (idx + 1 < parentBindingPanel.getButtons().length) {
                parentBindingPanel.getButtons()[idx + 1].completeBind(-1);
                parentBindingPanel.getButtons()[idx + 1].setEnabled(false);
            }
            k = "null";
        } else {
            k = NativeKeyEvent.getKeyText(this.key);
            if (idx + 1 < parentBindingPanel.getButtons().length) {
                parentBindingPanel.getButtons()[idx + 1].setEnabled(true);
            }
        }
        super.setText(k);
    }
}
