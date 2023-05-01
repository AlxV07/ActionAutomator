import ActionSubTasks.*;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

import java.util.ArrayList;
import java.util.List;

public class ActionSubTaskSequenceBuilder {
    private final KeyboardListener keyListener;
    private final MouseListener mouseListener;
    private final List<ActionSubTask> events;

    public ActionSubTaskSequenceBuilder() {
        this.keyListener = new KeyboardListener();
        this.mouseListener = new MouseListener();
        this.events = new ArrayList<>();
    }

    public void registerListeners() {
        GlobalScreen.addNativeKeyListener(this.keyListener);
        GlobalScreen.addNativeMouseListener(this.mouseListener);
        GlobalScreen.addNativeMouseMotionListener(this.mouseListener);
    }

    public void removeListeners() {
        GlobalScreen.removeNativeKeyListener(this.keyListener);
        GlobalScreen.removeNativeMouseListener(this.mouseListener);
        GlobalScreen.removeNativeMouseMotionListener(this.mouseListener);
    }

    public List<ActionSubTask> getEvents() {
        return this.events;
    }

    public void clearEvents() {
        this.events.clear();
    }

    private class KeyboardListener implements NativeKeyListener {
        @Override
        public void nativeKeyPressed(NativeKeyEvent event) {
            events.add(new KeySubTask(KeySubTaskType.PRESSED, Character.toLowerCase(NativeKeyEvent.getKeyText(event.getKeyCode()).charAt(0))));
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent event) {
            events.add(new KeySubTask(KeySubTaskType.RELEASED, Character.toLowerCase(NativeKeyEvent.getKeyText(event.getKeyCode()).charAt(0))));
        }
    }

    private class MouseListener implements NativeMouseInputListener {
        @Override
        public void nativeMousePressed(NativeMouseEvent event) {
            events.add(new MouseSubTask(MouseSubTaskType.PRESSED, event.getButton(), event.getX(), event.getY()));
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent event) {
            events.add(new MouseSubTask(MouseSubTaskType.RELEASED, event.getButton(), event.getX(), event.getY()));
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent event) {
            events.add(new MouseSubTask(MouseSubTaskType.MOVED, event.getButton(), event.getX(), event.getY()));
        }

        @Override
        public void nativeMouseDragged(NativeMouseEvent event) {
            events.add(new MouseSubTask(MouseSubTaskType.DRAGGED, event.getButton(), event.getX(), event.getY()));
        }
    }
}
