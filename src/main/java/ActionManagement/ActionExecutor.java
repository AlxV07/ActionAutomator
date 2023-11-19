package ActionManagement;

import ActionManagement.SubActions.ChangeSpeedSubAction;
import ActionManagement.SubActions.SubAction;
import ActionManagement.SubActions.SubActionType;

import java.awt.*;
import java.util.List;

public class ActionExecutor {
    private final Robot actionExecutor;
    private final CodeActionBuilder codeActionBuilder;
    private Thread runningActionThread;

    public ActionExecutor() {
        try {
            this.actionExecutor = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        codeActionBuilder = new CodeActionBuilder();
    }

    public void executeAction(Action action, int delay) {
        this.interrupt();
        this.runningActionThread = new Thread(() -> {
            long runIntervalDelay = delay;
            for (SubAction subAction : action.subActions()) {
                try {
                    if (subAction.getType() == SubActionType.CHANGE_SPEED) {
                        runIntervalDelay = ((ChangeSpeedSubAction) subAction).getSpeed();
                    }
                    subAction.execute(this.actionExecutor);
                    Thread.sleep(runIntervalDelay);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        this.runningActionThread.start();
    }

    public void executeActionFromCode(String code, int delay) throws CodeActionBuilder.SyntaxError {
        this.executeAction(codeActionBuilder.parseCodeIntoAction(code), delay);
    }

    public boolean isRunning() {
        return this.runningActionThread != null && this.runningActionThread.isAlive();
    }

    public void interrupt() {
        if (this.isRunning()) {
            this.runningActionThread.interrupt();
        }
    }

    public void cleanAction(Action action) {
        List<SubAction> tasks = action.subActions();
        int i;
        boolean nextSubAction;
        for (i = 0; i < tasks.size() - 1;) {
            nextSubAction = true;
            SubAction last_move = tasks.get(i);
            if (last_move.getType() == SubActionType.MOUSE_MOVED) {
                if (tasks.get(i + 1).getType() == SubActionType.MOUSE_MOVED) {
                    tasks.remove(i);
                    nextSubAction = false;
                }
            }
            if (nextSubAction) {
                i++;
            }
        }
    }
}
