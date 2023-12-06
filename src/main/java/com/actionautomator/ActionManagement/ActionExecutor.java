package com.actionautomator.ActionManagement;

import com.actionautomator.ActionManagement.SubActions.ChangeSpeedSubAction;
import com.actionautomator.ActionManagement.SubActions.RunSubAction;
import com.actionautomator.ActionManagement.SubActions.SubAction;
import com.actionautomator.ActionManagement.SubActions.SubActionType;
import com.actionautomator.BindingManagement.Binding;
import com.actionautomator.BindingManagement.BindingManager;

import java.awt.*;

public class ActionExecutor {
    private final Robot actionExecutor;
    private final BindingManager bindingManager;
    private Thread runningActionThread;

    public ActionExecutor(BindingManager bindingManager) {
        this.bindingManager = bindingManager;
        try {
            this.actionExecutor = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeAction(Action action, long delay) {
        if (action == null) {
            return;
        }
        this.interrupt();
        this.runningActionThread = new Thread(() -> {
            long runIntervalDelay = delay;
            for (SubAction subAction : action.subActions()) {
                try {
                    if (subAction.getType() == SubActionType.CHANGE_SPEED) {
                        runIntervalDelay = ((ChangeSpeedSubAction) subAction).getSpeed();
                    } else if (subAction.getType() == SubActionType.RUN){
                        executeAction(bindingManager.bindings.get(((RunSubAction) subAction).getName()).getAction(), runIntervalDelay);
                    } else {
                        subAction.execute(this.actionExecutor);
                    }
                    Thread.sleep(runIntervalDelay);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        this.runningActionThread.start();
    }

    public void executeActionFromBinding(Binding binding, int delay) {
        this.executeAction(binding.getAction(), delay);
    }

    public boolean isRunning() {
        return this.runningActionThread != null && this.runningActionThread.isAlive();
    }

    public void interrupt() {
        if (this.isRunning()) {
            this.runningActionThread.interrupt();
        }
    }
}
