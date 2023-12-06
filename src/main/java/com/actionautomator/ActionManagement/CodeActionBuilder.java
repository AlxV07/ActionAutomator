package com.actionautomator.ActionManagement;

import com.actionautomator.ActionManagement.SubActions.*;
import com.actionautomator.BindingManagement.Binding;
import com.actionautomator.BindingManagement.BindingManager;

import java.util.*;


public class CodeActionBuilder {
    public final HashSet<String> commandsWithNumberArgs = new HashSet<>(Set.of(
            "move",
            "wait",
            "setspeed"
    ));

    public final HashSet<String> commandsWithKeyArgs = new HashSet<>(Set.of(
            "kpress",
            "kup",
            "kdown"
    ));

    public final HashSet<String> saveCommands = new HashSet<>(Set.of(
            "savestr",
            "saveint"
    ));
    public final HashMap<String, Integer> commandsToNofArgs = new HashMap<>();

    private final BindingManager bindingManager;

    public CodeActionBuilder(BindingManager bindingManager) {
        this.bindingManager = bindingManager;
        commandsToNofArgs.put("lclick", 0);
        commandsToNofArgs.put("lup", 0);
        commandsToNofArgs.put("ldown", 0);
        commandsToNofArgs.put("rclick", 0);
        commandsToNofArgs.put("rup", 0);
        commandsToNofArgs.put("rdown", 0);
        commandsToNofArgs.put("kpress", 1);
        commandsToNofArgs.put("kdown", 1);
        commandsToNofArgs.put("kup", 1);
        commandsToNofArgs.put("type", 1);
        commandsToNofArgs.put("move", 2);
        commandsToNofArgs.put("wait", 1);
        commandsToNofArgs.put("setspeed", 1);
        commandsToNofArgs.put("repeat", 1);
        commandsToNofArgs.put("savestr", 2);
        commandsToNofArgs.put("saveint", 2);
        commandsToNofArgs.put("run", 1);
    }

    private int symbolToInt(String symbol, HashMap<String, Integer> savedInts) {
        if (savedInts.get(symbol) != null) {
            return savedInts.get(symbol);
        }
        return Integer.parseInt(symbol.strip());
    }

    private String symbolToStr(String symbol, HashMap<String, String> savedStrs) {
        if (savedStrs.get(symbol) != null) {
            return savedStrs.get(symbol);
        }
        return (symbol);
    }

    public Action parseCodeIntoAction(String code) throws SyntaxError {
        return new Action(parseCodeIntoSubActions(code));
    }

    public List<SubAction> parseCodeIntoSubActions(String code) throws SyntaxError {
        List<SubAction> subActions = new ArrayList<>();
        HashMap<String, String> savedStrs = new HashMap<>();
        HashMap<String, Integer> savedInts = new HashMap<>();
        String[] lines = code.split("\n");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.isBlank()) {
                continue;
            }
            if (line.startsWith("#")) {
                continue;
            }
            int openParenIdx = line.indexOf("(");
            if (openParenIdx == -1) {
                throw new SyntaxError(line);
            }
            String command = line.substring(0, openParenIdx).strip();
            int closeParenIdx = line.length() - 1 - (new StringBuilder(line).reverse()).indexOf(")");
            if (closeParenIdx == -1) {
                throw new SyntaxError(line);
            }
            if (command.equals("repeat")) {
                int x = line.indexOf("{");
                if (x != -1) {
                    line = line.substring(0, x + 1);
                }
            } else {
                line = line.substring(0, closeParenIdx + 1);
            }
            String baseArgs = line.substring(openParenIdx + 1, closeParenIdx);
            String[] args = baseArgs.split(",");

            if (args.length == 1) {
                if (args[0].isBlank()) { // 0 args
                    switch (command) {
                        // Mouse click commands
                        case "lclick" -> {
                            subActions.add(new MousePressedSubAction(1024));
                            subActions.add(new MouseReleasedSubAction(1024));
                        }
                        case "lup" -> subActions.add(new MouseReleasedSubAction(1024));
                        case "ldown" -> subActions.add(new MousePressedSubAction(1024));

                        case "rclick" -> {
                            subActions.add(new MousePressedSubAction(2048));
                            subActions.add(new MouseReleasedSubAction(2048));
                        }
                        case "rup" -> subActions.add(new MouseReleasedSubAction(2048));
                        case "rdown" -> subActions.add(new MousePressedSubAction(2048));
                        default -> throw new SyntaxError(line);
                    }
                } else { // 1 arg
                    String arg = args[0].strip();
                    switch (command) {
                        // Key commands
                        case "kpress" -> {
                            int[] vk_keys = NativeKeyConverter.stringToKeyEventVK(arg);
                            if (vk_keys == null) {
                                throw new SyntaxError(line);
                            }
                            for (int j : vk_keys) {
                                subActions.add(new KeyPressedSubAction(j));
                                subActions.add(new KeyReleasedSubAction(j));
                            }
                        }
                        case "kup" -> {
                            int[] vk_keys = NativeKeyConverter.stringToKeyEventVK(arg);
                            if (vk_keys == null) {
                                throw new SyntaxError(line);
                            }
                            for (int j : vk_keys) {
                                subActions.add(new KeyReleasedSubAction(j));
                            }
                        }
                        case "kdown" -> {
                            int[] vk_keys = NativeKeyConverter.stringToKeyEventVK(arg);
                            if (vk_keys == null) {
                                throw new SyntaxError(line);
                            }
                            for (int j : vk_keys) {
                                subActions.add(new KeyPressedSubAction(j));
                            }
                        }
                        case "type" -> {
                            String str = symbolToStr(baseArgs, savedStrs);
                            for (char c : str.toCharArray()) {
                                int[] vk_keys = NativeKeyConverter.charToKeyEventVK(c);
                                if (vk_keys == null) {
                                    throw new SyntaxError(line);
                                }
                                for (int key : vk_keys) {
                                    subActions.add(new KeyPressedSubAction(key));
                                }
                                for (int idx = vk_keys.length - 1; idx >= 0; idx--) {
                                    subActions.add(new KeyReleasedSubAction(vk_keys[idx]));
                                }
                            }
                        }
                        // Number commands
                        case "wait" -> {
                            try {
                                subActions.add(new WaitSubAction(symbolToInt(arg, savedInts)));
                            } catch (NumberFormatException e) {
                                throw new SyntaxError(line);
                            }
                        }
                        case "setspeed" -> {
                            try {
                                subActions.add(new ChangeSpeedSubAction(symbolToInt(arg, savedInts)));
                            } catch (NumberFormatException e) {
                                throw new SyntaxError(line);
                            }
                        }
                        case "repeat" -> {
                            int repeatTimes;
                            try {
                                repeatTimes = symbolToInt(arg, savedInts);
                            } catch (NumberFormatException e) {
                                throw new SyntaxError(line);
                            }
                            if (!line.endsWith("{")) {
                                throw new SyntaxError(line);
                            }
                            boolean broken = false;
                            for (int j = i + 1; j < lines.length; j++) {
                                String candLine = lines[j];
                                if (candLine.strip().equals("}")) {
                                    ArrayList<String> repeatedLines = new ArrayList<>(Arrays.asList(lines).subList(i + 1, j));
                                    for (int k = 0; k < repeatTimes; k++) {
                                        subActions.addAll(parseCodeIntoSubActions(String.join("\n", repeatedLines)));
                                    }
                                    broken = true;
                                    i = j;
                                    break;
                                }
                            }
                            if (!broken) {
                                throw new SyntaxError(line + " Unclosed {");
                            }
                        }
                        case "run" -> {
                            Binding binding = bindingManager.getBinding(arg);
                            if (binding == null) {
                                throw new SyntaxError(line);
                            }
                            subActions.add(new RunSubAction(arg));
                        }
                        default -> throw new SyntaxError(line);
                    }
                }
            } else if (args.length == 2) {  // 2 args
                for (int j = 0; j < args.length; j++) {
                    args[j] = args[j].strip();
                }
                switch (command) {
                    case "move" -> {
                        try {
                            subActions.add(new MouseMovedSubAction(
                                    symbolToInt(args[0], savedInts),
                                    symbolToInt(args[1], savedInts)));
                        } catch (NumberFormatException e) {
                            throw new SyntaxError(line);
                        }
                    }
                    case "savestr" -> {
                        if (!Character.isAlphabetic(args[0].charAt(0)) && args[0].charAt(0) != '_') {
                            throw new SyntaxError(line);
                        }
                        savedStrs.put(args[0], symbolToStr(args[1], savedStrs));
                    }
                    case "saveint" -> {
                        if (!Character.isAlphabetic(args[0].charAt(0)) && args[0].charAt(0) != '_') {
                            throw new SyntaxError(line);
                        }
                        savedInts.put(args[0], symbolToInt(args[1], savedInts));
                    }
                    default -> throw new SyntaxError(line);
                }
            } else { // >=3 args
                throw new SyntaxError(line);
            }
        }
        return subActions;
    }

    public static class SyntaxError extends Exception {
        public SyntaxError(String line) {
            super(line);
            System.out.println(line);
        }
    }
}
