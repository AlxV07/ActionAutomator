package com.actionautomator.BindingManagement;

import com.actionautomator.ActionManagement.CodeActionBuilder;
import com.actionautomator.Gui.GuiFrame.BindingPanelContainer.BindingPanel.BindingButton;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.HashMap;

public class BindingManager {
    public final HashMap<String, Binding> bindings;
    private BindingButton activeBindingButton;

    public BindingManager() {
        this.bindings = new HashMap<>();
    }

    public void setBinding(String name, Binding binding) {
        bindings.put(name, binding);
    }

    /**
     * Removes a binding with the given name from the stored bindings
     * @param name The name of the target binding
     */
    public void removeBinding(String name) {
        bindings.remove(name);
    }

    /**
     * @param name The name of the target Binding
     * @return The Binding object with the given name
     */
    public Binding getBinding(String name) {
        return bindings.get(name);
    }

    public Binding findBinding(Binding candBinding) {
        //TODO: Optimize find process
        for (Binding binding : bindings.values()) {
            if (binding.keysEqual(candBinding)) {
                return binding;
            }
        }
        return null;
    }

    /**
     * Sets the code of the target binding
     * @param name The name of the target binding
     * @param code The new code to be set
     */
    public void setBindingCode(String name, String code) throws CodeActionBuilder.SyntaxError {
        bindings.get(name).setCode(code);
    }

    public void startBindingButton(BindingButton bindingButton) {
        if (activeBindingButton != null) {
            this.cancelBindingButton();
        }
        activeBindingButton = bindingButton;
    }

    public void cancelBindingButton() {
        activeBindingButton.completeButtonBind(-1);
        activeBindingButton = null;
    }

    public void completeBindingButton(int key) {
        if (key == NativeKeyEvent.VC_ESCAPE) {
            activeBindingButton.completeButtonBind(-1);
        } else {
            activeBindingButton.completeButtonBind(key);
        }
        activeBindingButton = null;
    }

    public boolean isBindingButton() {
        return activeBindingButton != null;
    }
}
