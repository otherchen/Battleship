package chen681_CSCI201_Assignment5;

import java.io.Serializable;

public abstract class Ship implements Serializable{

	private static final long serialVersionUID = 1L;
	protected String[] coordinates;
	protected int lives;
	protected String marker;
	
	Ship(){
		
	}
	
	abstract public boolean hit(String coor);
	
	abstract public boolean sunk();
	
	abstract public String getMarker();
	
	abstract public String[] getCoordinates();
	
}
