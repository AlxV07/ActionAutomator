package com.actionautomator.Gui;

import com.actionautomator.ActionManagement.CodeActionBuilder;
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

public class ProgInterface extends ThemedTextPane {
    private final SimpleAttributeSet defaultAttributeSet;
    private final SimpleAttributeSet coloredAttributeSet;
    private final BindingManager bindingManager;
    private final SquigglePainter errorHighlighter;
    private Runnable onCodeChanged;

    public ProgInterface(BindingManager bindingManager) {
        super();
        this.bindingManager = bindingManager;
        super.setEnabled(true);
        super.setFont(ActionAutomatorResources.defaultFont);
        super.setMargin(ActionAutomatorResources.defaultMargin);
        StyledDocument doc = super.getStyledDocument();
        defaultAttributeSet = new SimpleAttributeSet();
        StyleConstants.setLeftIndent(defaultAttributeSet, 2);
        StyleConstants.setRightIndent(defaultAttributeSet, 2);
        StyleConstants.setSpaceAbove(defaultAttributeSet, 2);

        coloredAttributeSet = new SimpleAttributeSet();
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
        errorHighlighter = new SquigglePainter(Color.RED);
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
            bindingManager.setBindingCode(bindingManager.getPrevSelected(), getText());
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
        return bindingManager.setBindingCode(bindingName, getText());
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
        super.getHighlighter().removeAllHighlights();
        try {
            int idx = 0;
            for (String line : doc.getText(0, doc.getLength()).split("\n")) {
                String[] parts = line.split(" ", 2);

                String command = parts[0];
                int commandStartIdx = idx, commandEndIdx = idx + command.length();
                StyleConstants.setForeground(coloredAttributeSet, primaryColor);
                doc.setCharacterAttributes(commandStartIdx, commandEndIdx, coloredAttributeSet, true);
                if (!CodeActionBuilder.commandsToNofArgs.containsKey(command)) {
                    super.getHighlighter().addHighlight(commandStartIdx, commandEndIdx, errorHighlighter);
                }

                if (parts.length > 1) {
                    String args = parts[1];
                    int argsStartIdx = commandEndIdx + 1, argsEndIdx = argsStartIdx + args.length();
                    doc.setCharacterAttributes(argsStartIdx, argsEndIdx, defaultAttributeSet, true);
                    if (CodeActionBuilder.commandsToNofArgs.get(command) != null) {
                        int nofArgs = CodeActionBuilder.commandsToNofArgs.get(command);
                        String[] argParts = args.split(" ");
                        if (nofArgs < argParts.length) {
                            super.getHighlighter().addHighlight(argsEndIdx, argsEndIdx + 1, errorHighlighter);
                        } else if (nofArgs > argParts.length) {
                            int i = 0;
                            for (int j = 0; j < nofArgs - argParts.length; j++) {
                                i += argParts[argParts.length - j - 1].length() + 1;
                            }
                            super.getHighlighter().addHighlight(argsEndIdx - i, argsEndIdx, errorHighlighter);
                        }
                        if (CodeActionBuilder.commandsWithNumberArgs.contains(command)) {
                            for (int i = 0; i < args.length(); i++) {
                                if (!Character.isDigit(args.charAt(i)) && !Character.isWhitespace(args.charAt(i))) {
                                    super.getHighlighter().addHighlight(argsStartIdx + i, argsStartIdx + i + 1, errorHighlighter);
                                }
                            }
                        }
                    }
                }
                idx += line.length() + 1;

            }

        } catch (BadLocationException e) {
            throw new RuntimeException(e);
        }
    }
}
