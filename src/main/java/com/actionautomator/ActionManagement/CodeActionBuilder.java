package com.actionautomator.ActionManagement;

import com.actionautomator.ActionManagement.SubActions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeActionBuilder {
    public static Action parseCodeIntoAction(String code) throws SyntaxError {
        List<SubAction> subActions = new ArrayList<>();
        for (String line : code.split("\n")) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.strip().split(" ");
            String command = parts[0];

            if (parts.length == 1) {
                switch (command) {
                    case "lclick" -> {
                        subActions.add(new MousePressedSubAction(1024));
                        subActions.add(new MouseReleasedSubAction(1024));
                    }
                    case "ldown" -> subActions.add(new MousePressedSubAction(1024));
                    case "lup" -> subActions.add(new MouseReleasedSubAction(1024));

                    case "rclick" -> {
                        subActions.add(new MousePressedSubAction(2048));
                        subActions.add(new MouseReleasedSubAction(2048));
                    }
                    case "rdown" -> subActions.add(new MousePressedSubAction(2048));
                    case "rup" -> subActions.add(new MouseReleasedSubAction(2048));
                }
            } else {
                parts[0] = "";
                String args = String.join(" ", parts).strip();
                switch (command) {
                    case "type" -> {
                        for (char c : args.toCharArray()) {
                            List<Integer> vk_keys = new ArrayList<>();
                            int[] vk_key_supplemental = NativeKeyConverter.charToKeyEventVK(c);
                            if (vk_key_supplemental == null) {
                                throw new SyntaxError();
                            }
                            for (int i : vk_key_supplemental) {
                                vk_keys.add(i);
                            }
                            for (Integer i: vk_keys) {
                                subActions.add(new KeyPressedSubAction(i));
                            }
                            Collections.reverse(vk_keys);
                            for (Integer i: vk_keys) {
                                subActions.add(new KeyReleasedSubAction(i));
                            }
                        }
                    }
                    case "kdown" -> {
                        int[] vk_keys = NativeKeyConverter.stringToKeyEventVK(args);
                        if (vk_keys == null) {
                            throw new SyntaxError();
                        }
                        for (int i : vk_keys) {
                            subActions.add(new KeyPressedSubAction(i));
                        }
                    }
                    case "kup" -> {
                        int[] vk_keys = NativeKeyConverter.stringToKeyEventVK(args);
                        if (vk_keys == null) {
                            throw new SyntaxError();
                        }
                        for (int i : vk_keys) {
                            subActions.add(new KeyReleasedSubAction(i));
                        }
                    }
                    case "wait" -> {
                        try {
                            subActions.add(new WaitSubAction(Integer.parseInt(args.strip())));
                        } catch (NumberFormatException e) {
                            throw new SyntaxError();
                        }
                    }

                    case "speed" -> {
                        try {
                            subActions.add(new ChangeSpeedSubAction(Integer.parseInt(args.strip())));
                        } catch (NumberFormatException e) {
                            throw new SyntaxError();
                        }
                    }

                    case "move" -> {
                        String[] x_y = args.strip().split(",");
                        try {
                            subActions.add(new MouseMovedSubAction(
                                            Integer.parseInt(x_y[0].strip()),
                                            Integer.parseInt(x_y[1].strip())
                                    )
                            );
                        } catch (NumberFormatException e) {
                            throw new SyntaxError();
                        }
                    }
                }
            }
        }
        return new Action(subActions);
    }

    public static class SyntaxError extends Exception {}
}
