package Gui.TextEditorGui;// Java Program to create a text editor using java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class TextEditor extends JFrame implements ActionListener {
    private final JTextArea mainTextArea;
    private final JFrame f;

    // Constructor
    public TextEditor() {
        List<String> keywords = getAutoCompleteKeywordList();
        final String COMMIT_ACTION = "commit";
        mainTextArea = new JTextArea();
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

        f = new JFrame("Action Automator Text Editor");
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setJMenuBar(mb);
        f.setLayout(null);
        f.add(mainTextArea);
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
        keywords.add("left_click");
        keywords.add("left_button_down");
        keywords.add("right_click");
        keywords.add("right_button_down");
        keywords.add("move");
        return keywords;
    }

    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
    }
}
