# ActionAutomator

![ActionAutomatorLogo](https://github.com/AlxV05/ActionAutomator/blob/main/aaLogo.png)  

A flexible, light-weight, Java GUI Application for fast &amp; easy global keyboard + mouse automation & keybinding using JNativeHook & JavaAWTRobot.

### Overview:

ActionAutomator is a Java Swing GUI Application which enables users to bind global-keybinds to user-defined actions.

Definitions to understand the above statement:

Global Keybind: Like a keyboard-shortcut, except not just for inside a single window or application; key-presses and mouse movement ANYWHERE ON-SCREEN DESPITE THE SELECTED WINDOW are registered to ActionAutomator.  A user can create a "binding", defining a sequence of keys, which, if all keys in the sequence are pressed in the correct order by the user, will execute the corresponding Action.

Action:
An series of instructions for a JavaAWTRobot to execute, representing a series of key presses & releases + mouse movement & clicks entered into the ActionAutomator by the user. When executed, each "sub-action" will subsequently be executed by the JavaAWTRobot onscreen, whether it be pressing down a key, moving the mouse to a specified location, etc. (See Action Code Interface Documentation).

### Kudos:
- Java Swing & AWT
- JNativeHook
  - https://github.com/kwhat/jnativehook
- BingAIImageCreator
  - https://www.bing.com/images/create/a-logo-for-a-terminal-like-code-application--blac/1-657085177d48486a91e33156e33fda42?id=bFrwgoCNIxCMkIGwQHs5hA%3d%3d&view=detailv2&idpp=genimg&FORM=GCRIDP&ajaxhist=0&ajaxserp=0
- Programming Interface Error Highlight Painter
  - https://github.com/tips4java/tips4java/blob/main/source/SquigglePainter.java

### Privacy Concerns & TOS:
ActionAutomator is an offline application and does not save pictures on-screen or any user entered key presses or mouse movement.  All registered events are discarded once checked for corresponding actions.

By using ActionAutomator, you acknowledge the fact that the possibility for error is never completely 0%. You take full responsibility for any harm caused by your usage of robot automation.  If you have any further concerns, feel free to contact the author of ActionAutomator.

### Running:

Requirements: Java 17+
```
java -jar /path/to/ActionAutomator.jar
```
Macbook: on run, allow access to Accessibility Settings

(See "releases" page for jar file.)

### Gui Explanation:

(Disclaimer! I've not a graphical designer, and this is one of my first GUI applications (I'm more of a CLI guy), but please feel free to give feedback of any sort!)

![ActionAutomatorGUIExample](https://github.com/AlxV05/ActionAutomator/blob/main/aaGuiEx.png)

Menu Bar:
- `Settings` (Drop-Down Menu): `Toggle Dark Mode`, `Set Theme Color`, `Highlight Help Display`

- `New`: Create a new Action

- `Open`: Open an Action (.action) file

- `Delete`: Delete the selected Action from the current ActionAutomator session (.action file will still be kept in the saved actions and will be re-opened in following sessions if not manually removed)
- `Save`: Save the code in the Programming Interface into the .action file with the corresponding Action name (.action files can be found in the directory $HOME/.actionAutomator/). Remember to save every time after editing; running an unsaved action will run the code from the previously saved action!
- `Run`: execute the selected Action
- `Stop`: Stop the execution of any running Action, also is bound globally to the Esc key for emergency purposes.

"Binding Bars":
- `<->`/`<✓>`, or the "Code Status Indicator": displays whether the code for the corresponding action can run without errors.
    - `<✓>`: Code is good to go!
    - `<->`: Status of code is unknown; press `Save` to check!
    - `<X>`: Code has bugs!
- `Edit`: Select the Action for editing in the Programming Interface.
- `<Key>`/`Null`: Binding Buttons; when clicked, will wait for the user to press a key, and then insert that key into the key sequence if not already contained  (press Esc to set key to null).

Other:
- `Unlock`/`Lock`: Unlock/Lock editing in the Programming Interface (make sure to lock your code when you bind keys, otherwise when you press a key it will be entered into the code interface as well).
- `Rename`: Renames the selected Action.
- "Programming Interface": See Action Code Interface Documentation
- `Held Keys`: Displays all globally-held keys by the user
- `Mouse Coords`: Displays the global coordinates of the mouse real-time
- `Waypoint`: Save a display, or "waypoint" of the current mouse coords; will not update again until re-saved. (Press Esc to save a waypoint).
- `Timer`: A minimal milliseconds timer for convenient measurement-taking.
- `Help Display`: Hover over any component in the GUI to see what it does.

More in-GUI help can be found in the help-display.


### Action Code Interface Documentation
"Real" documentation below this segment:
```
# This is a comment :D
# Ooh look, a method!

# Move: moves the mouse to the given coordinates (coords displayed in-application)
move(100, 100)

# Left click!
lclick()
# Right click!
rclick

# Two times now y'all!
repeat(2) {
lclick()
rclick()
}

# Press and release!
lup()
ldown()
rup()
rdown()

# Let's start typing!
type(Any text works here! No need to escape these ) extra parentheses!)

# 1 key at a time (will highlight if valid key in IDE)
ktap(CTRL)
kdown(SHIFT)
kdown(ENTER)
# Don't forget to lift the keys!
kup(SHIFT)
kup(ENTER)

# Variables!
savestr(my_var, any text works here as well )()(A)!$&_@!%& hehe)
type(my_var)  # Hm... (Will override str w/ assigned variable value if one is assigned)

saveint(x, 100)
saveint(y, x)
move(x, y)

# Wait for a bit in milliseconds
wait(100)

# Change the delay time in between executing statements (milliseconds) (default = 100)
setspeed(10)  # zoom!

# Ooh, recursion works! BE CAREFUL THOUGH (To interrupt a running action, press Esc)
run(action_name)  # action_name is name of any Action programmed by user in-application
```
Real documentation:
```
# ALl COMMANDS & ARG PLACEHOLDERS
lclick()  # equivalent of `ldown` and then `lup`
ldown()   # press left mouse button
lup()     # lift left mouse button

rclick()  # equivalent of `rdown` and then `rup`
rdown()   # press right mouse button
rup()     # lift right mouse button

# single character or ALLUPPERCASENOSPACE e.x. "CTRL" or "PAGEUP"
# equivalent of `kdown` and then `kup`
ktap(key)  
kdown(key)
kup(key)

# Type all chars in the string
# Equivalent of `kpress` for each individual key
type(any string whatsover (!()&()!^@)&#)(*% hehe)  

# Move mouse to given location
move(x_coord, y_coord)  
wait(time_in_ms)
setspeed(time_in_ms)

repeat(nof_times) { 
# Code goes here
}

# Will override any existing variables with given name
savestr(var_name, any text)  
saveint(var_name, any_number_or_already_assigned_int)
run(action_name)

Valid "keys" for k-commands:
{+includes all type-able single characters on your keyboard}
PAGEUP
PAGEDOWN
ALT
CMND  # Macbook: "Command" key
WINDOWS # Windows: "Windows" key
CTRL  # Macbook: "Control" key, Other: "Ctrl" key 
BACK
SHIFT
CAPS
TAB
ESC
DEL
HOME
END
ENTER
F1
F2
F3
F4
F5
F6
F7
F8
F9
F10
F11
F12
UP 
DOWN 
LEFT 
RIGHT
```
