package sokoban;

import fr.uga.pddl4j.heuristics.state.StateHeuristic;
import fr.uga.pddl4j.plan.Plan;
import fr.uga.pddl4j.planners.InvalidConfigurationException;
import fr.uga.pddl4j.planners.LogLevel;
import fr.uga.pddl4j.planners.statespace.HSP;
import fr.uga.pddl4j.problem.operator.Action;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.Scanner;

public class Agent {
    public static void main(String[] args) throws IOException, ParseException {
        SokobanParser sok = new SokobanParser("test1.json");

        HSP planner = new HSP();
        // Sets the domain of the problem to solve
        planner.setDomain("config/domain.pddl");
        // Sets the problem to solve
        planner.setProblem("temp/problem.pddl");
        // Sets the timeout of the search in seconds
        planner.setTimeout(1000);
        // Sets log level
        planner.setLogLevel(LogLevel.INFO);
        // Selects the heuristic to use
        planner.setHeuristic(StateHeuristic.Name.MAX);
        // Sets the weight of the heuristic
        planner.setHeuristicWeight(1.2);

        // Solve and print the result
        try {
            Plan result = planner.solve();
            for(Action a : result.actions()){
                if(a.getName().equals("move_box_up") ||  a.getName().equals("move_worker_up")){
                    System.out.println("U");
                }
                if(a.getName().equals("move_box_down") ||  a.getName().equals("move_worker_down")){
                    System.out.println("D");
                }
                if(a.getName().equals("move_box_left") ||  a.getName().equals("move_worker_left")){
                    System.out.println("L");
                }
                if(a.getName().equals("move_box_right") ||  a.getName().equals("move_worker_right")){
                    System.out.println("R");
                }
            }

        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
