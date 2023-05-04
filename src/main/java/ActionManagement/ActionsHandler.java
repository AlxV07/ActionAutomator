package ActionManagement;

import ActionManagement.ActionSubTasks.ActionSubTask;

import java.awt.*;
import java.util.HashMap;
import java.util.List;

public class ActionsHandler {
    private final HashMap<String, Action> actions;
    private final Robot r;

    public ActionsHandler() {
        this.actions = new HashMap<>();
        try {
            this.r = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAction(String key, List<ActionSubTask> subTasks) {
        actions.put(key, new Action(subTasks));
    }

    public void setAction(String key, Action a) {
        actions.put(key, a);
    }

    public void removeAction(String key) {
        actions.remove(key);
    }

    public void executeAction(String key) {
        actions.get(key).execute(r);
    }

    public void interruptAction(String key) {
        actions.get(key).interrupt();
    }
}
