package com.actionautomator.ActionManagement;

import com.actionautomator.BindingManagement.Binding;
import com.actionautomator.BindingManagement.BindingManager;
import com.actionautomator.Gui.ActionAutomatorResources;
import com.actionautomator.Gui.GuiMainFrame.BindingContainer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ActionFileManager {
    private final BindingManager bindingManager;
    private final BindingContainer bindingPanels;
    private final CodeActionBuilder codeActionBuilder;

    public ActionFileManager(BindingManager bindingManager, BindingContainer bindingPanels, CodeActionBuilder codeActionBuilder) {
        this.bindingManager = bindingManager;
        this.bindingPanels = bindingPanels;
        this.codeActionBuilder = codeActionBuilder;
    }

    public void saveAllActions() {
        try (FileWriter writer = new FileWriter(ActionAutomatorResources.orderedActionsPath)) {
            for (String bindingName : bindingManager.getOrderedBindingNames()) {
                writer.write(bindingName + "\n");
                saveAction(bindingName);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void saveAction(String bindingName) {
        File directory = new File(ActionAutomatorResources.directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) throw new RuntimeException();
        }
        try (FileWriter writer = new FileWriter(ActionAutomatorResources.directoryPath + "/" + bindingName.strip() + ".action")) {
            Binding binding = bindingManager.getBinding(bindingName);
            writer.write(bindingName + "\n");
            String code = binding.getCode();
            if (code == null) code = "";
            writer.write(code.strip());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void readAllSavedActions() {
        File file = new File(ActionAutomatorResources.orderedActionsPath);
        if (file.exists()) {
            try {
                List<String> lines = Files.readAllLines(Path.of(ActionAutomatorResources.orderedActionsPath));
                for (String line : lines) {
                    readAction(
                            ActionAutomatorResources.directoryPath + "/" + line.strip() + ".action"
                    );
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readAction(String actionAbsoluteFilePath) {
        try {
            List<String> lines = Files.readAllLines(Path.of(actionAbsoluteFilePath));
            String bindingName = lines.get(0);
            StringBuilder code = new StringBuilder();
            for (int i = 1; i < lines.size(); i++) {
                code.append(lines.get(i)).append("\n");
            }
            Binding binding = new Binding();
            try {
                binding.setCode(code.toString(), codeActionBuilder);
            } catch (CodeActionBuilder.SyntaxError ignored) {
            }
            bindingPanels.addBinding(bindingName, binding);
        } catch (IOException ignored) {
        }
    }
}
