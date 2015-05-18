package chen681_CSCI201_Assignment5;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BattleshipServer extends Thread{
	
	private Vector<ServerThread> playerThreads = new Vector<ServerThread>();
	ServerSocket ss;
	int port;
	int clientCount;
	Object syncObj;
	ServerActions sa;
	public int fileCount;
	//Vector<String> chatArray;
	ArrayList<String> chatArray;
	
	public BattleshipServer(int port) {
		ss = null;
		this.port = port;
		clientCount = 0;
		syncObj = new Object();
		sa = new ServerActions();
		fileCount = 0;
		//chatArray = new Vector<String>();
		chatArray = new ArrayList<String>();
		chatArray.add("event:Round 1");
	}
	
	public void run(){
		try {
			System.out.println("Starting Battleship Server");
			ss = new ServerSocket(port);
			while (true) {
				//this only allows 2 players to connect at a time
				if(clientCount == 2){
					break;
				}
				System.out.println("Waiting for client to connect...");
				Socket s = ss.accept();
				System.out.println("Client " + s.getInetAddress() + ":" + s.getPort() + " connected");
				ServerThread st = new ServerThread(s, sa, this);
				playerThreads.add(st);
				st.start();
				clientCount++;
			}
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException ioe) {
					System.out.println("IOE closing ServerSocket: " + ioe.getMessage());
				}
			}
		}
		
		//never allow thread to terminate
		synchronized(syncObj){
			try {
				syncObj.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public int getClientSize(){
		return playerThreads.size();
	}
	
	public void removeChatThread(ServerThread st) {
		playerThreads.remove(st);
	}
	
	//sends message to all connections
	public void notifyClients(ServerThread st, String str) {
		for (ServerThread thread : playerThreads) {
			thread.sendMessage(str);
		}
	}
	
	//sends message to the 'other' connection
	public void notifyOpponent(ServerThread st, String str){
		for (ServerThread thread : playerThreads) {
			if (!st.equals(thread)) {
				thread.sendMessage(str);
			}
		}
	}
	
	public void notifySelf(ServerThread st, String str){
		for (ServerThread thread : playerThreads) {
			if (st.equals(thread)) {
				thread.sendMessage(str);
			}
		} 
	}
	
	//send chatArray to all connections
	synchronized public void notifyChat(){
		for(ServerThread thread: playerThreads){
			thread.sendChat(chatArray);
		}
	}
	
	//add string to chatArray
	synchronized public void addToChat(String str){
		chatArray.add(str);
	}
	
	//send guess to all connections
	public void notifyGuess(GuessObject guess){
		for(ServerThread thread : playerThreads) {
			thread.sendGuess(guess);
		}
	}

}

class ServerThread extends Thread {
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private BattleshipServer bs;
	private Socket s;
	private ServerActions sa;
	private String playerName;
	
	public ServerThread(Socket s, ServerActions sa, BattleshipServer bs) {
		this.bs = bs;
		this.s = s;
		this.sa = sa;
		this.playerName = null;
		
		try {
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException ioe) {
			System.out.println("IOE in ServersThread constructor: " + ioe.getMessage());
		}
	}

	public void sendMessage(String str) {
		
		try {
			oos.writeObject(str);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPlayerName(){
		return this.playerName;
	}
	
	public void sendGuess(GuessObject guess){
		try {
			oos.writeObject(guess);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendChat(ArrayList<String> strArray){
		try {
			ArrayList<String> newArr = new ArrayList<String>(strArray);
			oos.writeObject(newArr);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			
			//lets the clients know that the two clients have connected
			if(bs.getClientSize() == 2){
				bs.notifyClients(this, "ready");
			}
			
			//store the player's name
			playerName = (String) ois.readObject();
			bs.notifyOpponent(this, playerName);
			
			while (true) {
				String line = (String) ois.readObject();
				
				//this object can be used to split up the string by a delimeter
				StringTokenizer tok = new StringTokenizer(line, ":");
				String command = tok.nextToken();
				if(command.equals("file")){
					try {
						File file = (File) ois.readObject();
						sa.loadFile(file, tok.nextToken());
						bs.fileCount++;
						
						if(bs.fileCount == 2){
							bs.notifyClients(this, "start");
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if (command.equals("guess")){
					int row = Integer.parseInt(tok.nextToken());
					int col = Integer.parseInt(tok.nextToken());
					
					bs.notifyClients(this, "guess");
					GuessObject guess = sa.playerGuess(playerName, row, col);
					bs.notifyGuess(guess);
					
					if(guess.won == true){
						bs.notifySelf(this, "won");
						bs.notifyOpponent(this, "lose");
					}

				} else if (command.equals("chat") || command.equals("event")) {
					bs.addToChat(line);
					bs.notifyClients(this, "chat");
					bs.notifyChat();
				} else if (command.equals("rematch")){
					bs.fileCount = 0;
					sa.reset();
					String status = tok.nextToken();
					if(status.equals("true")){
						bs.notifyOpponent(this, "true");
					} else if (status.equals("false")){
						bs.notifyOpponent(this, "false");
					}
				} else if (line.equals("closing")){
					bs.notifyOpponent(this, "closing");
				}
				
			}
		} catch (IOException ioe) {
			bs.removeChatThread(this);
			System.out.println(s.getInetAddress() + ":" + s.getPort() + " disconnected.");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
}
