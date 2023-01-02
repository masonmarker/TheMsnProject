# ui for launching msn2 programs with the msn2 interpreter
#
# author : Mason Marker
# date : 12/28/2022


# creates a UI that looks like a programming IDE but for launching msn2 programs

import os

# clear the console regardless of system
os.system('cls' if os.name == 'nt' else 'clear')
print('[+] starting msn2 interpreter...')




# imports
import tkinter as tk
from tkinter import ttk

# import msn2 interpreter
import msnint2


def interpret(script, interpreter):



current_interpreter = msnint2.Interpreter()

# create window
window = tk.Tk()
window.title('msn2 launcher')

# add tabs to the window
tab_control = ttk.Notebook(window)


# create tab for the interpreter
interpreter_tab = ttk.Frame(tab_control)

recent = '/driver/recent'
processes = '/driver/processes'
yourprograms = '/driver/yourprograms'

# add a tab for each of the above
recent_tab = ttk.Frame(tab_control)

# recent tab stores a text box for running the most recent program
recent_textbox = tk.Text(recent_tab, height=50, width=70)
recent_textbox.pack()   



# add a button to run the program make the button big
recent_run_button = tk.Button(recent_tab, height='2',width=30, text='Run', command=lambda: current_interpreter.execute(recent_textbox.get('1.0', 'end')))
recent_run_button.pack()


# add a button to save the program



processes_tab = ttk.Frame(tab_control)
yourprograms_tab = ttk.Frame(tab_control)

# add tabs to the window    
tab_control.add(recent_tab, text='Recent')
tab_control.add(processes_tab, text='Processes')
tab_control.add(yourprograms_tab, text='Your Programs')


# pack tab control
tab_control.pack(expand=1, fill='both')








print('[+] started!')
print('out:')


# run window    
window.mainloop()

