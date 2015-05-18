package chen681_CSCI201_Assignment5;

import java.io.Serializable;

public class GuessObject implements Serializable {
	private static final long serialVersionUID = 1L;
	public String player = null;
	public boolean won = false;
	public String marker = "";
	public boolean hit = false;
	public boolean sunk = false;
	public Ship sunkShip = null;
	public int row = 0;
	public int column = 0;
	public boolean alreadyGuessed = false;
}
