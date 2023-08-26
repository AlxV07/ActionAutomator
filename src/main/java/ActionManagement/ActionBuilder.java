package ActionManagement;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.awt.event.KeyEvent;

public class ActionBuilder {
    private final ActionCopyBuilder actionCopyBuilder = new ActionCopyBuilder();

    public ActionBuilder() {
        actionCopyBuilder.registerListeners();
    }

    public void end() {
        actionCopyBuilder.removeListeners();
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }

    public Action copyBuild() {
        return copyBuild(KeyEvent.VK_ESCAPE, -1);
    }

    public Action copyBuild(int endKey, int waitKey) {
        actionCopyBuilder.setEndKey(endKey);
        actionCopyBuilder.setWaitKey(waitKey);
        actionCopyBuilder.setIsListening(true);
        while (actionCopyBuilder.getIsListening()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignore) {
            }
        }
        Action action = new Action(actionCopyBuilder.getEvents());
        actionCopyBuilder.clearEvents();
        return action;
    }

    public Action copyBuild(int endKey) {
        return copyBuild(endKey, -1);
    }
}
