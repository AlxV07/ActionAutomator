package ListenManagement;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;

public class NativeGlobalListener implements NativeMouseInputListener, NativeKeyListener, NativeMouseWheelListener {
    public NativeGlobalListener() {
    }

    public void register() {
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            GlobalScreen.addNativeMouseListener(this);
            GlobalScreen.addNativeMouseMotionListener(this);
            GlobalScreen.addNativeMouseWheelListener(this);
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }

    public void unregister() {
        try {
            GlobalScreen.removeNativeKeyListener(this);
            GlobalScreen.removeNativeMouseListener(this);
            GlobalScreen.removeNativeMouseMotionListener(this);
            GlobalScreen.removeNativeMouseWheelListener(this);
            GlobalScreen.unregisterNativeHook();
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }

    public void nativeMouseClicked(NativeMouseEvent nativeEvent) {
    }

    public void nativeMousePressed(NativeMouseEvent nativeEvent) {
    }

    public void nativeMouseReleased(NativeMouseEvent nativeEvent) {
    }

    public void nativeKeyTyped(NativeKeyEvent nativeEvent) {
    }

    public void nativeKeyPressed(NativeKeyEvent nativeEvent) {
    }

    public void nativeKeyReleased(NativeKeyEvent nativeEvent) {
    }
}


