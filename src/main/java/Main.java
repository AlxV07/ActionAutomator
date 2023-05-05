import ActionManagement.ActionBuilder;
import ActionManagement.ActionsHandler;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main {
    public static void main(String[] args) throws NativeHookException {
        ActionsHandler handler = new ActionsHandler();
        ActionBuilder builder = new ActionBuilder();
        GlobalScreen.registerNativeHook();
        handler.setAction("sayHi", builder.buildAction());
        handler.setAction("sayBy", builder.buildAction());
        GlobalScreen.unregisterNativeHook();
        handler.executeAction("sayHi");
        handler.executeAction("sayBy");
    }
}
