package Gui;

import BindingManagement.Binding;
import BindingManagement.BindingManager;
import Gui.AAComponents.AABox;
import Gui.GuiMain.CodeTextPane;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BindingPanelBox extends AABox {
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
        BindingPanel bindingPanel = new BindingPanel(bindingManager, binding, this);
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
//                bindingPanel.setBounds(0, i * 30, 480, 30);
                bindingPanel.updateColorTheme(darkMode, primaryColor, secondaryColor);
                if (name.equals(selected)) {
                    bindingPanel.setBackground(secondaryColor);
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
}
