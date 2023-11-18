package Gui;

import BindingManagement.Binding;
import BindingManagement.BindingManager;

import javax.swing.*;
import java.awt.*;

public class BindingPanel extends JPanel {
    private final BindingButton[] buttons;
    private final Binding binding;
    private final BindingManager bindingManager;

    public BindingPanel(BindingManager bindingManager, String actionName) {
        super();
        this.bindingManager = bindingManager;
        binding = new Binding();
        bindingManager.bindBindingToActionName(binding, actionName);
        bindingManager.bindActionNameToBinding(actionName, binding);
        buttons = new BindingButton[] {
            new BindingButton(bindingManager, this, 0),
            new BindingButton(bindingManager, this, 1),
            new BindingButton(bindingManager, this, 2),
            new BindingButton(bindingManager, this, 3),
        };
        buttons[0].completeBind(-1);
        super.setBorder(GuiResources.areaBorder);
    }

    public Binding getBinding() {
        return this.binding;
    }

    public BindingButton[] getButtons() {
        return this.buttons;
    }

    public void deleteComponents() {
        bindingManager.ActionNameToBinding.remove(bindingManager.BindingToActionName.remove(binding));
        for (BindingButton button : buttons) {
            super.remove(button);
        }
        revalidate();
        repaint();
    }

    public void setBounds(int x, int y) {
        super.setBounds(x, y, 300, 30);
        int i = 0;
        for (BindingButton button : buttons) {
            if (i > 0) {
                JButton b = buttons[i - 1];
                button.setBounds(b.getX() + b.getWidth(), y, 50, 20);
            } else {
                button.setBounds(x, y, 50, 20);
            }
            super.add(button);
            i++;
        }
    }
}
