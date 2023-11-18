package ActionManagement;

import ActionManagement.SubActions.*;

import java.awt.*;
import java.util.List;

public class Action {
    private final List<SubAction> subActions;
    private Thread actionThread;

    public Action(List<SubAction> subActions) {
        this.subActions = subActions;
    }

    public List<SubAction> getSubActions() {
        return this.subActions;
    }

    public void execute(Robot executor, int subActionDelayInterval) {
        this.actionThread = new Thread(() ->
        {
            for (SubAction subAction : subActions) {
                try {
                    subAction.execute(executor);
                    Thread.sleep(subActionDelayInterval);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        this.actionThread.start();
    }

    public boolean isRunning() {
        return this.actionThread != null && this.actionThread.isAlive();
    }

    public void interrupt() {
        if (this.isRunning()) {
            this.actionThread.interrupt();
        }
    }
}
