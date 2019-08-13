package othelloGamePackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Class describing a player. A player has several attributes: a color (black or
 * white), a score based on how many pieces they have on the board, a flag
 * denoting if their last turn was skipped, a boolean denoting whether or not
 * they can currently move, and a list of possible moves. This class contains a
 * few getters/setters to preserve encapsulation.
 * @author Jacob Coriell
 */
public class Player {
	private int score = 0;
	private String color;
	private boolean skipped = false;
	List<moveLocation> moves = new ArrayList<moveLocation>();
	private final int rows = 8;
	private final int columns = 8;
	private boolean robot = false;

	public Player(String clr) {
		color = clr;
	}

	public Player(String clr, boolean bot) {
		color = clr;
		robot = bot;
	}

	/**
	 * Set score of current player
	 * @param n score of current player
	 */
	protected void setScore(int n) {
		score = n;
	}

	/**
	 * @return score of current player
	 */
	protected int getScore() {
		return score;
	}

	/**
	 * Getter method to determine if this player was skipped last turn.
	 * @return a boolean that is true if player was skip last turn, false if not
	 */
	protected boolean wasSkipped() {
		return this.skipped;
	}

	protected boolean playerCanMove(Board gameBoard) {
		return (this.canMove(this.color, gameBoard));
	}

	/**
	 * This method will check to see if a player simply can move or has no available
	 * moves
	 * @param s is the current player's color
	 * @return true if the player can make a move this turn
	 */
	protected boolean canMove(String s, Board gameBoard) {
		boolean canMove = false;
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				// if (gameBoard.checkValid(x, y, s, false)) {
				if (gameBoard.checkMove(x, y, s)) {
					canMove = true;
				}
			}
		}
		return canMove;
	}

	protected void takeTurn(Board gameBoard) {
		if (this.canMove(this.color, gameBoard)) {
			Scanner reader = new Scanner(System.in);
			String rowString, colString;
			int row, col;
			do {
				System.out.println(this.color + " Player: Assuming rows are the horizontal sections and columns are the vertical sections, \nenter what row you want to place a piece: ");
				rowString = reader.next();
				System.out.println("Now enter the column you want to place a piece: ");
				colString = reader.next();

			} while (!validNums(rowString, colString, gameBoard));

			row = Integer.parseInt(rowString);
			col = Integer.parseInt(colString);
			gameBoard.makeMove(row - 1, col - 1, this.color);
			unskip();
		} else {
			System.out.println("Skipping turn for " + this.color);
			skip();
		}
	}

	protected void takeRandomTurn(Board gameBoard) {
		this.determineMoves(gameBoard);
		if (moves.isEmpty()) {
			this.skip();
			if (!robot)
				System.out.println("No moves available. Skipping turn.");
		} else {
			Random rand = new Random();
			moveLocation move = moves.get(rand.nextInt(moves.size()));
			gameBoard.makeMove(move.x, move.y, this.color);
			unskip();
		}
	}

	private void determineMoves(Board gameBoard) {
		moves.clear();
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				if (gameBoard.checkMove(x, y, this.color)) {
					moves.add(new moveLocation(x, y));
				}
			}
		}
	}

	private void skip() {
		this.skipped = true;
	}

	protected void unskip() {
		this.skipped = false;
	}

	private boolean validNums(String x, String y, Board gameBoard) {
		int row, col;
		if (strIsDecimal(x) && strIsDecimal(y)) {
			row = Integer.parseInt(x);
			col = Integer.parseInt(y);
		} else {
			System.out.println("Enter numbers only as rows and columns.");
			return false;
		}

		if (intIsDecimal(row) && intIsDecimal(col)) {
			if (gameBoard.checkMove(row - 1, col - 1, this.color)) {
				return true;
			} else {
				System.out.println("Please enter a valid location.");
			}
		}
		return false;
	}

	private boolean intIsDecimal(int num) {
		return (num == 1 || num == 2 || num == 3 || num == 4 || num == 5 || num == 6 || num == 7 || num == 8 || num == 9);
	}

	private boolean strIsDecimal(String s) {
		return (s.charAt(0) == ('1') || s.charAt(0) == ('2') || s.charAt(0) == ('3') || s.charAt(0) == ('4') || s.charAt(0) == ('5') || s.charAt(0) == ('6') || s.charAt(0) == ('7') || s.charAt(0) == ('8') || s.charAt(0) == ('9'));
	}

	public class moveLocation {
		public final int x;
		public final int y;

		public moveLocation(int xcoord, int ycoord) {
			this.x = xcoord;
			this.y = ycoord;
		}
	}

}
