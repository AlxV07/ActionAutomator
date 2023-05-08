package ActionManagement;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.awt.event.KeyEvent;

public class NativeKeyToVKKeyConverter {
    public static int convertNativeKeyToKeyEventVK(int n) {
        String s = NativeKeyEvent.getKeyText(n);
        if (s.length() == 1) {
            return KeyEvent.getExtendedKeyCodeForChar(s.charAt(0));
        }
        else {
            String[] p = s.toUpperCase().split(" ");
            s = String.join("_", p);
            try {
                if (s.equals("CTRL")) {
                    return KeyEvent.VK_CONTROL;
                }
                if (s.equals("BACKSPACE")) {
                    return KeyEvent.VK_BACK_SPACE;
                }
                if (s.equals("META")) {
                    return KeyEvent.VK_WINDOWS;
                }
                return (int) KeyEvent.class.getDeclaredField("VK_" + s).get(KeyEvent.class);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
            }
        }
    }
}
