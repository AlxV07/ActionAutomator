package ActionManagement;

import ActionManagement.SubActions.KeyPressedSubAction;
import ActionManagement.SubActions.KeyReleasedSubAction;
import ActionManagement.SubActions.SubAction;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ReadBuilder {
    public Action parseLinesIntoAction(List<String> lines) {
        List<SubAction> subActions = new ArrayList<>();

        for (String line : lines) {
            String[] parts = line.split("\\(", 2);
            String[] args = parts[1].substring(0, parts[1].length() - 1).split(",");
            String command = parts[0];
            switch (command) {
                case "typewrite" -> {
                    for (String s : command.split("")) {
//                        subActions.add(new KeyPressedSubAction());
//                        subActions.add(new KeyReleasedSubAction());
                    }
                }
                case "left_click" -> {}
                case "left_button_down" -> {}
                case "right_click" -> {}
                case "right_button_down" -> {}
                case "move" -> {}
            }
        }

        return new Action(subActions);
    }

    public static void main(String[] args) {
        ReadBuilder r = new ReadBuilder();
        List<String> lines = new ArrayList<>();
        lines.add("typewrite(\"soup\")");
        System.out.println(r.parseLinesIntoAction(lines));
    }
}
