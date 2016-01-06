h = open('src/Main.java', 'r')
l = h.readlines()
for L in l:
	L = L.strip()
	if L[:4] == "case":
		print(L[6:-2])
