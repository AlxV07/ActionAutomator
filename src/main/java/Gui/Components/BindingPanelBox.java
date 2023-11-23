package Gui.Components;

import BindingManagement.Binding;
import BindingManagement.BindingManager;
import Gui.GuiMain.CodeTextPane;
import Gui.GuiResources;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BindingPanelBox extends ThemedBox {
    final ArrayList<String> names;
    private final HashMap<String, BindingPanel> bindingPanels;
    private final BindingManager bindingManager;
    private final CodeTextPane codeTextPane;
    private String selected;

    public BindingPanelBox(BindingManager bindingManager, CodeTextPane codeTextPane) {
        super();
        this.bindingManager = bindingManager;
        this.codeTextPane = codeTextPane;
        this.names = new ArrayList<>();
        this.bindingPanels = new HashMap<>();

        SwingUtilities.invokeLater(() -> {
            try {
                List<String> lines = Files.readAllLines(Path.of("/home/alxv05/.action_automator/codes.txt"));
                int idx = 0;
                while (idx < lines.size()) {
                    String name = lines.get(idx);
                    StringBuilder code = new StringBuilder();
                    idx += 1;
                    while (!lines.get(idx).equals("===")) {
                        code.append(lines.get(idx)).append("\n");
                        idx += 1;
                    }
                    Binding binding = new Binding(name);
                    binding.setCode(code.toString());
                    this.addBinding(name, binding);
                    idx += 1;
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
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
            codeTextPane.updateFirstWordColor();
        } else {
            this.selected = names.get(idx);
            codeTextPane.setText(bindingManager.getBinding(selected).getCode());
            codeTextPane.updateFirstWordColor();
        }
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
                    super.setText(NativeKeyEvent.getKeyText(key));
                }
            }

            @Override
            public void setEnabled(boolean enabled) {
                super.setEnabled(enabled);
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
