n = 10
    
next = [(1,0),(0,1),(-1,0),(0,-1)]

def hello_neighbor(n):
    for i in range(1, n**2 + 1):
        for h, b in next:
            j = i + h*n + b
            if 1 <= j <= n**2 and not (b != 0 and (i-1)//n != (j-1)//n):
                print(f"(next case{i} case{j})")

hello_neighbor(n)

#def is_neighbor(i, j):
#    for x in range(max(0, i-1), min(i+1, n)):
#        for y in range(max(0, j-1), min(j+1, n)):
#            if(x != i or y != j):
#                print(f"(next case{i} case{y})")
      

