package ActionManagement;

import ActionManagement.ActionSubTasks.ActionSubTask;
import ActionManagement.ActionSubTasks.ActionSubTaskTypes;
import ActionManagement.ActionSubTasks.MouseSubTask;
import ActionManagement.ActionSubTasks.MouseSubTaskType;

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

    public void executeAction(String key, int speed) {
        actions.get(key).execute(r, speed);
    }

    public void interruptAction(String key) {
        actions.get(key).interrupt();
    }

    public void interruptAll() {
        for (Action a : actions.values()) {
            a.interrupt();
        }
    }

    public void cleanAction(String key) {
        removeUnnecessaryMouseMovementsFromAction(key);
    }

    private void removeUnnecessaryMouseMovementsFromAction(String key) {
        List<ActionSubTask> tasks = actions.get(key).getSubTasks();
        int i;
        for (i = 0; i < tasks.size() - 1;) {
            boolean proceed = true;
            if (tasks.get(i).getTypeOfTask() == ActionSubTaskTypes.MOUSE_TASK) {
                MouseSubTask s1 = (MouseSubTask) tasks.get(i);
                if (s1.specType() == MouseSubTaskType.MOVED) {
                    if (tasks.get(i + 1).getTypeOfTask() == ActionSubTaskTypes.MOUSE_TASK) {
                        MouseSubTask s2 = (MouseSubTask) tasks.get(i + 1);
                        if (s2.specType() == MouseSubTaskType.MOVED) {
                            tasks.remove(i);
                            proceed = false;
                        }
                    }
                }
            }
            if (proceed) {
                i++;
            }
        }
    }
}
