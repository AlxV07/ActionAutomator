package Gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gui {
    private final JFrame f = new JFrame();

    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> actions = new JList<>(model);
    private final ActionsListUpdater actionsListUpdater = new ActionsListUpdater();
    private final JButton buildNewAction = new JButton("+");
    private final JButton removeAction = new JButton("_");
    private final JLabel selectedAction = new JLabel("Default Text But Longer");
    private final JButton startAction = new JButton("Run:");

    private final JButton setBuildEndKey = new JButton();
    private final JLabel buildEndKey = new JLabel();

    private final JButton setInterruptKey = new JButton();
    private final JLabel interruptKey = new JLabel();


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
        for (int i = 0; i < 10; i++) {
            model.addElement(String.valueOf(i));
        }
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
            int i = actions.getSelectedIndex();
            if (i != -1) {
                model.remove(i);
            }
            f.requestFocus();
        });
        f.add(removeAction);

        startAction.setBounds(90, 25, 30, 20);
        startAction.setMargin(insets);
        startAction.setFont(font);
        startAction.setFocusPainted(false);
        f.add(startAction);
    }

    private void setUpLabels() {
        Font font = new Font("Arial", Font.PLAIN, 10);
        selectedAction.setBounds(125, 25, 300, 20);
        selectedAction.setFont(font);
        f.add(selectedAction);
    }



    private class ActionsListUpdater implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            selectedAction.setText(actions.getSelectedValue());
        }
    }

    public static void main(String[] args) {
        Gui gui = new Gui();
        gui.setUpFrame();
    }
}
