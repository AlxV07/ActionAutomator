package com.actionautomator.BindingManagement;

import com.actionautomator.ActionManagement.CodeActionBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class BindingManager {
    public final HashMap<String, Binding> bindings;
    public final ArrayList<String> orderedBindingNames;
    private Runnable onSelectedChanged;
    private String selected;
    private String prevSelected;

    public BindingManager() {
        this.bindings = new HashMap<>();
        this.orderedBindingNames = new ArrayList<>();
    }

    public String getSelected() {
        return selected;
    }

    public String getPrevSelected() {
        return prevSelected;
    }

    public void setSelected(String name) {
        this.prevSelected = this.selected;
        this.selected = name;
        runOnSelectedChanged();
    }

    private void runOnSelectedChanged() {
        if (onSelectedChanged != null) {
            onSelectedChanged.run();
        }
    }

    public void setOnSelectedChanged(Runnable runnable) {
        this.onSelectedChanged = runnable;
    }

    public ArrayList<String> getOrderedBindingNames() {
        return this.orderedBindingNames;
    }

    public boolean addBinding(String name, Binding binding) {
        if (bindings.get(name) != null) {
            return false;
        }
        bindings.put(name, binding);
        orderedBindingNames.add(name);
        return true;
    }

    public boolean setBindingName(String oldName, String newName) {
        if (bindings.get(newName) != null) {
            return false;
        }
        bindings.put(newName, bindings.remove(oldName));
        orderedBindingNames.set(orderedBindingNames.indexOf(oldName), newName);
        prevSelected = null;
        selected = newName;
        return true;
    }

    public boolean setBindingCode(String name, String code) {
        try {
            if (bindings.get(name) == null) {
                return false;
            }
            bindings.get(name).setCode(code);
            return true;
        } catch (CodeActionBuilder.SyntaxError e) {
            return false;
        }
    }

    public void removeBinding(String name) {
        bindings.remove(name);
        int idx = orderedBindingNames.indexOf(name);
        orderedBindingNames.remove(idx);
        if (orderedBindingNames.isEmpty()) {
            setSelected(null);
        } else {
            setSelected(orderedBindingNames.get(Math.max(idx - 1, 0)));
        }
    }

    public Binding getBinding(String name) {
        return bindings.get(name);
    }

    public Binding findBinding(Binding candBinding) {
        for (Binding binding : bindings.values()) {
            if (binding.keysEqual(candBinding)) {
                return binding;
            }
        }
        return null;
    }
}
