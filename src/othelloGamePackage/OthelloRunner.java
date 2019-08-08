package othelloGamePackage;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 
 * @author Jacob Coriell
 *
 */
public class OthelloRunner {

	private int therow;
	private int thecolumn;
	private boolean validmove = true;
	int checkForNoMoves = 0;
	protected Board gameBoard = new Board();
	Scanner reader = new Scanner(System.in);
	protected Player blackPlayer = new Player();
	protected Player whitePlayer = new Player();
	private int turnSkipped = 0;
	private String userchoice;

	public static void main(String args[]) throws IOException {
		OthelloRunner game = new OthelloRunner();
		Scanner reader = new Scanner(System.in);

		// Simulation mode
		System.out.println(
				"Would you like to run a Monte-Carlo simulation? This will auto-complete a number of simulations\nplayed by two robots and present data that can be easily entered and graphed in excel. Enter y or n: ");
		String str = reader.next();
		if (str.equalsIgnoreCase("y")) {
			System.out.println("Enter number of simulations to run (10,000 minimum recommended): ");
			int simulations = reader.nextInt();
			game.simulation(simulations);
			reader.close();
			System.exit(0);
		}

		System.out.println(
				"Would you like to play against a robot player? (Entering no will begin two-player mode) Enter y or n: ");
		str = reader.next();
		if (str.equalsIgnoreCase("Y"))
			game.blackturn(true);
		else
			game.blackturn(false);

	}

	/**
	 * This is the simulation method which will complete a user-specified amount of
	 * games with computer players and output the results
	 * 
	 * @param tests
	 *            amount of tests the user desires to compute
	 * @throws IOException 
	 */
	protected void simulation(int tests) throws IOException {
		int spread;
		HashMap<Integer, Integer> map = new HashMap<>();
		System.out.println("Running simulations, please wait...");
		while (tests > 0) {
			gameBoard.reset();
			while (!(gameBoard.noMoves())) {
				autoPlayBlack();
				autoPlayWhite();
			}
			spread = gameBoard.getNumPieces("B") - gameBoard.getNumPieces("W");

			if (map.containsKey(spread)) {
				map.put(spread, map.get(spread) + 1);
			} else {
				map.put(spread, 1);
			}

			tests--;
			//System.out.println(" Simulations remaining: " + tests);
		}
		System.out.println("Simulations Completed! Printing results:");

		// Print spread frequency in format easy to paste into excel
		for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
			int key = entry.getKey();
			int value = entry.getValue();
			System.out.println(key + "\t" + value);
		}
	}

	/**
	 * Initiates the normal playing sequence. If single player, calls for a bot
	 * move. If two player, calls for white move. Checks to see if user can move
	 * first.
	 * 
	 * @param solo
	 *            a boolean selected by the user, true if the user wants to play
	 *            solo (with a bot), false if the user wants to play with another
	 *            person
	 */
	protected void blackturn(boolean solo) {
		if (!playerCanMove("B")) {
			if (turnSkipped == 2)
				gameOver("NM");
			gameBoard.build();
			printScores();
			System.out.println("Black player can not move. Attempting to skip to white");
			whiteturn();
		}
		turnSkipped = 0;
		gameBoard.build();
		printScores();

		do {
			do {

				try {
					System.out.println(
							"Black Player: Assuming rows are the horizontal sections and columns are the vertical sections, enter what row you want to place a piece:");
					therow = reader.nextInt();

					System.out.println("Now enter the column you want to place a piece: ");
					thecolumn = reader.nextInt();
				} catch (Exception e) {
					System.out.println("Please enter numbers only. Try again.");
					reader.next();
					blackturn(false);
				}

				if (therow < 1 || thecolumn < 1 || therow > 8 || thecolumn > 8) {
					System.out.println("Please enter numbers greater than 1 and less than 9.");
				}
			} while (therow < 1 || thecolumn < 1 || therow > 8 || thecolumn > 8);

			validmove = gameBoard.checkValid(therow - 1, thecolumn - 1, "B", false);

			if (validmove == false)
				System.out.println("Please enter piece locations that follow the rules of Othello.");

		} while (validmove == false);

		gameBoard.checkValid(therow - 1, thecolumn - 1, "B", true);
		gameBoard.setPiece(therow - 1, thecolumn - 1, "B");
		if (solo) {
			compTurn();
		} else
			whiteturn();

	}

	/**
	 * This method only gets called if in two player mode. Gets move from white
	 * player and goes back to black player.
	 */
	protected void whiteturn() {

		if (!playerCanMove("W")) {
			if (turnSkipped == 2)
				gameOver("NM");
			gameBoard.build();
			printScores();
			System.out.println("White player can not move. Attempting to skip to black");
			blackturn(false);
		}

		turnSkipped = 0;
		gameBoard.build();
		printScores();

		do {
			do {

				try {
					System.out.println(
							"White Player: Assuming rows are the horizontal sections and columns are the vertical sections, enter what row you want to place a piece:");
					therow = reader.nextInt();

					System.out.println("Now enter the column you want to place a piece: ");
					thecolumn = reader.nextInt();
				} catch (Exception e) {
					System.out.println("Please enter numbers only. Try again.");
					reader.next();
					whiteturn();
				}

				if (therow < 1 || thecolumn < 1 || therow > 8 || thecolumn > 8) {
					System.out.println("Please enter numbers greater than 1 and less than 9.");
				}
			} while (therow < 1 || thecolumn < 1 || therow > 8 || thecolumn > 8);

			validmove = gameBoard.checkValid(therow - 1, thecolumn - 1, "W", false);

			if (validmove == false)
				System.out.println("Please enter piece locations that follow the rules of Othello.");
		} while (validmove == false);

		gameBoard.checkValid(therow - 1, thecolumn - 1, "W", true);
		gameBoard.setPiece(therow - 1, thecolumn - 1, "W");

		blackturn(false);
	}

	/**
	 * This method is called if the game is in one player mode -- will randomly
	 * select a move out of a list of moves and then returns to black users turn
	 */
	protected void compTurn() {
		int moves = 0;

		if (!playerCanMove("W")) {
			if (turnSkipped == 2)
				gameOver("NM");
			gameBoard.build();
			printScores();
			System.out.println("White player can not move. Attempting to skip to black");

			blackturn(true);
		}

		gameBoard.setNumMoves(0);
		if (playerCanMove("W")) {
			moves = gameBoard.getNumMoves();
		}

		int randMove = ThreadLocalRandom.current().nextInt(1, moves + 1);

		gameBoard.calculateMoves("W");
		gameBoard.randomMove(randMove, "W");

		blackturn(true);
	}

	/**
	 * Black player bot for simulation mode. Carries out a random move from
	 * available moves.
	 */
	protected void autoPlayBlack() {
		int moves = 0;

		gameBoard.setNumMoves(0);
		if (playerCanMove("B")) {
			moves = gameBoard.getNumMoves();
		}
		if (moves == 0)
			return;
		int randMove = ThreadLocalRandom.current().nextInt(1, moves + 1);
		gameBoard.calculateMoves("B");
		gameBoard.randomMove(randMove, "B");

	}

	/**
	 * White player bot for simulation mode. Carries out a random move from
	 * available moves.
	 */
	protected void autoPlayWhite() {
		int moves = 0;

		gameBoard.setNumMoves(0);
		if (playerCanMove("W")) {
			moves = gameBoard.getNumMoves();
		}

		if (moves == 0)
			return;

		int randMove = ThreadLocalRandom.current().nextInt(1, moves + 1);
		gameBoard.calculateMoves("W");
		gameBoard.randomMove(randMove, "W");

	}

	/**
	 * Displays the scores for both players
	 */
	protected void printScores() {
		blackPlayer.setScore(gameBoard.getNumPieces("B"));
		whitePlayer.setScore(gameBoard.getNumPieces("W"));
		System.out.println("Black Player Score: " + blackPlayer.getScore());
		System.out.println("White Player Score: " + whitePlayer.getScore());
	}

	/**
	 * This method determines whether or not the current player can make a move. If
	 * the player can not, the variable turnSkipped is incremented -- if turnSkipped
	 * is incremented a second time, then the game is over
	 * 
	 * @param s
	 *            is the current players color
	 * @return a boolean of true if the current player can make a move, false if the
	 *         player can not
	 */
	protected boolean playerCanMove(String s) {
		if (gameBoard.canMove(s)) {
			return true;
		} else {
			turnSkipped++;
			return false;
		}
	}

	/**
	 * This method will calculate the final scores and display the winner. It will
	 * also allow the user to play again.
	 * 
	 * @param s a string that shows end game condition
	 */
	protected void gameOver(String s) {
		gameBoard.build();
		blackPlayer.setScore(gameBoard.getNumPieces("B"));
		whitePlayer.setScore(gameBoard.getNumPieces("W"));
		if (s.equals("NM"))
			System.out.println("No moves available. Game over!");
		else
			System.out.println("Game board is full!");
		System.out.println("Black player has " + blackPlayer.getScore());
		System.out.println("White player has " + whitePlayer.getScore());
		if (blackPlayer.getScore() > whitePlayer.getScore()) {
			System.out.println("Black player wins!");
		} else if (blackPlayer.getScore() < whitePlayer.getScore())
			System.out.println("White player wins!");
		else
			System.out.println("It's a tie!");

		do {
			System.out.println("Would you like to play again? Enter y or n");
			userchoice = reader.next();
			if (userchoice.equalsIgnoreCase("y")) {
				gameBoard.reset();
				blackturn(false);
			} else if (userchoice.equalsIgnoreCase("n")) {
				System.out.println("Thanks for playing!");
				System.exit(0);
			} else
				System.out.println("Sorry I don't understand your choice. Try again.");
		} while (userchoice != "y" && userchoice != "Y" && userchoice != "N" && userchoice != "n");
	}
}
