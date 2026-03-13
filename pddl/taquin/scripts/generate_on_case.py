import sys

def main():
    if len(sys.argv) != 2:
        print("Usage : python script.py <fichier>")
        return

    filename = sys.argv[1]

    # lire les nombres depuis le fichier
    with open(filename, "r") as f:
        line = f.read().strip()
        numbers = [int(x) for x in line.split()]

    # générer le transcript exact du fichier
    for i, num in enumerate(numbers):
        cube = num
        case = i + 1
        print(f"(on cube{cube} case{case})")

    # dernière instruction : clear de la première case
    print(f"(clear case1)")

if __name__ == "__main__":
    main()