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

public class GuiMain extends JFrame {
    private final ActionManager actionManager;
    private final JLabel pressedLabel;
    private Action runningAction = null;
    private final BindingManager bindingManager;
    private final CodeActionBuilder codeActionBuilder;

    private final JButton newActionButton;
    private final JButton removeActionButton;

    private final MainMenuBar mainMenuBar;
    private final CodeTextPane codeTextPane;
    private final ActionList actionList;

    private final JButton runButton;
    private final JLabel coordLabel;


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
        codeTextPane.setText("null null");
        super.add(codeTextPane);

        newActionButton = new TexturedButton("New Action");
        newActionButton.addActionListener(e -> actionList.newAction());
        newActionButton.setFocusable(false);
        mainMenuBar.add(newActionButton);

        removeActionButton = new TexturedButton("Remove Action");
        removeActionButton.addActionListener(e -> actionList.removeSelectedAction());
        removeActionButton.setFocusable(false);
        mainMenuBar.add(removeActionButton);

        runButton = new TexturedButton("RUN");
        runButton.addActionListener(e -> {
            try {
                if (runningAction != null) {
                    runningAction.interrupt();
                }
                runningAction = codeActionBuilder.parseCodeIntoAction(codeTextPane.getText());
                actionManager.executeAction(runningAction, 100);
            } catch (CodeActionBuilder.SyntaxError e1) {
                e1.printStackTrace();
            }
        });
        mainMenuBar.add(runButton);

        coordLabel = new JLabel();
        coordLabel.setBounds(350, 350, 100, 40);
        super.add(coordLabel);

        pressedLabel = new JLabel();
        pressedLabel.setBounds(250, 370, 250, 40);
        pressedLabel.setText(pressedKeys.toString());
        pressedLabel.setHorizontalAlignment(SwingConstants.CENTER);
        super.add(pressedLabel);
    }

    public void start() {
        super.setTitle("ActionAutomator");
        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setLayout(null);
        super.setSize(500, 500);
        super.setVisible(true);
        nativeGlobalListener.register();
    }

    private final Binding pressedKeys = new Binding();

    private final NativeGlobalListener nativeGlobalListener = new NativeGlobalListener() {
        @Override
        public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
            int key = nativeKeyEvent.getKeyCode();

            if (bindingManager.isBinding()) {
                bindingManager.completeBindingButton(key);
                return;
            }

            pressedKeys.addKey(key);
            String actionName = bindingManager.getActionNameFromBinding(pressedKeys);
            pressedLabel.setText(pressedKeys + " " +actionName);
            if (actionName != null) {
                try {
                    if (runningAction != null) {
                        runningAction.interrupt();
                    }
                    runningAction = codeActionBuilder.parseCodeIntoAction(bindingManager.ActionNameToCode.get(actionName));
                    actionManager.executeAction(runningAction, 100);
                } catch (CodeActionBuilder.SyntaxError e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
            int x = nativeKeyEvent.getKeyCode();
            pressedKeys.removeKey(x);
            pressedLabel.setText(String.valueOf(pressedKeys));
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
            coordLabel.setText(nativeMouseEvent.getX() + " " + nativeMouseEvent.getY());
        }
    };

    class ActionList extends JList<String> {
        private final DefaultListModel<String> defaultListModel;

        public ActionList() {
            super(new DefaultListModel<>());
            defaultListModel = (DefaultListModel<String>) super.getModel();
            super.setFont(new Font("Arial", Font.PLAIN, 12));
            super.setBorder(GuiResources.areaBorder);
            super.setBackground(GuiResources.backgrououndColor);
            super.addListSelectionListener(e -> codeTextPane.setText(bindingManager.ActionNameToCode.get(getSelectedValue())));
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
                    BindingPanel bindingPanel = new BindingPanel(bindingManager, actionName);
                    getRootPane().add(bindingPanel);
                    bindingPanel.setBounds(250, 20 + defaultListModel.getSize() * 30);
                    bindingManager.bindActionNameTOBindingPanel(actionName, bindingPanel);
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
                int i = getSelectedIndex();
                bindingManager.ActionNameToCode.remove(actionName);
                BindingPanel panel = bindingManager.getBindingPanelFromActionName(actionName);
                panel.deleteComponents();
                getRootPane().remove(panel);
                getRootPane().revalidate();
                getRootPane().repaint();
                bindingManager.ActionNameToBindingPanel.remove(actionName);
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
            bindingManager.bindActionNameToCode(actionList.getSelectedValue(), codeTextPane.getText());
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
                    MutableAttributeSet attrs = new SimpleAttributeSet();
                    StyleConstants.setForeground(attrs, Color.RED);
                    doc.setCharacterAttributes(idx, i, attrs, false);
                    idx += line.length() + 1;
                }
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
}