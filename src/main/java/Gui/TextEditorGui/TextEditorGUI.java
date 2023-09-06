package Gui.TextEditorGui;

import ActionManagement.NativeKeyToVKKeyConverter;
import ActionManagement.ReadBuilder;
import ActionManagement.Action;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class TextEditorGUI extends JFrame implements ActionListener {
    private final JTextArea mainTextArea;
    private final JFrame f;
    private final ReadBuilder readBuilder = new ReadBuilder();
    private final CodeBuilder codeBuilder = new CodeBuilder();
    private final Robot executor;
    private final ActionList actionsList;
    private final HashMap<String, String> actionToCode;

    private final JLabel runKeyLabel = new JLabel();
    private final KeybindingButton runKeyBinder = new KeybindingButton("Default(None)", "Set Run Key:", runKeyLabel) {
        @Override
        public void keyPressed(KeyEvent e) {
            if (waitingForKey) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_BACK_SPACE) {
                    actionToKey.remove(keyToAction.remove(key));
                    key = -1;
                    label.setText(this.defaultText);
                } else {
                    String action = keyToAction.remove(key);
                    actionToKey.put(action, keyCode);
                    keyToAction.put(keyCode, action);
                    key = keyCode;
                    label.setText(KeyEvent.getKeyText(keyCode));
                }
                waitingForKey = false;
            }
        }
    };

    private final JLabel interruptKeyLabel = new JLabel();
    private final KeybindingButton interruptKeyBinder = new KeybindingButton("Default(Esc)", "Set Interrupt Key:", interruptKeyLabel);

    private final HashMap<Integer, String> keyToAction;
    private final HashMap<String, Integer> actionToKey;
    private int mouseX;
    private int mouseY;

    public TextEditorGUI() {
        keyToAction = new HashMap<>();
        actionToKey = new HashMap<>();
        try {
            this.executor = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        actionToCode = new HashMap<>();

        List<String> keywords = getAutoCompleteKeywordList();
        final String COMMIT_ACTION = "commit";
        final String QUOTE_ACTION = "quote";

        mainTextArea = new JTextArea("null");
        Autocomplete autoComplete = new Autocomplete(mainTextArea, keywords);
        mainTextArea.getDocument().addDocumentListener(autoComplete);

        mainTextArea.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        mainTextArea.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
        mainTextArea.getInputMap().put(KeyStroke.getKeyStroke("QUOTE"), QUOTE_ACTION);
        mainTextArea.getActionMap().put(QUOTE_ACTION, autoComplete.new QuoteFinishTask());
        mainTextArea.setFont(Font.getFont("Source Code Pro"));
        mainTextArea.setTabSize(2);
        mainTextArea.setMargin(new Insets(10, 10, 10, 10));
        mainTextArea.setBounds(165, -1, 335, 501);
        mainTextArea.setBorder(Resources.areaBorder);

        actionsList = new ActionList();

        runKeyBinder.setBounds(0, 240, 80, 20);
        runKeyBinder.setBorderPainted(false);
        runKeyLabel.setBounds(85, 240, 100, 20);
        runKeyLabel.setFont(Resources.labelFont);

        interruptKeyBinder.setBounds(0, 270, 80, 20);
        interruptKeyBinder.setBorderPainted(false);
        interruptKeyLabel.setBounds(85, 270, 100, 20);
        interruptKeyLabel.setFont(Resources.labelFont);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(Resources.areaBorder);
        menuBar.setBounds(0, 0, 166, 20);
        menuBar.setMargin(Resources.margin);
        JButton[] buttons = new JButton[]{new JButton("New"), new JButton("Save"), new JButton("Open")};
        for (JButton button : buttons) {
            button.setFont(Resources.labelFont);
            button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            button.setMargin(Resources.margin);
            button.setFocusPainted(false);
            button.addActionListener(this);
            menuBar.add(button);
        }

        JButton runButton = new JButton();
        runButton.addActionListener(e -> runAction());
        runButton.setBounds(126, -1, 40, 18);
        runButton.setFont(Resources.labelFont);
        runButton.setBorder(BorderFactory.createEmptyBorder(5, 11, 5, 10));
        runButton.setMargin(Resources.margin);
        runButton.setFocusPainted(false);
        runButton.setText("Run");
        menuBar.add(runButton);

        JLabel mx = new JLabel();
        JLabel my = new JLabel();
        mx.setBounds(7, 26, 100, 20);
        mx.setFont(Resources.labelFont);
        mx.setText("Mouse X:");
        mx.setHorizontalAlignment(SwingConstants.LEFT);
        my.setBounds(7, 48, 100, 20);
        my.setFont(Resources.labelFont);
        my.setHorizontalAlignment(SwingConstants.LEFT);
        my.setText("Mouse Y:");
        NativeGuiListener guiListener = new NativeGuiListener(mx, my);
        try {
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(guiListener);
            GlobalScreen.addNativeMouseMotionListener(guiListener);
        } catch (NativeHookException e) {
            throw new RuntimeException(e);
        }

        f = new JFrame("ActionAutomator Manual Editor");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setLayout(null);
        f.add(menuBar);
        f.add(actionsList.list);
        f.add(mainTextArea);
        f.add(mx); f.add(my);
        f.setSize(500, 500);
        f.add(runKeyLabel);
        f.add(runKeyBinder);
        f.add(interruptKeyLabel);
        f.add(interruptKeyBinder);
        f.setResizable(false);
        f.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        switch (s) {
            case "Save" -> {
            }
            case "Open" -> {
                JFileChooser j = new JFileChooser("f:");
                int r = j.showOpenDialog(null);

                if (r == JFileChooser.APPROVE_OPTION) {
                    File fi = new File(j.getSelectedFile().getAbsolutePath());
                    try {
                        String s1;
                        StringBuilder sl;

                        FileReader fr = new FileReader(fi);
                        BufferedReader br = new BufferedReader(fr);

                        sl = new StringBuilder(br.readLine());
                        while ((s1 = br.readLine()) != null) {
                            sl.append("\n").append(s1);
                        }

                        mainTextArea.setText(sl.toString());
                    } catch (Exception evt) {
                        JOptionPane.showMessageDialog(f, evt.getMessage());
                    }
                }
            }
            case "New" -> {
                actionToCode.put(actionsList.list.getSelectedValue(), String.join(";", mainTextArea.getText().split("\n")));
                String input = JOptionPane.showInputDialog("Enter New Action Name:");
                if (input != null && !input.isEmpty()) {
                    actionsList.addElement(input);
                    actionToCode.put(input, "speed=100\n");
                    actionsList.list.setSelectedIndex(actionsList.size() - 1);
                    runKeyBinder.key = -1;
                    runKeyBinder.label.setText(runKeyBinder.defaultText);
                }
            }
        }
    }

    private List<String> getAutoCompleteKeywordList() {
        List<String> keywords = new ArrayList<>();
        keywords.add("typewrite");
        keywords.add("left_click()");
        keywords.add("left_down()");
        keywords.add("left_up()");
        keywords.add("right_click()");
        keywords.add("right_down()");
        keywords.add("right_up()");
        keywords.add("wait");
        keywords.add("move");
        keywords.add("key_press");
        keywords.add("key_down");
        keywords.add("key_up");
        return keywords;
    }

    private Action runningAction = null;

    public void runAction() {
        List<String> lines = List.of(mainTextArea.getText().strip().split("\n"));
        try {
            List<String> codeLines = codeBuilder.getActionLinesFromCode(lines);
            int speed = Integer.parseInt(codeLines.get(0));
            if (runningAction != null && runningAction.isRunning()) {
                runningAction.interrupt();
            }
            runningAction = readBuilder.parseLinesIntoAction(codeLines.subList(1, codeLines.size()));
            runningAction.execute(executor, speed);

        } catch (IOException | ReadBuilder.SyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    public void runAction(String action) {
        actionsList.list.setSelectedValue(action, true);
        runAction();
    }


    private class NativeGuiListener implements NativeKeyListener, NativeMouseMotionListener {
        private final JLabel mx;
        private final JLabel my;

        public NativeGuiListener(JLabel mx, JLabel my) {
            this.mx = mx;
            this.my = my;
        }
        @Override
        public void nativeKeyPressed(NativeKeyEvent e) {
            int i = NativeKeyToVKKeyConverter.convertNativeKeyToKeyEventVK(e.getKeyCode());
            if (i == interruptKeyBinder.key || (i == KeyEvent.VK_ESCAPE && interruptKeyBinder.key == -1)) {
                if (runningAction != null && runningAction.isRunning()) {
                    runningAction.interrupt();
                }
            }
            else if (!interruptKeyBinder.waitingForKey && !runKeyBinder.waitingForKey){
                String action = keyToAction.getOrDefault(i, null);
                if (action != null) {
                    runAction(action);
                }
            }
        }

        @Override
        public void nativeMouseMoved(NativeMouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            mx.setText("Mouse X:  " + mouseX);
            my.setText("Mouse Y:  " + mouseY);
        }
    }

    private class ActionList extends DefaultListModel<String> {
        public final JList<String> list;
        public ActionList() {
            list = new JList<>(this);
            list.setBounds(-1, 80, 167, 160);
            list.setFont(new Font("Arial", Font.PLAIN, 12));
            list.setFocusable(false);
            list.addListSelectionListener(new ActionListUpdater());
            list.setBorder(Resources.areaBorder);
            list.setBackground(Resources.backgrououndColor);
        }

        private String previousSelected = "";

        private class ActionListUpdater implements ListSelectionListener {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!previousSelected.isBlank()) {
                    actionToCode.put(previousSelected, String.join(";", mainTextArea.getText().split("\n")));
                    actionToKey.put(previousSelected, runKeyBinder.key);
                    keyToAction.put(runKeyBinder.key, previousSelected);
                }
                String s = list.getSelectedValue();
                previousSelected = s;
                mainTextArea.setText(String.join("\n", actionToCode.get(s).split(";")));
                Integer key = actionToKey.getOrDefault(s, null);
                if (key != null) {
                    runKeyBinder.key = key;
                    if (key != -1) {
                        runKeyBinder.label.setText(KeyEvent.getKeyText(key));
                    } else {
                        runKeyBinder.label.setText(runKeyBinder.defaultText);
                    }
                }
            }
        }
    }

}
