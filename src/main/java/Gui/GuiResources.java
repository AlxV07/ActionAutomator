package Gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class GuiResources {
    public static final Insets defaultMargin = new Insets(2, 2, 2, 2);
    public static final Font defaultFont = new Font("Source Code Pro", Font.PLAIN, 12);
    public static Font smallerFont = new Font("Source Code Pro", Font.PLAIN, 10);
    public static final Border lightThemeBorder = BorderFactory.createLineBorder(Color.BLACK);
    public static final Border darkThemeBorder = BorderFactory.createLineBorder(Color.WHITE);
    public static final Color lightThemeColor = Color.WHITE;
    public static final Color darkThemeColor = Color.BLACK;

    public static String pressedKeysLabel = "\n   Pressed Keys: ";

}
