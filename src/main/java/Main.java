import Gui.GuiMain;

import java.awt.*;

public class Main {
    /*
    TODO:
        - Cleaner look for all components
        - Use "~/" instead of "/home/alxv05/" for .action_automator saving
        - CodeTextArea IDE functionality
        - Waypoint saving, Action interruption, variables, math, looping, etc.
     */
    public static void main(String[] args) {
        GuiMain guiMain = new GuiMain();
        guiMain.start(true, Color.GREEN, Color.GRAY);
    }
}
