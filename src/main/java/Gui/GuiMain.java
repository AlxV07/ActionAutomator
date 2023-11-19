package Gui;

import ActionManagement.CodeActionBuilder;
import ActionManagement.Action;
import ActionManagement.ActionManager;
import BindingManagement.Binding;
import BindingManagement.BindingManager;
import ListenManagement.NativeGlobalListener;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.util.HashMap;

public class GuiMain extends JFrame {
    private final ActionManager actionManager;
    private Action runningAction = null;
    private final BindingManager bindingManager;
    private final CodeActionBuilder codeActionBuilder;

    private final MainMenuBar mainMenuBar;
    private final CodeTextPane codeTextPane;
    private final ActionList actionList;

    private final JButton newActionButton;
    private final JButton removeActionButton;
    private final JButton runButton;

    private final JLabel coordLabel;
    private final JLabel pressedLabel;
    private final JLabel debugLabel;


    public GuiMain() {
        actionManager = new ActionManager();
        bindingManager = new BindingManager();
        codeActionBuilder = new CodeActionBuilder();

        mainMenuBar = new MainMenuBar();
        mainMenuBar.setBounds(0, 0, 500, 20);
        super.add(mainMenuBar);
        actionList = new ActionList();
        actionList.setBounds(0, 20, 250, 280);
        super.add(actionList);

        codeTextPane = new CodeTextPane();
        codeTextPane.setBounds(0, 300, 250, 200);
        super.add(codeTextPane);

        newActionButton = new TexturedButton("New Action");
        newActionButton.addActionListener(e -> actionList.newAction());
        newActionButton.setFocusable(false);
        mainMenuBar.add(newActionButton);

        removeActionButton = new TexturedButton("Remove Action");
        removeActionButton.addActionListener(e -> actionList.removeSelectedAction());
        removeActionButton.setFocusable(false);
        mainMenuBar.add(removeActionButton);

        coordLabel = new JLabel("Mouse Coord Label");
        coordLabel.setBounds(350, 340, 100, 40);
        super.add(coordLabel);

        pressedLabel = new JLabel("Pressed Keys Label");
        pressedLabel.setBounds(250, 360, 250, 40);
        pressedLabel.setText(pressedKeys.toString());
        pressedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(pressedLabel);

        debugLabel = new JLabel("Debug Label");
        debugLabel.setBounds(250, 380, 250, 40);
        debugLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(debugLabel);

        runButton = new TexturedButton("RUN");
        runButton.addActionListener(e -> {
            try {
                if (runningAction != null) {
                    runningAction.interrupt();
                }
                runningAction = codeActionBuilder.parseCodeIntoAction(codeTextPane.getText());
                actionManager.executeAction(runningAction, 100);
            } catch (CodeActionBuilder.SyntaxError e1) {
                debugLabel.setText(toString());
            }
        });
        mainMenuBar.add(runButton);
    }

    public void start() {
        super.setTitle("ActionAutomator");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setLayout(null);
        super.setSize(500, 500);
        super.setVisible(true);
        nativeGlobalListener.register();
    }

    private final Binding pressedKeys = new Binding("PressedKeys");

    private final NativeGlobalListener nativeGlobalListener = new NativeGlobalListener() {
        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
            int key = nativeKeyEvent.getKeyCode();

            if (bindingManager.isBindingButton()) {
                bindingManager.completeBindingButton(key);
                return;
            }

            if (pressedKeys.getNofKeys() < pressedKeys.getKeySequence().length) {
                pressedKeys.addKey(key);
                Binding binding = bindingManager.findBinding(pressedKeys);
                pressedLabel.setText(pressedKeys + " \n" + binding);
                if (binding != null) {
                    try {
                        if (runningAction != null) {
                            runningAction.interrupt();
                        }
                        runningAction = codeActionBuilder.parseCodeIntoAction(binding.getCode());
                        actionManager.executeAction(runningAction, 100);
                    } catch (Exception e) {
                        debugLabel.setText(e.toString());
                    }
                }
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
            pressedKeys.removeKey(nativeKeyEvent.getKeyCode());
            pressedLabel.setText(String.valueOf(pressedKeys));
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
            coordLabel.setText(nativeMouseEvent.getX() + " " + nativeMouseEvent.getY());
        }
    };

    class ActionList extends JList<String> {
        private final DefaultListModel<String> defaultListModel;
        private final HashMap<String, BindingPanel> bindingPanels;

        public ActionList() {
            super(new DefaultListModel<>());
            this.defaultListModel = (DefaultListModel<String>) super.getModel();
            super.setFont(new Font("Arial", Font.PLAIN, 12));
            super.setBorder(GuiResources.areaBorder);
            super.setBackground(GuiResources.backgrououndColor);
            super.addListSelectionListener(e -> codeTextPane.setText(bindingManager.getBinding(getSelectedValue()).getCode()));
            super.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    int preferredHeight = 30;
                    label.setPreferredSize(new Dimension(label.getPreferredSize().width, preferredHeight));

                    if (index % 2 == 0) {
                        label.setBackground(Color.LIGHT_GRAY); // Set alternate background color for even-indexed cells
                    } else {
                        label.setBackground(Color.GRAY);
                    }
                    return label;
                }
            });
            this.bindingPanels = new HashMap<>();
        }

        public void newAction() {
            String actionName = JOptionPane.showInputDialog("Enter Action name:");
            if (actionName == null) {
                return;
            }
            actionName = actionName.strip();
            if (!actionName.isEmpty()) {
                if (defaultListModel.contains(actionName)) {
                    JOptionPane.showMessageDialog(this, String.format("\"%s\" already exists.", actionName));
                } else {
                    bindingManager.createNewBinding(actionName);
                    BindingPanel bindingPanel = new BindingPanel(bindingManager, bindingManager.getBinding(actionName));
                    getRootPane().add(bindingPanel);
                    bindingPanel.setBounds(250, 20 + defaultListModel.getSize() * 30);
                    bindingPanels.put(actionName, bindingPanel);
                    defaultListModel.add(defaultListModel.getSize(), actionName);
                    super.setSelectedIndex(defaultListModel.getSize() - 1);
                    codeTextPane.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Empty Action name.");
            }
        }

        public void removeSelectedAction() {
            String actionName = getSelectedValue();
            if (actionName == null) {
                JOptionPane.showMessageDialog(this, "No selected Action to remove.");
                return;
            }
            if (JOptionPane.showConfirmDialog(this, String.format("Remove Action \"%s\"?", actionName)) == JOptionPane.YES_OPTION) {
                bindingManager.removeBinding(actionName);
                BindingPanel panel = this.bindingPanels.remove(actionName);
                panel.removeComponents();
                getRootPane().remove(panel);
                getRootPane().revalidate();
                getRootPane().repaint();
                int i = getSelectedIndex();
                defaultListModel.remove(i);
                if (defaultListModel.getSize() > 0) {
                    super.setSelectedIndex(Math.max(i - 1, 0));
                }
            }
        }

        @Override
        public String getSelectedValue() {
            return super.getSelectedValue();
        }
    }

    class CodeTextPane extends JTextPane {

        public CodeTextPane() {
            super();
            super.setFont(Font.getFont("Source Code Pro"));
            super.setMargin(new Insets(10, 10, 10, 10));
            super.setBorder(GuiResources.areaBorder);
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
            if (actionList.getSelectedValue() == null) {
                debugLabel.setText("No selected Action to edit.");
                return;
            }
            bindingManager.setBindingCode(actionList.getSelectedValue(), codeTextPane.getText());
            SwingUtilities.invokeLater(this::updateFirstWordColor);
        }

        private void updateFirstWordColor() {
            StyledDocument doc = super.getStyledDocument();
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
                    MutableAttributeSet redAttr = new SimpleAttributeSet();
                    StyleConstants.setForeground(redAttr, Color.RED);
                    doc.setCharacterAttributes(idx, i, redAttr, true);
                    MutableAttributeSet normAttr = new SimpleAttributeSet();
                    doc.setCharacterAttributes(idx + i, line.length() - i, normAttr, true);
                    idx += line.length() + 1;
                }
            } catch (BadLocationException e) {
                debugLabel.setText(e.toString());
            }
        }
    }
}