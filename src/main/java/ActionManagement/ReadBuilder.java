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
            if (!line.endsWith(")")) {
                throw new SyntaxError();
            }
            String[] parts = line.split("\\(", 2);
            String command = parts[0];
            String args = parts[1].strip().substring(0, parts[1].length() - 1);
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
