package com.actionautomator.Gui;

import com.actionautomator.ActionManagement.ActionExecutor;
import com.actionautomator.ActionManagement.CodeActionBuilder;
import com.actionautomator.ActionManagement.NativeKeyConverter;
import com.actionautomator.BindingManagement.Binding;
import com.actionautomator.BindingManagement.BindingFileManager;
import com.actionautomator.BindingManagement.BindingManager;
import com.actionautomator.ActionManagement.NativeGlobalListener;
import com.actionautomator.Gui.ThemedComponents.*;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GuiMainFrame extends ThemedFrame {
    private final ActionExecutor actionExecutor;
    private final BindingManager bindingManager;
    private final BindingFileManager bindingFileManager;
    private final Binding heldKeys;

    private final ProgInterface progInterface;
    private final ThemedButton renameSelectedButton;
    private final BindingContainer bindingContainer;

    private final ThemedLabel mouseCoordLabel;
    private final ThemedLabel coordWaypointLabel;
    private final ThemedLabel timerLabel;
    private final ThemedTextArea heldKeysLabel;
    private final ThemedButton stopButton;
    private boolean timerCounting = false;
    private long timerStartTime;
    private final ThemedTextArea helpDisplay;

    public GuiMainFrame() {
        bindingManager = new BindingManager();
        actionExecutor = new ActionExecutor(bindingManager);
        CodeActionBuilder codeActionBuilder = new CodeActionBuilder(bindingManager);
        heldKeys = new Binding();

        helpDisplay = new ThemedTextArea();
        helpDisplay.setText(ActionAutomatorResources.helpDisplayDoc);
        helpDisplay.setBounds(250, 380, 250, 120);
        helpDisplay.setHelp(helpDisplay, ActionAutomatorResources.helpDisplayDoc);
        helpDisplay.setLineWrap(true);
        helpDisplay.setFont(ActionAutomatorResources.defaultFont);
        add(helpDisplay);

        ThemedMenuBar mainMenuBar = new ThemedMenuBar();
        mainMenuBar.setBounds(0, 0, 500, 21);
        super.add(mainMenuBar);

        // Prog Interface
        progInterface = new ProgInterface(bindingManager, codeActionBuilder);
        ThemedScrollPane progInterfaceScrollPane = new ThemedScrollPane(progInterface);
        progInterfaceScrollPane.setBounds(0, 240, 250, 260);
        progInterface.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                helpDisplay.setText(ActionAutomatorResources.progInterfaceDoc);
            }
        });
        super.add(progInterfaceScrollPane);

        // Binding Container
        bindingContainer = new BindingContainer();
        ThemedScrollPane bindingContainerScrollPane = new ThemedScrollPane(bindingContainer);
        bindingContainerScrollPane.setBounds(-1, 20, 500, 200);
        bindingContainerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bindingContainerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        super.add(bindingContainerScrollPane);

        bindingFileManager = new BindingFileManager(bindingManager, bindingContainer, codeActionBuilder);

        // Name Selected
        renameSelectedButton = new ThemedButton("null") {
            @Override
            public void setText(String text) {
                super.setText("Rename: " + text);
            }
        };
        renameSelectedButton.setFont(ActionAutomatorResources.defaultFont);
        renameSelectedButton.setBounds(60, 220, 190, 20);
        renameSelectedButton.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name:");
            if (newName == null) {
                return;
            }
            newName = newName.strip();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Empty Action name.");
                return;
            }
            newName = newName.replaceAll(" ", "_");
            for (char c : newName.toCharArray()) {
                if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != '_') {
                    JOptionPane.showMessageDialog(this, "Illegal Action name:\nOnly Letters, Digits, '_' allowed");
                    return;
                }
            }
            if (!bindingContainer.setBindingName(bindingManager.getSelected(), newName)) {
                JOptionPane.showMessageDialog(this, "New given Action name already in use.");
            } else {
                renameSelectedButton.setText(bindingManager.getSelected());
            }
        });
        renameSelectedButton.setHelp(helpDisplay, ActionAutomatorResources.nameSelectedButtonDoc);
        super.add(renameSelectedButton);

        bindingManager.setOnSelectedChanged(() -> {
            bindingContainer.onSelectedChanged();
            progInterface.onSelectedChanged();
            renameSelectedButton.setText(bindingManager.getSelected());
        });
        progInterface.setOnCodeChanged(bindingContainer::onCodeChanged);

        // Lock Editing
        ThemedButton lockEditingButton = new ThemedButton("Lock");
        lockEditingButton.setBounds(0, 220, 60, 20);
        lockEditingButton.addActionListener(e -> {
            progInterface.setEnabled(!progInterface.isEnabled());
            renameSelectedButton.setEnabled(!renameSelectedButton.isEnabled());
            lockEditingButton.setText(progInterface.isEnabled() ? "Lock" : "Unlock");
        });
        lockEditingButton.setHelp(helpDisplay, ActionAutomatorResources.lockEditingButtonDoc);
        super.add(lockEditingButton);

        // Mouse-Coords
        mouseCoordLabel = new ThemedLabel("Mouse Coords");
        mouseCoordLabel.setBounds(250, 270, 250, 31);
        mouseCoordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mouseCoordLabel.setHelp(helpDisplay, ActionAutomatorResources.mouseCoordLabelDoc);
        super.add(mouseCoordLabel);

        // Waypoint
        coordWaypointLabel = new ThemedLabel("Save Waypoint (Esc)");
        coordWaypointLabel.setBounds(250, 300, 250, 30);
        coordWaypointLabel.setHorizontalAlignment(SwingConstants.CENTER);
        coordWaypointLabel.setHelp(helpDisplay, ActionAutomatorResources.coordWayPointLabelDoc);
        super.add(coordWaypointLabel);

        // Timer
        int timerComponentY = 340;
        timerLabel = new ThemedLabel("Timer (ms)");
        timerLabel.setBounds(290, timerComponentY, 210, 30);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setHelp(helpDisplay, ActionAutomatorResources.timerLabelDoc);
        super.add(timerLabel);
        class TimerRunnable implements Runnable {
            @Override
            public void run() {
                timerLabel.setText("Timer (ms): " + (System.currentTimeMillis() - timerStartTime));
                try {
                    Thread.sleep(10);
                    if (timerCounting) {
                        new TimerRunnable().run();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ThemedButton timerButton = new ThemedButton("(|>)");
        timerButton.setBounds(250, timerComponentY, 40, 30);
        timerButton.addActionListener(e -> {
            if (!timerCounting) {
                timerButton.setText("(||)");
                timerCounting = true;
                timerStartTime = System.currentTimeMillis();
                SwingUtilities.invokeLater(() -> new Thread(() -> new TimerRunnable().run()).start());
            } else {
                timerButton.setText("(|>)");
                timerCounting = false;
            }
        });
        timerButton.setHelp(helpDisplay, ActionAutomatorResources.timerButtonDoc);
        super.add(timerButton);

        // Held Keys
        heldKeysLabel = new ThemedTextArea();
        heldKeysLabel.setText(ActionAutomatorResources.heldKeysLabelText);
        heldKeysLabel.setBounds(250, 220, 250, 40);
        heldKeysLabel.setHelp(helpDisplay, ActionAutomatorResources.heldKeysLabelDoc);
        super.add(heldKeysLabel);

        // Settings
        ThemedMenu settingsMenu = new ThemedMenu("Settings");
        settingsMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                helpDisplay.setText(ActionAutomatorResources.settingsMenuDoc);
            }
        });

        // Toggle DarkMode
        ThemedButton toggleDarkMode = new ThemedButton(" Toggle Dark Mode ");
        toggleDarkMode.addActionListener(e -> updateColorTheme(!darkMode, primaryColor, secondaryColor));
        toggleDarkMode.setHelp(helpDisplay, ActionAutomatorResources.toggleDarkModeButtonDoc);
        settingsMenu.add(toggleDarkMode);

        // Theme
        ThemedButton setAAThemeColor = new ThemedButton(" Set Theme Color  ");
        setAAThemeColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "ActionAutomator: Choose Theme Color", null);
            if (color != null) {
                updateColorTheme(this.darkMode, color, Color.GRAY);
            }
        });
        setAAThemeColor.setHelp(helpDisplay, ActionAutomatorResources.setThemeButtonDoc);
        settingsMenu.add(setAAThemeColor);

        mainMenuBar.add(settingsMenu);
        mainMenuBar.add(new ThemedLabel("|"));

        // New Action
        ThemedButton newActionButton = new ThemedButton(" New ");
        newActionButton.addActionListener(e -> bindingContainer.newBinding());
        newActionButton.setHelp(helpDisplay, ActionAutomatorResources.newButtonDoc);
        mainMenuBar.add(newActionButton);

        // Open Action
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("ActionAutomator File (.action)", "action");
        fileChooser.setFileFilter(fileNameExtensionFilter);
        ThemedButton openActionButton = new ThemedButton(" Open ");
        openActionButton.addActionListener(e -> {
            fileChooser.showDialog(openActionButton, "Select");
            File file = fileChooser.getSelectedFile();
            if (file == null) return;
            bindingFileManager.readBindings(file.getAbsolutePath());
        });
        openActionButton.setHelp(helpDisplay, ActionAutomatorResources.openButtonDoc);
        mainMenuBar.add(openActionButton);

        // Delete Action
        ThemedButton deleteActionButton = new ThemedButton(" Delete ");
        deleteActionButton.addActionListener(e -> bindingContainer.removeSelectedBinding());
        deleteActionButton.setHelp(helpDisplay, ActionAutomatorResources.deleteButtonDoc);
        mainMenuBar.add(deleteActionButton);

        // Save Code
        ThemedButton saveCodeButton = new ThemedButton(" Save ");
        saveCodeButton.addActionListener(e -> bindingContainer.saveCode());
        saveCodeButton.setHelp(helpDisplay, ActionAutomatorResources.saveCodeButtonDoc);
        mainMenuBar.add(saveCodeButton);

        // Run Action
        ThemedButton runButton = new ThemedButton(" Run ");
        runButton.addActionListener(e -> {
            if (bindingManager.getSelected() == null) {
                JOptionPane.showMessageDialog(this, "No selected Action to run.");
                return;
            }
            saveCodeButton.doClick();
            BindingContainer.BindingPanel bindingPanel = bindingContainer.bindingPanels.get(bindingManager.getSelected());
            if (bindingPanel.isRunnable()) {
                actionExecutor.executeActionFromBinding(bindingPanel.binding, 100);
            }
        });
        runButton.setHelp(helpDisplay, ActionAutomatorResources.runButtonDoc);
        mainMenuBar.add(runButton);

        // Stop Action
        stopButton = new ThemedButton(" Stop (Esc) ");
        stopButton.addActionListener(e -> actionExecutor.interrupt());
        stopButton.setHelp(helpDisplay, ActionAutomatorResources.stopButtonDoc);
        mainMenuBar.add(stopButton);

        // Read Saved Bindings
        SwingUtilities.invokeLater(() -> bindingFileManager.readBindings(ActionAutomatorResources.cachePath));
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                bindingFileManager.writeBindings(ActionAutomatorResources.cachePath);
                nativeGlobalListener.unregister();
            }
        });
    }

    public void start(boolean darkMode, Color primaryColor, Color secondaryColor) {
        super.setTitle("ActionAutomator");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setLayout(null);
        super.setSize(500, 500);
        super.setResizable(false);
        super.updateColorTheme(darkMode, primaryColor, secondaryColor);
        super.setVisible(true);
        try {
            super.setIconImage(ImageIO.read(new File(ActionAutomatorResources.logoPath)));
        } catch (IOException ignored) {}
        nativeGlobalListener.register();
    }

    public class BindingContainer extends ThemedPanel {
        private final HashMap<String, BindingPanel> bindingPanels;
        private BindingPanel.BindingButton activeBindingButton;

        public BindingContainer() {
            super();
            super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.bindingPanels = new HashMap<>();
        }

        public void onSelectedChanged() {
            if (bindingManager.getPrevSelected() != null) {
                BindingPanel bindingPanel = bindingPanels.get(bindingManager.getPrevSelected());
                bindingPanel.updateColorTheme(darkMode, primaryColor, secondaryColor);
                bindingPanel.saveCode();
            }
            if (bindingManager.getSelected() != null) {
                BindingPanel bindingPanel = bindingPanels.get(bindingManager.getSelected());
                bindingPanel.updateColorTheme(darkMode, primaryColor, secondaryColor);
                bindingPanel.setBackground(secondaryColor);
                bindingPanel.editButton.setBackground(secondaryColor);
            }
        }

        public void startBindingButton(BindingPanel.BindingButton bindingButton) {
            if (activeBindingButton != null) {
                this.cancelBindingButton();
            }
            activeBindingButton = bindingButton;
        }

        public void cancelBindingButton() {
            activeBindingButton.completeButtonBind(-1);
            activeBindingButton = null;
        }

        public void completeBindingButton(int key) {
            if (key == NativeKeyEvent.VC_ESCAPE) {
                activeBindingButton.completeButtonBind(-1);
            } else {
                activeBindingButton.completeButtonBind(key);
            }
            activeBindingButton = null;
        }

        public boolean isBindingButton() {
            return activeBindingButton != null;
        }

        public boolean setBindingName(String oldName, String newName) {
            if (!bindingManager.setBindingName(oldName, newName)) {
                return false;
            }
            bindingPanels.put(newName, bindingPanels.remove(oldName));
            bindingPanels.get(newName).setBindingName(newName);
            return true;
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
            actionName = actionName.replaceAll(" ", "_");
            for (char c : actionName.toCharArray()) {
                if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != '_') {
                    JOptionPane.showMessageDialog(this, "Illegal Action name:\nOnly Letters, Digits, '_' allowed");
                    return;
                }
            }
            if (!this.addBinding(actionName, new Binding())) {
                JOptionPane.showMessageDialog(this, String.format("\"%s\" already exists.", actionName));
            }
        }

        public boolean addBinding(String actionName, Binding binding) {
            actionName = actionName.replaceAll(" ", "_");
            for (char c : actionName.toCharArray()) {
                if (!Character.isDigit(c) && !Character.isAlphabetic(c) && c != '_') {
                    return false;
                }
            }
            if (!bindingManager.addBinding(actionName, binding)) {
                return false;
            }
            BindingPanel bindingPanel = new BindingPanel(actionName, binding);
            add(bindingPanel);
            bindingPanels.put(actionName, bindingPanel);
            bindingManager.setSelected(actionName);
            return true;
        }

        public void removeSelectedBinding() {
            String name = bindingManager.getSelected();
            if (name == null) {
                JOptionPane.showMessageDialog(this, "No selected Action to remove.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, String.format("Delete: Action \"%s\"?", name)) == JOptionPane.YES_OPTION) {
                super.remove(bindingPanels.remove(name));
                bindingManager.removeBinding(name);
                revalidate();
                repaint();
            }
        }

        public void onCodeChanged() {
            bindingPanels.get(bindingManager.getSelected()).updateCodeStatus();
        }

        public void saveCode() {
            bindingPanels.get(bindingManager.getSelected()).saveCode();
        }


        public class BindingPanel extends ThemedPanel {
            private String name;
            private final Binding binding;
            private final BindingButton[] bindingButtons;
            private final ThemedButton editButton;
            private final ThemedLabel codeStatusLabel;
            private final ThemedTextArea nameDisplay;
            private boolean codeStatus = false;

            public BindingPanel(String name, Binding binding) {
                super();
                this.setPreferredSize(new Dimension(500, 50));
                this.name = name;
                this.binding = binding;

                int height = 40;
                this.codeStatusLabel = new ThemedLabel( "<->") {
                    @Override
                    public void updateColorTheme(boolean darkMode, Color primaryColor, Color secondaryColor) {
                        super.updateColorTheme(darkMode, primaryColor, secondaryColor);
                        if (!codeStatus) {
                            setForeground(secondaryColor);
                        }
                    }
                };
                codeStatusLabel.setPreferredSize(new Dimension(23, height));
                codeStatusLabel.getInsets().set(0, 0, 0, 0);
                codeStatusLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                codeStatusLabel.setHelp(helpDisplay, ActionAutomatorResources.codeStatusLabelDoc);
                add(codeStatusLabel);

                this.nameDisplay = new ThemedTextArea();
                nameDisplay.setText(this.name);
                nameDisplay.setHelp(helpDisplay, ActionAutomatorResources.bindingNameLabelDoc);
                add(nameDisplay);
                nameDisplay.setPreferredSize(new Dimension(100, height));
                this.editButton = new ThemedButton("Edit");
                editButton.setPreferredSize(new Dimension(40, height));
                editButton.addActionListener(e -> bindingManager.setSelected(this.name));
                editButton.setHelp(helpDisplay, ActionAutomatorResources.bindingEditButtonDoc);
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

            public void updateCodeStatus(boolean saved) {
                codeStatus = saved;
                if (saved) {
                    this.codeStatusLabel.setText("<✓>");
                } else {
                    this.codeStatusLabel.setText("<X>");
                }
                codeStatusLabel.updateColorTheme(darkMode, primaryColor, secondaryColor);
            }

            public void updateCodeStatus() {
                codeStatus = false;
                this.codeStatusLabel.setText("<->");
                codeStatusLabel.updateColorTheme(darkMode, primaryColor, secondaryColor);
            }

            public boolean isRunnable() {
                return codeStatus;
            }

            public void saveCode() {
                updateCodeStatus(progInterface.saveCode(this.name));
            }

            public void setBindingName(String newName) {
                this.name = newName;
                this.nameDisplay.setText(newName);
            }

            public void completeBind(int idx, int key) {
                if (key == -1 || binding.containsKey(key)) {
                    if (binding.getKeySequence()[idx] != key) {
                        binding.removeKey(binding.getKeySequence()[idx]);
                    }
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
                    super.setFont(ActionAutomatorResources.smallerFont);
                    super.setPreferredSize(new Dimension(60, 40));
                    this.idx = idx;
                    addActionListener(e -> {
                        startBindingButton(this);
                        super.setText("Bind...");
                        super.setForeground(secondaryColor);
                        super.setBackground(primaryColor);
                        super.requestFocusInWindow();
                    });
                    this.setHelp(helpDisplay, ActionAutomatorResources.bindingButtonDoc);
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
                        super.setBackground(ActionAutomatorResources.darkThemeColor);
                    } else {
                        super.setBackground(ActionAutomatorResources.lightThemeColor);
                    }
                    completeBind(this.idx, key);
                }
            }
        }
    }

    private final NativeGlobalListener nativeGlobalListener = new NativeGlobalListener() {
        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
            int key = nativeKeyEvent.getKeyCode();

            if (key == NativeKeyEvent.VC_ESCAPE) {
                stopButton.doClick();
                coordWaypointLabel.setText("Waypoint (Esc): " + mouseCoordLabel.getText().substring("Mouse Coords: ".length()));
            }

            if (bindingContainer.isBindingButton()) {
                bindingContainer.completeBindingButton(key);
                return;
            }

            if (heldKeys.getNofKeys() < heldKeys.getKeySequence().length) {
                if (!heldKeys.containsKey(key)) {
                    heldKeys.addKey(key);
                }
                StringBuilder keyString = new StringBuilder(ActionAutomatorResources.heldKeysLabelText);
                for (int k : heldKeys.getKeySequence()) {
                    if (k == -1) break;
                    keyString.append(NativeKeyConverter.nativeKeyToString(k)).append("+");
                }
                heldKeysLabel.setText(keyString.substring(0, Math.max(0, keyString.length() - 1)));

                Binding binding = bindingManager.findBinding(heldKeys);
                if (binding != null) {
                    actionExecutor.executeActionFromBinding(binding, 100);
                }
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
            heldKeys.removeKey(nativeKeyEvent.getKeyCode());
            StringBuilder keyString = new StringBuilder(ActionAutomatorResources.heldKeysLabelText);
            for (int k : heldKeys.getKeySequence()) {
                if (k == -1) break;
                keyString.append(NativeKeyConverter.nativeKeyToString(k)).append("+");
            }
            heldKeysLabel.setText(keyString.substring(0, Math.max(0, keyString.length() - 1)));
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
            mouseCoordLabel.setText(String.format("Mouse Coords: %s-X %s-Y", nativeMouseEvent.getX(), nativeMouseEvent.getY()));
        }
    };
}