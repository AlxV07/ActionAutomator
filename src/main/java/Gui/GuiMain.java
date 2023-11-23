package Gui;

import ActionManagement.ActionExecutor;
import ActionManagement.CodeActionBuilder;
import ActionManagement.NativeKeyConverter;
import BindingManagement.Binding;
import BindingManagement.BindingFileManager;
import BindingManagement.BindingManager;
import GlobalListener.NativeGlobalListener;
import Gui.Components.*;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GuiMain extends ThemedFrame {
    private final ActionExecutor actionExecutor;
    private final BindingManager bindingManager;
    private final BindingFileManager bindingFileManager;
    private final Binding pressedKeys;

    private final CodeTextPane codeTextPane;
    private final ThemedTextArea selectedLabel;
    private final BindingPanelBox bindingPanels;

    private final ThemedLabel coordLabel;
    private final ThemedTextArea pressedLabel;
    private final ThemedLabel debugLabel;

    public GuiMain() {
        actionExecutor = new ActionExecutor();
        bindingManager = new BindingManager();
        pressedKeys = new Binding("Pressed Keys");

        ThemedMenuBar mainMenuBar = new ThemedMenuBar();
        mainMenuBar.setBounds(0, 0, 500, 21);
        super.add(mainMenuBar);

        codeTextPane = new CodeTextPane();
        codeTextPane.setBounds(0, 270, 250, 230);
        super.add(codeTextPane);

        ThemedButton enableEditing = new ThemedButton("Lock");
        enableEditing.setBounds(0, 250, 60, 20);
        enableEditing.addActionListener(e -> {
            codeTextPane.setEnabled(!codeTextPane.isEnabled());
            enableEditing.setText(codeTextPane.isEnabled() ? "Lock" : "Unlock");
        });
        super.add(enableEditing);

        selectedLabel = new ThemedTextArea() {@Override public void setText(String text) {super.setText(" " + text);}};
        selectedLabel.setFont(GuiResources.defaultFont);
        selectedLabel.setBounds(60, 250, 190, 20);
        super.add(selectedLabel);

        bindingPanels = new BindingPanelBox();
        bindingPanels.setBounds(0, 20, 500, 230);
        super.add(bindingPanels);

        bindingFileManager = new BindingFileManager(bindingManager, bindingPanels);

        coordLabel = new ThemedLabel("Mouse Coords");
        coordLabel.setBounds(250, 250, 250, 50);
        coordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(coordLabel);

        pressedLabel = new ThemedTextArea();
        pressedLabel.setText(GuiResources.pressedKeysLabel);
        pressedLabel.setFont(GuiResources.defaultFont);
        pressedLabel.setBounds(250, 300, 250, 50);
        super.add(pressedLabel);

        debugLabel = new ThemedLabel(" Debug Label ");
        debugLabel.setBounds(250, 350, 250, 50);
        debugLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(debugLabel);

        ThemedButton newActionButton = new ThemedButton(" New Action ");
        newActionButton.addActionListener(e -> bindingPanels.newBinding());
        mainMenuBar.add(newActionButton);

        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("ActionAutomator File", "action");
        fileChooser.setFileFilter(fileNameExtensionFilter);
        ThemedButton openActionButton = new ThemedButton(" Open Action ");
        openActionButton.addActionListener(e -> {
            fileChooser.showDialog(openActionButton, "Select");
            File file = fileChooser.getSelectedFile();
            if (file == null) return;
            bindingFileManager.readBindings(file.getAbsolutePath());
        });
        mainMenuBar.add(openActionButton);

        ThemedButton removeActionButton = new ThemedButton(" Remove Action ");
        removeActionButton.addActionListener(e -> bindingPanels.removeSelectedBinding());
        mainMenuBar.add(removeActionButton);

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
        mainMenuBar.add(runButton);

        SwingUtilities.invokeLater(() -> bindingFileManager.readBindings(GuiResources.cachePath));
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bindingFileManager.writeBindings(GuiResources.cachePath);
            }
        });
    }

    public void start(boolean darkMode, Color primaryColor, Color secondaryColor) {
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
                StringBuilder keyString = new StringBuilder(GuiResources.pressedKeysLabel);
                for (int k : pressedKeys.getKeySequence()) {
                    if (k == -1) break;
                    keyString.append(NativeKeyConverter.nativeKeyToString(k)).append("+");
                }
                pressedLabel.setText(keyString.substring(0, Math.max(0, keyString.length() - 1)));

                Binding binding = bindingManager.findBinding(pressedKeys);
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
            StringBuilder keyString = new StringBuilder(GuiResources.pressedKeysLabel);
            for (int k : pressedKeys.getKeySequence()) {
                if (k == -1) break;
                keyString.append(NativeKeyConverter.nativeKeyToString(k)).append("+");
            }
            pressedLabel.setText(keyString.substring(0, Math.max(0, keyString.length() - 1)));
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
            coordLabel.setText(String.format("Mouse Coords: %s-X %s-Y", nativeMouseEvent.getX(), nativeMouseEvent.getY()));
        }
    };

    public class CodeTextPane extends ThemedTextPane {
        private final SimpleAttributeSet defaultAttributeSet;
        private final SimpleAttributeSet coloredAttributeSet;

        public CodeTextPane() {
            super();
            super.setEnabled(true);
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
            SwingUtilities.invokeLater(this::updateTextColor);
        }

        public void updateTextColor() {
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

    public class BindingPanelBox extends ThemedBox {
        public final ArrayList<String> names;
        private final HashMap<String, BindingPanel> bindingPanels;
        private String selected;

        public BindingPanelBox() {
            super();
            this.names = new ArrayList<>();
            this.bindingPanels = new HashMap<>();
        }

        public void newBinding() {
            String actionName = JOptionPane.showInputDialog("Enter Action name:");
            if (actionName == null) {
                return;
            }
            actionName = actionName.strip();
            if (actionName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Empty Action name.");
                return;
            }
            if (names.contains(actionName)) {
                JOptionPane.showMessageDialog(this, String.format("\"%s\" already exists.", actionName));
                return;
            }
            this.addBinding(actionName, new Binding(actionName));
        }

        public void addBinding(String name, Binding binding) {
            bindingManager.setBinding(name, binding);
            BindingPanel bindingPanel = new BindingPanel(binding);
            add(bindingPanel);
            names.add(name);
            bindingPanels.put(name, bindingPanel);
            setSelected(names.size() - 1);
        }

        public void removeSelectedBinding() {
            String name = getSelected();
            if (name == null) {
                JOptionPane.showMessageDialog(this, "No selected Action to remove.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, String.format("Remove Action \"%s\"?", name)) == JOptionPane.YES_OPTION) {
                bindingManager.removeBinding(name);
                super.remove(bindingPanels.remove(name));
                int i = names.indexOf(name);
                if (i == -1) {
                    names.add(-1, "");
                }
                names.remove(i);
                if (names.isEmpty()) {
                    setSelected(-1);
                } else {
                    setSelected(Math.max(i - 1, 0));
                }
                revalidate();
                repaint();
                updatePanels();
            }
        }

        public String getSelected() {
            return this.selected;
        }

        private void updatePanels() {
            if (names != null) {
                int i = 0;
                while (i < names.size()) {
                    String name = names.get(i);
                    BindingPanel bindingPanel = bindingPanels.get(name);
                    bindingPanel.updateColorTheme(darkMode, primaryColor, secondaryColor);
                    if (name.equals(selected)) {
                        bindingPanel.setBackground(secondaryColor);
                        bindingPanel.editButton.setBackground(secondaryColor);
                    } else {
                        if (darkMode) {
                            bindingPanel.setBackground(GuiResources.darkThemeColor);
                        } else {
                            bindingPanel.setBackground(GuiResources.lightThemeColor);
                        }
                    }
                    i++;
                }
            }
        }

        public void setSelected(int idx) {
            if (idx == -1) {
                this.selected = null;
                codeTextPane.setText("null");
            } else {
                this.selected = names.get(idx);
                codeTextPane.setText(bindingManager.getBinding(selected).getCode());
            }
            selectedLabel.setText(this.selected);
            SwingUtilities.invokeLater(codeTextPane::updateTextColor);
            updatePanels();
        }

        public class BindingPanel extends ThemedPanel {
            /**
             * JPanel interface for interacting w/ Binding classes through GUI
             */
            private final Binding binding;
            private final BindingButton[] bindingButtons;
            public final ThemedButton editButton;
            private final ThemedTextArea nameLabel;

            public BindingPanel(Binding binding) {
                super();
                this.setLayout(null);
                this.binding = binding;
                this.nameLabel = new ThemedTextArea();
                this.nameLabel.setText(binding.getName());
                this.editButton = new ThemedButton("Edit");
                add(nameLabel);
                this.editButton.addActionListener(e -> setSelected(names.indexOf(binding.getName())));
                add(editButton);
                this.bindingButtons = new BindingButton[] {
                        new BindingButton(0),
                        new BindingButton(1),
                        new BindingButton(2),
                        new BindingButton(3),
                };
                for (BindingButton button : bindingButtons) {
                    add(button);
                }
                this.completeBind(0, -1);
            }

            /**
             * Set bounds for this panel and all it's BindingButtons
             * @param x X coord
             * @param y Y coord
             */
            public void setBounds(int x, int y, int width, int height) {
                super.setBounds(x, y, width, height);
                height -= 20;
                int i = 0;
                int compX = 5;
                int compY = 10;
                int buttonW = 85;
                nameLabel.setBounds(compX, compY, 80, height);
                editButton.setBounds(compX + 80, compY, 50, height);
                for (BindingButton button : bindingButtons) {
                    if (i > 0) {
                        JButton b = bindingButtons[i - 1];
                        button.setBounds(b.getX() + b.getWidth(), compY, buttonW, height);
                    } else {
                        button.setBounds(compX + 140, compY, buttonW, height);
                    }
                    i++;
                }
            }

            /**
             * Complete the binding for a given index and key
             * @param idx The index of the bound key
             * @param key The key that was bound (NativeKeyEvent VC)
             */
            public void completeBind(int idx, int key) {
                if (key == -1 || binding.containsKey(key)) {
                    binding.removeKey(binding.getKeySequence()[idx]);
                    for (int i = idx; i < binding.getNofKeys() + 1; i++) {
                        bindingButtons[i].setEnabled(true);
                        bindingButtons[i].setKeyText(binding.getKeySequence()[i]);
                    }
                    for (int i = binding.getNofKeys() + 1; i < binding.getKeySequence().length; i++) {
                        bindingButtons[i].setEnabled(false);
                        bindingButtons[i].setKeyText(binding.getKeySequence()[i]);
                    }
                } else {
                    binding.setKey(idx, key);
                    if (binding.getNofKeys() < bindingButtons.length) {
                        bindingButtons[binding.getNofKeys()].setEnabled(true);
                    }
                    bindingButtons[idx].setKeyText(key);
                }
                bindingButtons[0].setEnabled(true);
            }

            public class BindingButton extends ThemedButton {
                private final int idx;

                public BindingButton(int idx) {
                    super("null");
                    super.setFont(GuiResources.smallerFont);
                    this.idx = idx;
                    addActionListener(e -> {
                        bindingManager.startBindingButton(this);
                        super.setText("Binding...");
                        super.setForeground(secondaryColor);
                        super.setBackground(primaryColor);
                        super.requestFocusInWindow();
                    });
                }

                /**
                 * Set key text to be displayed on button, updates background color if marker position
                 * @param key The key whose text is to be displayed (NativeKeyEvent VC)
                 */
                public void setKeyText(int key) {
                    if (key == -1) {
                        super.setText("null");
                    } else {
                        super.setText(NativeKeyConverter.nativeKeyToString(key));
                    }
                }

                /**
                 * Complete a binding process with the binding panel
                 * @param key The resulting key of the binding process (NativeKeyEvent VC)
                 */
                public void completeButtonBind(int key) {
                    super.setForeground(primaryColor);
                    if (darkMode) {
                        super.setBackground(GuiResources.darkThemeColor);
                    } else {
                        super.setBackground(GuiResources.lightThemeColor);
                    }
                    completeBind(this.idx, key);
                }
            }
        }
    }
}