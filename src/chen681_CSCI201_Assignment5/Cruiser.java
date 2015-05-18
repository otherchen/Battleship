package chen681_CSCI201_Assignment5;

public class Cruiser extends Ship{

	Cruiser(String[] array){
		coordinates = new String[3];
		coordinates = array;
		lives = 3;
		marker = "C";
	}

	public boolean hit(String coor){		
		boolean hit = false;
		for(int i = 0; i < 3; i++){
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
