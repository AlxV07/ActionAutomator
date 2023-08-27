package Gui.TextEditorGui;// Java Program to create a text editor using java

import ActionManagement.ReadBuilder;

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
    private final Robot executor;
    private final ActionList actionsList;
    private final HashMap<String, String> nameToAction;

    public TextEditorGUI() {
        try {
            this.executor = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
        nameToAction = new HashMap<>();

        List<String> keywords = getAutoCompleteKeywordList();
        final String COMMIT_ACTION = "commit";
        final String QUOTE_ACTION = "quote";

        mainTextArea = new JTextArea();
        Autocomplete autoComplete = new Autocomplete(mainTextArea, keywords);
        mainTextArea.getDocument().addDocumentListener(autoComplete);

        mainTextArea.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        mainTextArea.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
        mainTextArea.getInputMap().put(KeyStroke.getKeyStroke("QUOTE"), QUOTE_ACTION);
        mainTextArea.getActionMap().put(QUOTE_ACTION, autoComplete.new QuoteFinishTask());

        mainTextArea.setFont(Font.getFont("Source Code Pro"));
        mainTextArea.setTabSize(2);
        mainTextArea.setMargin(new Insets(5, 5, 5, 5));
        mainTextArea.setBounds(165, 0, 335, 500);

        actionsList = new ActionList(nameToAction, mainTextArea);

        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi3 = new JMenuItem("Save");
        JMenuItem mi2 = new JMenuItem("Open");
        mi1.addActionListener(this);
        mi3.addActionListener(this);
        mi2.addActionListener(this);
        m1.add(mi1);
        m1.add(mi3);
        m1.add(mi2);
        mb.add(m1);

        JButton runButton = new JButton();
        runButton.addActionListener(e -> runAction());
        runButton.setBounds(125, 0, 40, 18);
        runButton.setFont(new Font("Source Code Pro", Font.PLAIN, 10));
        runButton.setMargin(new Insets(0, 0, 0, 0));
        runButton.setFocusPainted(false);
        runButton.setText("Run");

        f = new JFrame("ActionAutomator Manual Editor");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setJMenuBar(mb);
        f.setLayout(null);
        f.add(actionsList.list);
        f.add(mainTextArea);
        f.add(runButton);
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
                else {
                    JOptionPane.showMessageDialog(f, "The User cancelled the operation");
                }
            }
            case "New" -> {
                nameToAction.put(actionsList.list.getSelectedValue(), String.join(";", mainTextArea.getText().split("\n")));
                String input = JOptionPane.showInputDialog("");
                if (!input.isEmpty()) {
                    actionsList.addElement(input);
                    nameToAction.put(input, "speed=100\n");
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
        keywords.add("move");
        return keywords;
    }

    public void runAction() {
        List<String> lines = List.of(mainTextArea.getText().strip().split("\n"));
        if (lines.size() > 1 && lines.get(0).startsWith("speed=")) {
            try {
                readBuilder.parseLinesIntoAction(lines.subList(1, lines.size())).execute(executor,
                                                 Integer.parseInt(lines.get(0).substring(6)));
            } catch (ReadBuilder.SyntaxError ignored) {
            }
        }
    }
}
