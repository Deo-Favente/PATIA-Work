package fr.uga.pddl4j.yasp;

import fr.uga.pddl4j.plan.Plan;
import fr.uga.pddl4j.plan.SequentialPlan;
import fr.uga.pddl4j.problem.Fluent;
import fr.uga.pddl4j.problem.Problem;
import fr.uga.pddl4j.problem.operator.Action;
import fr.uga.pddl4j.util.BitVector;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class implements a planning problem/domain encoding into DIMACS
 *
 * @author H. Fiorino
 * @version 0.1 - 30.03.2024
 */
public final class SATEncoding {
    /*
     * A SAT problem in dimacs format is a list of int list a.k.a clauses
     */
    private List<List<Integer>> initList = new ArrayList<List<Integer>>();

    /*
     * Goal
     */
    private List<Integer> goalList = new ArrayList<Integer>();

    /*
     * Actions
     */
    private List<List<Integer>> actionPreconditionList = new ArrayList<List<Integer>>();
    private List<List<Integer>> actionEffectList = new ArrayList<List<Integer>>();

    /*
     * State transistions
     */
    private HashMap<Integer, List<Integer>> addList = new HashMap<Integer, List<Integer>>();
    private HashMap<Integer, List<Integer>> delList = new HashMap<Integer, List<Integer>>();
    private List<List<Integer>> stateTransitionList = new ArrayList<List<Integer>>();

    /*
     * Action disjunctions
     */
    private List<List<Integer>> actionDisjunctionList = new ArrayList<List<Integer>>();

    /*
     * Current DIMACS encoding of the planning domain and problem for #steps steps
     * Contains the initial state, actions and action disjunction
     * Goal is no there!
     */
    public List<List<Integer>> currentDimacs = new ArrayList<List<Integer>>();

    /*
     * Current goal encoding
     */
    public List<Integer> currentGoal = new ArrayList<Integer>();

    /*
     * Current number of steps of the SAT encoding
     */
    private int steps;
    private int nb_fluents, nb_actions;

    public SATEncoding(Problem problem, int steps) {

        this.steps = steps;

        // Encoding of init
        // Each fact is a unit clause
        // Init state step is 1
        // We get the initial state from the planning problem
        // State is a bit vector where the ith bit at 1 corresponds to the ith fluent
        // being true
        nb_fluents = problem.getFluents().size();
        nb_actions = problem.getActions().size();
        // System.out.println(" fluents = " + nb_fluents );

        BitVector init = problem.getInitialState().getPositiveFluents();
        // Equation de l'initial state
        // s_0 ∧ { ∧ f/∈s_0 ¬f }
        for (int i = 0; i < nb_fluents; i++) {
            if (init.get(i)) {
                initList.add(List.of(pair(i + 1, 0)));
            } else {
                initList.add(List.of(-pair(i + 1, 0)));
            }
        }

        // Equation du goal
        // { ∧ f ∈g+ f } ∧ { ∧ f ∈g− ¬f }
        BitVector goal_pos = problem.getGoal().getPositiveFluents();

        for (int i = 0; i < goal_pos.size(); i++) {
            if (goal_pos.get(i)) {
                goalList.add(i + 1);
            }
        }

        BitVector goal_neg = problem.getGoal().getNegativeFluents();

        for (int i = 0; i < goal_neg.size(); i++) {
            if (goal_neg.get(i)) {
                goalList.add(-(i + 1));
            }
        }

        for (int f = 1; f <= nb_fluents; f++) { // Init des listes pour chaque fluent
            addList.put(f, new ArrayList<>());
            delList.put(f, new ArrayList<>());
        }

        // Actions
        List<Action> actions = problem.getActions();
        for (int i = 0; i < actions.size(); i++) {
            Action a_i = actions.get(i);
            // Effects
            // a_i → {∧ precond(a_i)} ∧ {∧ effect+ (a_i)} ∧ {∧ ¬effect− (a_i)}
            // Remember that A → B ≡ ¬A ∨ B

            int actionVar = nb_fluents + i + 1;

            // Equation Préconditions (positives)
            BitVector precond_ai = a_i.getPrecondition().getPositiveFluents();
            for (int j = 0; j < precond_ai.size(); j++) {
                if (precond_ai.get(j)) {
                    actionPreconditionList.add(
                            List.of(-pair(actionVar, 1), pair(j + 1, 1)));
                }
            }

            // Equation Effets positifs
            BitVector pos_ai = a_i.getUnconditionalEffect().getPositiveFluents();
            for (int j = 0; j < pos_ai.size(); j++) {
                if (pos_ai.get(j)) {
                    actionEffectList.add(
                            List.of(-pair(actionVar, 1), pair(j + 1, 1)));

                    addList.get(j + 1).add(actionVar);
                }
            }

            // Equation Effets négatifs
            BitVector neg_ai = a_i.getUnconditionalEffect().getNegativeFluents();
            for (int j = 0; j < neg_ai.size(); j++) {
                if (neg_ai.get(j)) {
                    actionEffectList.add(
                            List.of(-pair(actionVar, 1), -pair(j + 1, 1)));

                    delList.get(j + 1).add(actionVar);
                }
            }

            // Equation Action disjunction (1 à la fois)
            // ¬a_i ∨ ¬b_i pour toutes les suivantes
            for (int j = i + 1; j < nb_actions; j++) {
                int otherActionVar = nb_fluents + j + 1;

                actionDisjunctionList.add(
                        List.of(-actionVar, -otherActionVar));
            }
        }

        // Makes DIMACS encoding from 1 to steps
        encode(1, steps);
    }

    /*
     * SAT encoding for next step
     */
    public void next() {
        this.steps++;
        encode(this.steps, this.steps);
    }

    public String toString(final List<Integer> clause, final Problem problem) {
        final int nb_fluents = problem.getFluents().size();
        List<Integer> dejavu = new ArrayList<Integer>();
        String t = "[";
        String u = "";
        int tmp = 1;
        int[] couple;
        int bitnum;
        int step;
        for (Integer x : clause) {
            if (x > 0) {
                couple = unpair(x);
                bitnum = couple[0];
                step = couple[1];
            } else {
                couple = unpair(-x);
                bitnum = -couple[0];
                step = couple[1];
            }
            t = t + "(" + bitnum + ", " + step + ")";
            t = (tmp == clause.size()) ? t + "]\n" : t + " + ";
            tmp++;
            final int b = Math.abs(bitnum);
            if (!dejavu.contains(b)) {
                dejavu.add(b);
                u = u + b + " >> ";
                if (nb_fluents >= b) {
                    Fluent fluent = problem.getFluents().get(b - 1);
                    u = u + problem.toString(fluent) + "\n";
                } else {
                    u = u + problem.toShortString(problem.getActions().get(b - nb_fluents - 1)) + "\n";
                }
            }
        }
        return t + u;
    }

    public Plan extractPlan(final List<Integer> solution, final Problem problem) {
        Plan plan = new SequentialPlan();
        HashMap<Integer, Action> sequence = new HashMap<Integer, Action>();
        final int nb_fluents = problem.getFluents().size();
        int[] couple;
        int bitnum;
        int step;
        for (Integer x : solution) {
            if (x > 0) {
                couple = unpair(x);
                bitnum = couple[0];
            } else {
                couple = unpair(-x);
                bitnum = -couple[0];
            }
            step = couple[1];
            // This is a positive (asserted) action
            if (bitnum > nb_fluents) {
                final Action action = problem.getActions().get(bitnum - nb_fluents - 1);
                sequence.put(step, action);
            }
        }
        for (int s = sequence.keySet().size(); s > 0; s--) {
            plan.add(0, sequence.get(s));
        }
        return plan;
    }

    // Cantor paring function generates unique numbers
    private static int pair(int num, int step) {
        return (int) (0.5 * (num + step) * (num + step + 1) + step);
    }

    private static int[] unpair(int z) {
        /*
         * Cantor unpair function is the reverse of the pairing function. It takes a
         * single input
         * and returns the two corespoding values.
         */
        int t = (int) (Math.floor((Math.sqrt(8 * z + 1) - 1) / 2));
        int bitnum = t * (t + 3) / 2 - z;
        int step = z - t * (t + 1) / 2;
        return new int[] { bitnum, step }; // Returning an array containing the two numbers
    }

    private void encode(int from, int to) {
        this.currentDimacs.clear();

        // Listes qui sont toujours les mêmes
        if (from == 1)
            currentDimacs.addAll(initList);

        currentDimacs.addAll(actionPreconditionList);
        currentDimacs.addAll(actionEffectList);

        currentDimacs.addAll(actionDisjunctionList);

        // Equation state transtitions
        // ¬fi ∧ fi+1 → { V fi+1∈effect+(ai) }
        // fi ∧ ¬fi+1 → { V fi+1∈effect− (ai) }
        for (int f = 1; f <= addList.size(); f++) {

            List<Integer> adders = addList.get(f);
            List<Integer> deleters = delList.get(f);

            for (int t = 1; t <= steps; t++) {

                int f_t = pair(f, t);
                int f_t1 = (t < steps) ? pair(f, t + 1) : -1;

                if (t < steps && deleters != null && !deleters.isEmpty()) {
                    List<Integer> clauseDel = new ArrayList<>();
                    clauseDel.add(-f_t);
                    clauseDel.add(f_t1);

                    for (Integer a : deleters) {
                        clauseDel.add(a);
                    }

                    currentDimacs.add(clauseDel);
                }

                if (t < steps && adders != null && !adders.isEmpty()) {
                    List<Integer> clauseAdd = new ArrayList<>();
                    clauseAdd.add(f_t);
                    clauseAdd.add(-f_t1);

                    for (Integer a : adders) {
                        clauseAdd.add(a);
                    }

                    currentDimacs.add(clauseAdd);
                }
            }

            // Mise à jour du goal selon les steps
            this.currentGoal.clear();
            BitVector goal_pos = new BitVector(goalList.size());
            BitVector goal_neg = new BitVector(goalList.size());

            for (Integer g : goalList) {
                if (g > 0) {
                    currentGoal.add(pair(g, steps));
                } else {
                    currentGoal.add(-pair(-g, steps));
                }
            }

            System.out.println("Encoding : successfully done (" + (this.currentDimacs.size()
                    + this.currentGoal.size()) + " clauses, " + to + " steps)");
        }
    }
}
