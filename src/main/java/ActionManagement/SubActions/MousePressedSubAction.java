package ActionManagement.SubActions;

import java.awt.Robot;

public class MousePressedSubAction implements SubAction {
    private final int button;

    public MousePressedSubAction(int button) {
        this.button = button;
    }

    @Override
    public SubActionType getType() {
        return SubActionType.MOUSE_PRESSED;
    }

    @Override
    public void execute(Robot executor) {
        executor.mousePress(button);
    }
}
