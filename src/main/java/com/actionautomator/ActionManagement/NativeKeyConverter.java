package com.actionautomator.ActionManagement;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import static java.awt.event.KeyEvent.*;

public class NativeKeyConverter {
    public static String nativeKeyToString(int key) {
        String str = NativeKeyEvent.getKeyText(key);
        if (str.startsWith("Unknown")) {
            return String.format("?Key{%s}", key);
        }
        switch (str) {
            case "Open Bracket" -> {return "[";}
            case "Close Bracket" -> {return "]";}
            case "Back Slash" -> {return "\\";}
            case "Back Quote" -> {return "`";}
            case "Comma" -> {return ",";}
            case "Period" -> {return ".";}
            case "Quote" -> {return "'";}
            case "Semicolon" -> {return ";";}
            case "Equals" -> {return "=";}
            case "Minus" -> {return "-";}
            case "Slash" -> {return "/";}
            case "Escape" -> {return "Esc";}
            case "Delete" -> {return "Del";}
            case "Backspace" -> {return "Back";}
            case "Caps Lock" -> {return "Caps";}
            case "Page Up" -> {return "PgUp";}
            case "Page Down" -> {return "PgDn";}
            default -> {return str;}
        }
    }

    public static int[] stringToKeyEventVK(String str) {
        if (str.length() > 1) {
            return new int[]{NativeKeyConverter.specialStringToKeyEventVK(str)};
        } else {
            return NativeKeyConverter.charToKeyEventVK(str.charAt(0));
        }
    }

    public static int specialStringToKeyEventVK(String str) {
        switch (str) {
            case "ALT" -> {
                return VK_ALT;
            }
            case "META" -> {
                return VK_META;
            }
            case "BACKSPACE" -> {
                return VK_BACK_SPACE;
            }
            case "SHIFT" -> {
                return VK_SHIFT;
            }
            case "CTRL" -> {
                return VK_CONTROL;
            }
            case "CAPSLOCK" -> {
                return VK_CAPS_LOCK;
            }
            case "TAB" -> {
                return VK_TAB;
            }
            case "ESCAPE" -> {
                return VK_ESCAPE;
            }
            case "DELETE" -> {
                return VK_DELETE;
            }
            case "HOME" -> {
                return VK_HOME;
            }
            case "END" -> {
                return VK_END;
            }
            case "ENTER" -> {
                return VK_ENTER;
            }
            case "F1" -> {
                return VK_F1;
            }
            case "F2" -> {
                return VK_F2;
            }
            case "F3" -> {
                return VK_F3;
            }
            case "F4" -> {
                return VK_F4;
            }
            case "F5" -> {
                return VK_F5;
            }
            case "F6" -> {
                return VK_F6;
            }
            case "F7" -> {
                return VK_F7;
            }
            case "F8" -> {
                return VK_F8;
            }
            case "F9" -> {
                return VK_F9;
            }
            case "F10" -> {
                return VK_F10;
            }
            case "F11" -> {
                return VK_F11;
            }
            case "F12" -> {
                return VK_F12;
            }
        }
        return -1;
    }

    public static int[] charToKeyEventVK(char c) {
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