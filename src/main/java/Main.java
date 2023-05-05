import ActionManagement.ActionBuilder;
import ActionManagement.ActionsHandler;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) throws NativeHookException {
        ActionsHandler handler = new ActionsHandler();
        ActionBuilder builder = new ActionBuilder();
        GlobalScreen.registerNativeHook();
        handler.setAction("a", builder.buildAction());
        builder.buildAction(KeyEvent.VK_ENTER); // WAY-TO-DELAY
        GlobalScreen.unregisterNativeHook();
        handler.executeAction("a", 20);
    }
}
