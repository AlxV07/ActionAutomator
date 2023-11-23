package com.actionautomator.ActionManagement.SubActions;

import java.awt.Robot;

public class MouseMovedSubAction implements SubAction {
    private final int x;
    private final int y;

    public MouseMovedSubAction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public SubActionType getType() {
        return SubActionType.MOUSE_MOVED;
    }

    @Override
    public void execute(Robot executor) {
        executor.mouseMove(x, y);
    }
}
