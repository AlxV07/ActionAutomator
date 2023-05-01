import ActionSubTasks.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<ActionSubTask> s = new ArrayList<>();
        s.add(new KeySubTask(KeySubTaskType.PRESSED, 'H'));
        s.add(new KeySubTask(KeySubTaskType.RELEASED, 'H'));
        s.add(new MouseSubTask(MouseSubTaskType.MOVED, 1, 100, 100));
        ActionsHandler handler = new ActionsHandler();
        Action action = new Action(s);
        handler.setAction("a", action);
        handler.executeAction("a");

    }
}
