package ActionManagement;

import ActionManagement.ActionSubTasks.*;
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
    private List<ActionSubTask> events;
    private int endKey;
    private int waitKey;
    private boolean isListening;

    private boolean isWaiting;
    private long waitStartTime;

    public ActionSubTaskSequenceBuilder() {
        this.keyListener = new KeyboardListener();
        this.mouseListener = new MouseListener();
        this.events = new ArrayList<>();
    }

    public void setWaitKey(int waitKey) {
        this.waitKey = waitKey;
    }

    public void setEndKey(int endKey) {
        this.endKey = endKey;
    }

    public synchronized boolean getIsListening() {
        return this.isListening;
    }

    public void setIsListening(boolean b) {
        this.isListening = b;
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
        return new ArrayList<>(this.events);
    }

    public void clearEvents() {
        this.events = new ArrayList<>();
    }


    private class KeyboardListener implements NativeKeyListener {
        @Override
        public void nativeKeyPressed(NativeKeyEvent event) {
            int n = NativeKeyToVKKeyConverter.convertNativeKeyToKeyEventVK(event.getKeyCode());
            if (ActionSubTaskSequenceBuilder.this.isListening) {
                if (n == endKey) {
                    ActionSubTaskSequenceBuilder.this.setIsListening(false);
                } else if (n == waitKey) {
                    isWaiting = true;
                    waitStartTime = System.currentTimeMillis();
                } else {
                    events.add(new KeySubTask(KeySubTaskType.PRESSED, n));
                }
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent event) {
            if (ActionSubTaskSequenceBuilder.this.isListening) {
                if (ActionSubTaskSequenceBuilder.this.isWaiting && ActionSubTaskSequenceBuilder.this.waitKey == NativeKeyToVKKeyConverter.convertNativeKeyToKeyEventVK(event.getKeyCode())) {
                    events.add(new WaitSubTask(System.currentTimeMillis() - ActionSubTaskSequenceBuilder.this.waitStartTime));
                } else {
                    events.add(new KeySubTask(KeySubTaskType.RELEASED, NativeKeyToVKKeyConverter.convertNativeKeyToKeyEventVK(event.getKeyCode())));
                }
            }
        }
    }

    private class MouseListener implements NativeMouseInputListener {
        @Override
        public void nativeMousePressed(NativeMouseEvent event) {
            if (ActionSubTaskSequenceBuilder.this.isListening) {
                events.add(new MouseSubTask(MouseSubTaskType.PRESSED, 1 << (9 + event.getButton()), event.getX(), event.getY()));
            }
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent event) {
            if (ActionSubTaskSequenceBuilder.this.isListening) {
                events.add(new MouseSubTask(MouseSubTaskType.RELEASED, 1 << (9 + event.getButton()), event.getX(), event.getY()));
            }
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent event) {
            if (ActionSubTaskSequenceBuilder.this.isListening) {
                events.add(new MouseSubTask(MouseSubTaskType.MOVED, event.getButton(), event.getX(), event.getY()));
            }
        }

        @Override
        public void nativeMouseDragged(NativeMouseEvent event) {
            if (ActionSubTaskSequenceBuilder.this.isListening) {
                events.add(new MouseSubTask(MouseSubTaskType.DRAGGED, event.getButton(), event.getX(), event.getY()));
            }
        }
    }
}
