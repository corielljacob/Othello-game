package othelloGamePackage;

import java.util.LinkedList;

/**
 * Class representing a board used for the game. Can check if the board is full,
 * can count the number of pieces for either player, can check for valid plays.
 * @author Jacob Coriell
 */
public class Board {

	private final int rows = 8;
	private final int columns = 8;
	private Piece[][] pieceChart;
	private int numblackpieces;
	private int numwhitepieces;
	private boolean isFull;
	LinkedList<Integer> moves = new LinkedList<Integer>();

	/**
	 * This constructor constructs the 2D array of piece objects, pieceChart. This
	 * array keeps track of the state of each piece in play.
	 */
	public Board() {
		pieceChart = new Piece[rows][columns];
		reset();
	}

	/**
	 * This method will place a piece at the coordinates passed in
	 * @param r is the row of where the piece should be set
	 * @param c is the column of where the piece should be set
	 * @param p is the color of the piece being set
	 */
	protected void setPiece(int r, int c, String p) {
		pieceChart[r][c].setPieceColor(p);
	}

	/**
	 * 
	 * @param s is what player you want to know's piece count
	 * @return the number of pieces for that player
	 */
	protected int getNumPieces(String s) {
		calculateNumPieces();
		if (s.equals("B")) {
			return numblackpieces;
		} else if (s.equals("W")) {
			return numwhitepieces;
		} else
			return 0;
	}

	protected boolean checkIfFull() {
		checkFull();
		return isFull;
	}

	/**
	 * This method will reset the board so that a new game can begin
	 */
	protected void reset() {
		for (int x = 0; x < pieceChart.length; x++) {
			for (int y = 0; y < pieceChart[0].length; y++) {
				pieceChart[x][y] = new Piece("null");
			}
		}

		pieceChart[3][4] = new Piece("B");
		pieceChart[3][3] = new Piece("B");
		pieceChart[4][3] = new Piece("B");
		pieceChart[4][4] = new Piece("B");

	}

	/**
	 * This method loops through the 2D array and outputs where each piece lays
	 */
	protected void build() {

		numblackpieces = 0;
		numwhitepieces = 0;
		System.out.println("    1   2   3   4   5   6   7   8");
		for (int x = 0; x < pieceChart.length; x++) {

			System.out.print("  ---------------------------------");
			System.out.println("");
			System.out.print(x + 1 + " |");
			for (int y = 0; y < pieceChart[0].length; y++) {

				if (pieceChart[x][y] == null) {
					System.out.print("   |");
				} else if (pieceChart[x][y].getPieceColor().equals("B")) {
					System.out.print(" B |");
					numblackpieces++;
				} else if (pieceChart[x][y].getPieceColor().equals("W")) {
					System.out.print(" W |");
					numwhitepieces++;
				} else
					System.out.print("   |");
			}
			if (x == 3) {
				System.out.print(" Black player score: " + getNumPieces("B"));
			} else if (x == 4) {
				System.out.print(" White player score: " + getNumPieces("W"));
			}
			System.out.println("");

		}
		System.out.println("  ---------------------------------");
	}

	/**
	 * Calculates the number of pieces for each player
	 */
	private void calculateNumPieces() {
		numblackpieces = 0;
		numwhitepieces = 0;
		for (int x = 0; x < pieceChart.length; x++) {
			for (int y = 0; y < pieceChart[0].length; y++) {
				if (pieceChart[x][y] == null)
					;
				else if (pieceChart[x][y].getPieceColor().equals("B")) {
					numblackpieces++;
				} else if (pieceChart[x][y].getPieceColor().equals("W"))
					numwhitepieces++;
			}
		}
	}

	/**
	 * 
	 * @return a boolean that is true if there are no empty spots on the board
	 */
	private void checkFull() {
		int counter = 0;
		for (int x = 0; x < pieceChart.length; x++) {
			for (int y = 0; y < pieceChart[0].length; y++) {
				if (pieceChart[x][y].getPieceColor().equals("B") || pieceChart[x][y].getPieceColor().equals("W"))
					counter++;
			}
		}
		if (counter == 64) {
			isFull = true;
		} else
			isFull = false;
	}

	/**
	 * This method will travel in each direction around the desired placement in
	 * order to check if the desired location is valid
	 * @param x is the row where the user wishes to place a piece
	 * @param y is the column where the user wishes to place a piece
	 * @param s is the color of the player attempting to place a piece
	 * @param flip is a boolean that signifies if this method is actually being
	 *            called to place a move
	 * @return a boolean that is true if the desired location is a valid move
	 */
	protected boolean checkValid(int x, int y, String s, boolean flip) {
		int[] xoffsets = { 1, -1, 0, 0, 1, 1, -1, -1 };
		int[] yoffsets = { 0, 0, -1, 1, 1, -1, 1, -1 };
		boolean validMove = false;
	
		for (int i = 0; i < pieceChart.length; i++) {
			if (checkDirection(x, y, xoffsets[i], yoffsets[i], s, flip)) {
				validMove = true;
			}
		}
		return validMove;
	}

	/**
	 * This method travels in a direction determined by the checkValid method.
	 * Determines if the current player has a valid move in that direction
	 * @param x is the desired location row
	 * @param y is the desired location column
	 * @param deltax is the offset from the desired row
	 * @param deltay is the offset from the desired column
	 * @param s is the current player's color
	 * @param flip is a boolean that tells the method whether or not it should flip
	 *            the pieces in the direction being traveled
	 * @return true if the direction traversed contains a valid move for the current
	 *         player
	 */
	private boolean checkDirection(int x, int y, int deltax, int deltay, String s, boolean flip) {
		int desiredx = x;
		int desiredy = y;
		int dx = desiredx += deltax;
		int dy = desiredy += deltay;
		boolean opposite_color_seen = false;

		if (!(pieceChart[x][y].getPieceColor().equals("null"))) {
			return false;
		}

		while (dx >= 0 && dy >= 0 && dx < pieceChart.length && dy < pieceChart[0].length) {
			if (pieceChart[dx][dy].getPieceColor().equals("null"))
				break;
			else if (pieceChart[dx][dy].getPieceColor() != s) {
				opposite_color_seen = true;
				dx += deltax;
				dy += deltay;
			} else if (opposite_color_seen) {
				if (flip) {
					flip(x, y, deltax, deltay, s);
				}
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * Travels in the direction given through checkDirection -- flips each piece of
	 * opposite color to the current user
	 * @param x is the desired row
	 * @param y is the desired column
	 * @param dx is the offset row
	 * @param dy is the offset column
	 * @param clr is the current player's color
	 */
	private void flip(int x, int y, int dx, int dy, String clr) {
		x += dx;
		y += dy;
		while (!(pieceChart[x][y].getPieceColor().equals(clr))) {
			pieceChart[x][y].flipColor();
			x += dx;
			y += dy;
		}
	}

}
