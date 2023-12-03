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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class GuiMainFrame extends ThemedFrame {
    private final ActionExecutor actionExecutor;
    private final BindingManager bindingManager;
    private final BindingFileManager bindingFileManager;
    private final Binding heldKeys;

    private final ProgInterface progInterface;
    private final ThemedButton selectedDisplay;
    private final BindingContainer bindingContainer;

    private final ThemedLabel mouseCoordLabel;
    private final ThemedLabel coordWaypointLabel;
    private final ThemedLabel timerLabel;
    private final ThemedTextArea heldKeysLabel;
    private final ThemedButton interruptButton;
    private boolean timerCounting = false;
    private long timerStartTime;

    public GuiMainFrame() {
        actionExecutor = new ActionExecutor();
        bindingManager = new BindingManager();
        heldKeys = new Binding();

        ThemedMenuBar mainMenuBar = new ThemedMenuBar();
        mainMenuBar.setBounds(0, 0, 500, 21);
        super.add(mainMenuBar);

        progInterface = new ProgInterface(bindingManager);
        ThemedScrollPane progInterfaceScrollPane = new ThemedScrollPane(progInterface);
        progInterfaceScrollPane.setBounds(0, 240, 250, 260);
        super.add(progInterfaceScrollPane);

        bindingContainer = new BindingContainer();
        ThemedScrollPane bindingContainerScrollPane = new ThemedScrollPane(bindingContainer);
        bindingContainerScrollPane.setBounds(-1, 20, 500, 200);
        bindingContainerScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        bindingContainerScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        super.add(bindingContainerScrollPane);

        bindingFileManager = new BindingFileManager(bindingManager, bindingContainer);

        selectedDisplay = new ThemedButton("null");
        selectedDisplay.setFocusable(true);
        selectedDisplay.setFont(ActionAutomatorResources.defaultFont);
        selectedDisplay.setBounds(60, 220, 190, 20);
        selectedDisplay.addActionListener(e -> {
            String newName = JOptionPane.showInputDialog("Enter new name:");
            if (newName == null) {
                return;
            }
            newName = newName.strip();
            if (newName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Empty Action name.");
                return;
            }
            if (!bindingContainer.setBindingName(bindingManager.getSelected(), newName)) {
                JOptionPane.showMessageDialog(this, "Invalid new name.");
            } else {
                selectedDisplay.setText(bindingManager.getSelected());
            }
        });
        super.add(selectedDisplay);

        bindingManager.setOnSelectedChanged(() -> {
            bindingContainer.onSelectedChanged();
            progInterface.onSelectedChanged();
            selectedDisplay.setText(bindingManager.getSelected());
        });

        ThemedButton enableEditing = new ThemedButton("Lock");
        enableEditing.setBounds(0, 220, 60, 20);
        enableEditing.addActionListener(e -> {
            progInterface.setEnabled(!progInterface.isEnabled());
            selectedDisplay.setEnabled(!selectedDisplay.isEnabled());
            enableEditing.setText(progInterface.isEnabled() ? "Lock" : "Unlock");
        });
        super.add(enableEditing);

        mouseCoordLabel = new ThemedLabel("Mouse Coords");
        mouseCoordLabel.setBounds(250, 270, 250, 40);
        mouseCoordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(mouseCoordLabel);

        coordWaypointLabel = new ThemedLabel("Save Waypoint (Esc)");
        coordWaypointLabel.setBounds(250, 310, 250, 40);
        coordWaypointLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(coordWaypointLabel);

        int timerComponentY = 360;

        timerLabel = new ThemedLabel("Timer (ms)");
        timerLabel.setBounds(290, timerComponentY, 210, 40);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(timerLabel);

        // Timer
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
        ThemedButton timerButton = new ThemedButton("|>");
        timerButton.setBounds(250, timerComponentY, 40, 40);
        timerButton.addActionListener(e -> {
            if (!timerCounting) {
                timerButton.setText("||");
                timerCounting = true;
                timerStartTime = System.currentTimeMillis();
                SwingUtilities.invokeLater(() -> new Thread(() -> new TimerRunnable().run()).start());
            } else {
                timerButton.setText("|>");
                timerCounting = false;
            }
        });
        super.add(timerButton);

        // Held Keys
        heldKeysLabel = new ThemedTextArea();
        heldKeysLabel.setText(ActionAutomatorResources.heldKeysLabelText);
        heldKeysLabel.setBounds(250, 220, 250, 40);
        super.add(heldKeysLabel);

        // Help
        ThemedMenu settingsMenu = new ThemedMenu("Settings");

        ThemedButton helpButton = new ThemedButton(" Open Help Page   ");
        helpButton.addActionListener(e -> {
            try {
                BufferedImage image = ImageIO.read(new File(ActionAutomatorResources.helpPage));
                ImageIcon icon = new ImageIcon(image);
                JOptionPane.showMessageDialog(null, new JLabel(icon));
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Could not find Help page :(");
            }
        });

        // Theme
        ThemedButton toggleDarkMode = new ThemedButton(" Toggle Dark Mode ");
        toggleDarkMode.addActionListener(e -> updateColorTheme(!darkMode, primaryColor, secondaryColor));

        ThemedButton setAAThemeColor = new ThemedButton(" Set Theme Color  ");
        setAAThemeColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "ActionAutomator: Choose Theme Color", null);
            if (color != null) {
                updateColorTheme(this.darkMode, color, Color.GRAY);
            }
        });
        settingsMenu.add(toggleDarkMode);
        settingsMenu.add(setAAThemeColor);
        settingsMenu.add(helpButton);
        settingsMenu.updateColorTheme(false, Color.GREEN, Color.RED);

        mainMenuBar.add(settingsMenu);
        mainMenuBar.add(new ThemedLabel("|"));

        // New Action
        ThemedButton newActionButton = new ThemedButton(" New ");
        newActionButton.addActionListener(e -> bindingContainer.newBinding());
        mainMenuBar.add(newActionButton);

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
        mainMenuBar.add(openActionButton);

        ThemedButton removeActionButton = new ThemedButton(" Remove ");
        removeActionButton.addActionListener(e -> bindingContainer.removeSelectedBinding());
        mainMenuBar.add(removeActionButton);

        ThemedButton runButton = new ThemedButton(" Run ");
        runButton.addActionListener(e -> {
            if (bindingManager.getSelected() == null) {
                JOptionPane.showMessageDialog(this, "No selected Action to run.");
                return;
            }
            try {
                actionExecutor.executeActionFromCode(progInterface.getText(), 100);
            } catch (CodeActionBuilder.SyntaxError e1) {
                throw new RuntimeException(e1);
            }
        });
        mainMenuBar.add(runButton);

        interruptButton = new ThemedButton(" Stop (Esc) ");
        interruptButton.addActionListener(e -> actionExecutor.interrupt());
        mainMenuBar.add(interruptButton);

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
        super.updateColorTheme(darkMode, primaryColor, secondaryColor);
        super.setVisible(true);
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
                bindingPanels.get(bindingManager.getPrevSelected()).updateColorTheme(darkMode, primaryColor, secondaryColor);
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
            if (!this.addBinding(actionName, new Binding())) {
                JOptionPane.showMessageDialog(this, String.format("\"%s\" already exists.", actionName));
            }
        }

        public boolean addBinding(String name, Binding binding) {
            if (!bindingManager.addBinding(name, binding)) {
                return false;
            }
            BindingPanel bindingPanel = new BindingPanel(name, binding);
            add(bindingPanel);
            bindingPanels.put(name, bindingPanel);
            bindingManager.setSelected(name);
            return true;
        }

        public void removeSelectedBinding() {
            String name = bindingManager.getSelected();
            if (name == null) {
                JOptionPane.showMessageDialog(this, "No selected Action to remove.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, String.format("Remove Action \"%s\"?", name)) == JOptionPane.YES_OPTION) {
                super.remove(bindingPanels.remove(name));
                bindingManager.removeBinding(name);
                revalidate();
                repaint();
            }
        }


        public class BindingPanel extends ThemedPanel {
            private String name;
            private final Binding binding;
            private final BindingButton[] bindingButtons;
            private final ThemedButton editButton;
            private final ThemedTextArea nameDisplay;

            public BindingPanel(String name, Binding binding) {
                super();
                this.setPreferredSize(new Dimension(500, 50));
                this.name = name;
                this.binding = binding;
                this.nameDisplay = new ThemedTextArea();
                nameDisplay.setText(this.name);
                add(nameDisplay);
                nameDisplay.setPreferredSize(new Dimension(105, 40));
                this.editButton = new ThemedButton("Edit");
                editButton.setPreferredSize(new Dimension(45, 40));
                editButton.addActionListener(e -> bindingManager.setSelected(this.name));
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
                    super.setPreferredSize(new Dimension(70, 40));
                    this.idx = idx;
                    addActionListener(e -> {
                        startBindingButton(this);
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
                interruptButton.doClick();
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