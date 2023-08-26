package Gui;

import ActionManagement.ActionBuilder;
import ActionManagement.ActionsHandler;
import ActionManagement.NativeKeyToVKKeyConverter;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;


public class GuiMain {
    private final ActionsHandler actionsHandler = new ActionsHandler();
    private final ActionBuilder builder = new ActionBuilder();

    private final JTextField speed = new JTextField("100");
    private final JLabel speedLabel = new PresetLabel("Execution Speed (ms):");

    private JButton buildNewAction;
    private final JButton removeAction = new PresetButton("_");
    private final JButton startAction = new PresetButton("Run:");
    private final JButton cleanAction = new PresetButton("Clean Selected Action");

    private final JLabel waitKey = new PresetLabel("Default (None)");
    private final KeybindingButton setWaitKey = new KeybindingButton("Set Wait Key:", waitKey) {
        @Override
        public void keyPressed(KeyEvent e) {
            if (waitingForKey) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_BACK_SPACE) {
                    keyPressed = -1;
                    label.setText("Default (None)");
                } else {
                    keyPressed = e.getKeyCode();
                    label.setText(KeyEvent.getKeyText(keyCode));
                }
                waitingForKey = false;
            }
        }
    };

    private final JLabel buildEndKey = new PresetLabel("Default (Escape)");
    private final KeybindingButton setBuildEndKey = new KeybindingButton("Set Build-End Key:", buildEndKey);

    private final JLabel interruptKey = new PresetLabel("Default (Escape)");
    private final KeybindingButton setInterruptKey = new KeybindingButton("Set Interrupt Key:", interruptKey);

    private final HashMap<Integer, String> runKeyBindingsToActions = new HashMap<>();
    private final HashMap<String, Integer> actionsToRunKeyBindings = new HashMap<>();


    private MainFrame frame;
    private ActionsList actionsList;


    private KeybindingButton setRunKey;
    private DefaultListModel<String> model;


    private void setUpFrame() {
        frame = new MainFrame();
        model = new DefaultListModel<>();
        actionsList = new ActionsList(frame, actionsToRunKeyBindings, model);

        buildNewAction = new TextPromptButton("+",
                setBuildEndKey,
                setWaitKey,
                builder,
                actionsHandler,
                model
        );

        setRunKey = new KeybindingButton("Set Run Key:", actionsList.runKeyLabel) {
            @Override
            public void keyPressed(KeyEvent e) {
                if (waitingForKey) {
                    int keyCode = e.getKeyCode();
                    if (keyCode == KeyEvent.VK_BACK_SPACE) {
                        runKeyBindingsToActions.remove(actionsToRunKeyBindings.remove(actionsList.getSelectedValue()));

                        keyPressed = -1;
                        label.setText("Default (None)");
                    } else {
                        runKeyBindingsToActions.remove(actionsToRunKeyBindings.remove(actionsList.getSelectedValue()));

                        runKeyBindingsToActions.put(e.getKeyCode(), actionsList.getSelectedValue());
                        actionsToRunKeyBindings.put(actionsList.getSelectedValue(), e.getKeyCode());

                        keyPressed = e.getKeyCode();
                        label.setText(KeyEvent.getKeyText(keyCode));
                    }
                    waitingForKey = false;
                }
            }
        };

        setUpComponents();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    actionsList.setFocusable(true);
                    actionsList.clearSelection();
                    actionsList.setFocusable(false);
                }
            }
        });
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                builder.end();
            }
        });
        frame.setFocusable(true);
        frame.setVisible(true);
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(new InterruptAndRunKeyListener());
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }
    }

    private void setUpComponents() {
        setUpButtons();
        setUpLabels();
        speed.setBounds(285, 210, 50, 20);
        speed.setMargin(Resources.buttonInsets);
        speed.setFont(Resources.buttonFont);
        speed.setHorizontalAlignment(JTextField.CENTER);
        frame.add(speed);
    }

    private void setUpButtons() {
        buildNewAction.setBounds(25, 25, 20, 20);
        frame.add(buildNewAction);

        removeAction.setBounds(50, 25, 20, 20);
        removeAction.setMargin(new Insets(0, 0, 5, 0));
        removeAction.addActionListener(e -> {
            int i = actionsList.getMinSelectionIndex();
            if (i != -1) {
                for (int j = i; j < actionsList.getMaxSelectionIndex(); j++) {
                    runKeyBindingsToActions.remove(actionsToRunKeyBindings.remove(
                            model.get(actionsList.getMinSelectionIndex() + i)
                    ));
                }
                model.removeRange(i, actionsList.getMaxSelectionIndex());
            }
            frame.requestFocus();
        });
        frame.add(removeAction);

        startAction.setBounds(90, 25, 30, 20);
        startAction.addActionListener(e -> {
            int s;
            try {
                s = Integer.parseInt(speed.getText());
            } catch (ClassCastException ex) {
                s = 100;
            }
            if (actionsList.getSelectedValue() != null) {
                actionsHandler.executeAction(actionsList.getSelectedValue(), s);
            }
        });
        frame.add(startAction);

        cleanAction.setBounds(170, 180, 100, 20);
        cleanAction.addActionListener(e -> {
            if (actionsList.getSelectedValue() != null) {
                actionsHandler.cleanAction(actionsList.getSelectedValue());
            }
        });
        frame.add(cleanAction);

        setBuildEndKey.setBounds(170, 60, 80, 20);
        frame.add(setBuildEndKey);

        setInterruptKey.setBounds(170, 90, 80, 20);
        frame.add(setInterruptKey);

        setWaitKey.setBounds(170, 120, 80, 20);
        frame.add(setWaitKey);

        setRunKey.setBounds(170, 150, 80, 20);
        frame.add(setRunKey);
    }

    private void setUpLabels() {
        buildEndKey.setBounds(260, 60, 300, 20);
        frame.add(buildEndKey);

        interruptKey.setBounds(260, 90, 300, 20);
        frame.add(interruptKey);

        waitKey.setBounds(260, 120, 300, 20);
        frame.add(waitKey);

        speedLabel.setBounds(170, 210, 300, 20);
        frame.add(speedLabel);
    }

    public void start() {
        this.setUpFrame();
    }


    private class InterruptAndRunKeyListener implements NativeKeyListener {
        @Override
        public void nativeKeyPressed(NativeKeyEvent e) {
            if (setInterruptKey.keyPressed == 0) {
                setInterruptKey.keyPressed = KeyEvent.VK_ESCAPE;
            }

            int i = NativeKeyToVKKeyConverter.convertNativeKeyToKeyEventVK(e.getKeyCode());
            if (i == setInterruptKey.keyPressed) {
                actionsHandler.interrupt();
            } else if (runKeyBindingsToActions.get(i) != null) {
                int s;
                try {
                    s = Integer.parseInt(speed.getText());
                } catch (ClassCastException ex) {
                    s = 100;
                }
                if (actionsList.getSelectedValue() != null) {
                    actionsHandler.executeAction(runKeyBindingsToActions.get(i), s);
                }
            }
        }
    }
}
