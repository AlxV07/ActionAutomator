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

    public final static String heldKeysLabelText = "\n  Held Keys: ";
    public final static String directoryPath = System.getProperty("user.home") + "/.actionAutomator";
    public final static String cachePath = System.getProperty("user.home") + "/.actionAutomator/actionCache.action";
    public final static String logoPath = System.getProperty("user.home") + "/.actionAutomator/aaLogo.png";

    public final static String bindingButtonDoc = """
Key Bind Button:
Set a key in the key-sequence for the corresponding Action.
""";
    public final static String bindingEditButtonDoc = """
Edit:
Select the target Action for editing in the Programming Interface.
""";
    public final static String newButtonDoc = """
New:
Create a new blank Action (will prompt for a name).
""";
    public static String openButtonDoc = """
Open:
Open a .action file and load all contained Actions.
""";
    public static String deleteButtonDoc = """
Delete:
Delete the selected Action.
""";
    public static String runButtonDoc = """
Run:
Run the selected Action.
""";
    public static String stopButtonDoc = """
Stop: (Bound to "Esc")
Stop an Action's execution if one is currently running.
""";
    public static String setThemeButtonDoc = """
Set Theme Color:
Set the main theme color for your ActionAutomator interface.
""";
    public static String toggleDarkModeButtonDoc = """
Toggle Dark Mode:
Self-evident ;)
""";
    public static String timerButtonDoc = """
(|>) / (||) Button:
Start/Stop the millisecond timer. Once stopped, the next start will both reset & start the timer.
""";
    public static String lockEditingButtonDoc = """
Lock/Unlock Button:
Lock / unlock the Rename Button, the Save Button, and editing in the Programming Interface.
""";
    public static String nameSelectedButtonDoc = """
Rename Button:
Change the name of the selected Action.
""";
    public static String timerLabelDoc = """
Milliseconds Timer:
A simple convenient timer for making quick time-interval measurements.
""";
    public static String mouseCoordLabelDoc = """
Mouse Coordinates:
Displays the realtime X & Y coordinates of the mouse on the global screen.
""";
    public static String coordWayPointLabelDoc = """
Waypoint: (Bound to "Esc")
Saves and displays the mouse's current X & Y coordinates. Does not update realtime like the Mouse Coordinates display.
""";
    public static String heldKeysLabelDoc = """
Held Keys:
Displays the realtime keys held by the user, no-matter which window is in focus.
""";
    public static String progInterfaceDoc = """
Programming Interface:
Write code for the execution of the selected Action. (see "https://github.com/AlxV05/ActionAutomator#readme")
""";
    public static String bindingNameLabelDoc = """
Binding:
If the bound key-sequence of this binding is pressed by the user, the corresponding Action will execute.
""";

    public static String settingsMenuDoc = """
Settings:
Open the ActionAutomator settings drop-down menu.
""";
    public static String helpDisplayDoc = """
Help Display:
Display the help prompt for any component in the ActionAutomator interface. Hover over any component to try it out!
""";
    public static String saveCodeButtonDoc = """
Save Button:
Save code in the Programming Interface to the selected Action. The Code Status Indicator will update to display the change.
""";
    public static String codeStatusLabelDoc = """
Code Status Indicator:
The Action's code is:
<✓> : saved & can run.
<-> : unsaved & unknown run result.
<X> : bugged & cannot run.
""";
}
