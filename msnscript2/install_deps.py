# Installs all dependencies used in msn2 programs.
#
# author : Mason Marker
# date : 8/15/2023

# main
if __name__ == "__main__":
    
    # starting message
    print("Installing dependencies for MSN2...")
    
    # attempt to install 'pip'
    try:
        import pip
    except ImportError:
        print("* pip not installed *")
        print('install pip with "{python_alias} -m pip install --upgrade pip"')
    
    # read in the text from 'dependencies.txt'
    with open("dependencies.txt", "r", encoding='utf-8') as f:
        lines = f.readlines()
        
    # determine pip installs
    for line in lines:
        if line.startswith('pip'):
            import os
            # install the dependency
            os.system(line)