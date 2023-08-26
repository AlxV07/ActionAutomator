package ActionManagement;

import ActionManagement.SubActions.*;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;

import java.util.ArrayList;
import java.util.List;

public class ActionCopyBuilder {
    private final KeyboardListener keyListener;
    private final MouseListener mouseListener;
    private List<SubAction> events;
    private int endKey;
    private int waitKey;
    private boolean isListening;

    private boolean isWaiting;
    private long waitStartTime;

    public ActionCopyBuilder() {
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

    public List<SubAction> getEvents() {
        return new ArrayList<>(this.events);
    }

    public void clearEvents() {
        this.events = new ArrayList<>();
    }


    private class KeyboardListener implements NativeKeyListener {
        @Override
        public void nativeKeyPressed(NativeKeyEvent event) {
            int n = NativeKeyToVKKeyConverter.convertNativeKeyToKeyEventVK(event.getKeyCode());
            if (ActionCopyBuilder.this.isListening) {
                if (n == endKey) {
                    ActionCopyBuilder.this.setIsListening(false);
                } else if (n == waitKey) {
                    isWaiting = true;
                    waitStartTime = System.currentTimeMillis();
                } else {
                    events.add(new KeyPressedSubAction(n));
                }
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent event) {
            if (ActionCopyBuilder.this.isListening) {
                if (ActionCopyBuilder.this.isWaiting && ActionCopyBuilder.this.waitKey == NativeKeyToVKKeyConverter.convertNativeKeyToKeyEventVK(event.getKeyCode())) {
                    events.add(new WaitSubAction(System.currentTimeMillis() - ActionCopyBuilder.this.waitStartTime));
                } else {
                    events.add(new KeyReleasedSubAction(NativeKeyToVKKeyConverter.convertNativeKeyToKeyEventVK(event.getKeyCode())));
                }
            }
        }
    }

    private class MouseListener implements NativeMouseInputListener {
        @Override
        public void nativeMousePressed(NativeMouseEvent event) {
            if (ActionCopyBuilder.this.isListening) {
                events.add(new MousePressedSubAction(1 << (9 + event.getButton())));
            }
        }

        @Override
        public void nativeMouseReleased(NativeMouseEvent event) {
            if (ActionCopyBuilder.this.isListening) {
                events.add(new MouseReleasedSubAction(1 << (9 + event.getButton())));
            }
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent event) {
            if (ActionCopyBuilder.this.isListening) {
                events.add(new MouseMovedSubAction(event.getX(), event.getY()));
            }
        }

        @Override
        public void nativeMouseDragged(NativeMouseEvent event) {
            if (ActionCopyBuilder.this.isListening) {
                events.add(new MouseMovedSubAction(event.getX(), event.getY()));
            }
        }
    }
}
