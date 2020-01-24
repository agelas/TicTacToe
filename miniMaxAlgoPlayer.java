package TicTacToe;

import java.util.*;

public class miniMaxAlgoPlayer {

    public static void main(String[] args) {

        ArrayList testMax = new ArrayList<Integer>();
        testMax.add(5);
        testMax.add(3);
        //testMax.add(4);

        ArrayList testMin = new ArrayList<Integer>(); //min is "human"
        testMin.add(1);
        testMin.add(2);
        testMin.add(4);


        miniMaxAI ai = new miniMaxAI();
        //ai.passPositionsIn(testMax, testMin);
        //int move = ai.findBestMove(testMax, testMin);
        int move = ai.makeMove(testMax, testMin);
        System.out.println("Chose " + move);

    }
}

/**
 * miniMaxAI class that performs all the hard work, although AI is a bit misleading because this is really just an
 * algorithm that's looking for the most desirable position. miniMaxAI needs to know where the opponent moved, where it can move,
 * and what winning looks like. Based on that it can return the spot on the tic tac toe board it wants to return back to the
 * game engine.
 */
class miniMaxAI {

    private ArrayList<Integer> maximizingPlayerPositionList;
    private ArrayList<Integer> minimizingPlayerPositionList;
    private ArrayList<Integer> possiblePositionsList;

    /**
     * Only method that can be called by outside users. Does all the hard work.
     * @param maximizingPlayerPositions The moves the miniMaxAI has made.
     * @param minimizingPlayerPositions The moves the opponent has made.
     * @return The index of the best available move.
     */
    public int makeMove(ArrayList<Integer> maximizingPlayerPositions, ArrayList<Integer> minimizingPlayerPositions) {
        passPositionsIn(maximizingPlayerPositions, minimizingPlayerPositions);
        return findBestMove(maximizingPlayerPositions, minimizingPlayerPositions);
    }

    /**
     * Method that recursively looks at every possible move to find the best option.
     * @param maximizingPlayerPositions The moves the miniMaxAI has made.
     * @param minimizingPlayerPositions The moves the opponent has made.
     * @return The index of the best available move.
     */
    private int findBestMove(ArrayList<Integer> maximizingPlayerPositions, ArrayList<Integer> minimizingPlayerPositions) {
        //If board is empty, ie miniMaxAI moves first, or human didn't take the center, then miniMaxAI should take it
        // because its the most desirable spot on the board.
        if (!minimizingPlayerPositions.contains(5) && !maximizingPlayerPositions.contains(5)) {
            return 5;
        }
        //Next best thing is a corner position if the middle is already taken.
        if (minimizingPlayerPositions.contains(5) && maximizingPlayerPositions.size() == 0) {
            return 1;
        }

        ArrayList evals = new ArrayList<Integer>();
        ArrayList goodIndex = new ArrayList<Integer>();
        ArrayList okIndex = new ArrayList<Integer>();
        for (int i = 0; i < possiblePositionsList.size(); i++) {
            System.out.println("EVALUATING " + possiblePositionsList.get(i));
            evals.add(i, minimax(possiblePositionsList, this.maximizingPlayerPositionList, this.minimizingPlayerPositionList,
                    possiblePositionsList.get(i), 4, true));
        }
        System.out.println(evals);

        //Find all values that have a 2 and choose between them
        for (int j = 0; j < evals.size(); j++) {
            if ((int) evals.get(j) == 2) {
                goodIndex.add(j);
            } else if ((int) evals.get(j) == 0) { //If tie is the best it can hope for then go with that
                okIndex.add(j);
            }
        }

        //Currently a deterministic implementation, will randomize later.
        if (goodIndex.size() != 0) {
            return possiblePositionsList.get((int) goodIndex.get(0));
        } else {
            return possiblePositionsList.get((int) okIndex.get(0));
        }
    }

    /**
     * Sets up the initial conditions needed the miniMaxAI to work: 1)The moves it has already made, 2)The moves made
     * by the opponent, 3) The moves available for it to make.
     * @param maximizingPlayerPositions The moves the miniMaxAI has made.
     * @param minimizingPlayerPositions The moves the opponent has made.
     */
    void passPositionsIn(ArrayList<Integer> maximizingPlayerPositions, ArrayList<Integer> minimizingPlayerPositions) {
        this.maximizingPlayerPositionList = maximizingPlayerPositions;
        this.minimizingPlayerPositionList = minimizingPlayerPositions;

        this.possiblePositionsList = findAvailable(maximizingPlayerPositions, minimizingPlayerPositions);
    }

    private int minimax(ArrayList<Integer> possibleList, ArrayList<Integer> maxList, ArrayList<Integer> minList, Object position, int depth, boolean maximizingPlayer) {
        System.out.println("DEPTH: " + depth);

        ArrayList newPossibleList = new ArrayList<>(possibleList);
        ArrayList maximizingPlayerListIteration = new ArrayList<Integer>(maxList);
        ArrayList minimizingPlayerListIteration = new ArrayList<Integer>(minList);

        if (maximizingPlayer) {
            maximizingPlayerListIteration.add(position); //these are not flowing downstream
            newPossibleList.remove(position);
            System.out.println("adding to max list: " + position);
        } else {
            minimizingPlayerListIteration.add(position); //and this too
            newPossibleList.remove(position);
            System.out.println("adding to min list: " + position);
        }
        String result = checkWinner(maximizingPlayerListIteration, minimizingPlayerListIteration);

        if (depth == 0 || result.length() > 1) { //this is where you get an eval back
            if (result.equals("Player 1 wins!")) {
                System.out.println("hit p1");
                return 2;
            } else if (result.equals("Player 2 wins!")) {
                System.out.println("hit p2");
                return 1;
            }
            else if (result.equals("Tie")) { //Tie
                System.out.println("Tie");
                return 0;
            }
            else {
                System.out.println("This should never happen");
                return 5;
            }
        }

        if (maximizingPlayer) {
            int maxEval = -100;
            //for each child
            for (Object o : newPossibleList) {
                int eval = minimax(newPossibleList, maximizingPlayerListIteration, minimizingPlayerListIteration, o, depth - 1, false);
                System.out.println("Maxeval: " + maxEval + " Eval: " + eval);
                maxEval = max(maxEval, eval);
            }
            System.out.println("MaxEval: " + maxEval);
            return maxEval;
        }
        else {
            int minEval = 100;
            //for each child
            for (Object o : newPossibleList) {
                int eval = minimax(newPossibleList, maximizingPlayerListIteration,minimizingPlayerListIteration, o, depth - 1, true);
                System.out.println("Mineval: " + minEval + " Eval: " + eval);
                minEval = min(minEval, eval);
            }
            System.out.println("MinEval: " + minEval);
            return minEval;
        }
    }

    private ArrayList findAvailable(ArrayList unavailable1, ArrayList unavailable2) {
        HashSet<Integer> available = new HashSet<Integer>();
        available.add(1);
        available.add(2);
        available.add(3);
        available.add(4);
        available.add(5);
        available.add(6);
        available.add(7);
        available.add(8);
        available.add(9);

        int sizeMarker = Math.min(unavailable1.size(), unavailable2.size());

        for (int i = 0; i < sizeMarker; i++) {
            int badPos1 = (int) unavailable1.get(i);
            if (available.contains(unavailable1.get(i))) {
                available.remove(badPos1);
            }

            int badPos2 = (int) unavailable2.get(i);
            if (available.contains(unavailable2.get(i))) {
                available.remove(badPos2);
            }
        }

        if (unavailable1.size() != unavailable2.size()) {
            if (sizeMarker == unavailable1.size()) {
                int badPos = (int) unavailable2.get(sizeMarker);
                available.remove(badPos);
            } else {
                int badPos = (int) unavailable1.get(sizeMarker);
                available.remove(badPos);
            }
        }

        Object[] arrayRep = available.toArray();

        ArrayList availableRet = new ArrayList<Integer>();

        Collections.addAll(availableRet, arrayRep); //convert HashSet to a list

        return availableRet;
    }

    private String checkWinner(ArrayList maximizingList, ArrayList minimizingList) {
        //All the winning positions.
        List topRow = Arrays.asList(1, 2, 3);
        List midRow = Arrays.asList(4, 5, 6);
        List botRow = Arrays.asList(7, 8, 9);
        List leftCol = Arrays.asList(1, 4, 7);
        List midCol = Arrays.asList(2, 5, 8);
        List rightCol = Arrays.asList(3, 6, 9);
        List cross1 = Arrays.asList(1, 5, 9);
        List cross2 = Arrays.asList(7, 5, 3);

        List<List> winningConditions = new ArrayList<List>();
        winningConditions.add(topRow);
        winningConditions.add(midRow);
        winningConditions.add(botRow);
        winningConditions.add(leftCol);
        winningConditions.add(midCol);
        winningConditions.add(rightCol);
        winningConditions.add(cross1);
        winningConditions.add(cross2);

        for (List l : winningConditions) {
            if (maximizingList.containsAll(l)) {
                return "Player 1 wins!";
            } else if (minimizingList.containsAll(l)) {
                return "Player 2 wins!";
            } else if (maximizingList.size() + minimizingList.size() == 9) {
                return "Tie";
            }
        }
        return "";
    }

    private int max(int in1, int in2) {
        return Math.max(in1, in2);
        //can make this random in case in1 == in2
    }

    private int min(int in1, int in2) {
        return Math.min(in1, in2);
    }
}
