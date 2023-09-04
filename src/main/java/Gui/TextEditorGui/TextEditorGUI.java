package Gui.TextEditorGui;

import ActionManagement.NativeKeyToVKKeyConverter;
import ActionManagement.ReadBuilder;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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

        actionsList = new ActionList(actionToCode, mainTextArea);


        JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(Resources.areaBorder);
        menuBar.setBounds(0, 0, 166, 20);
        menuBar.setMargin(Resources.margin);
        JButton[] buttons = new JButton[] {
            new JButton("New"),
            new JButton("Save"),
            new JButton("Open")
        };
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
        return keywords;
    }

    public void runAction() {
        List<String> lines = List.of(mainTextArea.getText().strip().split("\n"));
        try {
            List<String> codeLines = codeBuilder.getActionLinesFromCode(lines);
            int speed = Integer.parseInt(codeLines.get(0));
            readBuilder.parseLinesIntoAction(codeLines.subList(1, codeLines.size()))
                    .execute(executor, speed);

        } catch (IOException | ReadBuilder.SyntaxError e) {
            throw new RuntimeException(e);
        }
    }

    public void runAction(String action) {

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
            String action = keyToAction.getOrDefault(i, null);
            if (action != null) {
                runAction(action);
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
}
