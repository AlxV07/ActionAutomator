package ActionManagement;

import java.awt.event.KeyEvent;

public class ActionBuilder {
    private final ActionSubTaskSequenceBuilder builder = new ActionSubTaskSequenceBuilder();

    public Action buildAction() {
        return buildAction(KeyEvent.VK_ESCAPE);
    }

    public Action buildAction(int endKey) {
        builder.setEndKey(endKey);
        builder.registerListeners();
        while (builder.getIsListening()) {
        }
        Action action = new Action(builder.getEvents());
        builder.clearEvents();
        return action;
    }
}
