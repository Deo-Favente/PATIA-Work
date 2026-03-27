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
    "IDDFS(100 max.)": lambda o: solve_iddfs(o[0], 100),
    "A* (Manhattan heur.)": lambda o: solve_astar(o)
}

TEST_FILES = [
    "easy/npuzzle_3x3_len3_2.txt",
    "medium/npuzzle_5x5_len5_2.txt",
    "hard/npuzzle_10x10_len9_2.txt",
    "extreme/npuzzle_12x12_len15_2.txt",
    "impossible/npuzzle_15x15_len40_2.txt"
]

DIFFICULTIES = [
    "Facile (3x3 len 3)",
    "Medium (5x5 len 5)",
    "Hard (10x10_len9)",
    "Extreme (12x12_len15)",
    "Impossible (15x15_len40)"
]

MAX_TIME = 40
TIMEOUT = 30


def run_solver(solver, file):
    puzzle = load_puzzle("tests/" + file)
    print("Puzzle en cours : " + file)
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
        results.append([run_solver(solver, f) for f in TEST_FILES])
    return results


def clean_results(results):
    return [[min(x, MAX_TIME) for x in algo] for algo in results]


def sort_by_bfs(results):
    bfs_times = results[0]
    indices = np.argsort(bfs_times)

    sorted_diff = [DIFFICULTIES[i] for i in indices]
    sorted_res = [[algo[i] for i in indices] for algo in results]

    return sorted_diff, sorted_res


def plot_results(difficulties, results):
    plt.figure(figsize=(10, 6))

    for i, (name, algo_res) in enumerate(zip(ALGOS.keys(), results)):
        plt.plot(difficulties, algo_res, marker='o', label=name)

        for j, val in enumerate(algo_res):
            label = "TIMEOUT" if val == MAX_TIME else f"{val:.3e}"
            plt.text(j, val, label, ha='center', va='bottom', fontsize=8)

    plt.yscale('log')
    plt.ylabel("Temps (s)")
    plt.xlabel("Difficulté des puzzles (selon BFS)")
    plt.ylim(1e-6, MAX_TIME)

    plt.title("Comparaison des performances des algorithmes de résolution du taquin", pad=18)
    plt.legend()
    plt.grid(True, which="both", linestyle="--", linewidth=0.5)

    plt.tight_layout()
    plt.savefig('courbes_algos.png')


if __name__ == "__main__":
    raw_results = compute_results()
    cleaned = clean_results(raw_results)
    difficulties, sorted_results = sort_by_bfs(cleaned)
    plot_results(difficulties, sorted_results)