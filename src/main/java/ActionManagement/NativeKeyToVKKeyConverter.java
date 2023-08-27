package ActionManagement;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.*;

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

    public static int[] convertCharToKeyEventVK(char c) {
        switch (c) {
            case 'a' -> {
                return new int[]{(VK_A)};
            }
            case 'b' -> {
                return new int[]{(VK_B)};
            }
            case 'c' -> {
                return new int[]{(VK_C)};
            }
            case 'd' -> {
                return new int[]{(VK_D)};
            }
            case 'e' -> {
                return new int[]{(VK_E)};
            }
            case 'f' -> {
                return new int[]{(VK_F)};
            }
            case 'g' -> {
                return new int[]{(VK_G)};
            }
            case 'h' -> {
                return new int[]{(VK_H)};
            }
            case 'i' -> {
                return new int[]{(VK_I)};
            }
            case 'j' -> {
                return new int[]{(VK_J)};
            }
            case 'k' -> {
                return new int[]{(VK_K)};
            }
            case 'l' -> {
                return new int[]{(VK_L)};
            }
            case 'm' -> {
                return new int[]{(VK_M)};
            }
            case 'n' -> {
                return new int[]{(VK_N)};
            }
            case 'o' -> {
                return new int[]{(VK_O)};
            }
            case 'p' -> {
                return new int[]{(VK_P)};
            }
            case 'q' -> {
                return new int[]{(VK_Q)};
            }
            case 'r' -> {
                return new int[]{(VK_R)};
            }
            case 's' -> {
                return new int[]{(VK_S)};
            }
            case 't' -> {
                return new int[]{(VK_T)};
            }
            case 'u' -> {
                return new int[]{(VK_U)};
            }
            case 'v' -> {
                return new int[]{(VK_V)};
            }
            case 'w' -> {
                return new int[]{(VK_W)};
            }
            case 'x' -> {
                return new int[]{(VK_X)};
            }
            case 'y' -> {
                return new int[]{(VK_Y)};
            }
            case 'z' -> {
                return new int[]{(VK_Z)};
            }
            case 'A' -> {
                return new int[]{VK_SHIFT,VK_A};
            }
            case 'B' -> {
                return new int[]{VK_SHIFT,VK_B};
            }
            case 'C' -> {
                return new int[]{VK_SHIFT,VK_C};
            }
            case 'D' -> {
                return new int[]{VK_SHIFT,VK_D};
            }
            case 'E' -> {
                return new int[]{VK_SHIFT,VK_E};
            }
            case 'F' -> {
                return new int[]{VK_SHIFT,VK_F};
            }
            case 'G' -> {
                return new int[]{VK_SHIFT,VK_G};
            }
            case 'H' -> {
                return new int[]{VK_SHIFT,VK_H};
            }
            case 'I' -> {
                return new int[]{VK_SHIFT,VK_I};
            }
            case 'J' -> {
                return new int[]{VK_SHIFT,VK_J};
            }
            case 'K' -> {
                return new int[]{VK_SHIFT,VK_K};
            }
            case 'L' -> {
                return new int[]{VK_SHIFT,VK_L};
            }
            case 'M' -> {
                return new int[]{VK_SHIFT,VK_M};
            }
            case 'N' -> {
                return new int[]{VK_SHIFT,VK_N};
            }
            case 'O' -> {
                return new int[]{VK_SHIFT,VK_O};
            }
            case 'P' -> {
                return new int[]{VK_SHIFT,VK_P};
            }
            case 'Q' -> {
                return new int[]{VK_SHIFT,VK_Q};
            }
            case 'R' -> {
                return new int[]{VK_SHIFT,VK_R};
            }
            case 'S' -> {
                return new int[]{VK_SHIFT,VK_S};
            }
            case 'T' -> {
                return new int[]{VK_SHIFT,VK_T};
            }
            case 'U' -> {
                return new int[]{VK_SHIFT,VK_U};
            }
            case 'V' -> {
                return new int[]{VK_SHIFT,VK_V};
            }
            case 'W' -> {
                return new int[]{VK_SHIFT,VK_W};
            }
            case 'X' -> {
                return new int[]{VK_SHIFT,VK_X};
            }
            case 'Y' -> {
                return new int[]{VK_SHIFT,VK_Y};
            }
            case 'Z' -> {
                return new int[]{VK_SHIFT,VK_Z};
            }
            case '`' -> {
                return new int[]{(VK_BACK_QUOTE)};
            }
            case '0' -> {
                return new int[]{(VK_0)};
            }
            case '1' -> {
                return new int[]{(VK_1)};
            }
            case '2' -> {
                return new int[]{(VK_2)};
            }
            case '3' -> {
                return new int[]{(VK_3)};
            }
            case '4' -> {
                return new int[]{(VK_4)};
            }
            case '5' -> {
                return new int[]{(VK_5)};
            }
            case '6' -> {
                return new int[]{(VK_6)};
            }
            case '7' -> {
                return new int[]{(VK_7)};
            }
            case '8' -> {
                return new int[]{(VK_8)};
            }
            case '9' -> {
                return new int[]{(VK_9)};
            }
            case '-' -> {
                return new int[]{(VK_MINUS)};
            }
            case '=' -> {
                return new int[]{(VK_EQUALS)};
            }
            case '~' -> {
                return new int[]{VK_SHIFT,VK_BACK_QUOTE};
            }
            case '!' -> {
                return new int[]{VK_SHIFT, VK_1};
            }
            case '@' -> {
                return new int[]{VK_SHIFT, VK_2};
            }
            case '#' -> {
                return new int[]{VK_SHIFT, VK_3};
            }
            case '$' -> {
                return new int[]{VK_SHIFT, VK_4};
            }
            case '%' -> {
                return new int[]{VK_SHIFT,VK_5};
            }
            case '^' -> {
                return new int[]{VK_SHIFT, VK_6};
            }
            case '&' -> {
                return new int[]{VK_SHIFT, VK_7};
            }
            case '*' -> {
                return new int[]{VK_SHIFT, VK_8};
            }
            case '(' -> {
                return new int[]{(VK_LEFT_PARENTHESIS)};
            }
            case ')' -> {
                return new int[]{(VK_RIGHT_PARENTHESIS)};
            }
            case '_' -> {
                return new int[]{(VK_UNDERSCORE)};
            }
            case '+' -> {
                return new int[]{(VK_PLUS)};
            }
            case '\t' -> {
                return new int[]{(VK_TAB)};
            }
            case '\n' -> {
                return new int[]{(VK_ENTER)};
            }
            case '[' -> {
                return new int[]{(VK_OPEN_BRACKET)};
            }
            case ']' -> {
                return new int[]{(VK_CLOSE_BRACKET)};
            }
            case '\\' -> {
                return new int[]{(VK_BACK_SLASH)};
            }
            case '{' -> {
                return new int[]{VK_SHIFT,VK_OPEN_BRACKET};
            }
            case '}' -> {
                return new int[]{VK_SHIFT,VK_CLOSE_BRACKET};
            }
            case '|' -> {
                return new int[]{VK_SHIFT,VK_BACK_SLASH};
            }
            case ';' -> {
                return new int[]{(VK_SEMICOLON)};
            }
            case ':' -> {
                return new int[]{(VK_COLON)};
            }
            case '\'' -> {
                return new int[]{(VK_QUOTE)};
            }
            case '"' -> {
                return new int[]{(VK_QUOTEDBL)};
            }
            case ',' -> {
                return new int[]{VK_COMMA};
            }
            case '<' -> {
                return new int[]{VK_SHIFT,VK_COMMA};
            }
            case '.' -> {
                return new int[]{(VK_PERIOD)};
            }
            case '>' -> {
                return new int[]{VK_SHIFT,VK_PERIOD};
            }
            case '/' -> {
                return new int[]{(VK_SLASH)};
            }
            case '?' -> {
                return new int[]{VK_SHIFT,VK_SLASH};
            }
            case ' ' -> {
                return new int[]{(VK_SPACE)};
            } default -> {
                return null;
            }
        }
    }
}
