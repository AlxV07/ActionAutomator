package Gui;

import BindingManagement.Binding;
import BindingManagement.BindingManager;
import Gui.AAComponents.AAPanel;

import javax.swing.*;
import java.awt.*;

public class BindingPanel extends AAPanel {
    /**
     * JPanel interface for interacting w/ Binding classes through GUI
     */
    private final BindingManager bindingManager;
    private final Binding binding;
    private final BindingButton[] buttons;

    public BindingPanel(BindingManager bindingManager, Binding binding) {
        super();
        super.setBorder(GuiResources.defaultBorder);
        this.bindingManager = bindingManager;
        this.binding = binding;
        this.buttons = new BindingButton[] {
            new BindingButton(bindingManager, this, 0),
            new BindingButton(bindingManager, this, 1),
            new BindingButton(bindingManager, this, 2),
            new BindingButton(bindingManager, this, 3),
        };
        for (BindingButton button : buttons) {
            super.add(button);
        }
        this.completeBind(0, -1);
    }

    /**
     * Remove BindingButton components from this panel, in preparation for removal of this panel
     */
    public void removeComponents() {
        bindingManager.removeBinding(binding.getName());
        for (BindingButton button : buttons) {
            super.remove(button);
        }
        revalidate();
        repaint();
    }

    /**
     * Set bounds for this panel and all it's BindingButtons
     * @param x X coord
     * @param y Y coord
     */
    public void setBounds(int x, int y) {
        super.setBounds(x, y, 300, 30);
        int i = 0;
        for (BindingButton button : buttons) {
            if (i > 0) {
                JButton b = buttons[i - 1];
                button.setBounds(b.getX() + b.getWidth(), y, 50, 20);
            } else {
                button.setBounds(0, 0, 50, 20);
            }
            i++;
        }
    }

    @Override
    public void updateColorTheme(Color primaryColor, Color secondaryColor, Color alternateColor) {
        for (BindingButton button : buttons) {
            button.updateColorTheme(primaryColor, secondaryColor, alternateColor);
        }
        super.updateColorTheme(primaryColor, secondaryColor, alternateColor);
    }

    /**
     * Complete the binding for a given index and key
     * @param idx The index of the bound key
     * @param key The key that was bound (NativeKeyEvent VC)
     */
    public void completeBind(int idx, int key) {
        if (key == -1) {
            binding.removeKey(binding.getKeySequence()[idx]);
            for (int i = idx; i < binding.getNofKeys() + 1; i++) {
                buttons[i].setEnabled(true);
                buttons[i].setKeyText(binding.getKeySequence()[i]);
            }
            for (int i = binding.getNofKeys() + 1; i < binding.getKeySequence().length; i++) {
                buttons[i].setEnabled(false);
                buttons[i].setKeyText(binding.getKeySequence()[i]);
            }
        } else {
            binding.setKey(idx, key);
            if (binding.getNofKeys() < buttons.length) {
                buttons[binding.getNofKeys()].setEnabled(true);
            }
            buttons[idx].setKeyText(key);
        }
    }
}
