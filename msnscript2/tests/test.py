

from concurrent.futures import ThreadPoolExecutor
import sys

def some_action(line):
    print (line)

with ThreadPoolExecutor() as executor:
    for line in sys.stdin:
        future = executor.submit(some_action, line)