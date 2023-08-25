package Gui;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class ActionsList extends JList<String> {
    private final HashMap<String, Integer> actionsToRunKeyBindings;
    private final JLabel selectedActionLabel;
    public final JLabel runKeyLabel = new JLabel("Default (None)");


    public ActionsList(JFrame frame, HashMap<String, Integer> actionsToRunKeyBindings, DefaultListModel<String> model) {
        super(model);

        this.actionsToRunKeyBindings = actionsToRunKeyBindings;
        super.setBounds(25, 55, 130, 175);
        super.setFont(new Font("Arial", Font.PLAIN, 12));
        super.setFocusable(false);
        super.addListSelectionListener(new ActionsListUpdater());
        frame.add(this);

        this.selectedActionLabel = new JLabel("None");
        this.selectedActionLabel.setBounds(125, 25, 300, 20);
        this.selectedActionLabel.setFont(Resources.labelFont);
        frame.add(selectedActionLabel);

        this.runKeyLabel.setBounds(260, 150, 300, 20);
        this.runKeyLabel.setFont(Resources.labelFont);
        frame.add(runKeyLabel);

    }


    private class ActionsListUpdater implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            selectedActionLabel.setText(getSelectedValue());
            Integer x = actionsToRunKeyBindings.getOrDefault(getSelectedValue(), -1);
            if (x == -1) {
                runKeyLabel.setText("Default (None)");
            } else {
                runKeyLabel.setText(KeyEvent.getKeyText(x));
            }
        }
    }
}
