# Installs all dependencies used in msn2 programs.
#
# author : Mason Marker
# date : 8/15/2023

import os
import json

# main
if __name__ == "__main__":
    
    # starting message
    print("Installing dependencies for MSN2...")
    
    # read in the text from 'dependencies.txt'
    with open("dependencies.txt", "r") as f:
        lines = f.readlines()
        
    # determine pip installs
    for line in lines:
        if line.startswith('pip'):
            
            # install the dependency
            os.system(line)