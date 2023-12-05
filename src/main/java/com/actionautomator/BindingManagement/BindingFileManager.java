package com.actionautomator.BindingManagement;

import com.actionautomator.ActionManagement.CodeActionBuilder;
import com.actionautomator.Gui.GuiMainFrame.BindingContainer;
import com.actionautomator.Gui.ActionAutomatorResources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BindingFileManager {
    private final BindingManager bindingManager;
    private final BindingContainer bindingPanels;
    private final CodeActionBuilder codeActionBuilder;

    public BindingFileManager(BindingManager bindingManager, BindingContainer bindingPanels, CodeActionBuilder codeActionBuilder) {
        this.bindingManager = bindingManager;
        this.bindingPanels = bindingPanels;
        this.codeActionBuilder = codeActionBuilder;
    }
    public void writeBindings(String targetPath) {
        File directory = new File(ActionAutomatorResources.directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (FileWriter writer = new FileWriter(targetPath)) {
            for (String bindingName : bindingManager.getOrderedBindingNames()) {
                Binding binding = bindingManager.getBinding(bindingName);
                writer.write(bindingName + "\n");
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
                Binding binding = new Binding();
                try {
                    binding.setCode(code.toString(), codeActionBuilder);
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
