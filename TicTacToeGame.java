package TicTacToe;
import java.util.*;

public class TicTacToeGame {

    public static void main(String[] args) {
        ticTacGameEngine tt = new ticTacGameEngine();
        tt.playGame();
    }
}

class ticTacGameEngine {
    private static List<Integer> player1Positions = new ArrayList<>();
    private static List<Integer> player2Positions = new ArrayList<>();

    void playGame() {
        boolean keepPlaying = true;
        int player1Wins = 0;
        int player2Wins = 0;
        int ties = 0;

        while (keepPlaying) {
            int winner = setup();
            if (winner == 1) {
                player1Wins++;
            } else if (winner == 2) {
                player2Wins++;
            } else {
                ties++;
            }

            System.out.println("Player 1 has won " + player1Wins + " time(s)");
            System.out.println("Player 2 has won " + player2Wins + " time(s)");
            System.out.println("Players tied: " + ties + " time(s)");

            String response;
            Scanner userInput = new Scanner(System.in);
            System.out.println("Would you like to keep playing (yes/no)?");
            response = userInput.nextLine();
            if (response.equals("yes")) {
                //literally do nothing
            }
            else if (response.equals("no")) {
                keepPlaying = false;
            }
        }
    }

    private int setup() {

        player1Positions.clear();
        player2Positions.clear();

        String user;
        String cpu;

        char[][] gameBoard = {{' ', '|', ' ', '|', ' '},
                {'-', '+', ' ', '+', '-'},
                {' ', '|', ' ', '|', ' '},
                {'-', '+', ' ', '+', '-'},
                {' ', '|', ' ', '|', ' '}};

        printGameBoard(gameBoard);

        Scanner scan1 = new Scanner(System.in);
        System.out.println("Who goes first (User/CPU)?");
        String firstMover = scan1.next();
        System.out.println(firstMover);
        Scanner cpuPlayer = new Scanner(System.in);
        System.out.println("Would you like to play against Random/miniMax CPU?");
        String choice = cpuPlayer.next();

        if (firstMover.equals("User")) {
            System.out.println("You are player 1");
            user = "player1";
            cpu = "player2";
            while (true) {
                playerMove(gameBoard, user);
                String result = checkWinner();
                if (result.length() > 0) {
                    printGameBoard(gameBoard);
                    System.out.println(result);
                    if (result.equals("Tie")) {
                        return 0;
                    } else {
                        return 1;
                    }
                }

                cpuMove(gameBoard, choice, cpu);
                printGameBoard(gameBoard);
                result = checkWinner();
                if (result.length() > 0) {
                    printGameBoard(gameBoard);
                    System.out.println(result);
                    if (result.equals("Tie")) {
                        return 0;
                    } else {
                        return 2;
                    }
                }
            }

        } else {
            System.out.println("You are player 2");
            user = "player2";
            cpu = "player1";

            while (true) {
                cpuMove(gameBoard, choice, cpu);
                printGameBoard(gameBoard);
                String result = checkWinner();
                if (result.length() > 0) {
                    printGameBoard(gameBoard);
                    System.out.println(result);
                    if (result.equals("Tie")) {
                        return 0;
                    } else {
                        return 1;
                    }
                }

                playerMove(gameBoard, user);
                result = checkWinner();
                if (result.length() > 0) {
                    printGameBoard(gameBoard);
                    System.out.println(result);
                    if (result.equals("Tie")) {
                        return 0;
                    } else {
                        return 2;
                    }
                }
            }
        }
    }

    private static void printGameBoard(char[][] gameBoard) {
        for (char[] row : gameBoard) {
            for (char col : row) {
                System.out.print(col);
            }
            System.out.println();
        }
    }

    private static void placePiece(char[][] gameBoard, int pos, String user) {
        char symbol = 'X';

        if (user.equals("player1")) {
            player1Positions.add(pos);
        } else if (user.equals("player2")) {
            symbol = 'O';
            player2Positions.add(pos);
        }

        switch (pos) {
            case 1:
                gameBoard[0][0] = symbol;
                break;
            case 2:
                gameBoard[0][2] = symbol;
                break;
            case 3:
                gameBoard[0][4] = symbol;
                break;
            case 4:
                gameBoard[2][0] = symbol;
                break;
            case 5:
                gameBoard[2][2] = symbol;
                break;
            case 6:
                gameBoard[2][4] = symbol;
                break;
            case 7:
                gameBoard[4][0] = symbol;
                break;
            case 8:
                gameBoard[4][2] = symbol;
                break;
            case 9:
                gameBoard[4][4] = symbol;
                break;
            default:
                break;
        }
    }

    private static String checkWinner() {
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
            //System.out.println("size: " + (playerPositions.size() + cpuPositions.size()));
            if (player1Positions.containsAll(l)) {
                return "Player 1 wins!";
            } else if (player2Positions.containsAll(l)) {
                return "Player 2 wins!";
            } else if (player1Positions.size() + player2Positions.size() == 9) {
                return "Tie";
            }
        }
        return "";
    }

    private static void playerMove(char[][] gameBoard, String player) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your placement (1-9): ");
        int pos = scan.nextInt();
        while (player1Positions.contains(pos) || player2Positions.contains(pos)) {
            System.out.println("Position taken, enter a valid position");
            pos = scan.nextInt();
        }

        placePiece(gameBoard, pos, player);
    }

    /**
     * cpuMove method that just selects a randomly available slot and puts a piece there.
     * @param gameBoard The board.
     * @param player Whether its player1 or player2.
     */
    private static void randMove(char[][] gameBoard, String player) {
        Random rand = new Random();
        int cpuPos = rand.nextInt(9) + 1;
        while (player1Positions.contains(cpuPos) || player2Positions.contains(cpuPos)) {
            cpuPos = rand.nextInt(9) + 1;
        }
        placePiece(gameBoard, cpuPos, player);
    }

    private static void miniMaxMove(char[][] gameBoard, String player) {
        miniMaxAI mmAI = new miniMaxAI();
        int move;
        if (player.equals("player1")) {
            move = mmAI.makeMove((ArrayList<Integer>) player1Positions, (ArrayList<Integer>) player2Positions);
        } else {
            move = mmAI.makeMove((ArrayList<Integer>) player2Positions, (ArrayList<Integer>) player1Positions);
        }
        System.out.println("Move: " + move);
        placePiece(gameBoard, move, player);

    }

    /**
     * Method that returns the move either made by random or by miniMax.
     * @param gameBoard pointer to the board.
     * @param option Random/miniMax.
     * @param player player1 or player2.
     */
    private static void cpuMove(char[][] gameBoard, String option, String player) {
        if (option.equals("Random")) {
            randMove(gameBoard, player);
        } else if (option.equals("miniMax")) {
            miniMaxMove(gameBoard, player);
        }
    }
}
