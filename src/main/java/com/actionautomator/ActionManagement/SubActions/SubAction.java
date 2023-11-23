package com.actionautomator.ActionManagement.SubActions;

import java.awt.Robot;

public interface SubAction {
    SubActionType getType();

    void execute(Robot executor);
}