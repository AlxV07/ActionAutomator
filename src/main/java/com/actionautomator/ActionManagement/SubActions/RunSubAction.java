package com.actionautomator.ActionManagement.SubActions;

import java.awt.*;

public class RunSubAction implements SubAction {
    private final String name;

    public RunSubAction(String name) {
        this.name = name;
    }
    @Override
    public SubActionType getType() {
        return SubActionType.RUN;
    }

    @Override
    public void execute(Robot executor) {
        throw new IllegalStateException();
    }

    public String getName() {
        return name;
    }
}
