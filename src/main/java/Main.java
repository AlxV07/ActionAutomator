import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

public class Main {
    public static void main(String[] args) throws NativeHookException, InterruptedException {
//        ArrayList<ActionSubTask> s;
        //        s.add(new KeySubTask(KeySubTaskType.PRESSED, 'H'));
//        s.add(new KeySubTask(KeySubTaskType.RELEASED, 'H'));
//        s.add(new MouseSubTask(MouseSubTaskType.MOVED, 1, 100, 100));

        GlobalScreen.registerNativeHook();

        ActionSubTaskSequenceBuilder b = new ActionSubTaskSequenceBuilder();
        ActionsHandler handler = new ActionsHandler();

        Thread.sleep(1000);
        b.registerListeners();
        Thread.sleep(5000);
        b.removeListeners();
        GlobalScreen.unregisterNativeHook();

        handler.setAction("a", b.getEvents());
        System.out.println("Event list " + b.getEvents());

        System.out.println("Executing Action");
        handler.executeAction("a");
    }
}
