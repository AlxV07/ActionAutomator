package Gui;

import ActionManagement.ActionBuilder;
import ActionManagement.ActionsHandler;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class TextPromptButton extends PresetButton {

    public TextPromptButton(String txt,
                            KeybindingButton setBuildEndKey,
                            KeybindingButton setWaitKey,
                            ActionBuilder builder,
                            ActionsHandler actionsHandler,
                            DefaultListModel<String> model) {
        super(txt);
        addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Enter name:");
            if (name != null && !name.isEmpty()) {
                try {
                    int p;
                    if (setBuildEndKey.keyPressed == 0) {
                        p = KeyEvent.VK_ESCAPE;
                    } else {
                        p = setBuildEndKey.keyPressed;
                    }
                    new SwingWorker<>() {
                        @Override
                        protected Void doInBackground() {
                            setEnabled(false);
                            actionsHandler.setAction(name, builder.copyBuild(p, setWaitKey.keyPressed));
                            model.addElement(name);
                            return null;
                        }

                        @Override
                        protected void done() {
                            setEnabled(true);
                        }
                    }.execute();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
