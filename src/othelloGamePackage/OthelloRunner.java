package othelloGamePackage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Jacob Coriell This is the driver class that should be run from the
 *         terminal. The player can chose to jump into simulation mode, single
 *         player mode, or two player mode.
 */
public class OthelloRunner {
	int checkForNoMoves = 0;
	private Board gameBoard = new Board();
	private Scanner reader = new Scanner(System.in);
	private Player blackPlayer = new Player("B");
	private Player whitePlayer = new Player("W");
	private Player cpuW = new Player("W", true);
	private Player cpuB = new Player("B", true);
	private String userchoice;

	public static void main(String args[]) throws IOException {
		OthelloRunner game = new OthelloRunner();
		game.crossroad();
	}

	/**
	 * Allows user to pick which mode to play the game in.
	 * @throws IOException
	 */
	protected void crossroad() throws IOException {
		reader = new Scanner(System.in);
		String userInput;
		System.out.println("Would you like to run a Monte-Carlo simulation? This will auto-complete a number of simulations\nplayed by two robots and present data that can be easily entered and graphed in excel. Enter y or n: ");
		userInput = reader.next();
		if (userInput.equalsIgnoreCase("Y")) {
			System.out.println("Enter number of simulations to run (10,000 minimum recommended): ");
			int simulations = reader.nextInt();
			this.simulation(simulations);
			reader.close();
			System.exit(0);
		}

		System.out.println("Would you like to play against a robot player? (Entering no will begin two-player mode) Enter y or n: ");
		userInput = reader.next();
		if (userInput.equalsIgnoreCase("Y"))
			this.singlePlayer();
		else
			this.twoPlayer();
	}

	/**
	 * Two player version of the game. Black player goes first and then turns are
	 * taken until game over.
	 * @throws IOException
	 */
	private void twoPlayer() throws IOException {
		while ((!blackPlayer.wasSkipped() && !whitePlayer.wasSkipped()) && !gameBoard.checkIfFull()) {
			gameBoard.build("B");
			blackPlayer.takeTurn(gameBoard);
			if (gameBoard.checkIfFull()) {
				gameOver2P();
				return;
			}
			gameBoard.build("W");
			whitePlayer.takeTurn(gameBoard);
		}
		gameOver2P();
	}

	/**
	 * Allows one player to play against a computer opponent. Human player is black
	 * while computer is white.
	 * @throws IOException
	 */
	private void singlePlayer() throws IOException {
		while (!(blackPlayer.wasSkipped() && cpuW.wasSkipped()) && !gameBoard.checkIfFull()) {
			gameBoard.build("B");
			blackPlayer.takeTurn(gameBoard);
			if (gameBoard.checkIfFull()) {
				gameOver1P();
				return;
			}
			if (!blackPlayer.wasSkipped()) {
				gameBoard.build("null");
			}
			System.out.println("\nComputer turn:");
			cpuW.takeRandomTurn(gameBoard);
		}
		gameOver1P();
	}

	/**
	 * This is the simulation method which will complete a user-specified amount of
	 * games with computer players and output the results
	 * 
	 * @param tests amount of tests the user desires to compute
	 * @throws IOException
	 */
	private void simulation(int tests) throws IOException {
		int spread;
		HashMap<Integer, Integer> map = new HashMap<>();
		System.out.println("Running simulations, please wait...");
		while (tests > 0) {
			gameBoard.reset();
			while (!gameBoard.checkIfFull() && !(cpuB.wasSkipped() && cpuW.wasSkipped())) {
				cpuB.takeRandomTurn(gameBoard);
				cpuW.takeRandomTurn(gameBoard);
			}
			spread = gameBoard.getNumPieces("B") - gameBoard.getNumPieces("W");
			if (map.containsKey(spread)) {
				map.put(spread, map.get(spread) + 1);
			} else {
				map.put(spread, 1);
			}
			tests--;
			cpuB.unskip();
			cpuW.unskip();
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
	 * This method will calculate the final scores and display the winner for a two
	 * player game. It will also allow the users to play again.
	 * @throws IOException
	 */
	private void gameOver2P() throws IOException {
		int blackScore = gameBoard.getNumPieces("B");
		int whiteScore = gameBoard.getNumPieces("W");
		if ((blackPlayer.wasSkipped() && whitePlayer.wasSkipped()) && !gameBoard.checkIfFull()) {
			System.out.print("Neither player can move. ");
		}
		System.out.println("Game Over. Final board:");
		gameBoard.build("null");
		if (blackScore > whiteScore) {
			System.out.println("Black player wins!");
		} else if (blackScore < whiteScore)
			System.out.println("White player wins!");
		else
			System.out.println("It's a tie!");

		do {
			System.out.println("Would you like to play again? Enter y or n");
			userchoice = reader.next();
			if (userchoice.equalsIgnoreCase("y")) {
				gameBoard.reset();
				this.crossroad();
			} else if (userchoice.equalsIgnoreCase("n")) {
				System.out.println("Thanks for playing!");
				reader.close();
				System.exit(0);
			} else
				System.out.println("Sorry I don't understand your choice. Try again.");
		} while (userchoice != "y" && userchoice != "Y" && userchoice != "N" && userchoice != "n");
	}

	/**
	 * This method will calculate the final scores and display the winner for a one
	 * player game. It will also allow the user to play again.
	 * @throws IOException
	 */
	private void gameOver1P() throws IOException {
		int blackScore = gameBoard.getNumPieces("B");
		int whiteScore = gameBoard.getNumPieces("W");

		if ((blackPlayer.wasSkipped() && cpuW.wasSkipped()) && !gameBoard.checkIfFull()) {
			System.out.print("\nNeither player can move. ");
		}
		System.out.println("Game Over. Final board:");
		gameBoard.build("null");
		if (blackScore > whiteScore) {
			System.out.println("Black player wins!");
		} else if (blackScore < whiteScore)
			System.out.println("White player wins!");
		else
			System.out.println("It's a tie!");

		do {
			System.out.println("Would you like to play again? Enter y or n");
			userchoice = reader.next();
			if (userchoice.equalsIgnoreCase("y")) {
				gameBoard.reset();
				this.crossroad();
			} else if (userchoice.equalsIgnoreCase("n")) {
				System.out.println("Thanks for playing!");
				reader.close();
				System.exit(0);
			} else
				System.out.println("Sorry I don't understand your choice. Try again.");
		} while (userchoice != "y" && userchoice != "Y" && userchoice != "N" && userchoice != "n");
	}
}
