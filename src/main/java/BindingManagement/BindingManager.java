package BindingManagement;

import Gui.BindingButton;
import Gui.BindingPanel;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.HashMap;

public class BindingManager {
    public final HashMap<String, String> ActionNameToCode;
    public final HashMap<Binding, String> BindingToActionName;
    public final HashMap<String, Binding> ActionNameToBinding;
    public final HashMap<String, BindingPanel> ActionNameToBindingPanel;
    private BindingButton activeBindingButton;

    public BindingManager() {
        ActionNameToCode = new HashMap<>();
        ActionNameToBinding = new HashMap<>();
        BindingToActionName = new HashMap<>();
        ActionNameToBindingPanel = new HashMap<>();
    }

    public String getActionNameFromBinding(Binding binding) {
        for (Binding b : BindingToActionName.keySet()) {
            if (b.equals(binding)) {
                return BindingToActionName.get(b);
            }
        }
        return null;
    }

    public void bindActionNameToCode(String actionName, String code) {
        ActionNameToCode.put(actionName, code);
    }

    public void bindActionNameToBinding(String actionName, Binding binding) {
        ActionNameToBinding.put(actionName, binding);
    }

    public void bindBindingToActionName(Binding binding, String actionName) {
        BindingToActionName.put(binding, actionName);
    }

    public void startBindingButton(BindingButton bindingButton) {
        if (activeBindingButton != null) {
            this.cancelBindingButton();
        }
        activeBindingButton = bindingButton;
    }

    public void cancelBindingButton() {
        if (activeBindingButton != null) {
            activeBindingButton.completeBind(-1);
            activeBindingButton = null;
        }
    }

    public void completeBindingButton(int key) {
        if (activeBindingButton != null) {
            if (key == NativeKeyEvent.VC_ESCAPE) {
                activeBindingButton.completeBind(-1);
            } else {
                activeBindingButton.completeBind(key);
            }
            activeBindingButton = null;
        }
    }

    public boolean isBinding() {
        return activeBindingButton != null;
    }

    public BindingPanel getBindingPanelFromActionName(String actionName) {
        return ActionNameToBindingPanel.get(actionName);
    }

    public void bindActionNameTOBindingPanel(String actionName, BindingPanel bindingPanel) {
        this.ActionNameToBindingPanel.put(actionName, bindingPanel);
    }
}
