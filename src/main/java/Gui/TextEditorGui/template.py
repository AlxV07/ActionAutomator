action = []
speed=100
def move(x, y): action.append(f'move({x},{y})')
def wait(t): action.append(f'wait({t})')
def left_click(): action.append('left_click')
def left_up(): action.append('left_up')
def left_down(): action.append('left_down')
def right_click(): action.append('right_click')
def right_up(): action.append('right_up')
def right_down(): action.append('right_down')
def typewrite(text): action.append(f'typewrite(\'{text}\')')
def format_action(): return str(speed) + '}.{' + '}.{'.join(action)


def gen_action():
start


gen_action()
print(format_action())
