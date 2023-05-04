import ActionSubTasks.ActionSubTask;
import ActionSubTasks.KeySubTask;
import ActionSubTasks.KeySubTaskType;
import ActionSubTasks.MouseSubTask;

import java.awt.*;
import java.util.List;

public class Action {
    private final List<ActionSubTask> subTasks;
    private final Thread actionThread;
    private Robot r;

    public Action(List<ActionSubTask> subTasks) {
        this.subTasks = subTasks;
        this.actionThread = new Thread(this::iExecute);
    }

    public void execute(Robot r) {
        this.r = r;
        actionThread.start();
    }

    private void iExecute() {
        for (ActionSubTask s : subTasks) {
            if (s.isKeyboardTask()) {
                KeySubTask s1 = (KeySubTask) s;
                if (s1.t() == KeySubTaskType.PRESSED) {
                    r.keyPress(s1.k());
                } else {
                    r.keyRelease(s1.k());
                }
            } else {
                MouseSubTask s2 = (MouseSubTask) s;
                switch (s2.t()) {
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
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void interrupt() {
        actionThread.interrupt();
    }
}
