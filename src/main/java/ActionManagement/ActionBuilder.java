package ActionManagement;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.awt.event.KeyEvent;

public class ActionBuilder {
    private final ActionSubTaskSequenceBuilder builder = new ActionSubTaskSequenceBuilder();

    public ActionBuilder() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
        builder.registerListeners();
    }

    public void end() {
        builder.removeListeners();
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }

    public Action buildAction() {
        return buildAction(KeyEvent.VK_ESCAPE);
    }

    public Action buildAction(int endKey) {
        builder.setEndKey(endKey);
        builder.setIsListening(true);
        while (builder.getIsListening()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignore) {
            }
        }
        Action action = new Action(builder.getEvents());
        builder.clearEvents();
        return action;
    }
}
