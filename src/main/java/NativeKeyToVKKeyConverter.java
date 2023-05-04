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
                return (int) KeyEvent.class.getDeclaredField("VK_" + s).get(KeyEvent.class);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                if (s.equals("CTRL")) {
                    return KeyEvent.VK_CONTROL;
                } else {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
