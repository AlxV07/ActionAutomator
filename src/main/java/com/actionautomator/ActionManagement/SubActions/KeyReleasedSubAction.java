package com.actionautomator.ActionManagement.SubActions;

import java.awt.Robot;

public class KeyReleasedSubAction implements SubAction {
    private final int vk_key;

    public KeyReleasedSubAction(int vk_key) {
        this.vk_key = vk_key;  // VK_KeyCode
    }

    @Override
    public SubActionType getType() {
        return SubActionType.KEY_RElEASED;
    }

    @Override
    public void execute(Robot executor) {
        executor.keyRelease(vk_key);
    }
}
