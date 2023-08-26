package ActionManagement;

import ActionManagement.SubActions.*;

import java.awt.*;
import java.util.List;

public class Action {
    private final List<SubAction> subActions;
    private Thread actionThread;
    private int subActionDelayInterval;

    public Action(List<SubAction> subActions) {
        this.subActions = subActions;
    }

    public List<SubAction> getSubActions() {
        return this.subActions;
    }

    public void execute(Robot executor, int subActionDelayInterval) {
        this.subActionDelayInterval = subActionDelayInterval;
        this.actionThread = new Thread(() -> executeSubActions(executor));
        this.actionThread.start();
    }

    private void executeSubActions(Robot executor) {
        for (SubAction subAction : subActions) {
            try {
                subAction.execute(executor);
                Thread.sleep(subActionDelayInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void interrupt() {
        if (actionThread != null) {
            actionThread.interrupt();
        }
    }
}
