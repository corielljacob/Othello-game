package othelloGamePackage;
/**
 * 
 * @author Jacob Coriell
 *
 */
public class Player {
	private int score = 0;
	
	/**
	 * sets score of current player
	 * @param n score of current player
	 */
	protected void setScore(int n) {
		score = n;
	}
	
	/**
	 * 
	 * @return score of current player
	 */
	protected int getScore() {
		return score;
	}

}
