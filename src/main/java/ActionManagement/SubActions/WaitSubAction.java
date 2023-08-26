package ActionManagement.SubActions;

import java.awt.Robot;

public class WaitSubAction implements SubAction {
    private long waitTime;

    public WaitSubAction(long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public SubActionType getType() {
        return SubActionType.WAIT;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public void execute(Robot executor) {
        try {
            Thread.sleep(waitTime);
        } catch (InterruptedException ignored) {
            // Ignored because will get handled in Action execution.
        }
    }
}
