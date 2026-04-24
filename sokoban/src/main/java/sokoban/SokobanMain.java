package sokoban;

import com.codingame.gameengine.runner.SoloGameRunner;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class SokobanMain {
    public static void main(String[] args) throws IOException, ParseException {
        if(args.length != 1) {
            System.err.println("Usage: SokobanMain sokoban-level.json");
        }

        SoloGameRunner gameRunner = new SoloGameRunner();
        gameRunner.setAgent(Agent.class);
        gameRunner.setTestCase(args[0]);
        gameRunner.start(4200);
    }
}
