package othelloGamePackage;

import java.util.ArrayList;
import java.util.List;

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
	private int[] xoffsets = { 1, -1, 0, 0, 1, 1, -1, -1 };
	private int[] yoffsets = { 0, 0, -1, 1, 1, -1, 1, -1 };
	private List<Pair> moves = new ArrayList<Pair>();
	private List<Pair> validDirections = new ArrayList<Pair>();

	/**
	 * This constructor constructs the 2D array of piece objects, pieceChart. This
	 * array keeps track of the state of each piece in play.
	 */
	public Board() {
		pieceChart = new Piece[rows][columns];
		reset();
	}

	/**
	 * This method prints the state of the board by looping thru the array containing the piece data. 
	 * The board will display possible move locations for human players. 
	 */
	protected void build(String s) {
		boolean locPlaced = false;
		if(s.equals("B") || s.equals("W")) {
			this.determineMoves(s);
		}
		numblackpieces = 0;
		numwhitepieces = 0;
		System.out.println("    1   2   3   4   5   6   7   8");
		for (int x = 0; x < pieceChart.length; x++) {
	
			System.out.print("  ---------------------------------");
			System.out.println("");
			System.out.print(x + 1 + " |");
			for (int y = 0; y < pieceChart[0].length; y++) {
				locPlaced = false;
				for(int i = 0; i < moves.size(); i++) {
					if (moves.get(i).x == x && moves.get(i).y == y) {
						System.out.print(" O |");
						locPlaced = true;
						break;
					}
				}
				if(locPlaced) {
					continue;
				}
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
	 * Protected access method to full checking method
	 */
	protected boolean checkIfFull() {
		checkFull();
		return isFull;
	}

	/**
	 * Protected access method to checking if a move is valid
	 * @param x is the row of the selected location
	 * @param y is the column of the selected location
	 * @param s is the color of the piece that would be placed
	 * @return true if this move can be made, false if not
	 */
	protected boolean checkMove(int x, int y, String s) {
		return (moveCheck(x, y, s));
	}

	/**
	 * Protected access method for getting the number of pieces for a specified player
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
	
	/**
	 * Protected access method to making a move on the board. Sets a piece on the board and flips any pieces as needed.
	 * @param x is the row that the move should be made in
	 * @param y is the column that the move should be made in
	 * @param s is the color of the player trying to make this move
	 */
	protected void makeMove(int x, int y, String s) {
		if (moveCheck(x, y, s)) {
			setPiece(x, y, s);
			flipPieces(x, y, s);
		}
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
	
		pieceChart[3][4] = new Piece("W");
		pieceChart[3][3] = new Piece("B");
		pieceChart[4][3] = new Piece("W");
		pieceChart[4][4] = new Piece("B");
	
	}

	/**
	 * This method will place a piece at the coordinates passed in
	 * @param x is the row of where the piece should be set
	 * @param y is the column of where the piece should be set
	 * @param s is the color of the piece being set
	 */
	protected void setPiece(int x, int y, String s) {
		pieceChart[x][y].setPieceColor(s);
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
	 * Check if board is full with chips
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
	 * Determine all possible moves for a player
	 * @param s is the color of the player to check all moves for
	 */
	private void determineMoves(String s) {
		moves.clear();
		for (int x = 0; x < rows; x++) {
			for (int y = 0; y < columns; y++) {
				if (this.checkMove(x, y, s)) {
					moves.add(new Pair(x, y));
				}
			}
		}
	}
	
	/**
	 * Flips any appropriate pieces along the directions that have flips.
	 * @param x row that the original piece was placed in
	 * @param y column that the original piece was placed in
	 * @param s color of player who placed the original piece
	 */
	private void flipPieces(int x, int y, String s) {
		for (int i = 0; i < validDirections.size(); i++) {
			int xoffset = (validDirections.get(i).x);
			int yoffset = (validDirections.get(i).y);
			int row = x + xoffset;
			int col = y + yoffset;
			while (!(pieceChart[row][col].getPieceColor().equals(s))) {
				pieceChart[row][col].flipColor();
				row += xoffset;
				col += yoffset;
			}
		}
	}
	
	/**
	 * Check if a move can be made. If a move is possible, the offsets used are stored for an easier flipping algorithm
	 * @param x row to place a piece in
	 * @param y column to place a piece in
	 * @param s color of the player attemping to make a move
	 * @return true if the move can be made at this location
	 */
	private boolean moveCheck(int x, int y, String s) {
		boolean adjacentCheck = false;
		boolean valid = false;
		validDirections.clear();
		int xoffset, yoffset, nextx, nexty;
		if (!(pieceChart[x][y].getPieceColor().equals("null"))) {
			return false;
		}
		for (int index = 0; index < xoffsets.length; index++) {
			xoffset = xoffsets[index];
			yoffset = yoffsets[index];
			nextx = x + xoffset;
			nexty = y + yoffset;
			adjacentCheck = false;
			while (nextx >= 0 && nextx < pieceChart.length && nexty >= 0 && nexty < pieceChart[0].length) {
				if ((pieceChart[nextx][nexty].getPieceColor().equals("null"))) {
					break;
				}
				else if (!(pieceChart[nextx][nexty].getPieceColor().equals(s))) {
					adjacentCheck = true;
					nextx += xoffset;
					nexty += yoffset;
				} else if (adjacentCheck) {
					validDirections.add(new Pair(xoffset, yoffset));
					valid = true;
					break;
				} else {
					break;
				}
			}
		}
		return valid;
	}
	
	/**
	 * Helper class that simulates a pair/tuple data structure
	 * @author Corieljt
	 *
	 */
	private class Pair {
		public final int x;
		public final int y;

		public Pair(int xcoord, int ycoord) {
			this.x = xcoord;
			this.y = ycoord;
		}
	}

}
