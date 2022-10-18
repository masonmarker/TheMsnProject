

map1 = {'h': 1, 'e': 1, 'l': 2, 'o': 1}
map2 = {'g': 1, 'o': 2, 'f': 1, 'y': 1}

# hello
# goofy

changes = 0


for char, freq in map1.items():
    if char not in map2.keys():
        changes += 1
    else:
        changes += abs(map2[char] - map1[char])


print(changes)





