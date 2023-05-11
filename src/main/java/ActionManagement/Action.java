package ActionManagement;

import ActionManagement.ActionSubTasks.*;

import java.awt.*;
import java.util.List;

public class Action {
    private final List<ActionSubTask> subTasks;
    private Thread actionThread;
    private int speed;

    public Action(List<ActionSubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public List<ActionSubTask> getSubTasks() {
        return this.subTasks;
    }

    public void execute(Robot r) {
        execute(r, 100);
    }

    public void execute(Robot r, int speed) {
        this.speed = speed;
        actionThread = new Thread(() -> iExecute(r));
        actionThread.start();
    }

    private void iExecute(Robot r) {
        for (ActionSubTask s : subTasks) {
            switch (s.getTypeOfTask()) {
                case ActionSubTaskTypes.KEYBOARD_TASK -> {
                    KeySubTask s1 = (KeySubTask) s;
                    if (s1.specType() == KeySubTaskType.PRESSED) {
                        r.keyPress(s1.k());
                    } else {
                        r.keyRelease(s1.k());
                    }
                }
                case ActionSubTaskTypes.MOUSE_TASK -> {
                    MouseSubTask s2 = (MouseSubTask) s;
                    switch (s2.specType()) {
                        case PRESSED -> r.mousePress(s2.button());
                        case RELEASED -> r.mouseRelease(s2.button());
                        case MOVED -> r.mouseMove(s2.x(), s2.y());
                        case DRAGGED -> {
                            r.mousePress(s2.button());
                            r.mouseMove(s2.x(), s2.y());
                            r.mouseRelease(s2.button());
                        }
                    }
                }
            }
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }

    public void interrupt() {
        if (actionThread != null) {
            actionThread.interrupt();
        }
    }
}
