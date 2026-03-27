if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <bfs / dfs / astar /iddfs>"
    exit 1
fi

for difficulty in "easy" "medium" "hard" "impossible" "extreme"; do
    echo "-------------------------------"
    echo "Launching tests for $difficulty"
    echo "-------------------------------"
    for file in "tests/$difficulty"/*; do
        echo "Launching tests for $file"
        timeout --foreground 10s python3 solve_npuzzle.py -a $1 "$file" > /tmp/solution.txt
        code=$?
        if grep -q "True" /tmp/solution.txt; then
            duration=$(grep "Duration" /tmp/solution.txt)
            path=$(grep "Path Length" /tmp/solution.txt)
            echo -e "\033[32mSUCCESS: Test passed\033[0m" $duration $path    
        elif grep -q "already solved" /tmp/solution.txt; then
            echo -e "\033[33mWARNING: Test is already solved, consider removing it\033[0m"
        else
            echo -e "\033[31mERROR: Test failed \033[0m"
        fi
        if [ $code -eq 124 ]; then
            echo -e "\033[31mERROR: Test aborted (possible too long loop)\033[0m"
        fi
    done
done
