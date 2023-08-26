package ActionManagement;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ActionBuilder {
    private final CopyBuilder copyBuilder;
    private final ReadBuilder readBuilder;

    public ActionBuilder() {
        copyBuilder = new CopyBuilder();
        readBuilder = new ReadBuilder();
        copyBuilder.registerListeners();
    }

    public void end() {
        copyBuilder.removeListeners();
        try {
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }


    public Action copyBuild(int endKey, int waitKey) {
        copyBuilder.setEndKey(endKey);
        copyBuilder.setWaitKey(waitKey);
        copyBuilder.setIsListening(true);
        while (copyBuilder.getIsListening()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignore) {
            }
        }
        Action action = new Action(copyBuilder.getEvents());
        copyBuilder.clearEvents();
        return action;
    }

    public Action readBuild(String actnFilePath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(actnFilePath));
            return readBuilder.parseLinesIntoAction(lines);
        } catch (IOException e) {
            return null;
        }
    }
}
