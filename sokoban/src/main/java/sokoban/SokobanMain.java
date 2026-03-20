package sokoban;

import com.codingame.gameengine.runner.SoloGameRunner;
import fr.uga.pddl4j.heuristics.state.StateHeuristic;
import fr.uga.pddl4j.plan.Plan;
import fr.uga.pddl4j.planners.InvalidConfigurationException;
import fr.uga.pddl4j.planners.LogLevel;
import fr.uga.pddl4j.planners.statespace.HSP;
import fr.uga.pddl4j.problem.operator.Action;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class SokobanMain {
    public static void main(String[] args) throws IOException, ParseException {
        if(args.length != 1) {
            System.err.println("Usage: SokobanMain sokoban-lvel.json");
        }


        SoloGameRunner gameRunner = new SoloGameRunner();
        gameRunner.setAgent(Agent.class);
        gameRunner.setTestCase(args[0]);
        gameRunner.start();
    }
}
