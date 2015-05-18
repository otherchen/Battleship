package chen681_CSCI201_Assignment5;

public class Battleship extends Ship{

	Battleship(String[] array){
		coordinates = new String[4];
		coordinates = array;
		lives = 4;
		marker = "B";
	}

	public boolean hit(String coor){		
		boolean hit = false;
		for(int i = 0; i < 4; i++){
			if(coor.equals(coordinates[i])){
				hit = true;
				lives--;
				break;
			}
		}
		return hit;
	}
	
	public boolean sunk(){
		if(lives <= 0){
			return true;
		} else {
			return false;
		}
	}
	
	public String getMarker(){
		return marker;
	}
	
	public String[] getCoordinates(){
		return coordinates;
	}
	
}
