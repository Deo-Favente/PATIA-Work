from solve_npuzzle import solve_bfs, solve_dfs, solve_astar, solve_iddfs, load_puzzle, Node
import time
import math
import matplotlib.pyplot as plt
import numpy as np
import signal


def timeout_handler(signum, frame):
    raise Exception("timeout")

signal.signal(signal.SIGALRM, timeout_handler)

ALGOS = {
    "BFS": lambda o: solve_bfs(o),
    "DFS": lambda o: solve_dfs(o),
    "IDDFS (max=100)": lambda o: solve_iddfs(o[0], 100),
    "A* (Manhattan)": lambda o: solve_astar(o)
}


TEST_FILES = [
    "graphics/npuzzle_3x3_len4_0.txt",
    "graphics/npuzzle_3x3_len5_0.txt",
    "graphics/npuzzle_3x3_len6_0.txt",
    "graphics/npuzzle_3x3_len7_0.txt",
    "graphics/npuzzle_3x3_len8_0.txt",
    "graphics/npuzzle_4x4_len4_0.txt",
    "graphics/npuzzle_4x4_len5_0.txt",
    "graphics/npuzzle_4x4_len6_0.txt",
    "graphics/npuzzle_4x4_len7_0.txt",
    "graphics/npuzzle_4x4_len8_0.txt",
    "graphics/npuzzle_5x5_len4_0.txt",
    "graphics/npuzzle_5x5_len5_0.txt",
    "graphics/npuzzle_5x5_len6_0.txt",
    "graphics/npuzzle_5x5_len7_0.txt",
    "graphics/npuzzle_5x5_len8_0.txt",
]

DIFFICULTIES = [
    "3x3 - 4 coups",
    "3x3 - 5 coups",
    "3x3 - 6 coups",
    "3x3 - 7 coups",
    "3x3 - 8 coups",
    "4x4 - 4 coups",
    "4x4 - 5 coups",
    "4x4 - 6 coups",
    "4x4 - 7 coups",
    "4x4 - 8 coups",
    "5x5 - 4 coups",
    "5x5 - 5 coups",
    "5x5 - 6 coups",
    "5x5 - 7 coups",
    "5x5 - 8 coups",
]


MAX_TIME = 40
TIMEOUT = 30


def run_solver(solver, file):
    puzzle = load_puzzle("tests/" + file)
    print("Puzzle en cours :", file)

    root = Node(state=puzzle, move=None)

    signal.alarm(TIMEOUT)

    try:
        start = time.time()
        solver([root])
        return time.time() - start
    except Exception:
        return math.inf
    finally:
        signal.alarm(0)


def compute_results():
    results = []
    for solver in ALGOS.values():
        algo_times = []
        for f in TEST_FILES:
            algo_times.append(run_solver(solver, f))
        results.append(algo_times)
    return results


def clean_results(results):
    return [[min(x, MAX_TIME) for x in algo] for algo in results]

def sort_by_difficulty(results):
    def key_func(label):
        size, moves = label.split(" - ")
        n = int(size.split("x")[0])     # 3, 4, 5
        m = int(moves.split()[0])       # 4, 5, ...
        return (n, m)

    indices = sorted(range(len(DIFFICULTIES)), key=lambda i: key_func(DIFFICULTIES[i]))

    sorted_diff = [DIFFICULTIES[i] for i in indices]
    sorted_res = [[algo[i] for i in indices] for algo in results]

    return sorted_diff, sorted_res


def plot_results(difficulties, results):
    plt.figure(figsize=(10, 6))

    for name, algo_res in zip(ALGOS.keys(), results):
        plt.plot(difficulties, algo_res, marker='o', label=name)

        for j, val in enumerate(algo_res):
            label = "TIMEOUT" if val == MAX_TIME else "" # f"{val:.3e}"
            plt.text(j, val, label, ha='center', va='bottom', fontsize=8)

    plt.yscale('log')
    plt.ylabel("Temps (s)")
    plt.xlabel("Difficulté")
    plt.ylim(1e-6, MAX_TIME)

    plt.title("Comparaison des algorithmes de résolution du taquin (échelle log)", pad=18)
    plt.legend()
    plt.grid(True, which="both", linestyle="--", linewidth=0.5)

    plt.xticks(rotation=45)
    plt.tight_layout()
    plt.savefig('courbes_algos.png')
    plt.show()


if __name__ == "__main__":
    raw_results = compute_results()
    cleaned = clean_results(raw_results)
    difficulties, sorted_results = sort_by_difficulty(cleaned)
    plot_results(difficulties, sorted_results)