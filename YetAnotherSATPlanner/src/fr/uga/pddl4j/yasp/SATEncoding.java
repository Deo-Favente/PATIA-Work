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

        // Equation de l'initial state
        // s_0 ∧ { ∧ f/∈s_0 ¬f }

        final BitVector init = problem.getInitialState().getPositiveFluents();

        for (int i = 0; i < nb_fluents; i++) {
            if (init.get(i))
                initList.add(List.of(pair(i + 1, 1))); // Le vecteur commence à 1 donc i + 1
            else
                initList.add(List.of(-pair(i + 1, 1)));
        }

        // Equation du goal
        // { ∧ f ∈g+ f } ∧ { ∧ f ∈g− ¬f }
        BitVector goal_pos = problem.getGoal().getPositiveFluents();
        BitVector goal_neg = problem.getGoal().getNegativeFluents();

        for (int i = 0; i < nb_fluents; i++) {
            if (goal_pos.get(i))
                goalList.add(i + 1);
            if (goal_neg.get(i))
                goalList.add(-(i + 1));
        }

        // Actions
        List<Action> actions = problem.getActions();

        for (int i = 0; i < nb_actions; i++) {
            Action a_i = actions.get(i);
            // Equation de l'action a_i
            // a_i → {∧ precond(a_i)} ∧ {∧ effect+ (a_i)} ∧ {∧ ¬effect− (a_i)}
            // Remember that A → B ≡ ¬A ∨ B
            BitVector precond_ai = a_i.getPrecondition().getPositiveFluents();
            BitVector pos_ai = a_i.getUnconditionalEffect().getPositiveFluents();
            BitVector neg_ai = a_i.getUnconditionalEffect().getNegativeFluents();

            List<Integer> preconditions = new ArrayList<>();
            List<Integer> effects = new ArrayList<>();

            for (int j = 0; j < nb_fluents; j++) {

                // precond(a_i)
                if (precond_ai.get(j))
                    preconditions.add(j + 1);

                // effect+ (a_i)
                if (pos_ai.get(j)) {
                    effects.add(j + 1);
                    addList.computeIfAbsent(j + 1, k -> new ArrayList<>()).add(i + nb_fluents + 1);
                }

                // effect− (a_i)
                if (neg_ai.get(j)) {
                    effects.add(-(j + 1));
                    delList.computeIfAbsent(j + 1, k -> new ArrayList<>()).add(i + nb_fluents + 1);
                }
            }

            actionPreconditionList.add(preconditions);
            actionEffectList.add(effects);

            // Equation Action disjunction (1 à la fois)
            // ¬a_i ∨ ¬b_i pour toutes les suivantes = {a_j | j > i}
            for (int k = i + 1; k < nb_actions; k++) {
                actionDisjunctionList.add(List.of(i + nb_fluents + 1, k + nb_fluents + 1));
            }
        }

        encode(1, steps);
    }

    public void next() {
        this.steps++;
        // On réencode tout depuis le début vu qu'on clear DIMACS
        // Je sais pas pourquoi c'était à (this.steps, this.steps) avant
        encode(1, this.steps);
    }

    public String toString(final List<Integer> clause, final Problem problem) {
        final int nb_fluents = problem.getFluents().size();
        List<Integer> dejavu = new ArrayList<>();
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
                    int index = b - nb_fluents - 1;
                    if (index >= 0 && index < problem.getActions().size()) {
                        Action a = problem.getActions().get(index);
                        try {
                            if (a != null) { // Pareil ici on peut avoir des null
                                u = u + problem.toShortString(a) + "\n";
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return t + u;
    }

    public Plan extractPlan(final List<Integer> solution, final Problem problem) {
        Plan plan = new SequentialPlan();
        HashMap<Integer, Action> sequence = new HashMap<>();
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

            int actionIndex = bitnum - nb_fluents - 1;

            if (bitnum > nb_fluents && actionIndex >= 0 && actionIndex < problem.getActions().size()) {
                Action action = problem.getActions().get(actionIndex);
                try {
                    if (action != null) { // Check rajouté sinon on peut avoir des actions nulles dans le plan extrait
                        problem.toShortString(action);
                        sequence.put(step, action);
                    }
                } catch (Exception e) {
                }
            }
        }

        for (int s = sequence.keySet().size(); s > 0; s--) {
            if (sequence.containsKey(s)) {
                plan.add(0, sequence.get(s));
            }
        }

        return plan;
    }

    private static int pair(int num, int step) {
        return (int) (0.5 * (num + step) * (num + step + 1) + step);
    }

    private static int[] unpair(int z) {
        int t = (int) (Math.floor((Math.sqrt(8 * z + 1) - 1) / 2));
        int bitnum = t * (t + 3) / 2 - z;
        int step = z - t * (t + 1) / 2;
        return new int[] { bitnum, step };
    }

    private void encode(int from, int to) {
        this.currentDimacs.clear();

        // Etat initial seulement à la première étape
        if (from == 1)
            currentDimacs.addAll(initList);

        for (int i = from; i <= to; i++) {
            encode_step(i);
        }

        // Mis à jour du goal
        this.currentGoal.clear();

        for (Integer predicate : goalList) {
            if (predicate > 0)
                currentGoal.add(pair(predicate, to + 1));
            else
                currentGoal.add(-pair(-predicate, to + 1));
        }

        System.out.println("Encoding : successfully done (" + (this.currentDimacs.size()
                + this.currentGoal.size()) + " clauses, " + to + " steps)");
    }

    
    private void encode_step(int step) {
        for (int i = 0; i < nb_actions; i++) {
            // Actions a_i à l'étape step
            // a_i → {∧ precond(a_i)} ∧ {∧ effect+ (a_i)} ∧ {∧ ¬effect− (a_i)}
            // Remember that A → B ≡ ¬A ∨ B
            int a_i = pair(i + nb_fluents + 1, step);

            // Préconditions au step
            for (int prec : actionPreconditionList.get(i)) {
                currentDimacs.add(List.of(-a_i, pair(prec, step)));
            }

            // Effets au step + 1
            for (int effect : actionEffectList.get(i)) {
                if (effect >= 0) { 
                    // effect+ (a_i)
                    currentDimacs.add(List.of(-a_i, pair(effect, step + 1)));
                } else {
                    // effect− (a_i)
                    currentDimacs.add(List.of(-a_i, -pair(-effect, step + 1)));
                }
            }
        }

        // Action disjunctions
        // ¬a_i ∨ ¬b_i pour toutes les suivantes
        for (List<Integer> pair : actionDisjunctionList) {
            List<Integer> temp = new ArrayList<>();
                    
            for (int action : pair) {
                temp.add(-pair(action, step));
            }
            
            currentDimacs.add(temp);
        }
        
        // Equation state transtitions
        // ¬fi ∧ fi+1 → { V fi+1∈effect+(ai) }
        // fi ∧ ¬fi+1 → { V fi+1∈effect− (ai) }

        for (int i = 0; i < nb_fluents; i++) {

            // f(t) ∧ ¬f(t+1) → (actions -)
            // Dont forget that A → B ≡ ¬A ∨ B

            List<Integer> negAxiom = new  ArrayList<>();
            negAxiom.add(-pair(i + 1, step));
            negAxiom.add(pair(i + 1, step+1));

            List<Integer> negativeEffects = delList.get(i + 1); 
            if(negativeEffects != null){
                for (Integer action : negativeEffects) {
                    negAxiom.add(pair(action, step));
                }
            }
            currentDimacs.add(negAxiom);

            // ¬f(t) ∧ f(t+1) → (actions +)
            // Dont forget that A → B ≡ ¬A ∨ B

            List<Integer> posAxiom = new ArrayList<>();
            posAxiom.add(pair(i + 1, step));
            posAxiom.add(-pair(i + 1, step+1));
            List<Integer> positiveEffects = addList.get(i + 1);
            if(positiveEffects != null){
                for (Integer action : positiveEffects) {
                    posAxiom.add(pair(action, step));
                }
            }
            currentDimacs.add(posAxiom);
        }
    }
}