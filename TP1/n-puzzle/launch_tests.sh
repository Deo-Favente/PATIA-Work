if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <algorithm>"
    exit 1
fi

for difficulty in "easy" "medium" "hard" "impossible"; do
    echo "-------------------------------"
    echo "Launching tests for $difficulty"
    echo "-------------------------------"
    for file in "tests/$difficulty"/*; do
        echo "Launching tests for $file"
        timeout --foreground 30s python3 solve_npuzzle.py -a $1 "$file" > /tmp/solution.txt
        code=$?
        if grep -q "True" /tmp/solution.txt; then
            duration=$(grep "Duration" /tmp/solution.txt)
            echo -e "\033[32mSUCCESS: Test passed\033[0m" $duration         
        elif grep -q "already solved" /tmp/solution.txt; then
            echo -e "\033[33mWARNING: Test is already solved, consider removing it\033[0m"
        else
            # Print error in red
            echo -e "\033[31mERROR: Test failed \033[0m"
        fi
        if [ $code -eq 124 ]; then
            echo -e "\033[31mERROR: Test aborted (timeout > 10s, possible too long loop)\033[0m"
        fi
    done
done
