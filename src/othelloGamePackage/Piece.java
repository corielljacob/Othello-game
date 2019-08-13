package othelloGamePackage;

/**
 * 
 * @author Jacob Coriell
 *
 */
public class Piece {
	private String color;

	protected Piece() {
		// default constructor
	}

	/**
	 * modified constructor -- create new piece with color based on user
	 * 
	 * @param clr color based on the user. Player 1 is black. Player 2 is white.
	 */
	protected Piece(String clr) {
		color = clr;
	}

	/**
	 * change color of piece
	 * 
	 * @param clr desired color to change a piece to
	 */
	protected void setPieceColor(String clr) {
		color = clr;
	}

	/**
	 * Gets color of this piece
	 * 
	 * @return string detailing color of this piece
	 */
	protected String getPieceColor() {
		return color;
	}

	/**
	 * Flip color of this piece to color opposite of current
	 */
	protected void flipColor() {
		if (this.getPieceColor().equals("W"))
			this.setPieceColor("B");
		else
			this.setPieceColor("W");
	}
}
