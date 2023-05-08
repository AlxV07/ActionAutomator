import ActionManagement.ActionBuilder;
import ActionManagement.ActionsHandler;
import com.github.kwhat.jnativehook.GlobalScreen;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;

public class Gui {
    private final ActionsHandler actionsHandler = new ActionsHandler();
    private ActionBuilder builder = new ActionBuilder();

    private final JFrame f = new JFrame();

    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> actions = new JList<>(model);
    private final ActionsListUpdater actionsListUpdater = new ActionsListUpdater();
    private final JButton buildNewAction = new TextPromptButton("+");
    private final JButton removeAction = new JButton("_");
    private final JLabel selectedAction = new JLabel("Default Text But Longer");
    private final JButton startAction = new JButton("Run:");

    private final JLabel buildEndKey = new JLabel("None");
    private final KeyButton setBuildEndKey = new KeyButton("Set Build End Key:", buildEndKey);

    private final JLabel interruptKey = new JLabel("None");
    private final JButton setInterruptKey = new KeyButton("Set Interrupt Key:", interruptKey);


    private void setUpFrame() {
        f.setSize(500, 400);
        f.setTitle("Gui");
        f.setLayout(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setUpComponents();
        f.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    actions.setFocusable(true);
                    actions.clearSelection();
                    actions.setFocusable(false);
                }
            }
        });
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                builder.end();
            }
        });
        f.setFocusable(true);
        f.setVisible(true);
    }

    private void setUpComponents() {
        setUpActionsList();
        setUpButtons();
        setUpLabels();
    }

    private void setUpActionsList() {
        actions.setBounds(25, 55, 130, 255);
        actions.setFont(new Font("Arial", Font.PLAIN, 12));
        actions.setFocusable(false);
        actions.addListSelectionListener(actionsListUpdater);
        f.add(actions);
    }

    private void setUpButtons() {
        Font font = new Font("Arial", Font.PLAIN, 8);
        Insets insets = new Insets(0, 0, 0, 0);
        buildNewAction.setBounds(25, 25, 20, 20);
        buildNewAction.setMargin(insets);
        buildNewAction.setFont(font);
        buildNewAction.setFocusPainted(false);
        f.add(buildNewAction);

        removeAction.setBounds(50, 25, 20, 20);
        removeAction.setMargin(new Insets(0, 0, 5, 0));
        removeAction.setFont(font);
        removeAction.setFocusPainted(false);
        removeAction.addActionListener(e -> {
            int i = actions.getMinSelectionIndex();
            if (i != -1) {
                model.removeRange(i, actions.getMaxSelectionIndex());
            }
            f.requestFocus();
        });
        f.add(removeAction);

        startAction.setBounds(90, 25, 30, 20);
        startAction.setMargin(insets);
        startAction.setFont(font);
        startAction.setFocusPainted(false);
        startAction.addActionListener(e -> {
            if (actions.getSelectedValue() != null) {
                actionsHandler.executeAction(actions.getSelectedValue());
            }
        });
        f.add(startAction);

        setBuildEndKey.setBounds(170, 60, 80, 20);
        setBuildEndKey.setMargin(insets);
        setBuildEndKey.setFont(font);
        f.add(setBuildEndKey);

        setInterruptKey.setBounds(170, 90, 80, 20);
        setInterruptKey.setMargin(insets);
        setInterruptKey.setFont(font);
        f.add(setInterruptKey);
    }

    private void setUpLabels() {
        Font font = new Font("Arial", Font.PLAIN, 10);

        selectedAction.setBounds(125, 25, 300, 20);
        selectedAction.setFont(font);
        f.add(selectedAction);

        buildEndKey.setBounds(260, 60, 300, 20);
        buildEndKey.setFont(font);
        f.add(buildEndKey);

        interruptKey.setBounds(260, 90, 300, 20);
        interruptKey.setFont(font);
        f.add(interruptKey);
    }

    public void start() {
        this.setUpFrame();
    }

    private class ActionsListUpdater implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            selectedAction.setText(actions.getSelectedValue());
        }
    }

    private static class KeyButton extends JButton implements KeyListener {
        private boolean waitingForKey = false;
        public int keyPressed;
        private final JLabel label;

        public KeyButton(String text, JLabel label) {
            super(text);
            this.label = label;
            addActionListener(e -> {
                this.label.setText("Press a key...");
                requestFocusInWindow();
                waitingForKey = true;
            });
            addKeyListener(this);
            setFocusPainted(false);
        }

        public void keyPressed(KeyEvent e) {
            if (waitingForKey) {
                int keyCode = e.getKeyCode();
                if (keyCode != KeyEvent.VK_BACK_SPACE) {
                    keyPressed = e.getKeyCode();
                    label.setText(KeyEvent.getKeyText(keyCode));
                } else {
                    label.setText("None");
                }
                waitingForKey = false;
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }

    private class TextPromptButton extends JButton {

        public TextPromptButton(String txt) {
            super(txt);
            addActionListener(e -> {
                String name = JOptionPane.showInputDialog("Enter name:");
                if (name != null && !name.isEmpty()) {
                    try {
                        if (!buildEndKey.getText().equals("None")) {
                            actionsHandler.setAction(name, builder.buildAction(setBuildEndKey.keyPressed));
                        } else {
                            actionsHandler.setAction(name, builder.buildAction());
                        }
                        Gui.this.model.addElement(name);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }
    }
}
