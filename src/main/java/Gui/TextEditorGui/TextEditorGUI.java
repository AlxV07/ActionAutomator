package Gui.TextEditorGui;// Java Program to create a text editor using java

import ActionManagement.ReadBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class TextEditorGUI extends JFrame implements ActionListener {
    private final JTextArea mainTextArea;
    private final JFrame f;
    private ReadBuilder readBuilder = new ReadBuilder();
    private Robot executor;

    // Constructor
    public TextEditorGUI() {
        try {
            this.executor = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        List<String> keywords = getAutoCompleteKeywordList();
        final String COMMIT_ACTION = "commit";
        mainTextArea = new JTextArea("speed=100");
        Autocomplete autoComplete = new Autocomplete(mainTextArea, keywords);
        mainTextArea.getDocument().addDocumentListener(autoComplete);
        mainTextArea.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        mainTextArea.getActionMap().put(COMMIT_ACTION, autoComplete.new CommitAction());
        mainTextArea.setFont(Font.getFont("Source Code Pro"));
        mainTextArea.setTabSize(2);
        mainTextArea.setBounds(165, 0, 335, 500);

        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenuItem mi1 = new JMenuItem("New");
        JMenuItem mi2 = new JMenuItem("Open");
        JMenuItem mi3 = new JMenuItem("Save");
        mi1.addActionListener(this);
        mi2.addActionListener(this);
        mi3.addActionListener(this);
        m1.add(mi1);
        m1.add(mi2);
        m1.add(mi3);
        mb.add(m1);

        JButton runButton = new JButton();
        runButton.addActionListener(e -> runAction());
        runButton.setBounds(20, 20, 40, 20);

        runButton.setFont(new Font("Arial", Font.PLAIN, 10));
        runButton.setMargin(new Insets(0, 0, 0, 0));
        runButton.setText("Run");

        f = new JFrame("Action Automator Text Editor");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setJMenuBar(mb);
        f.setLayout(null);
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
                JFileChooser j = new JFileChooser("f:");
                int r = j.showSaveDialog(null);

                if (r == JFileChooser.APPROVE_OPTION) {
                    File fi = new File(j.getSelectedFile().getAbsolutePath());
                    try {
                        FileWriter wr = new FileWriter(fi, false);
                        BufferedWriter w = new BufferedWriter(wr);
                        w.write(mainTextArea.getText());
                        w.flush();
                        w.close();
                    } catch (Exception evt) {
                        JOptionPane.showMessageDialog(f, evt.getMessage());
                    }
                }
                else {
                    JOptionPane.showMessageDialog(f, "The User cancelled the operation");
                }
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
            case "New" -> mainTextArea.setText("");
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
            readBuilder.parseLinesIntoAction(lines.subList(1, lines.size())).execute(executor,
                                             Integer.parseInt(lines.get(0).substring(6)));
        }
    }
}
