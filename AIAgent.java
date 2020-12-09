import java.util.Random;
import java.util.Stack;

public class AIAgent {
    Random rand;

    public AIAgent() {
        rand = new Random();
    }


    //Random Move AIAgent
    public Move randomMove(Stack possibilities) {

        int moveID = rand.nextInt(possibilities.size());
        System.out.println("Agent randomly selected move : " + moveID);
        for (int i = 0; i < (possibilities.size() - (moveID)); i++) {
            possibilities.pop();
        }
        Move selectedMove = (Move) possibilities.pop();
        System.out.println("piece taken was : " + selectedMove.getLanding().getName());
        return selectedMove;
    }//end random move ai method

    //Next Best Move AIAgent
    public Move nextBestMove(Stack possibilities) {
        Stack clonedPossibilities = (Stack) possibilities.clone();
        Stack bestMove = new Stack();
        int score = 0;

        for (int i = 0; i < clonedPossibilities.size(); i++) { //run a loop through a clone of possibilities so the original stack of possibilities is not shortened
            Move landing = (Move) possibilities.pop(); //an object that will pop possibilities from the top of the stack
            int y = landing.getLanding().getYC(); //a variable for a pieces y coordinate movement

            if ((((y == 3)) || (y == 4)) && (score <= 1)) {//if the the y coordinates of the board are e
                score = 1;
                bestMove.add(landing);
            }
            if (landing.getLanding().getName().contains("Pawn") && (score <= 2)) { //if a piece is a pawn and has a score that is less than or equal to 2
                if (score != 2) { //if the score is not equal to 2
                    bestMove.clear(); //remove it from the bestMove stack
                }
                score = 2; //otherwise the score is equal to 2
                bestMove.add(landing); //add the object of that particular piece to the best move stack

            }
            if (landing.getLanding().getName().contains("Knight") || (landing.getLanding().getName().contains("Bishup")) && (score <= 3)) {
                if (score != 3) {
                    bestMove.clear();
                }
                score = 3;
                bestMove.add(landing);
            }
            if (landing.getLanding().getName().contains("Rook") && (score <= 5)) {
                if (score != 5) {
                    bestMove.clear();
                }
                score = 5;
                bestMove.add(landing);
            }
            if (landing.getLanding().getName().contains("Queen") && (score <= 9)) {
                if (score != 9) {
                    bestMove.clear();
                }
                score = 9;
                bestMove.add(landing);
            }
            if ((landing.getLanding().getName().contains("King")) && (score <= 100)) {
                if (score != 100) {
                    bestMove.clear();
                }
                score = 100;
                bestMove.add(landing);
            }

        }//end for loop for clones possibilities

        if (score >= 1) { //if the score is of a significant value perform a move based on moves that will give the ai points
            Stack cloneBestMove = (Stack) bestMove.clone();
            int moveID = rand.nextInt(bestMove.size());
            for (int j = 1; j < (cloneBestMove.size() - (moveID)); j++) {
                bestMove.pop();
            }
            Move nextBest = (Move) bestMove.pop();
            return nextBest;
        }
        return randomMove(clonedPossibilities); //else if there is no move of any significant value just make a random move.
    }//end next best move ai method

    //Two Levels Deep AIAgent
    public Move twoLevelsDeep(Stack possibilities, Stack possibilities2) {
        Stack whitePossibilities = (Stack) possibilities.clone();
        Stack blackPossibilities = (Stack) possibilities2.clone();
        Stack bestMoves = new Stack();
        Move whiteMove = null;
        Move blackMove = null;
        Square whitePosition;
        Square blackPosition;
        int bestWhiteScore = 0;
        int AIScore = 0;
        int bestAIScore = -100; //given a minus value as when blackScore - bestWhiteScore, the value can be a minus

        while (!blackPossibilities.isEmpty()) { //while all black moves stack is not empty
            int blackScore = 0;
            blackMove = (Move) blackPossibilities.pop(); //assign a variable to pop a black move
            blackPosition = blackMove.getLanding(); //assign a variable to get the position of the black piece
            //get the score for black if it can take a white piece
            if (blackPosition.getName().contains("Pawn")) {
                blackScore = 2;
            } else if ((blackPosition.getName().contains("Knight")) || (blackPosition.getName().contains("Bishup"))) {
                blackScore = 3;
            } else if (blackPosition.getName().contains("Rook")) {
                blackScore = 5;
            } else if (blackPosition.getName().contains("Queen")) {
                blackScore = 9;
            } else if (blackPosition.getName().contains("King")) {
                blackScore = 100;
            } else {
                blackScore = 0;
            }

            int whiteScore = 0;
            while (!whitePossibilities.isEmpty()) {//while all white moves stack is not empty
                whiteMove = (Move) whitePossibilities.pop();
                whitePosition = whiteMove.getLanding();
                //get the score for white if it can take a black piece
                if (whitePosition.getName().contains("Pawn")) {
                    whiteScore = 2;
                } else if ((whitePosition.getName().contains("Knight")) || (whitePosition.getName().contains("Bishup"))) {
                    whiteScore = 3;
                } else if (whitePosition.getName().contains("Rook")) {
                    whiteScore = 5;
                } else if (whitePosition.getName().contains("Queen")) {
                    whiteScore = 9;
                } else if (whitePosition.getName().contains("King")) {
                    whiteScore = 100;
                } else {
                    whiteScore = 0;
                }
                if (bestWhiteScore < whiteScore) { //getting the highest white score that can be made
                    bestWhiteScore = whiteScore;
                }//end if for getting the highest white score
            }//end while for white move stack

            //compare the black score and highest white score and set it to a variable
            AIScore = blackScore - bestWhiteScore;

            //make sure the AI makes the best move possible to achieve a higher score than the white score
            if (AIScore >= bestAIScore) {
                if (AIScore != bestAIScore) { //if the AI score is not equal to the best score. Clear the stack so the move is not made.
                    bestMoves.clear();
                }
                bestAIScore = AIScore; //otherwise the best AI score can be made
                bestMoves.add(blackMove);//add the best black move to the stack of best moves
                return randomMove(bestMoves); //return a random move of the best black moves that can be made
            }

        }//end main while loop

        //otherwise return a random move of the next best move.
        return nextBestMove(possibilities);
    }//end two levels deep ai

}//end AIAgent
