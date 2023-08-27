package Gui.TextEditorGui;


import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.HashMap;

public class ActionList extends DefaultListModel<String> {
    public final JList<String> list;
    private final HashMap<String, String> nameToActionBindings;
    private final JTextArea mainTextArea;

    public ActionList(HashMap<String, String> nameToActionBindings, JTextArea mainTextArea) {
        this.nameToActionBindings = nameToActionBindings;
        this.mainTextArea = mainTextArea;
        list = new JList<>(this);
        list.setBounds(0, 0, 124, 160);
        list.setFont(new Font("Arial", Font.PLAIN, 12));
        list.setFocusable(false);
        list.addListSelectionListener(new ActionListUpdater());
    }

    private String previousSelected = "";

    private class ActionListUpdater implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!previousSelected.isBlank()) {
                nameToActionBindings.put(previousSelected, String.join(";", mainTextArea.getText().split("\n")));
            }
            String s = list.getSelectedValue();
            previousSelected = s;
            mainTextArea.setText(String.join("\n", nameToActionBindings.get(s).split(";")));
        }
    }
}
