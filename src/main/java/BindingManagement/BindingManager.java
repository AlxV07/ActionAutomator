package BindingManagement;

import Gui.BindingButton;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.HashMap;

public class BindingManager {
    public final HashMap<String, Binding> bindings;
    private BindingButton activeBindingButton;

    public BindingManager() {
        this.bindings = new HashMap<>();
    }

    /**
     * Creates and adds a new binding to the stored bindings, if a binding with the given name does not already exit
     * @param name The name to set the new binding to
     */
    public void createNewBinding(String name) {
        bindings.put(name, new Binding(name));
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
    public void setBindingCode(String name, String code) {
        bindings.get(name).setCode(code);
    }

    /**
     * Renames the target binding
     * @param oldName The name of the target binding
     * @param newName The new name for the target binding
     */
    public void setBindingName(String oldName, String newName) {
        bindings.get(oldName).setName(newName);
        bindings.put(newName, bindings.remove(oldName));
    }

    public void startBindingButton(BindingButton bindingButton) {
        if (activeBindingButton != null) {
            this.cancelBindingButton();
        }
        activeBindingButton = bindingButton;
    }

    public void cancelBindingButton() {
        activeBindingButton.completeBind(-1);
        activeBindingButton = null;
    }

    public void completeBindingButton(int key) {
        if (key == NativeKeyEvent.VC_ESCAPE) {
            activeBindingButton.completeBind(-1);
        } else {
            activeBindingButton.completeBind(key);
        }
        activeBindingButton = null;
    }

    public boolean isBindingButton() {
        return activeBindingButton != null;
    }
}
