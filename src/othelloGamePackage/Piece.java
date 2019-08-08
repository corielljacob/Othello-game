package othelloGamePackage;
/**
 * 
 * @author Jacob Coriell
 *
 */
public class Piece {
	private String color;

	protected Piece() {
		//default constructor
	}
	/**
	 * modified constructor -- create new piece with color based on user
	 * @param clr is the color based on the user
	 */
	protected Piece(String clr) {	
		color = clr;
	}
	/**
	 *  change color of piece 
	 * @param clr is the desired color 
	 */
	protected void setPieceColor(String clr) {
		color = clr;
	}
	
	/**
	 *
	 * @return string that is the color of the piece
	 */
	protected String getPieceColor() {	
		return color;
	}
	
	/**
	 * change color of current piece to opposite player's color
	 */
	protected void flipColor() { 
		if (this.getPieceColor().equals("W")) 
			this.setPieceColor("B");
		else
			this.setPieceColor("W");
		}
	}

