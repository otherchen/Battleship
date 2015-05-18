package chen681_CSCI201_Assignment5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerActions {
	//game member variables
	boolean player1Won;
	boolean player2Won;
	String[][] player2Map;
	String[][] player2Original;
	Ship[] ship2;
	int shipCount2;
	int numAir2;
	int numBat2;
	int numCruis2;
	int numDestr2; 
	String[][] player1Map;
	String[][] player1Original;
	Ship[] ship1;
	int shipCount1;
	int numAir1;
	int numBat1;
	int numCruis1;
	int numDestr1; 
	File player1File;
	File player2File;
	String player1Name;
	String player2Name;
	
	ServerActions(){
		player1Won = false;
		player2Won = false;
		player2Map = new String[10][10];
		player2Original = new String[10][10];
		ship2 = new Ship[5];
		shipCount2 = 0;
		numAir2 = 0;
		numBat2 = 0;
		numCruis2 = 0;
		numDestr2 = 0; 
		player1Map = new String[10][10];
		player1Original = new String[10][10];
		ship1 = new Ship[5];
		shipCount1 = 0;
		numAir1 = 0;
		numBat1 = 0;
		numCruis1 = 0;
		numDestr1 = 0; 
		player1Name = null;
		player2Name = null;
		
		//setting up player's board
		setUpBoard();
	}
	
	private void setUpBoard(){
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				player1Map[i][j] = "X";
				player1Original[i][j] = "X";
			}
		}
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				player2Map[i][j] = "X";
				player2Original[i][j] = "X";
			}
		}
	}
	
	public void storeBoard(File file){
		
		/************** store game board **************/
		
		BufferedReader br = null;
		String[] parsed;
		String line;
		
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
					
					player2Map[i][j] = parsed[j].toUpperCase();
					player2Original[i][j] = parsed[j].toUpperCase();

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
		shipCount2 = 0;
		ship2 = new Ship[5];
		
		//checking to see where the ships are located
		for(int i = 0; i < 10; i++){
			for(int j = 0; j < 10; j++){
				
				String marker;
				if(!player2Map[i][j].equals("X")){
					
					marker = player2Map[i][j];
					player2Map[i][j] = "X";
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
						} else if(!player2Map[i][j+x].equals(marker)){
							//next index doesn't equal marker
							notHorizontal = true;
							break;
						} else if(player2Map[i][j+x].equals(marker)){
							//next index equals marker
							array[x] = position(i, j+x);
						} 
					}
						
					if(notHorizontal == false) {
						for(int a = 1; a < size; a++){
							player2Map[i][j+a] = "X";
						}
					} else if(notHorizontal == true){
						for(int y = 1; y < size; y++){
							if((i+y) > 9){
								//going beyond array bounds
								notVertical = true;
								break;
							} else if(!player2Map[i+y][j].equals(marker)){
								notVertical = true;
								break;
							} else if(player2Map[i+y][j].equals(marker)){
								array[y] = position(i+y, j);
								player2Map[i+y][j] = "X";
							}
						}
					}
						
					if(notHorizontal && notVertical){
						System.out.println("Ship not right length");
						System.out.println("Incorrect file format, terminating program.");
						return;
					} else {
						
						shipCount2++;
						
						if(shipCount2 > 5){
							System.out.println("ShipCount: " + shipCount2);
							System.out.println("Improper file format, terminating program");
							return;
						} else if(marker.equals("A")){
							numAirComp++;
							ship2[shipCount2 - 1] = new AirCarrier(array);
						} else if(marker.equals("B")){
							numBatComp++;
							ship2[shipCount2 - 1] = new Battleship(array);
						} else if(marker.equals("C")){
							numCruisComp++;
							ship2[shipCount2 - 1] = new Cruiser(array);
						} else if(marker.equals("D")){
							numDestrComp++;
							ship2[shipCount2 - 1] = new Destroyer(array);
						}

					}
					
				}
				
			}
			
		}
		
		//making sure there are 5 ships
		if(shipCount2 != 5){
			System.out.println("ShipCount: " + shipCount2);
			System.out.println("Improper file format, terminating program");
			return;
		} else if ((numAirComp != 1) || (numBatComp != 1) || (numCruisComp != 1) || (numDestrComp != 2)){
			System.out.println("Incorrect number of boats");
			System.out.println("Improper file format, terminating program");
			return;
		}
	}
	
	public void reset(){
		player1Won = false;
		player2Won = false;
		player2Map = new String[10][10];
		player2Original = new String[10][10];
		ship2 = new Ship[5];
		shipCount2 = 0;
		player1Map = new String[10][10];
		player1Original = new String[10][10];
		ship1 = new Ship[5];
		shipCount1 = 0;
		numAir1 = 0;
		numBat1 = 0;
		numCruis1 = 0;
		numDestr1 = 0; 
		numAir2 = 0;
		numBat2 = 0;
		numCruis2 = 0;
		numDestr2 = 0;
		
		//setting up player's board
		setUpBoard();
	}
	
	public String[] getShipIndices(int row, int column){
		boolean hit = false;
		String[] coordinates = null;
		for(int i = 0; i < shipCount1; i++){
			hit = ship1[i].hit(position(row, column - 1));
			//erase ship that is at given index
			if(hit == true){
				coordinates = ship1[i].getCoordinates();
				return coordinates;
			}
		}
		return coordinates;
	}
	
	public void eraseShip(int row, int column){
		//erasing and deleting ship;
		boolean hit = false;
		String marker = "";
		for(int i = 0; i < shipCount1; i++){
			hit = ship1[i].hit(position(row, column - 1));
			
			//erase ship that is at given index
			if(hit == true){
				marker = ship1[i].getMarker();
				String[] coordinates = ship1[i].getCoordinates();
				
				//resetting the letters to "X's"
				for(int j = 0; j < coordinates.length; j++){
					String curr = coordinates[j];
					String[] currIndex = curr.split("");
					
					if(curr.length() > 2){
						currIndex[1] = currIndex[1]+currIndex[2];
					}
					
					int letter = row(currIndex[0]);
					int number = Integer.parseInt(currIndex[1])-1;
					player1Map[letter][number] = "X";
					player1Original[letter][number] = "X";
				}
					
				//decreasing ship count
				if(marker.equals("A")){
					numAir1--;
				} else if (marker.equals("B")){
					numBat1--;
				} else if (marker.equals("C")){
					numCruis1--;
				} else if (marker.equals("D")){
					numDestr1--;
				}
				shipCount1--;
				
				//deleting the ship reference
				for(int j = i; j < shipCount1; j++){
					ship1[j] = ship1[j+1];
				}
					
				break;
			}
		}
	}
	
	public boolean player1Won(){
		return player1Won;
	}
	
	public boolean player2Won(){
		return player2Won;
	}
	
	//public String playerGuess(int row, int column){
	public GuessObject playerGuess(String name, int row, int column){ //TODO
		//declare variables
		GuessObject result = new GuessObject();
		result.row = row;
		result.column = column;
		result.player = name;
		boolean hit = false;
		boolean sunk = false;
		
		if(player1Name.equals(name)){
		
			//check if already guessed
			if(player2Map[row][column-1].equals("-1")){
				result.alreadyGuessed = true;
				return result;
			}
			
			//check to see if it hit
			for(int i = 0; i < 5; i++){
				if(!ship2[i].sunk()){
					hit = ship2[i].hit(position(row, column - 1));
					if(hit == true){
						result.marker = ship2[i].getMarker();
						result.hit = true;
						sunk = ship2[i].sunk();
						if(sunk){
							shipCount2--;
							result.sunk = true;
							result.sunkShip = ship2[i];
						}
						break;
					}
				}
			}
			
			//setting index to "guessed" flag
			player2Map[row][column-1] = "-1";
			
			//if all ships are gone, you win!
			if(shipCount2 <= 0){
				result.won = true;
				player1Won = true;
			}
			
		} else if (player2Name.equals(name)){
			//check if already guessed
			if(player1Map[row][column-1].equals("-1")){
				result.alreadyGuessed = true;
				return result;
			}
			
			//check to see if it hit
			for(int i = 0; i < 5; i++){
				if(!ship1[i].sunk()){
					hit = ship1[i].hit(position(row, column - 1));
					if(hit == true){
						result.marker = ship1[i].getMarker();
						result.hit = true;
						sunk = ship1[i].sunk();
						if(sunk){
							shipCount1--;
							result.sunk = true;
							result.sunkShip = ship1[i];
						}
						break;
					}
				}
			}
			
			//setting index to "guessed" flag
			player1Map[row][column-1] = "-1";
			
			//if all ships are gone, you win!
			if(shipCount1 <= 0){
				result.won = true;
				player2Won = true;
			}
		}
		
		return result;		
	}
	
	public GuessObject compGuess(){
		//declare variables
		GuessObject result = new GuessObject();
		boolean hit = false;
		boolean sunk = false;
				
		//generate random guess
		int row = (int )(Math.random() * 10);
		int column = (int )(Math.random() * 10);
		column += 1;
				
		//check if already guessed
		if(player1Map[row][column-1].equals("-1")){
			result.alreadyGuessed = true;
			return result;
		}
				
		//check to see if it hit
		for(int i = 0; i < 5; i++){
			if(!ship1[i].sunk()){
				hit = ship1[i].hit(position(row, column - 1));
				if(hit == true){
					result.marker = ship1[i].getMarker();
					result.hit = true;
					sunk = ship1[i].sunk();
					if(sunk){
						shipCount1--;
						result.sunk = true;
						result.sunkShip = ship1[i];
					}
					break;
				}
			}
		}
				
		//setting index to "guessed" flag
		player1Map[row][column-1] = "-1";
				
		//if all ships are gone, you win!
		if(shipCount1 <= 0){
			player2Won = true;
		}
				
		result.row = row;
		result.column = column;
		return result;
	}
	
//	public File getPlayerMap(String playerName){
//		
//		//turn the ships into JSON objects and store in JSON file
//		JSONObject obj = new JSONObject();
//		JSONObject shipObj = new JSONObject();
//		
//		int destroyerCount = 0;
//		
//		for(int i = 0; i < ship1.length; i++){
//			String[] coords = ship1[i].getCoordinates();
//			String label = null;
//			
//			if(coords.length == 2){
//				destroyerCount++;
//				label = "Destroyer" + destroyerCount; 
//			} else if(coords.length == 3){
//				label = "Cruiser";
//			} else if(coords.length == 4){
//				label = "Battleship";
//			} else if(coords.length == 5){
//				label = "AircraftCarrier";
//			}
//			
//			shipObj.put(label, coords);
//		}
//		
//		//constructs the encompassing JSON obj
//		obj.put(playerName, shipObj);
//	 
//		try {
//	 
//			FileWriter file = new FileWriter("assets/map.json");
//			file.write(obj.toString());
//			file.flush();
//			file.close();
//	 
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	 
//		System.out.print(obj);
//		
//		File mapFile = new File("assets/map.json");
//		return mapFile;
//	}
	
	public void loadFile(File mapFile, String name){	
		
		//setting up temp variables
		String[][] map;
		String[][] original;
		int playerNum = 0;
		
		//storing the players data
		if(player1Name == null){
			player1Name = name;
			map = player1Map;
			original = player1Original;
			playerNum = 1;
		} else {
			player2Name = name;
			map = player2Map;
			original = player2Original;
			playerNum = 2;
		}

		/*********** Parsing the JSON file ***********/
		JSONParser parser = new JSONParser();
		Vector<String[]> allShips = new Vector<String[]>();
		 
		try {	
			//reading in the JSON file
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(mapFile));
			JSONObject playerShips = (JSONObject) jsonObject.get(name);
			
			//storing ship coordinates in JSONArrays
			JSONArray destr1Array = (JSONArray)playerShips.get("Destroyer1");
			JSONArray destr2Array = (JSONArray)playerShips.get("Destroyer2");
			JSONArray cruisArray = (JSONArray)playerShips.get("Cruiser");
			JSONArray battleArray = (JSONArray)playerShips.get("Battleship");
			JSONArray aircraftArray = (JSONArray)playerShips.get("AircraftCarrier");
			
			//Store the destroyer 1 coordinates
			Iterator<String> iterator = destr1Array.iterator();
			String[] array = new String[2];
			int indexCount = 0;
            while (iterator.hasNext()) {
            	array[indexCount] = iterator.next();
            	indexCount++;
            }
            allShips.add(array);
            
            //Store the destroyer 2 coordinates
            iterator = destr2Array.iterator();
            array = new String[2];
            indexCount = 0;
            while(iterator.hasNext()) {
            	array[indexCount] = iterator.next();
            	indexCount++;
            }
            allShips.add(array);
            
            //Store the cruiser coordinates
            iterator = cruisArray.iterator();
            array = new String[3];
            indexCount = 0;
            while(iterator.hasNext()) {
            	array[indexCount] = iterator.next();
            	indexCount++;
            }
            allShips.add(array);
            
            //Store the battleship coordinates
            iterator = battleArray.iterator();
            array = new String[4];
            indexCount = 0;
            while(iterator.hasNext()) {
            	array[indexCount] = iterator.next();
            	indexCount++;
            }
            allShips.add(array);
            
            //Store the Aircraft Carrier coordinates
            iterator = aircraftArray.iterator();
            array = new String[5];
            indexCount = 0;
            while(iterator.hasNext()) {
            	array[indexCount] = iterator.next();
            	indexCount++;
            }
            allShips.add(array);
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		
		for(String[] coords : allShips){
		
			int size = coords.length;
			String marker = null;
				
			if(size == 2){
				marker = "D";
			} else if (size == 3){
				marker = "C";
			} else if (size == 4){
				marker = "B";
			} else if (size == 5){
				marker = "A";
			}
			
			for(int i = 0; i < coords.length; i++){
				String letter = coords[i].substring(0,1);
				int row = row(letter);
				int column = Integer.parseInt(coords[i].substring(1)) - 1;
				
				map[row][column] = marker;
				original[row][column] = marker;
		
			}
			
			if(playerNum == 1){
				if(shipCount1 > 5){
					System.out.println("ShipCount: " + (shipCount1 + 1));
					return;
				} else if(marker.equals("A")){
					numAir1++;
					ship1[shipCount1] = new AirCarrier(coords);
				} else if(marker.equals("B")){
					numBat1++;
					ship1[shipCount1] = new Battleship(coords);
				} else if(marker.equals("C")){
					numCruis1++;
					ship1[shipCount1] = new Cruiser(coords);
				} else if(marker.equals("D")){
					numDestr1++;
					ship1[shipCount1] = new Destroyer(coords);
				}			
				shipCount1++;
			} else if (playerNum == 2){
				if(shipCount2 > 5){
					System.out.println("ShipCount: " + (shipCount2 + 1));
					return;
				} else if(marker.equals("A")){
					numAir2++;
					ship2[shipCount2] = new AirCarrier(coords);
				} else if(marker.equals("B")){
					numBat2++;
					ship2[shipCount2] = new Battleship(coords);
				} else if(marker.equals("C")){
					numCruis2++;
					ship2[shipCount2] = new Cruiser(coords);
				} else if(marker.equals("D")){
					numDestr2++;
					ship2[shipCount2] = new Destroyer(coords);
				}			
				shipCount2++;
			}
		}
		
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
					player1Map[i][j] = marker;
					player1Original[i][j] = marker;
					array[indexCount] = position(i, j);
					indexCount++;
				}	
			}
		} catch(IndexOutOfBoundsException e){
			System.out.println(e.getMessage());
		}
		
		if(shipCount1 > 5){
			System.out.println("ShipCount: " + (shipCount1 + 1));
			return;
		} else if(marker.equals("A")){
			numAir1++;
			ship1[shipCount1] = new AirCarrier(array);
		} else if(marker.equals("B")){
			numBat1++;
			ship1[shipCount1] = new Battleship(array);
		} else if(marker.equals("C")){
			numCruis1++;
			ship1[shipCount1] = new Cruiser(array);
		} else if(marker.equals("D")){
			numDestr1++;
			ship1[shipCount1] = new Destroyer(array);
		}
		
		shipCount1++;
		
	}
	
	public boolean fullShips(){
		if(shipCount1 >= 5){
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
					if(!player1Map[i][j].equals("X")){
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
		
		if(numDestr1 < 2){
			shipList.addElement("Destroyer");
		}
		if(numCruis1 < 1){
			shipList.addElement("Cruiser");
		}
		if(numBat1 < 1){
			shipList.addElement("Battleship");
		}
		if(numAir1 < 1){
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