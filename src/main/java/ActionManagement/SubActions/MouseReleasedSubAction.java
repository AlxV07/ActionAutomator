package ActionManagement.SubActions;

import java.awt.Robot;

public class MouseReleasedSubAction implements SubAction {
    private final int button;

    public MouseReleasedSubAction(int button) {
        this.button = button;
    }

    @Override
    public SubActionType getType() {
        return SubActionType.MOUSE_PRESSED;
    }

    @Override
    public void execute(Robot executor) {
        executor.mouseRelease(button);
    }
}
