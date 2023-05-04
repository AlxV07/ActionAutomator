import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main {
    public static void main(String[] args) throws NativeHookException, InterruptedException {
        GlobalScreen.registerNativeHook();

        ActionSubTaskSequenceBuilder b = new ActionSubTaskSequenceBuilder();
        ActionsHandler handler = new ActionsHandler();

        Thread.sleep(1000);
        System.out.println("Start Doing Stuff:");
        b.registerListeners();
        Thread.sleep(5000);
        System.out.println("\nEnding In 1 Second:");
        Thread.sleep(1000);
        b.removeListeners();
        GlobalScreen.unregisterNativeHook();

        System.out.println("Event list " + b.getEvents());
        handler.setAction("a", b.getEvents());
        b.clearEvents();

        System.out.println("Executing Action:");
        Thread.sleep(100);
        handler.executeAction("a");
    }
}
