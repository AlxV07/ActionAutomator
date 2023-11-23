package Gui;

import BindingManagement.Binding;
import BindingManagement.BindingManager;
import Gui.AAComponents.AAButton;
import Gui.AAComponents.AALabel;
import Gui.AAComponents.AAPanel;
import Gui.AAComponents.AATextArea;

import javax.swing.*;
import java.awt.*;

public class BindingPanel extends AAPanel {
    /**
     * JPanel interface for interacting w/ Binding classes through GUI
     */
    private final Binding binding;
    private final BindingButton[] bindingButtons;
    private final AAButton editButton;
    private final AATextArea nameLabel;

    public BindingPanel(BindingManager bindingManager, Binding binding, BindingPanelBox scrollPane) {
        super();
        super.setBorder(GuiResources.darkThemeBorder);
        this.setLayout(null);
        this.binding = binding;
        this.nameLabel = new AATextArea();
        this.nameLabel.setText(binding.getName());
        this.editButton = new AAButton("Edit");
        add(nameLabel);
        this.editButton.addActionListener(e -> scrollPane.setSelected(scrollPane.names.indexOf(binding.getName())));
        add(editButton);
        this.bindingButtons = new BindingButton[] {
            new BindingButton(bindingManager, this, 0),
            new BindingButton(bindingManager, this, 1),
            new BindingButton(bindingManager, this, 2),
            new BindingButton(bindingManager, this, 3),
        };
        for (BindingButton button : bindingButtons) {
            add(button);
        }
        this.completeBind(0, -1);
    }

    @Override
    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
        super.updateColorTheme(darkMode, primaryColor, secondaryColor);
    }

    /**
     * Set bounds for this panel and all it's BindingButtons
     * @param x X coord
     * @param y Y coord
     */
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        height -= 10;
        int i = 0;
        int compX = 5;
        int compY = 5;
        int buttonW = 85;
        nameLabel.setBounds(compX, compY, 80, height);
        editButton.setBounds(compX + 80, compY, 50, height);
        for (BindingButton button : bindingButtons) {
            if (i > 0) {
                JButton b = bindingButtons[i - 1];
                button.setBounds(b.getX() + b.getWidth(), compY, buttonW, height);
            } else {
                button.setBounds(compX + 140, compY, buttonW, height);
            }
            i++;
        }
    }

    /**
     * Complete the binding for a given index and key
     * @param idx The index of the bound key
     * @param key The key that was bound (NativeKeyEvent VC)
     */
    public void completeBind(int idx, int key) {
        if (key == -1 || binding.containsKey(key)) {
            binding.removeKey(binding.getKeySequence()[idx]);
            for (int i = idx; i < binding.getNofKeys() + 1; i++) {
                bindingButtons[i].setEnabled(true);
                bindingButtons[i].setKeyText(binding.getKeySequence()[i]);
            }
            for (int i = binding.getNofKeys() + 1; i < binding.getKeySequence().length; i++) {
                bindingButtons[i].setEnabled(false);
                bindingButtons[i].setKeyText(binding.getKeySequence()[i]);
            }
        } else {
            binding.setKey(idx, key);
            if (binding.getNofKeys() < bindingButtons.length) {
                bindingButtons[binding.getNofKeys()].setEnabled(true);
            }
            bindingButtons[idx].setKeyText(key);
        }
        bindingButtons[0].setEnabled(true);
    }
}
