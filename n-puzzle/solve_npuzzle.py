
from npuzzle import (Solution,
                     State,
                     Move,
                     UP, 
                     DOWN, 
                     LEFT, 
                     RIGHT,
                     create_goal,
                     get_children,
                     is_goal,
                     is_solution,
                     load_puzzle,
                     to_string)
from node import Node
from typing import Literal, List
import argparse
import math
import time
import heapq

BFS = 'bfs'
DFS = 'dfs'
ASTAR = 'astar'
IDDFS = 'iddfs'

def solve_bfs(open : List[Node]) -> Solution:
    '''Solve the puzzle using the BFS algorithm'''
    
    initPuzzle = open[0].get_state()
    size = int(math.sqrt(len(initPuzzle)))
    allMoves = [UP,DOWN,LEFT,RIGHT]
    
    while(len(open) > 0):
        if is_solution(initPuzzle,open[0].get_path()):
            return open[0].get_path()
        
        for s,m in get_children(open[0].get_state(), allMoves ,size):
            open.append(Node(s,m,parent = open[0]))
        
        open = open[1:]
    return None


def solve_dfs(open : List[Node]) -> Solution:
    '''Solve the puzzle using the DFS algorithm'''

    initPuzzle = open[0].get_state()
    size = int(math.sqrt(len(initPuzzle)))
    allMoves = [UP,DOWN,LEFT,RIGHT]
    dejaVu = [initPuzzle]

    return solve_dfs_aux(open, 10, dejaVu, allMoves, size, create_goal(size))

def solve_dfs_aux(open : List[Node],deep : int, dejaVu : List[State], allMoves : List[Move], size : int, goal : State) -> Solution:
    if(deep < 0):
        return None

    old_node = open[0]
    for s,m in get_children(old_node.get_state(),allMoves,size):
        if s not in dejaVu:
            if(is_goal(s,goal)):
                return Node(s,m,parent = old_node).get_path()

            dejaVu.append(s)
            sol = solve_dfs_aux([Node(s,m,parent = old_node)] + open[1:], deep-1, dejaVu, allMoves, size, goal)
            if sol != None:
                return sol

    return None
    

def solve_astar(open : List[Node]) -> Solution:
    '''Solve the puzzle using the A* algorithm'''
    initPuzzle = open[0].get_state()
    size = int(math.sqrt(len(initPuzzle)))
    allMoves = [UP,DOWN,LEFT,RIGHT]
    dejaVu = [initPuzzle]
    goal = create_goal(size)
    h = [open[0]]
    heapq.heapify(h)

    while(len(h) > 0):
                    
        parent = heapq.heappop(h)
        print(len(parent.get_path()), len(dejaVu))
        for s,m in get_children(parent.get_state(), allMoves ,size):
            if s not in dejaVu:
                if(is_goal(s,goal)):
                    return Node(s,m,parent = parent).get_path()

                dejaVu.append(s)
                heapq.heappush(h, Node(s,m,parent=parent, heuristic=heuristic(s,goal, size)))
            
    return None

def heuristic(current_state : State, goal_state : State, size : int) -> int:
    '''Calculate the Manhattan distance of the puzzle'''
    total = 0
    for i in range(len(current_state)):
        if i == current_state[i]:
            total -= size//4
        x = i//size
        y = i%size
        goalx = current_state[i]//size
        goaly = current_state[i]%size
        total += abs(x-goalx) + abs(y-goaly)
    return total

def depth_limited_search(node: Node, limit: int, goal_state: State, moves: List[Move], dimension: int) -> Solution | None:
    '''Perform a depth-limited search'''
    
    # Todo: implement depth-limited search
    pass

def solve_iddfs(root: Node, max_depth: int) -> Solution:
    '''Solve the puzzle using the Iterative Deepening Depth-First Search algorithm'''
    
    # Todo: implement IDDFS algorithm
    pass

def main():
    parser = argparse.ArgumentParser(description='Load an n-puzzle and solve it.')
    parser.add_argument('filename', type=str, help='File name of the puzzle')
    parser.add_argument('-a', '--algo', type=str, choices=['bfs', 'dfs', 'astar', 'iddfs'], required=True, help='Algorithm to solve the puzzle')
    parser.add_argument('-v', '--verbose', action='store_true', help='Increase output verbosity')
    parser.add_argument('-d', '--max_depth', type=int, default=100, help='Maximum depth for IDDFS')
    
    args = parser.parse_args()
    
    puzzle = load_puzzle(args.filename)
    
    if args.verbose:
        print('Puzzle:\n')
        print(to_string(puzzle))
    
    if not is_goal(puzzle, create_goal(int(math.sqrt(len(puzzle))))):   
         
        root = Node(state = puzzle, move = None)
        open = [root]
        
        if args.algo == BFS:
            print('BFS\n')
            start_time = time.time()
            solution = solve_bfs(open)
            duration = time.time() - start_time
            if solution:
                print('Solution:', solution)
                print('Path Length: ' , len(solution))
                print('Valid solution:', is_solution(puzzle, solution))
                print('Duration:', duration)
            else:
                print('No solution')
        elif args.algo == DFS:
            print('DFS\n')
            start_time = time.time()
            solution = solve_dfs(open)
            duration = time.time() - start_time
            if solution:
                print('Solution:', solution)
                print('Path Length: ' , len(solution))
                print('Valid solution:', is_solution(puzzle, solution))
                print('Duration:', duration)
            else:
                print('No solution')
        elif args.algo == ASTAR:
            print('A*')
            start_time = time.time()
            solution = solve_astar(open)
            duration = time.time() - start_time
            if solution:
                print('Solution:', solution)
                print('Path Length: ' , len(solution))
                print('Valid solution:', is_solution(puzzle, solution))
                print('Duration:', duration)
        elif args.algo == IDDFS:
            print('IDDFS')
            start_time = time.time()
            solution = solve_iddfs(root, args.max_depth)
            duration = time.time() - start_time
            if solution:
                print('Solution:', solution)
                print('Path Length: ' , len(solution))
                print('Valid solution:', is_solution(puzzle, solution))
                print('Duration:', duration)        
            else:
                print('No solution')
    else:
    
        print('Puzzle is already solved')
    
if __name__ == '__main__':
    main()