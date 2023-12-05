package com.actionautomator.ActionManagement;

import com.actionautomator.ActionManagement.SubActions.*;

import java.util.*;

public class CodeActionBuilder {
    public static final HashMap<String, Integer> commandsToNofArgs = new HashMap<>();
    public static final HashSet<String> commandsWithNumberArgs = new HashSet<>(Set.of(
            "move",
            "wait",
            "setspeed"
    ));

    public static final HashSet<String> commandsWithKeyArgs = new HashSet<>(Set.of(
            "kpress",
            "kup",
            "kdown"
    ));

    public static final HashSet<String> saveCommands = new HashSet<>(Set.of(
            "savestr",
            "saveint"
    ));

    static {
        commandsToNofArgs.put("move", 2);
        commandsToNofArgs.put("lclick", 0);
        commandsToNofArgs.put("rclick", 0);
        commandsToNofArgs.put("rup", 0);
        commandsToNofArgs.put("rdown", 0);
        commandsToNofArgs.put("lup", 0);
        commandsToNofArgs.put("ldown", 0);
        commandsToNofArgs.put("type", 1);
        commandsToNofArgs.put("kpress", 1);
        commandsToNofArgs.put("kdown", 1);
        commandsToNofArgs.put("kup", 1);
        commandsToNofArgs.put("wait", 1);
        commandsToNofArgs.put("setspeed", 1);
        commandsToNofArgs.put("run", 1);
        commandsToNofArgs.put("repeat", 1);
        commandsToNofArgs.put("savestr", 2);
        commandsToNofArgs.put("saveint", 2);
    }


    public static Action parseCodeIntoAction(String code) throws SyntaxError {
        List<SubAction> subActions = new ArrayList<>();
        for (String line : code.split("\n")) {
            if (line.isBlank()) {
                continue;
            }
            String[] parts = line.strip().split(" ", 2);
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
                    default -> throw new SyntaxError();
                }
            } else {
                String args = parts[1];
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

                    case "setspeed" -> {
                        try {
                            subActions.add(new ChangeSpeedSubAction(Integer.parseInt(args.strip())));
                        } catch (NumberFormatException e) {
                            throw new SyntaxError();
                        }
                    }

                    case "move" -> {
                        String[] x_y = args.strip().split(" ");
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
                    default -> throw new SyntaxError();
                }
            }
        }
        return new Action(subActions);
    }

    record Range(int start, int end, int type) {
        /*
        [Inclusive, exclusive] indices
        Types:
        0 = error
        1 = command
         */
    }

    public static void scanCodeForErrors(String code) {
        List<Range> highlights = new ArrayList<>();
        String[] lines = code.split(";");
        int idx = 0;
        for (String line: lines) {
            int endOfCommand = line.indexOf("(");
            if (endOfCommand == -1) {
                highlights.add(new Range(idx + line.length() - 1, idx + line.length(), 0));
                continue;
            }
            String command = line.substring(endOfCommand);
            if (commandsToNofArgs.get(command) == null) {
                highlights.add(new Range(idx, idx + command.length(), 0));
                continue;
            }
            int nofArgs = commandsToNofArgs.get(command);
            highlights.add(new Range(idx, idx + command.length(), 1));
            if (nofArgs == 0) {

            }
            int actualArgs = 0;
            List<String> args = new ArrayList<>();
            boolean stringBuilding = false;
            StringBuilder string = new StringBuilder();
            for (int i = endOfCommand; i < line.length(); i++) {
                if (line.charAt(i) == ')') {

                } else if (line.charAt(i) == '"') {

                } else if (line.charAt(i) == '\\') {

                } else if (line.charAt(i) == ',') {

                }
            }
        }
    }

    /* Syntax
    names = alpha_numeric_with_underscore_lowercase # cannot start with a digit
    str = enclosed within double-quotes, escape: \", \\
    int = all digits
    key = ALLUPPERCASENOSPACES
    ```
    # Comment
    method_name();
    method_name(arg);
    method_name(arg1, arg2);
    save_int(a_name, 0123);
    save_str(a_name, "hello\" there");
    kpress(CTRL);
    repeat (5) {
    run(an_action);  # no recursion allowed
    kpress(SHIFT);
    }
    ```

     */

    public static class SyntaxError extends Exception {}
}
