package ActionManagement.SubActions;

import java.awt.*;

public class ChangeSpeedSubAction implements SubAction{
    private long speed;

    public ChangeSpeedSubAction(long speed) {
        this.speed = speed;
    }

    @Override
    public SubActionType getType() {
        return SubActionType.CHANGE_SPEED;
    }

    public long getSpeed() {
        return this.speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    @Override
    public void execute(Robot executor) {
        throw new IllegalStateException();
    }
}
