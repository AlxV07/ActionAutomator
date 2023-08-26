package ActionManagement;

import ActionManagement.SubActions.SubAction;
import ActionManagement.SubActions.SubActionType;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class ActionsHandler {
    private final Robot actionExecutor;
    private final HashMap<String, Action> actions;

    public ActionsHandler() {
        this.actions = new HashMap<>();
        try {
            this.actionExecutor = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAction(String key, Action a) {
        actions.put(key, a);
    }

    public void removeAction(String key) {
        actions.remove(key);
    }

    public void executeAction(String key, int speed) {
        actions.get(key).execute(actionExecutor, speed);
    }

    public void interrupt() {
        for (Action a : actions.values()) {
            a.interrupt();
        }
    }

    public void cleanAction(String key) {
        removeUnnecessaryMouseMovementsFromAction(key);
    }

    private void removeUnnecessaryMouseMovementsFromAction(String key) {
        List<SubAction> tasks = actions.get(key).getSubActions();
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
