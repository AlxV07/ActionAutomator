package com.actionautomator.BindingManagement;

import com.actionautomator.ActionManagement.CodeActionBuilder;
import com.actionautomator.Gui.GuiFrame.BindingPanelContainer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BindingFileManager {
    private final BindingManager bindingManager;
    private final BindingPanelContainer bindingPanels;

    public BindingFileManager(BindingManager bindingManager, BindingPanelContainer bindingPanels) {
        this.bindingManager = bindingManager;
        this.bindingPanels = bindingPanels;
    }
    public void writeBindings(String targetPath) {
        try (FileWriter writer = new FileWriter(targetPath)) {
            for (String bindingName : bindingPanels.names) {
                Binding binding = bindingManager.getBinding(bindingName);
                writer.write(binding.getName().strip() + "\n");
                String code = binding.getCode();
                if (code == null) code = "";
                writer.write(code.strip() + "\n");
                writer.write("===\n");
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void readBindings(String targetPath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(targetPath));
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
                try {
                    binding.setCode(code.toString());
                } catch (CodeActionBuilder.SyntaxError ignored) {
                }
                bindingPanels.addBinding(name, binding);
                idx += 1;
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
