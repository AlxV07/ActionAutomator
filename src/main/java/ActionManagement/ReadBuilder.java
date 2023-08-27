package ActionManagement;

import ActionManagement.SubActions.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ReadBuilder {
    public Action parseLinesIntoAction(List<String> lines) {
        List<SubAction> subActions = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.split("\\(", 2);
            String[] args = new String[]{""};
            if (parts[1].length() > 1) {
                args = parts[1].substring(0, parts[1].length() - 1).split(",");
            }
            String command = parts[0];
            switch (command) {
                case "typewrite" -> {
                    int i = 1;
                    for (String arg: args) {
                        for (char c : arg.toCharArray()) {
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
                        if (i < args.length) {
                            int[] vk_key = NativeKeyToVKKeyConverter.convertCharToKeyEventVK(',');
                            assert vk_key != null;
                            subActions.add(new KeyPressedSubAction(vk_key[0]));
                            subActions.add(new KeyReleasedSubAction(vk_key[0]));
                            i += 1;
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

                case "move" -> subActions.add(new MouseMovedSubAction(Integer.parseInt(args[0].strip()), Integer.parseInt(args[1].strip())));
            }
        }
        return new Action(subActions);
    }
}
