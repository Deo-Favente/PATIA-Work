package sokoban;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class SokobanParserOld {


    public SokobanParserOld(String filename) throws IOException, ParseException {
        StringBuilder pddl = new StringBuilder();

        pddl.append("(define (problem pblm)\n");
        pddl.append("(:domain sokoban)\n");

        Object o = new JSONParser().parse(new FileReader("config/" + filename));

        JSONObject jsono = (JSONObject) o;

        String jsonstring = (String) jsono.get("testIn");

        String[] jsonlines =  jsonstring.split("\n");

        int lines = jsonlines.length;
        int columns = jsonlines[0].length();

        pddl.append("(:objects ");
        for (int i = 0; i < lines*columns; i++) {
            pddl.append("case"+i+" ");
        }
        pddl.append("- case)\n");

        pddl.append("(:init ");

        StringBuilder goal = new StringBuilder();

        //Creer les proprietes des cases
        for(int i = 0; i < lines; i++){
            for(int j = 0; j < columns; j++){
                switch(jsonlines[i].charAt(j)){
                    case '$':
                        pddl.append("(boxOn case"+ (i*columns +j) +") \n");
                        break;
                    case '.':
                        pddl.append("(clear case"+ (i*columns +j) +") \n");
                        goal.append("(boxOn case").append(i * columns + j).append(") ");
                        break;
                    case '*':
                        pddl.append("(boxOn case"+ (i*columns +j) +") \n");
                        goal.append("(boxOn case").append(i * columns + j).append(") ");
                        break;
                    case '@':
                        pddl.append("(workerOn case"+ (i*columns +j) +") \n");
                        break;
                    case '+':
                        pddl.append("(workerOn case"+ (i*columns +j) +") \n");
                        goal.append("(boxOn case").append(i * columns + j).append(") ");
                        break;
                    case ' ':
                        pddl.append("(clear case"+ (i*columns +j) +") \n");
                        break;
                    default:
                        break;
                }
            }
        }

        //Creer l'adjacence des cases
        for(int i = 0; i < lines; i++){
            for(int j = 0; j < columns; j++){
                //Up
                if(i>0){
                    pddl.append("(nextUp case" + (i * columns + j) + " case" + ((i-1) * columns + j) + ") ");
                }
                //Down
                if(i < lines-2){
                    pddl.append("(nextDown case" + (i * columns + j) + " case" + ((i+1) * columns + j) + ") ");
                }
                //Left
                if(j > 1){
                    pddl.append("(nextLeft case" + (i * columns + j) + " case" + (i * columns + (j-1)) + ") ");
                }
                //Right
                if(j < columns-2){
                    pddl.append("(nextRight case" + (i * columns + j) + " case" + (i * columns + (j+1)) + ") ");
                }

                pddl.append("\n");
            }
        }

        pddl.append(")\n");

        pddl.append("(:goal (and " + goal.toString() + "))\n");

        pddl.append(")");

        try (FileWriter writer = new FileWriter("temp/problem.pddl")) {
            writer.write(pddl.toString());
            writer.flush();
        }
    }

}
