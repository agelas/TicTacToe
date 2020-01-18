package TicTacToe;

import java.util.*;

public class miniMaxAlgoPlayer {

    public static void main(String[] args) {

        ArrayList testMax = new ArrayList<Integer>();
        testMax.add(1);
        testMax.add(2);

        ArrayList testMin = new ArrayList<Integer>();
        testMin.add(5);
        testMin.add(4);

        miniMaxAI ai = new miniMaxAI();
        ai.passPositionsIn(testMax, testMin);
        ai.findBestMove(testMax, testMin);

    }
}

    //I mean so the AI basically needs to know where the opponent moved, where it can move, and what winning looks like
    //It also has to know like if it's going first or second because first=5 moves, second=4 moves
    //Then it has to return where it wants to move and pass it back to the game engine.
    //So where the opponent moved and where it has moved are two ArrayLists, those can be passed in easy enough.
    //There's only like 9 places it can go so just looking through the ArrayList should be easy enough.
    //Need to update the lists as they go down recursive rabbit hole, or something needs to be updated idk



class miniMaxAI {

    private ArrayList<Integer> maximizingPlayerPositionList;
    private ArrayList<Integer> minimizingPlayerPositionList;
    private ArrayList<Integer> possiblePositionsList;

    public int findBestMove(ArrayList<Integer> maximizingPlayerPositions, ArrayList<Integer> minimizingPlayerPositions) {
        ArrayList evals = new ArrayList<Integer>();
        for (int i = 0; i < possiblePositionsList.size(); i++) {
            System.out.println("EVALUATING " + possiblePositionsList.get(i));
            evals.add(i, minimax(possiblePositionsList, this.maximizingPlayerPositionList, this.minimizingPlayerPositionList, possiblePositionsList.get(i), 5, true));
            //reset(maximizingPlayerPositions, minimizingPlayerPositions);
        }
        System.out.println(evals);
        //so find all values that have a 1 and choose between them?
        return 0;
    }

    void passPositionsIn(ArrayList<Integer> maximizingPlayerPositions, ArrayList<Integer> minimizingPlayerPositions) {
        this.maximizingPlayerPositionList = maximizingPlayerPositions;
        this.minimizingPlayerPositionList = minimizingPlayerPositions;

        this.possiblePositionsList = findAvailable(maximizingPlayerPositions, minimizingPlayerPositions);
    }

    private int minimax(ArrayList<Integer> possibleList, ArrayList<Integer> maxList, ArrayList<Integer> minList, Object position, int depth, boolean maximizingPlayer) {
        System.out.println("DEPTH: " + depth);
        //System.out.println(possibleList);

        ArrayList newPossibleList = new ArrayList<>(possibleList);
        ArrayList maximizingPlayerListIteration = new ArrayList<Integer>(maxList);
        ArrayList minimizingPlayerListIteration = new ArrayList<Integer>(minList);

        if (maximizingPlayer) {
            maximizingPlayerListIteration.add(position); //these are not flowing downstream
            newPossibleList.remove(position);
        } else {
            minimizingPlayerListIteration.add(position); //and this too
            newPossibleList.remove(position);
        }
        String result = checkWinner(maximizingPlayerListIteration, minimizingPlayerListIteration);

        if (depth == 0 || result.length() > 1) { //this is where you get an eval back
            //System.out.println("bailing: " + lDepth);
            if (result.equals("Player 1 wins!")) {
                System.out.println("hit p1");
                return 1;
            } else if (result.equals("Player 2 wins!")) {
                System.out.println("hit p2");
                return -1;
            }
            else if (result.equals("Tie")) { //Tie
                System.out.println("tie");
                return 0;
            }
            else {
                return 5;
            }
        }

        if (maximizingPlayer) {
            int maxEval = -100;
            //for each child
            for (Object o : newPossibleList) {
                //this.maximizingPlayerPositionList.add(pos); //idk if this is right
                int eval = minimax(newPossibleList, maximizingPlayerListIteration, minimizingPlayerListIteration, o, depth - 1, false);
                //depth++; //ok but why
                maxEval = max(maxEval, eval);
            }
            //System.out.println("bailingMax");
            return maxEval;
        }
        else {
            int minEval = 100;
            //for each child
            //System.out.println(possibleList);
            for (Object o : newPossibleList) {
                //System.out.println("human");
                //this.minimizingPlayerPositionList.add(pos); //also don't know if this is right
                int eval = minimax(newPossibleList, maximizingPlayerListIteration,minimizingPlayerListIteration, o, depth - 1, true);
                //depth++; //But I ask again, why
                minEval = min(minEval, eval);
            }
            //System.out.println("bailingMin");
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

        int sizeMarker = Math.max(unavailable1.size(), unavailable2.size());

        for (int i = 0; i < sizeMarker; i++) {
            int badPos1 = (int) unavailable1.get(i);
            int badPos2 = (int) unavailable2.get(i);
            if (available.contains(unavailable1.get(i))) {
                available.remove(badPos1);
            }
            if (available.contains(unavailable2.get(i))) {
                available.remove(badPos2);
            }
        }

        Object[] arrayRep = available.toArray();

        ArrayList availableRet = new ArrayList<Integer>();

        Collections.addAll(availableRet, arrayRep); //convert HashSet to a list

        return availableRet;
    }

    private String checkWinner(ArrayList maximizingList, ArrayList minimizingList) {
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

    //Resets the position lists to the original input.
    private void reset(ArrayList initialMaxPlayerPositions, ArrayList initialMinPlayerPositions) {
        this.maximizingPlayerPositionList = initialMaxPlayerPositions;
        this.minimizingPlayerPositionList = initialMinPlayerPositions;
        //this.possiblePositionsList = findAvailable(initialMaxPlayerPositions, initialMinPlayerPositions);
    }

    private int max(int in1, int in2) {
        return Math.max(in1, in2);
        //can make this random in case in1 == in2
    }

    private int min(int in1, int in2) {
        return Math.min(in1, in2);
    }
}
