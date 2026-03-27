package sokoban;

import fr.uga.pddl4j.heuristics.state.StateHeuristic;
import fr.uga.pddl4j.plan.Plan;
import fr.uga.pddl4j.planners.InvalidConfigurationException;
import fr.uga.pddl4j.planners.LogLevel;
import fr.uga.pddl4j.planners.statespace.HSP;
import fr.uga.pddl4j.problem.operator.Action;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class Agent {
    public static void main(String[] args) throws IOException, ParseException {
        StringBuilder pddl = new StringBuilder();
        Scanner in = new Scanner(System.in);

        int columns = in.nextInt();
        int lines = in.nextInt();
        int nbBoxes = in.nextInt();
        in.nextLine();

        //read board
        char[][] grid = new char[lines][columns];
        for (int i = 0; i < lines; i++) {
            String line = in.nextLine();
            for(int j = 0; j < columns; j++) {
                char charlu = line.charAt(j);
                switch(charlu) {
                    case '.':
                        grid[i][j] = ' ';
                        break;
                    case '*':
                        grid[i][j] = '.';
                        break;
                    default:
                        grid[i][j] = charlu;
                }
            }
        }

        //set worker
        int xWorker = in.nextInt();
        int yWorker = in.nextInt();
        if(grid[yWorker][xWorker] == '.'){
            grid[yWorker][xWorker] = '+';
        }else {
            grid[yWorker][xWorker] = '@';
        }

        //set boxes
        int xBox, yBox;
        for(int i = 0; i < nbBoxes; i++) {
            xBox = in.nextInt();
            yBox = in.nextInt();
            if(grid[yBox][xBox] == '.'){
                grid[yBox][xBox] = '*';
            }else{
                grid[yBox][xBox] = '$';
            }

        }

        pddl.append("(define (problem pblm)\n");
        pddl.append("(:domain sokoban)\n");

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
                switch(grid[i][j]){
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

        try(FileWriter writer = new FileWriter("temp/problem.pddl")) {
            writer.write(pddl.toString());
            writer.close();
        }


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
