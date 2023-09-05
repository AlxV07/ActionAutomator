package ActionManagement;

import ActionManagement.SubActions.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ReadBuilder {
    public Action parseLinesIntoAction(List<String> lines) throws SyntaxError {
        List<SubAction> subActions = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\\(", 2);
            String command = parts[0];
            String args = "";
            if (parts.length > 1) {
                args = parts[1].strip().substring(0, parts[1].length() - 1);
                assert args.endsWith(")");
            }
            switch (command) {
                case "typewrite" -> {
                    if (args.startsWith("'") && args.endsWith("'")) {
                        args = args.substring(1, args.length() - 1);
                        for (char c : args.toCharArray()) {
                            int[] vk_key = NativeKeyToVKKeyConverter.convertCharToKeyEventVK(c);
                            assert vk_key != null;
                            if (vk_key.length > 1) {
                                subActions.add(new KeyPressedSubAction(vk_key[0]));
                                subActions.add(new KeyPressedSubAction(vk_key[1]));
                                subActions.add(new KeyReleasedSubAction(vk_key[1]));
                                subActions.add(new KeyReleasedSubAction(vk_key[0]));
                            } else {
                                subActions.add(new KeyPressedSubAction(vk_key[0]));
                                subActions.add(new KeyReleasedSubAction(vk_key[0]));
                            }
                        }
                    }
                }

                case "key_press" -> {
                    if (args.startsWith("'") && args.endsWith("'")) {
                        args = args.substring(1, args.length() - 1);
                        int vk_key = -1;
                        if (args.length() > 1) {
                            vk_key = NativeKeyToVKKeyConverter.convertSpecialKeyToKeyEventVK(args);
                            assert vk_key != -1;
                        } else if (args.length() == 1) {
                            int[] t = NativeKeyToVKKeyConverter.convertCharToKeyEventVK(args.charAt(0));
                            assert t != null;
                            vk_key = t[0];
                        }
                        subActions.add(new KeyPressedSubAction(vk_key));
                        subActions.add(new KeyReleasedSubAction(vk_key));
                    }
                }
                case "key_down" -> {
                    if (args.startsWith("'") && args.endsWith("'")) {
                        args = args.substring(1, args.length() - 1);
                        int vk_key = -1;
                        if (args.length() > 1) {
                            vk_key = NativeKeyToVKKeyConverter.convertSpecialKeyToKeyEventVK(args);
                            assert vk_key != -1;
                        } else if (args.length() == 1) {
                            int[] t = NativeKeyToVKKeyConverter.convertCharToKeyEventVK(args.charAt(0));
                            assert t != null;
                            vk_key = t[0];
                        }
                        subActions.add(new KeyPressedSubAction(vk_key));
                    }
                }
                case "key_up" -> {
                    if (args.startsWith("'") && args.endsWith("'")) {
                        args = args.substring(1, args.length() - 1);
                        int vk_key = -1;
                        if (args.length() > 1) {
                            vk_key = NativeKeyToVKKeyConverter.convertSpecialKeyToKeyEventVK(args);
                            assert vk_key != -1;
                        } else if (args.length() == 1) {
                            int[] t = NativeKeyToVKKeyConverter.convertCharToKeyEventVK(args.charAt(0));
                            assert t != null;
                            vk_key = t[0];
                        }
                        subActions.add(new KeyReleasedSubAction(vk_key));
                    }
                }

                case "left_click" -> {
                    subActions.add(new MousePressedSubAction(1024));
                    subActions.add(new MouseReleasedSubAction(1024));
                }
                case "left_down" -> subActions.add(new MousePressedSubAction(1024));
                case "left_up" -> subActions.add(new MouseReleasedSubAction(1024));

                case "right_click" -> {
                    subActions.add(new MousePressedSubAction(2048));
                    subActions.add(new MouseReleasedSubAction(2048));
                }
                case "right_down" -> subActions.add(new MousePressedSubAction(2048));
                case "right_up" -> subActions.add(new MouseReleasedSubAction(2048));

                case "wait" -> subActions.add(new WaitSubAction(Integer.parseInt(args)));

                case "move" -> {
                    String[] x_y = args.split(",");
                    subActions.add(new MouseMovedSubAction(
                            Integer.parseInt(x_y[0].strip()),
                            Integer.parseInt(x_y[1].strip())
                            )
                    );
                }
            }
        }
        return new Action(subActions);
    }

    public static class SyntaxError extends Exception {}
}
