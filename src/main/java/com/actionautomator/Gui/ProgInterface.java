package com.actionautomator.Gui;

import com.actionautomator.ActionManagement.CodeActionBuilder;
import com.actionautomator.ActionManagement.NativeKeyConverter;
import com.actionautomator.BindingManagement.BindingManager;
import com.actionautomator.Gui.ThemedComponents.ThemedTextPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.HashSet;

public class ProgInterface extends ThemedTextPane {
    private final SimpleAttributeSet defaultAttributeSet;
    private final SimpleAttributeSet strAttributeSet;
    private final SimpleAttributeSet numberAttributeSet;
    private final SimpleAttributeSet keyAttributeSet;
    private final SimpleAttributeSet runAttributeSet;
    private final BindingManager bindingManager;
    private final CodeActionBuilder codeActionBuilder;
    private final SquigglePainter errorHighlighter;
    private final SimpleAttributeSet commandAttributeSet;
    private final SimpleAttributeSet varAttributeSet;
    private Runnable onCodeChanged;

    public ProgInterface(BindingManager bindingManager, CodeActionBuilder codeActionBuilder) {
        super();
        this.bindingManager = bindingManager;
        this.codeActionBuilder = codeActionBuilder;
        this.errorHighlighter = new SquigglePainter(Color.RED);
        super.setEnabled(true);
        super.setFont(ActionAutomatorResources.defaultFont);
        super.setMargin(ActionAutomatorResources.defaultMargin);
        StyledDocument doc = super.getStyledDocument();

        defaultAttributeSet = new SimpleAttributeSet();
        StyleConstants.setLeftIndent(defaultAttributeSet, 2);
        StyleConstants.setRightIndent(defaultAttributeSet, 2);
        StyleConstants.setSpaceAbove(defaultAttributeSet, 2);
        commandAttributeSet = new SimpleAttributeSet();
        StyleConstants.setItalic(commandAttributeSet, true);
        strAttributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(strAttributeSet, true);
        numberAttributeSet = new SimpleAttributeSet();
        StyleConstants.setItalic(numberAttributeSet, true);
        keyAttributeSet = new SimpleAttributeSet();
        StyleConstants.setBold(keyAttributeSet, true);
        StyleConstants.setUnderline(keyAttributeSet, true);
        runAttributeSet = new SimpleAttributeSet();
        varAttributeSet = new SimpleAttributeSet();
        StyleConstants.setItalic(varAttributeSet, true);

        doc.setParagraphAttributes(0, doc.getLength(), defaultAttributeSet, true);
        super.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {}
        });
    }

    private String prevCode = null;

    private void update() {
        if (bindingManager.getSelected() == null) {
            return;
        }
        if (prevCode != null && !prevCode.isEmpty() && !getText().isEmpty() && !getText().strip().equals(prevCode.strip())) {
            runOnCodeChanged();
        }
        prevCode = getText().strip();
        SwingUtilities.invokeLater(this::updateTextDisplay);
    }

    public void onSelectedChanged() {
        if (bindingManager.getPrevSelected() != null) {
            bindingManager.setBindingCode(bindingManager.getPrevSelected(), getText(), codeActionBuilder);
        }
        if (bindingManager.getSelected() != null) {
            String text = bindingManager.getBinding(bindingManager.getSelected()).getCode();
            prevCode = text;
            setText(text);
        }
        SwingUtilities.invokeLater(this::updateTextDisplay);
    }

    @Override
    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
        super.updateColorTheme(darkMode, primaryColor, secondaryColor);
        updateTextDisplay();
    }

    public boolean saveCode(String bindingName) {
        // Action gets set to null if code doesn't compile
        return bindingManager.setBindingCode(bindingName, getText(), codeActionBuilder);
    }

    public void setOnCodeChanged(Runnable runnable) {
        this.onCodeChanged = runnable;
    }

    private void runOnCodeChanged() {
        if (onCodeChanged != null) {
            onCodeChanged.run();
        }
    }

    public void updateTextDisplay() {
        if (darkMode) {
            setCaretColor(ActionAutomatorResources.lightThemeColor);
            StyleConstants.setForeground(defaultAttributeSet, ActionAutomatorResources.lightThemeColor);
        } else {
            setCaretColor(ActionAutomatorResources.darkThemeColor);
            StyleConstants.setForeground(defaultAttributeSet, ActionAutomatorResources.darkThemeColor);
        }
        StyledDocument doc = getStyledDocument();
        doc.setParagraphAttributes(0, doc.getLength(), defaultAttributeSet, true);
        getHighlighter().removeAllHighlights();
        StyleConstants.setForeground(strAttributeSet, primaryColor);
        StyleConstants.setForeground(numberAttributeSet, primaryColor);
        StyleConstants.setForeground(keyAttributeSet, primaryColor);
        StyleConstants.setForeground(runAttributeSet, primaryColor);
        StyleConstants.setForeground(varAttributeSet, primaryColor);
        HashSet<String> knownIntsVars = new HashSet<>();
        try {
            int idx = 0;
            for (String line : doc.getText(0, doc.getLength()).split("\n")) {
                if (line.isEmpty()) {
                    idx ++;
                    continue;
                }
                if (line.strip().equals("}")) {
                    idx+=2;
                    continue;
                }
                doc.setCharacterAttributes(idx, idx + line.length(), defaultAttributeSet, true);  // Reset
                if (line.startsWith("#")) {
                    idx += line.length() + 1;
                    continue;
                }
                // Command & parentheses
                int openParenIdx = line.indexOf("(");
                if (openParenIdx == -1) {
                    getHighlighter().addHighlight(idx + line.length() - 1, idx + line.length(), errorHighlighter);
                    idx += line.length() + 1;
                    continue;
                }
                String command = line.substring(0, openParenIdx).strip();
                int commandStartIdx = idx, commandEndIdx = idx + command.length();
                if (!codeActionBuilder.commandsToNofArgs.containsKey(command)) {
                    getHighlighter().addHighlight(commandStartIdx, commandEndIdx, errorHighlighter);
                    idx += line.length() + 1;
                    continue;
                } else {
                    doc.setCharacterAttributes(commandStartIdx, commandEndIdx, commandAttributeSet, true);
                }
                int t = (new StringBuilder(line).reverse()).indexOf(")");
                int closeParenIdx = line.length() - 1 - t;
                if (t == -1) {
                    getHighlighter().addHighlight(idx + line.length() - 1, idx + line.length(), errorHighlighter);
                    idx += line.length() + 1;
                    continue;
                }
                int argsStartIdx = idx + openParenIdx + 1, argsEndIdx = idx + closeParenIdx;
                // Correct nof-args
                String baseArgs = line.substring(argsStartIdx - idx, argsEndIdx - idx);
                String[] args = baseArgs.split(",");
                int expectedNofArgs = codeActionBuilder.commandsToNofArgs.get(command);
                if (args.length > expectedNofArgs) {
                    int i = 0;
                    for (int j = 0; j < expectedNofArgs - args.length; j++) {
                        i += args[args.length - j - 1].length() + 1;
                    }
                    getHighlighter().addHighlight(argsEndIdx - i, argsEndIdx, errorHighlighter);
                    idx += line.length() + 1;
                    continue;
                } else if (args.length < expectedNofArgs) {
                    getHighlighter().addHighlight(argsEndIdx, argsEndIdx + 1, errorHighlighter);
                    idx += line.length() + 1;
                    continue;
                }
                // Arg-types
                if (codeActionBuilder.commandsWithNumberArgs.contains(command)) {  // Number command
                    int p = 0;
                    for (String arg : args) {
                        if (arg.isBlank()) {
                            getHighlighter().addHighlight(argsStartIdx + p, argsStartIdx + 1 + p, errorHighlighter);
                        } else {
                            boolean isVarName = false;
                            for (int i = 0; i < arg.length(); i++) {
                                if (!Character.isDigit(arg.charAt(i)) && !Character.isWhitespace(arg.charAt(i))) {
                                    isVarName = true;
                                    break;
                                }
                            }
                            if (isVarName) {
                                if (!knownIntsVars.contains(arg.strip())) {
                                    getHighlighter().addHighlight(argsStartIdx + p, argsStartIdx + p + arg.strip().length(), errorHighlighter);
                                } else {
                                    doc.setCharacterAttributes(argsStartIdx + p, arg.length(), varAttributeSet, true);
                                }
                            } else {
                                doc.setCharacterAttributes(argsStartIdx + p, arg.length(), numberAttributeSet, true);
                            }
                        }
                        p += arg.length() + 1;
                    }
                } else if (codeActionBuilder.commandsWithKeyArgs.contains(command)) {  // Key command
                    if (baseArgs.isBlank()) {
                        getHighlighter().addHighlight(argsStartIdx, argsStartIdx + 1, errorHighlighter);
                    } else {
                        int i = 0;
                        if (NativeKeyConverter.stringToKeyEventVK(baseArgs.strip()) == null) {
                            getHighlighter().addHighlight(argsStartIdx + i, argsStartIdx + i + baseArgs.length(), errorHighlighter);
                        } else {
                            doc.setCharacterAttributes(argsStartIdx + i, baseArgs.strip().length(), keyAttributeSet, true);
                        }
                    }
                } else if (codeActionBuilder.saveCommands.contains(command)) {  // Save command
                    boolean broken = false;
                    int p = 0;
                    for (String arg : args) {
                        if (arg.isBlank()) {
                            getHighlighter().addHighlight(argsStartIdx + p, argsStartIdx + 1 + p, errorHighlighter);
                            broken = true;
                        }
                        p += arg.length() + 1;
                    }
                    if (!broken) {
                        if (command.equals("saveint")) {
                            knownIntsVars.add(args[0].strip());
                        }
                        doc.setCharacterAttributes(argsStartIdx, args[0].strip().length(), runAttributeSet, true);
                    }
                } else if (command.equals("run")) {
                    if (baseArgs.isBlank()) {
                        getHighlighter().addHighlight(argsStartIdx, argsStartIdx + 1, errorHighlighter);
                    } else if (bindingManager.getBinding(baseArgs.strip()) == null) {
                        getHighlighter().addHighlight(argsStartIdx, argsStartIdx + baseArgs.strip().length(), errorHighlighter);
                    } else {
                        doc.setCharacterAttributes(argsStartIdx, baseArgs.strip().length(), runAttributeSet, true);
                    }
                } else if (command.equals("type")) {
                    doc.setCharacterAttributes(argsStartIdx, baseArgs.strip().length(), strAttributeSet, true);
                }
                idx += line.length() + 1;  // +1 for newline
            }
        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
