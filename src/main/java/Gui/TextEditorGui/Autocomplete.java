package Gui.TextEditorGui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.event.ActionEvent;
import java.io.Serial;
import java.util.Collections;
import java.util.List;

public class Autocomplete implements DocumentListener {

    private enum Mode {
        INSERT,
        COMPLETION
    }

    private final JTextArea textArea;
    private final List<String> keywords;
    private Mode mode = Mode.INSERT;

    public Autocomplete(JTextArea textArea, List<String> keywords) {
        this.textArea = textArea;
        this.keywords = keywords;
        Collections.sort(keywords);
    }

    @Override
    public void changedUpdate(DocumentEvent ev) { }

    @Override
    public void removeUpdate(DocumentEvent ev) { }

    @Override
    public void insertUpdate(DocumentEvent ev) {
        if (ev.getLength() != 1)
            return;

        int pos = ev.getOffset();
        String content = null;
        try {
            content = textArea.getText(0, pos + 1);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        // Find where the word starts
        int w;
        for (w = pos; w >= 0; w--) {
            assert content != null;
            if (Character.isWhitespace(content.charAt(w))) {
                break;
            }
        }

        // Too few chars
        if (pos - w < 2)
            return;

        String prefix = content.substring(w + 1).toLowerCase();
        int n = Collections.binarySearch(keywords, prefix);
        if (n < 0 && -n <= keywords.size()) {
            String match = keywords.get(-n - 1);
            if (match.startsWith(prefix)) {
                // A completion is found
                String completion = match.substring(pos - w);
                // We cannot modify Document from within notification,
                // so we submit a task that does the change later
                SwingUtilities.invokeLater(new CompletionTask(completion, pos + 1));
            }
        } else {
            // Nothing found
            mode = Mode.INSERT;
        }
    }

    public class CommitAction extends AbstractAction {
        @Serial
        private static final long serialVersionUID = 5794543109646743416L;

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (mode == Mode.COMPLETION) {
                int pos = textArea.getSelectionEnd();
                StringBuilder sb = new StringBuilder(textArea.getText());
                if (sb.charAt(sb.length() - 1) != ')') {
                    sb.insert(pos, "()");
                } else {
                    pos -= 1;
                }
                textArea.setText(sb.toString());
                textArea.setCaretPosition(pos + 1);
                mode = Mode.INSERT;
            } else {
                textArea.replaceSelection("\t");
            }
        }
    }

    private class CompletionTask implements Runnable {
        private final String completion;
        private final int position;

        CompletionTask(String completion, int position) {
            this.completion = completion;
            this.position = position;
        }

        public void run() {
            StringBuilder sb = new StringBuilder(textArea.getText());
            sb.insert(position, completion);
            textArea.setText(sb.toString());
            textArea.setCaretPosition(position + completion.length());
            textArea.moveCaretPosition(position);
            mode = Mode.COMPLETION;
        }
    }
}