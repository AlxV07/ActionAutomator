# ActionAutomator

A flexible light-weight Java GUI Application for fast &amp; easy global keyboard + mouse automation & keybinding using JNativeHook.

In-GUI help can be found in the bottom right display.

### Action Code Interface Documentation
"Real" documentation at bottom:
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
kpress(CTRL)
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
kpress(key)  
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
{All type-able single characters on your keyboard}
PAGEUP
PAGEDOWN
ALT
META
BACK
SHIFT
CTRL
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