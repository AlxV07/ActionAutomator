package ActionManagement.SubActions;

import java.awt.Robot;

public class KeyPressedSubAction implements SubAction {
    private final int vk_key;

    public KeyPressedSubAction(int vk_key) {
        this.vk_key = vk_key;  // VK_KeyCode
    }

    @Override
    public SubActionType getType() {
        return SubActionType.KEY_PRESSED;
    }

    @Override
    public void execute(Robot executor) {
        executor.keyPress(vk_key);
    }
}
