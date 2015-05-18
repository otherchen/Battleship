package chen681_CSCI201_Assignment5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import org.json.JSONObject;

public class GameActions {
	//game member variables
	boolean playerWon;
	boolean computerWon;
	String[][] computerMap;
	String[][] computerOriginal;
	Ship[] shipComp;
	int shipCountComp;
	String[][] playerMap;
	String[][] playerOriginal;
	Ship[] ship;
	int shipCount;
	int numAir;
	int numBat;
	int numCruis;
	int numDestr; 
	File compFile;
	
	GameActions(){
		playerWon = false;
		computerWon = false;
		computerMap = new String[10][10];
		computerOriginal = new String[10][10];
		shipComp = new Ship[5];
		shipCountComp = 0;
		playerMap = new String[10][10];
		playerOriginal = new String[10][10];
		ship = new Ship[5];
		shipCount = 0;
		numAir = 0;
		numBat = 0;
		numCruis = 0;
		numDestr = 0; 
		
		//setting up player's board
		setUpBoard();
	}
	
	private void setUpBoard(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				playerMap[i][j] = "X";
				playerOriginal[i][j] = "X";
			}
		}
	}
	
	public void storeBoard(File file){
		
		/************** store game board **************/
		
		BufferedReader br = null;
		String[] parsed;
		String line;
		compFile = file;
		
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {	
			//iterates through the game board
			for(int i = 0; i < 10; i++){
				line = br.readLine();
				parsed = line.split("");
				
				//ensures the width of the board is 10
				if(parsed.length != 10){
					System.out.println("The width of the board is not 10 characters");
					System.out.println("Improper file format, terminating program");
					return;
				}
				
				for(int j = 0; j < 10; j++){
					
					//checks if the character is recognized (X, A, B, C, D)
					if(!charCheck(parsed[j])){
						System.out.println("There exists unrecognized characters on the board");
						System.out.println("Improper file format, terminating program");
						return;
					}
					
					computerMap[i][j] = parsed[j].toUpperCase();
					computerOriginal[i][j] = parsed[j].toUpperCase();

				}

			}
			
			//makes sure the length isn't greater than 10 or there's nothing extra after the board
			if(br.readLine() != null){
				System.out.println("Too many lines after the 10x10 board game");
				System.out.println("Improper file format, terminating program");
				return;
			}
				
			br.close();
		
		//makes sure the board is not less than 10x10
		} catch (Exception e){
			System.out.println("Board provided has too few characters");
			System.out.println("Improper file format, terminating program");
			return;
		}
		
		int numAirComp = 0;
		int numBatComp = 0;
		int numCruisComp = 0;
		int numDestrComp = 0; 
		shipCountComp = 0;
		shipComp = new Ship[5];
		
		//checking to see where the ships are located
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				
				String marker;
				if(!computerMap[i][j].equals("X")){
					
					marker = computerMap[i][j];
					computerMap[i][j] = "X";
					String[] array = null;
					int size = 0;
					
					if(marker.equals("A")){
						size = 5;
					} else if (marker.equals("B")){
						size = 4;
					} else if (marker.equals("C")){
						size = 3;
					} else if (marker.equals("D")){
						size = 2;
					} else {
						System.out.println("marker was not recognized.");
						System.out.println("Improper file format, terminating program");
						return;
					}	

					array = new String[size];
					array[0] = position(i,j);
					boolean notHorizontal = false;
					boolean notVertical = false;
						
					//search for same marker to the right
					for(int x = 1; x < size; x++){
						if((j+x) > 9){
							//going beyond array bounds 
							notHorizontal = true;
							break;
						} else if(!computerMap[i][j+x].equals(marker)){
							//next index doesn't equal marker
							notHorizontal = true;
							break;
						} else if(computerMap[i][j+x].equals(marker)){
							//next index equals marker
							array[x] = position(i, j+x);
						} 
					}
						
					if(notHorizontal == false) {
						for(int a = 1; a < size; a++){
							computerMap[i][j+a] = "X";
						}
					} else if(notHorizontal == true){
						for(int y = 1; y < size; y++){
							if((i+y) > 9){
								//going beyond array bounds
								notVertical = true;
								break;
							} else if(!computerMap[i+y][j].equals(marker)){
								notVertical = true;
								break;
							} else if(computerMap[i+y][j].equals(marker)){
								array[y] = position(i+y, j);
								computerMap[i+y][j] = "X";
							}
						}
					}
						
					if(notHorizontal && notVertical){
						System.out.println("Ship not right length");
						System.out.println("Incorrect file format, terminating program.");
						return;
					} else {
						
						shipCountComp++;
						
						if(shipCountComp > 5){
							System.out.println("ShipCount: " + shipCountComp);
							System.out.println("Improper file format, terminating program");
							return;
						} else if(marker.equals("A")){
							numAirComp++;
							shipComp[shipCountComp - 1] = new AirCarrier(array);
						} else if(marker.equals("B")){
							numBatComp++;
							shipComp[shipCountComp - 1] = new Battleship(array);
						} else if(marker.equals("C")){
							numCruisComp++;
							shipComp[shipCountComp - 1] = new Cruiser(array);
						} else if(marker.equals("D")){
							numDestrComp++;
							shipComp[shipCountComp - 1] = new Destroyer(array);
						}

					}
					
				}
				
			}
			
		}
		
		//making sure there are 5 ships
		if(shipCountComp != 5){
			System.out.println("ShipCount: " + shipCountComp);
			System.out.println("Improper file format, terminating program");
			return;
		} else if ((numAirComp != 1) || (numBatComp != 1) || (numCruisComp != 1) || (numDestrComp != 2)){
			System.out.println("Incorrect number of boats");
			System.out.println("Improper file format, terminating program");
			return;
		}
	}
	
	public void reset(){
		playerWon = false;
		computerWon = false;
		computerMap = new String[10][10];
		computerOriginal = new String[10][10];
		shipComp = new Ship[5];
		shipCountComp = 0;
		playerMap = new String[10][10];
		playerOriginal = new String[10][10];
		ship = new Ship[5];
		shipCount = 0;
		numAir = 0;
		numBat = 0;
		numCruis = 0;
		numDestr = 0; 
		
		//setting up player's board
		setUpBoard();
		//storeBoard(compFile);
	}
	
	public String[] getShipIndices(int row, int column){
		boolean hit = false;
		String[] coordinates = null;
		for(int i = 0; i < shipCount; i++){
			hit = ship[i].hit(position(row, column - 1));
			//erase ship that is at given index
			if(hit == true){
				coordinates = ship[i].getCoordinates();
				return coordinates;
			}
		}
		return coordinates;
	}
	
	public void eraseShip(int row, int column){
		//erasing and deleting ship;
		boolean hit = false;
		String marker = "";
		for(int i = 0; i < shipCount; i++){
			hit = ship[i].hit(position(row, column - 1));
			
			//erase ship that is at given index
			if(hit == true){
				marker = ship[i].getMarker();
				String[] coordinates = ship[i].getCoordinates();
				
				//resetting the letters to "X's"
				for(int j = 0; j < coordinates.length; j++){
					String curr = coordinates[j];
					String[] currIndex = curr.split("");
					
					if(curr.length() > 2){
						currIndex[1] = currIndex[1]+currIndex[2];
					}
					
					int letter = row(currIndex[0]);
					int number = Integer.parseInt(currIndex[1])-1;
					playerMap[letter][number] = "X";
					playerOriginal[letter][number] = "X";
				}
					
				//decreasing ship count
				if(marker.equals("A")){
					numAir--;
				} else if (marker.equals("B")){
					numBat--;
				} else if (marker.equals("C")){
					numCruis--;
				} else if (marker.equals("D")){
					numDestr--;
				}
				shipCount--;
				
				//deleting the ship reference
				for(int j = i; j < shipCount; j++){
					ship[j] = ship[j+1];
				}
					
				break;
			}
		}
	}
	
	public boolean playerWon(){
		return playerWon;
	}
	
	public boolean computerWon(){
		return computerWon;
	}
	
	//public String playerGuess(int row, int column){
	public GuessObject playerGuess(int row, int column){
		//declare variables
		GuessObject result = new GuessObject();
		boolean hit = false;
		boolean sunk = false;
		
		//check if already guessed
		if(computerMap[row][column-1].equals("-1")){
			result.alreadyGuessed = true;
			return result;
		}
		
		//check to see if it hit
		for(int i = 0; i < 5; i++){
			if(!shipComp[i].sunk()){
				hit = shipComp[i].hit(position(row, column - 1));
				if(hit == true){
					result.marker = shipComp[i].getMarker();
					result.hit = true;
					sunk = shipComp[i].sunk();
					if(sunk){
						shipCountComp--;
						result.sunk = true;
						result.sunkShip = shipComp[i];
					}
					break;
				}
			}
		}
		
		//setting index to "guessed" flag
		computerMap[row][column-1] = "-1";
		
		//if all ships are gone, you win!
		if(shipCountComp <= 0){
			playerWon = true;
		}
		
		return result;
		
	}
	
	public GuessObject computerGuess(){
		//declare variables
		GuessObject result = new GuessObject();
		boolean hit = false;
		boolean sunk = false;
				
		//generate random guess
		int row = (int )(Math.random() * 10);
		int column = (int )(Math.random() * 10);
		column += 1;
				
		//check if already guessed
		if(playerMap[row][column-1].equals("-1")){
			result.alreadyGuessed = true;
			return result;
		}
				
		//check to see if it hit
		for(int i = 0; i < 5; i++){
			if(!ship[i].sunk()){
				hit = ship[i].hit(position(row, column - 1));
				if(hit == true){
					result.marker = ship[i].getMarker();
					result.hit = true;
					sunk = ship[i].sunk();
					if(sunk){
						shipCount--;
						result.sunk = true;
						result.sunkShip = ship[i];
					}
					break;
				}
			}
		}
				
		//setting index to "guessed" flag
		playerMap[row][column-1] = "-1";
				
		//if all ships are gone, you win!
		if(shipCount <= 0){
			computerWon = true;
		}
				
		result.row = row;
		result.column = column;
		return result;
	}
	
	public File getPlayerMap(String playerName){
		
		//turn the ships into JSON objects and store in JSON file
		JSONObject obj = new JSONObject();
		JSONObject shipObj = new JSONObject();
		
		int destroyerCount = 0;
		
		for(int i = 0; i < ship.length; i++){
			String[] coords = ship[i].getCoordinates();
			String label = null;
			
			if(coords.length == 2){
				destroyerCount++;
				label = "Destroyer" + destroyerCount; 
			} else if(coords.length == 3){
				label = "Cruiser";
			} else if(coords.length == 4){
				label = "Battleship";
			} else if(coords.length == 5){
				label = "AircraftCarrier";
			}
			
			shipObj.put(label, coords);
		}
		
		//constructs the encompassing JSON obj
		obj.put(playerName, shipObj);
	 
		try {
	 
			FileWriter file = new FileWriter("assets/map.json");
			file.write(obj.toString());
			file.flush();
			file.close();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		File mapFile = new File("assets/map.json");
		return mapFile;
	}
	
	public void placeShip(int row, int column, String shipName, String direction){
		int rowStart = row;
		int colStart = column-1;
		int rowStop = row+1;
		int colStop = column;
		int indexCount = 0;
		int size = 0;
		String marker = "";
		
		if(shipName.equals("Destroyer")){
			size = 2;
		} else if (shipName.equals("Cruiser")){
			size = 3;
		} else if (shipName.equals("Battleship")){
			size = 4;
		} else if (shipName.equals("Aircraft Carrier")){
			size = 5;
		}
		
		if(direction.equals("North")){
			rowStart = row-size+1;
			rowStop = row+1; 
		} else if (direction.equals("South")){
			rowStart = row;
			rowStop = row+size;
		} else if (direction.equals("East")){
			colStart = column-1;
			colStop = column+size-1;
		} else if (direction.equals("West")){
			colStart = column-size;
			colStop = column;
		}
		
		if(size == 2){
			marker = "D";
		} else if (size == 3){
			marker = "C";
		} else if (size == 4){
			marker = "B";
		} else if (size == 5){
			marker = "A";
		}
		
		String[] array = new String[size];
		
		try{
			for(int i = rowStart; i < rowStop; i++){
				for(int j = colStart; j < colStop; j++){
					playerMap[i][j] = marker;
					playerOriginal[i][j] = marker;
					array[indexCount] = position(i, j);
					indexCount++;
				}	
			}
		} catch(IndexOutOfBoundsException e){
			System.out.println(e.getMessage());
		}
		
		if(shipCount > 5){
			System.out.println("ShipCount: " + (shipCount + 1));
			return;
		} else if(marker.equals("A")){
			numAir++;
			ship[shipCount] = new AirCarrier(array);
		} else if(marker.equals("B")){
			numBat++;
			ship[shipCount] = new Battleship(array);
		} else if(marker.equals("C")){
			numCruis++;
			ship[shipCount] = new Cruiser(array);
		} else if(marker.equals("D")){
			numDestr++;
			ship[shipCount] = new Destroyer(array);
		}
		
		shipCount++;
		
	}
	
	public boolean fullShips(){
		if(shipCount >= 5){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isValid(int row, int column, int size, String direction){
		boolean isGood = true;
		int rowStart = row;
		int colStart = column-1;
		int rowStop = row+1;
		int colStop = column;
		
		if(direction.equals("North")){
			rowStart = row-size+1;
			rowStop = row+1; 
		} else if (direction.equals("South")){
			rowStart = row;
			rowStop = row+size;
		} else if (direction.equals("East")){
			colStart = column-1;
			colStop = column+size-1;
		} else if (direction.equals("West")){
			colStart = column-size;
			colStop = column;
		}
		
		try{
			for(int i = rowStart; i < rowStop; i++){
				for(int j = colStart; j < colStop; j++){
					if(!playerMap[i][j].equals("X")){
						return false;
					}		
				}	
			}
		} catch(IndexOutOfBoundsException e){
			return false;
		}
		
		return isGood;
	}
	
	public String[] getPlayerShips(){
		Vector<String> shipList = new Vector<String>();
		
		if(numDestr < 2){
			shipList.addElement("Destroyer");
		}
		if(numCruis < 1){
			shipList.addElement("Cruiser");
		}
		if(numBat < 1){
			shipList.addElement("Battleship");
		}
		if(numAir < 1){
			shipList.addElement("Aircraft Carrier");
		}
		
		String[] shipString = new String[shipList.size()];
		
		for(int i = 0; i < shipList.size(); i++){
			shipString[i] = shipList.get(i);
		}
		
		return (shipString);
	}
	
	public int row(String letter){
		int num = -1;
		letter = letter.toUpperCase();
		
		if(letter.equals("A")){
			num = 0;
		} else if (letter.equals("B")){
			num = 1;
		} else if (letter.equals("C")){
			num = 2;
		} else if (letter.equals("D")){
			num = 3;
		} else if (letter.equals("E")){
			num = 4;
		} else if (letter.equals("F")){
			num = 5;
		} else if (letter.equals("G")){
			num = 6;
		} else if (letter.equals("H")){
			num = 7;
		} else if (letter.equals("I")){
			num = 8;
		} else if (letter.equals("J")){
			num = 9;
		}
		
		return num;
	}
	
	public String position(int row, int column){
		
		String letter = "";
		String number = Integer.toString(column+1);
		
		if(row == 0){
			letter = "A";
		} else if (row == 1){
			letter = "B";
		} else if (row == 2){
			letter = "C";
		} else if (row == 3){
			letter = "D";
		} else if (row == 4){
			letter = "E";
		} else if (row == 5){
			letter = "F";
		} else if (row == 6){
			letter = "G";
		} else if (row == 7){
			letter = "H";
		} else if (row == 8){
			letter = "I";
		} else if (row == 9){
			letter = "J";
		}
		
		return (letter + number);
		
	}
	
	private boolean charCheck(String letter){
		letter = letter.toUpperCase();
		if(!letter.equals("X") && !letter.equals("A") && !letter.equals("B")
			&& !letter.equals("C") && !letter.equals("D")){
			return false;
		} else {
			return true;
		}
	}
	
	public static boolean rankCheck(double [] rank){
		boolean correct = true;
		for(int i = 1; i <= 10; i++){
			if((int)(rank[i-1]) != i){
				correct = false;
			}
		}
		return correct;
	}
	
}
