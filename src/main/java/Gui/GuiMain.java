package Gui;

import ActionManagement.ActionExecutor;
import ActionManagement.CodeActionBuilder;
import BindingManagement.Binding;
import BindingManagement.BindingManager;
import GlobalListener.NativeGlobalListener;
import Gui.Components.*;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;

public class GuiMain extends ThemedFrame {
    private final ActionExecutor actionExecutor;
    private final BindingManager bindingManager;
    private final Binding pressedKeys;

    private final CodeTextPane codeTextPane;
    private final BindingPanelBox bindingPanels;

    private final ThemedLabel coordLabel;
    private final ThemedTextArea pressedLabel;
    private final ThemedLabel debugLabel;


    public GuiMain() {
        actionExecutor = new ActionExecutor();
        bindingManager = new BindingManager();
        pressedKeys = new Binding("Pressed Keys");

        ThemedMenuBar AAMenuBar = new ThemedMenuBar();
        AAMenuBar.setBounds(0, 0, 500, 21);
        super.add(AAMenuBar);

        codeTextPane = new CodeTextPane();
        codeTextPane.setBounds(0, 250, 250, 250);
        super.add(codeTextPane);

        bindingPanels = new BindingPanelBox(bindingManager, codeTextPane);
        bindingPanels.setBounds(0, 20, 500, 230);
        super.add(bindingPanels);

        coordLabel = new ThemedLabel("Mouse Coord Label");
        coordLabel.setBounds(250, 250, 250, 50);
        coordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(coordLabel);

        pressedLabel = new ThemedTextArea();
        pressedLabel.setText("\n   Pressed Keys: ");
        pressedLabel.setBounds(250, 300, 250, 50);
        pressedLabel.setFont(GuiResources.defaultFont);
        super.add(pressedLabel);

        debugLabel = new ThemedLabel(" Debug Label ");
        debugLabel.setBounds(250, 350, 250, 50);
        debugLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(debugLabel);

        ThemedButton newActionButton = new ThemedButton(" New Action ");
        newActionButton.addActionListener(e -> bindingPanels.newBinding());
        newActionButton.setFocusable(false);
        AAMenuBar.add(newActionButton);

        ThemedButton removeActionButton = new ThemedButton(" Remove Action ");
        removeActionButton.addActionListener(e -> bindingPanels.removeSelectedBinding());
        removeActionButton.setFocusable(false);
        AAMenuBar.add(removeActionButton);

        ThemedButton runButton = new ThemedButton(" Run Action ");
        runButton.addActionListener(e -> {
            if (bindingPanels.getSelected() == null) {
                JOptionPane.showMessageDialog(this, "No selected Action to run.");
                return;
            }
            try {
                actionExecutor.interrupt();
                actionExecutor.executeActionFromCode(codeTextPane.getText(), 100);
            } catch (CodeActionBuilder.SyntaxError e1) {
                debugLabel.setText(toString());
            }
        });
        AAMenuBar.add(runButton);
    }

    public void start(boolean darkMode, Color primaryColor, Color secondaryColor) {
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try (FileWriter writer = new FileWriter("/home/alxv05/.action_automator/codes.txt")){
                    for (Binding binding : bindingManager.bindings.values()) {
                        writer.write(binding.getName() + "\n");
                        writer.write(binding.getCode() + "\n");
                        writer.write("===\n");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        super.updateColorTheme(darkMode, primaryColor, secondaryColor);
        super.setVisible(true);
        nativeGlobalListener.register();
    }

    private final NativeGlobalListener nativeGlobalListener = new NativeGlobalListener() {
        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
            int key = nativeKeyEvent.getKeyCode();

            if (bindingManager.isBindingButton()) {
                bindingManager.completeBindingButton(key);
                return;
            }

            if (pressedKeys.getNofKeys() < pressedKeys.getKeySequence().length) {
                if (!pressedKeys.containsKey(key)) {
                    pressedKeys.addKey(key);
                }
                Binding binding = bindingManager.findBinding(pressedKeys);
                StringBuilder keyString = new StringBuilder("\n   Pressed Keys: ");
                for (int k : pressedKeys.getKeySequence()) {
                    if (k == -1) {
                        break;
                    }
                    keyString.append(NativeKeyEvent.getKeyText(k)).append("+");
                }
                pressedLabel.setText(keyString.substring(0, Math.max(0, keyString.length() - 1)));
                if (binding != null) {
                    try {
                        actionExecutor.interrupt();
                        actionExecutor.executeActionFromCode(binding.getCode(), 100);
                    } catch (Exception e) {
                        debugLabel.setText(e.toString());
                    }
                }
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
            pressedKeys.removeKey(nativeKeyEvent.getKeyCode());
            StringBuilder keyString = new StringBuilder("\n   Pressed Keys: ");
            for (int k : pressedKeys.getKeySequence()) {
                if (k == -1) {
                    break;
                }
                keyString.append(NativeKeyEvent.getKeyText(k)).append("+");
            }
            pressedLabel.setText(keyString.substring(0, Math.max(0, keyString.length() - 1)));
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
            coordLabel.setText("Mouse Coords: " + nativeMouseEvent.getX() + "-X, " + nativeMouseEvent.getY() + "-Y");
        }
    };

    public class CodeTextPane extends ThemedTextPane {
        private final SimpleAttributeSet defaultAttributeSet;
        private final SimpleAttributeSet coloredAttributeSet;


        public CodeTextPane() {
            super();
            super.setFont(GuiResources.defaultFont);
            super.setMargin(GuiResources.defaultMargin);
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
        }

        private void update() {
            if (bindingPanels.getSelected() == null) {
                debugLabel.setText("No selected Action to edit.");
                return;
            }
            bindingManager.setBindingCode(bindingPanels.getSelected(), getText());
            SwingUtilities.invokeLater(this::updateFirstWordColor);
        }

        public void updateFirstWordColor() {
            StyledDocument doc = getStyledDocument();
            try {
                int idx = 0;
                for (String line: doc.getText(0, doc.getLength()).split("\n")) {
                    int i = 0;
                    while (i < line.length()) {
                        if (line.charAt(i) == ' ') {
                            break;
                        }
                        i += 1;
                    }
                    StyleConstants.setForeground(coloredAttributeSet, primaryColor);
                    doc.setCharacterAttributes(idx, i, coloredAttributeSet, true);
                    if (darkMode) {
                        setCaretColor(GuiResources.lightThemeColor);
                        StyleConstants.setForeground(defaultAttributeSet, GuiResources.lightThemeColor);
                    } else {
                        setCaretColor(GuiResources.darkThemeColor);
                        StyleConstants.setForeground(defaultAttributeSet, GuiResources.darkThemeColor);
                    }
                    doc.setCharacterAttributes(idx + i, line.length() - i, defaultAttributeSet, true);
                    idx += line.length() + 1;
                }
            } catch (BadLocationException e) {
                debugLabel.setText(e.toString());
            }
        }
    }
}