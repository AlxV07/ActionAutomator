package com.actionautomator.Gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class ActionAutomatorResources {
    public static final Insets defaultMargin = new Insets(2, 2, 2, 2);
    public static final Font defaultFont = new Font("Source Code Pro", Font.PLAIN, 12);
    public static Font smallerFont = new Font("Source Code Pro", Font.PLAIN, 10);
    public static final Border lightThemeBorder = BorderFactory.createLineBorder(Color.BLACK);
    public static final Border darkThemeBorder = BorderFactory.createLineBorder(Color.WHITE);

    public static final Border lightThemeThickBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
    public static final Border darkThemeThickBorder = BorderFactory.createLineBorder(Color.WHITE, 2);

    public static final Color lightThemeColor = Color.WHITE;
    public static final Color darkThemeColor = Color.BLACK;

    public static String heldKeysLabelText = "\n  Held Keys: ";
    public static String directoryPath = System.getProperty("user.home") + "/.actionAutomator";
    public static String cachePath = System.getProperty("user.home") + "/.actionAutomator/actionCache.action";
    public static String helpPage = System.getProperty("user.home") + "/.actionAutomator/helpPage.png";
}
