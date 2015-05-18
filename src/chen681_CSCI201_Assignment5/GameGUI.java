package chen681_CSCI201_Assignment5;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;

public class GameGUI extends JFrame{

	private static final long serialVersionUID = 1L;
	
	//static variables
	private static final int ROW = 11;
	private static final int COLUMN = 11;
	private static final int SPLASHLENGTH = 100;
	private static final int SPLASHDELAY = 0;
	private static final int EXPLODELENGTH = 350;
	private static final int EXPLODEDELAY = 0;
	private static final int SINKLENGTH = 250;
	private static final int SINKDELAY = 1750;
	private static final int WATERLENGTH = 500;
	private static final int REPAINTLENGTH = 100;
	private static final int CANNONDELAY = 1700;
	
	//member variables
	JPanel outerContainer;
	JPanel waitingScreen;
	JPanel playingScreen;
	JPanel gameBoard;
	JPanel computerPanel;
	ImageLabel[][] computerGrid;
	JLabel computerLabel;
	JPanel playerPanel;
	ImageLabel[][] playerGrid;
	JLabel playerLabel;
	JPanel bottomBar;
	JLabel log;
	JButton fileButton;
	JButton startButton;
	JLabel filename;
	GameActions game;
	File file;
	boolean editMode;
	boolean canPlace;
	JMenuBar jmb;
	JMenu info;
	JMenuItem about;
	JMenuItem howTo;
	JLabel timerLabel;  
	int countDown;
	boolean playerGuessed;
	boolean compGuessed;
	int roundCounter;
	JTextArea gameLog;
	JScrollPane logScrollPane;
	JPanel logPanel;
	JPanel cards;
	boolean animHasEnded;
	boolean gameEnded;
	
	//timers
	Timer timer;
	Timer animationTimer;
	Timer waterTimer;
	Timer soundTimer;
	Timer winTimer;
	Timer waitTimer;
	
	//JDialog Network Menu
	BattleshipServer gameServer;
	JDialog networkMenu;
	String clientIpAddress;
	String hostIpAddress;
	String playerName;
	int portNumber;
	String onlineMapName;
	Socket s;
	JLabel waitLabel;
	int waitingCount;
	//BufferedReader br;
	//PrintWriter pw;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	boolean isReady;
	JCheckBox mapCheckBox;
	JTextField nameTextField;
	String opponentName;
	boolean playerCanMove;
	boolean opponentCanMove;
	GuessObject playerGuess;
	GuessObject opponentGuess;
	//Lock guessLock;
	boolean notStart;
	boolean notReady;
	JLabel yourIpLabel;
	ArrayList<String> logArray;
	JCheckBox hostCheckBox;
	
	//chat
	JTextField chatMsg;
	JButton msgButton;
	JCheckBox chatCheckBox;
	JCheckBox eventCheckBox;
	JPanel bottomCard;
	JPanel chatCard;
	JTextArea chatLog;
	JScrollPane chatScroll;
	
	//resources
	File qImg;
	File water1Img;
	File water2Img;
	File explImages[];
	File splashImages[];
	File splashSound;
	File cannonSound;
	File explodeSound;
	File sinkSound;
	
	public GameGUI (){
		super("Battleship"); 
		//JFrame setup
		setLocation(350, 50); 
		setSize(650, 480);
		setResizable(false);
 		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
 		
 		//Handle the magic
 		initialize();
 		createGUI();
 		eventHandlers();
 		
 		//set visible
 		setVisible(true);
	}
	
	private void initialize(){
		outerContainer = new JPanel();
		waitingScreen = new JPanel();
		playingScreen = new JPanel();
		gameBoard = new JPanel();
		playerLabel = new JLabel("PLAYER");
 		computerLabel = new JLabel("COMPUTER");
 		playerPanel = new JPanel();
 		playerGrid = new ImageLabel[ROW][COLUMN];
 		computerPanel = new JPanel();
 		computerGrid = new ImageLabel[ROW][COLUMN];
 		bottomBar = new JPanel();
 		log = new JLabel("Log: You are in edit mode, click to place your ships.");
 		fileButton = new JButton("Select File");
 		startButton = new JButton("Start");
 		filename = new JLabel("File: ");
 		game = new GameActions();
 		file = null;
 		editMode = true;
 		canPlace = true;
 		jmb = new JMenuBar();
 		info = new JMenu("Info");
 		about = new JMenuItem("About");
 		howTo = new JMenuItem("How To");
 		timerLabel = new JLabel("Time - 0:15"); 
 		countDown = 15;
 		playerGuessed = false;
 		compGuessed = false;
 		roundCounter = 1;
 		gameLog = new JTextArea();
 		logScrollPane = new JScrollPane(gameLog);
 		logPanel = new JPanel();
 		cards = new JPanel();
 		animHasEnded = false;
 		gameEnded = false;
 		
 		//timers
 		timer = new Timer(); 
 		animationTimer = new Timer();
 		waterTimer = new Timer();
 		soundTimer = new Timer();
 		winTimer = new Timer();
 		waitTimer = new Timer();
 		
 		//network menu
 		networkMenu = new JDialog(this, "Battleship Menu", true);
 		clientIpAddress = null;
 		hostIpAddress = null;
 		playerName = null;
 		portNumber = 0;
 		onlineMapName = null;
 		waitLabel = new JLabel("Waiting for another player... 30 seconds until timeout.");
 		waitingCount = 30;
 		isReady = false;
 		opponentName = null;
 		playerCanMove = false;
 		opponentCanMove = false;
 		playerGuess = null;
 		opponentGuess = null;
 		//guessLock = new ReentrantLock();
 		notStart = true;
 		notReady = true;
 		
 		//chat
 		chatMsg = new JTextField(40);
 		msgButton = new JButton("Send");
 		chatCheckBox = new JCheckBox();
 		eventCheckBox = new JCheckBox();
 		bottomCard = new JPanel();
 		chatCard = new JPanel();
 		chatLog = new JTextArea();
 		chatScroll = new JScrollPane(chatLog);
 		logArray = new ArrayList<String>();
 		
 		//loading resources
 		explImages = new File[5];
 		splashImages = new File[7];
 		splashSound = new File("assets/sounds/splash.wav");
 		cannonSound = new File("assets/sounds/cannon.wav");
 		explodeSound = new File("assets/sounds/explode.wav");
 		sinkSound = new File("assets/sounds/sinking.wav");
		water1Img = new File("assets/water1.png");
		water2Img = new File("assets/water2.png");
		qImg = new File("assets/Q.png");
	}
	
	private void createGUI(){
		
		//setting up the two screens
 		waitingScreen.add(waitLabel);
 		playingScreen.setLayout(new BorderLayout());
 		
 		//setting up card layout
 		outerContainer.setLayout(new CardLayout());
 		outerContainer.add(waitingScreen, "waiting");
 		outerContainer.add(playingScreen, "playing");
 		
		//setting up the connection configurations
 		createNetworkMenu();
		
		//setting up the game board
 		gameBoard.setLayout(new GridLayout(1, 2));
 		
 		//setting up the explosion images
 		explImages[0] = new File("assets/explosion/expl1.png");
 		explImages[1] = new File("assets/explosion/expl2.png");
 		explImages[2] = new File("assets/explosion/expl3.png");
 		explImages[3] = new File("assets/explosion/expl4.png");
 		explImages[4] = new File("assets/explosion/expl5.png");
 		
 		//setting up the splash images
 		splashImages[0] = new File("assets/splash/splash1.png");
 		splashImages[1] = new File("assets/splash/splash2.png");
 		splashImages[2] = new File("assets/splash/splash3.png");
 		splashImages[3] = new File("assets/splash/splash4.png");
 		splashImages[4] = new File("assets/splash/splash5.png");
 		splashImages[5] = new File("assets/splash/splash6.png");
 		splashImages[6] = new File("assets/splash/splash7.png");
 		
 		//setting up the menu
 		info.add(about);
 		info.add(howTo);
 		jmb.add(info);
 		setJMenuBar(jmb);
 		about.setMnemonic('A');
 		howTo.setMnemonic('H');
 		info.setMnemonic('I');
 		about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
 		howTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
 		
 		//setting up the board labels
 		JPanel topLabels = new JPanel();
 		topLabels.setLayout(new GridLayout(1, 3));		
 		JPanel jp1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
 		jp1.add(playerLabel);
 		JPanel jp2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
 		jp2.add(computerLabel);
 		JPanel jp3 = new JPanel();
 		jp3.add(timerLabel);
 		topLabels.add(jp1);
 		topLabels.add(jp3);
 		topLabels.add(jp2);
 		playingScreen.add(topLabels, BorderLayout.NORTH);
 		
 		//setting up the bottom bar
 		bottomBar.setLayout(new BoxLayout(bottomBar, BoxLayout.X_AXIS));
 		bottomBar.add(Box.createHorizontalStrut(10));
 		bottomBar.add(log);
 		bottomBar.add(Box.createGlue());
 		bottomBar.add(fileButton);
 		bottomBar.add(Box.createHorizontalStrut(10));
 		bottomBar.add(filename);
 		bottomBar.add(Box.createGlue());
 		bottomBar.add(startButton);
 		bottomBar.add(Box.createRigidArea(new Dimension(10, 35)));
 		bottomBar.setBorder(BorderFactory.createTitledBorder("Edit Mode"));
 		startButton.setEnabled(false);
 		
 		//hiding unnecessary GUI elements
 		fileButton.setVisible(false);
 		filename.setVisible(false);
 		
 		//setting up the log Panel
		gameLog.setLineWrap(true);
		gameLog.setEditable(false);
		DefaultCaret caret = (DefaultCaret)gameLog.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		gameLog.append("Round " + roundCounter);
		logPanel.setLayout(new BorderLayout());
		logPanel.add(logScrollPane, BorderLayout.CENTER);
		logPanel.setBorder(BorderFactory.createTitledBorder("Game Log"));
		
		//setting the round events
		//pw.println("event:Round " + roundCounter); //TODO
//		try {
//			oos.writeObject("event:Round " + roundCounter);
//			oos.flush();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		} //TODO
 		
 		//adding the bottom panels to the cards panel
		cards.setLayout(new CardLayout());
		cards.add(bottomBar, "first");
		cards.add(logPanel, "second");
		CardLayout cl = (CardLayout)(cards.getLayout());
	    cl.show(cards, "first");
 		playingScreen.add(cards, BorderLayout.SOUTH);
 		
 		//setting up chat log
 		JPanel chatContainer = new JPanel();
 		chatLog.setLineWrap(true);
		chatLog.setEditable(false);
		DefaultCaret caret2 = (DefaultCaret)chatLog.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatContainer.setLayout(new BorderLayout());
		chatContainer.add(chatScroll, BorderLayout.CENTER);
		chatContainer.setBorder(BorderFactory.createTitledBorder("Game Log"));
 		chatContainer.setPreferredSize( new Dimension( 550, 100 ) );
 		
 		//setting up the bottom chat panel
 		JLabel chatPlayerLabel = new JLabel(nameTextField.getText() + ":");
 		JLabel filterLabel = new JLabel("Filter:");
 		JLabel chatLabel = new JLabel("Chat");
 		JLabel eventLabel = new JLabel("Event");
 		JPanel filterPanel = new JPanel();
 		filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
 		JPanel chatPanel = new JPanel();
 		JPanel eventPanel = new JPanel();
 		chatPanel.add(chatCheckBox);
 		chatPanel.add(chatLabel);
 		eventPanel.add(eventCheckBox);
 		eventPanel.add(eventLabel);
 		filterPanel.add(filterLabel);
 		filterPanel.add(chatPanel);
 		filterPanel.add(eventPanel);
 		JPanel rowPanel = new JPanel();
 		rowPanel.add(chatContainer);
 		rowPanel.add(filterPanel);
 		JPanel msgPanel = new JPanel();
 		msgPanel.add(chatPlayerLabel);
 		msgPanel.add(chatMsg);
 		msgPanel.add(msgButton);
 		chatCard.setLayout(new BoxLayout(chatCard, BoxLayout.Y_AXIS));
 		chatCard.add(rowPanel);
 		chatCard.add(msgPanel);
 		
 		cards.add(chatCard, "third");
 		
 		/******** setting up player board ********/
 		//setting up the center panel
 		JPanel playerOuterPanel = new JPanel();
 		playerOuterPanel.setLayout(new BoxLayout(playerOuterPanel, BoxLayout.X_AXIS));
 		JPanel playerInnerPanel = new JPanel();
 		playerInnerPanel.setLayout(new BoxLayout(playerInnerPanel, BoxLayout.X_AXIS));
 		playerInnerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
 		playerInnerPanel.setBackground(Color.LIGHT_GRAY);
 		playerPanel.setLayout(new GridLayout(ROW, COLUMN));
 		
 		//setting up 2d array of labels 		
		for(int i = 0; i < (ROW); i++){
			for(int j = 0; j < COLUMN; j++){
				
				if(j == 0 || i == 10) {
					playerGrid[i][j] = new ImageLabel(true);
					playerGrid[i][j].setBackground(Color.LIGHT_GRAY);
				} else {
					playerGrid[i][j] = new ImageLabel();
				}
				
				playerGrid[i][j].saveImages(water1Img, water2Img, qImg);
				playerGrid[i][j].setIcon(new GameIcon("assets/Q.png", "?"));
				playerGrid[i][j].setName("" + i + j);
				playerGrid[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				playerPanel.add(playerGrid[i][j]);
				playerPanel.setBackground(Color.LIGHT_GRAY);
				playerGrid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
			}
		}
		
		//setting up the y axis labels
		playerGrid[0][0].setText("A");
		playerGrid[1][0].setText("B");
		playerGrid[2][0].setText("C");
		playerGrid[3][0].setText("D");
		playerGrid[4][0].setText("E");
		playerGrid[5][0].setText("F");
		playerGrid[6][0].setText("G");
		playerGrid[7][0].setText("H");
		playerGrid[8][0].setText("I");
		playerGrid[9][0].setText("J");
		playerGrid[10][0].setText("");
		
		//setting up the y axis labels
		playerGrid[0][0].setIcon(null);
		playerGrid[1][0].setIcon(null);
		playerGrid[2][0].setIcon(null);
		playerGrid[3][0].setIcon(null);
		playerGrid[4][0].setIcon(null);
		playerGrid[5][0].setIcon(null);
		playerGrid[6][0].setIcon(null);
		playerGrid[7][0].setIcon(null);
		playerGrid[8][0].setIcon(null);
		playerGrid[9][0].setIcon(null);
		playerGrid[10][0].setIcon(null);
		
		//taking off border of the y axis labels
		playerGrid[0][0].setBorder(null);
		playerGrid[1][0].setBorder(null);
		playerGrid[2][0].setBorder(null);
		playerGrid[3][0].setBorder(null);
		playerGrid[4][0].setBorder(null);
		playerGrid[5][0].setBorder(null);
		playerGrid[6][0].setBorder(null);
		playerGrid[7][0].setBorder(null);
		playerGrid[8][0].setBorder(null);
		playerGrid[9][0].setBorder(null);
		playerGrid[10][0].setBorder(null);
		
		//setting up the x axis labels
		for(int x = 1; x < 11; x++){
			playerGrid[10][x].setText(Integer.toString(x));
			playerGrid[10][x].setBorder(null);
			playerGrid[10][x].setIcon(null);
		}
		
		playerInnerPanel.add(playerPanel);
		playerInnerPanel.add(Box.createHorizontalStrut(10));
		
		playerOuterPanel.add(Box.createHorizontalStrut(20));
		playerOuterPanel.add(playerInnerPanel);
		playerOuterPanel.add(Box.createHorizontalStrut(10));
 		
 		/******** setting up computer board ********/
 		//setting up the center panel
		JPanel compOuterPanel = new JPanel();
 		compOuterPanel.setLayout(new BoxLayout(compOuterPanel, BoxLayout.X_AXIS));
 		JPanel compInnerPanel = new JPanel();
 		compInnerPanel.setLayout(new BoxLayout(compInnerPanel, BoxLayout.X_AXIS));
 		compInnerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
 		compInnerPanel.setBackground(Color.LIGHT_GRAY);
 		computerPanel.setLayout(new GridLayout(ROW, COLUMN));

 		//setting up 2d array of labels
		for(int i = 0; i < (ROW); i++){
			for(int j = 0; j < COLUMN; j++){
				
				if(j == 0 || i == 10) {
					computerGrid[i][j] = new ImageLabel(true);
					computerGrid[i][j].setBackground(Color.LIGHT_GRAY);
				} else {
					computerGrid[i][j] = new ImageLabel();
				}
				
				computerGrid[i][j].saveImages(water1Img, water2Img, qImg);
				computerGrid[i][j].setIcon(new GameIcon("assets/Q.png", "?"));
				computerGrid[i][j].setName("" + i + j);
				computerPanel.add(computerGrid[i][j]);
				computerGrid[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				computerPanel.setBackground(Color.LIGHT_GRAY);
				computerGrid[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
			}
		}
		
		//setting up the y axis labels
		computerGrid[0][0].setText("A");
		computerGrid[1][0].setText("B");
		computerGrid[2][0].setText("C");
		computerGrid[3][0].setText("D");
		computerGrid[4][0].setText("E");
		computerGrid[5][0].setText("F");
		computerGrid[6][0].setText("G");
		computerGrid[7][0].setText("H");
		computerGrid[8][0].setText("I");
		computerGrid[9][0].setText("J");
		computerGrid[10][0].setText("");		
		
		//setting up the y axis labels
		computerGrid[0][0].setIcon(null);
		computerGrid[1][0].setIcon(null);
		computerGrid[2][0].setIcon(null);
		computerGrid[3][0].setIcon(null);
		computerGrid[4][0].setIcon(null);
		computerGrid[5][0].setIcon(null);
		computerGrid[6][0].setIcon(null);
		computerGrid[7][0].setIcon(null);
		computerGrid[8][0].setIcon(null);
		computerGrid[9][0].setIcon(null);
		computerGrid[10][0].setIcon(null);
		
		//removing border from the y axis labels
		computerGrid[0][0].setBorder(null);
		computerGrid[1][0].setBorder(null);
		computerGrid[2][0].setBorder(null);
		computerGrid[3][0].setBorder(null);
		computerGrid[4][0].setBorder(null);
		computerGrid[5][0].setBorder(null);
		computerGrid[6][0].setBorder(null);
		computerGrid[7][0].setBorder(null);
		computerGrid[8][0].setBorder(null);
		computerGrid[9][0].setBorder(null);
		computerGrid[10][0].setBorder(null);
		
		//setting up the x axis labels
		for(int x = 1; x < COLUMN; x++){
			computerGrid[10][x].setText(Integer.toString(x));
			computerGrid[10][x].setBorder(null);
			computerGrid[10][x].setIcon(null);
		}
		
		compInnerPanel.add(computerPanel);
		compInnerPanel.add(Box.createHorizontalStrut(10));
		
		compOuterPanel.add(Box.createHorizontalStrut(10));
		compOuterPanel.add(compInnerPanel);
		compOuterPanel.add(Box.createHorizontalStrut(20));
		
		//scheduling the animation threads
		waterTimer.schedule(new WaterTask(), 0, WATERLENGTH);
		waterTimer.schedule(new TimerTask(){
			public void run(){
				for(int i = 0; i < (ROW); i++){
					for(int j = 0; j < COLUMN; j++){
						playerGrid[i][j].repaint();
						computerGrid[i][j].repaint();
					}
				}
			}
		}, 0, REPAINTLENGTH);
		
		//adding the panel
		gameBoard.add(playerOuterPanel);
		gameBoard.add(compOuterPanel);
		
		//adding game board to playing screen
		playingScreen.add(gameBoard, BorderLayout.CENTER);
		
		//adding the card to the border layout
		add(outerContainer, BorderLayout.CENTER);	
//		CardLayout screenSwitch = (CardLayout)(outerContainer.getLayout());
//		if(mapCheckBox.isSelected()){
//			screenSwitch.show(outerContainer, "playing");
//		} else {
//			screenSwitch.show(outerContainer, "waiting");
//		}
		
		//TODO - still need to make this piece of code execute each
		//time the user clicks the connect button. Right now it only
		//executes once at the beginning
		if(!mapCheckBox.isSelected()){
	 		try {
	 			while(notReady){ //TODO
	 				//loop
	 				Thread.sleep(500);
	 			}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void eventHandlers(){
		
		/*********************  send msg buton  **********************/
		
		msgButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent arg0){
//				pw.println("chat:"+ playerName + ": " +chatMsg.getText());
//				pw.flush();
				
				try {
					oos.writeObject("chat:" + playerName + ": " + chatMsg.getText());
					oos.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				chatMsg.setText("");
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		});
		
		/*************************************************************/	
		about.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				JDialog aboutDialog = new JDialog(GameGUI.this);
				aboutDialog.setSize(300,300);
				aboutDialog.setLocationRelativeTo(GameGUI.this);
				aboutDialog.setModal(true);
				
				JPanel dialogPanel = new JPanel();
				dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));
				
				JLabel madeBy = new JLabel("Made by Andrew Chen");
				JLabel assign = new JLabel("CSCI201 USC: Assignment 3");
				JLabel image = new JLabel();
				madeBy.setAlignmentX(Component.CENTER_ALIGNMENT);
				assign.setAlignmentX(Component.CENTER_ALIGNMENT);
				image.setAlignmentX(Component.CENTER_ALIGNMENT);
				
				dialogPanel.add(madeBy);
				dialogPanel.add(Box.createGlue());
				dialogPanel.add(image);
				dialogPanel.add(Box.createGlue());
				dialogPanel.add(assign);
				aboutDialog.add(dialogPanel);
				
				ImageIcon imageIcon = new ImageIcon("assets/me.jpg");
         		Image img = imageIcon.getImage();
         		img = img.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
         		imageIcon.setImage(img);
         		image.setIcon(imageIcon);
				
				aboutDialog.setVisible(true);		
			}
			
		});
		
		howTo.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				//making the dialog
				JDialog howToDialog = new JDialog(GameGUI.this);
				howToDialog.setSize(300,300);
				howToDialog.setLocationRelativeTo(GameGUI.this);
				howToDialog.setLayout(new BorderLayout());
				howToDialog.setModal(true);
				
				//setting up the text area
				JTextArea textArea = new JTextArea();
				textArea.setLineWrap(true);
				textArea.setWrapStyleWord(true);
				textArea.setEditable(false);
				
				//putting text area in scroll pane
				JScrollPane jsp = new JScrollPane(textArea);
				
				//putting scroll pane in border layout
				JPanel jp = new JPanel();
				jp.setLayout(new BorderLayout());
				jp.add(jsp, BorderLayout.CENTER);
				howToDialog.add(jp);
				
				//setting the text
				try {
					BufferedReader br = new BufferedReader(new FileReader("assets/instructions.txt"));
					String line = br.readLine();
					textArea.setText("");
					while(line != null){
					  textArea.append(line + "\n");
					  line = br.readLine();
					}
					br.close();
				} catch (FileNotFoundException e1) {
					textArea.append("Battleship Instructions: \n\n" +
							"Step 1: Place your ships while in edit mode. \n\n" + 
							"Step 2: Choose a file to upload for the computer's board. \n\n" +
							"Step 3: Click the 'Start' button to begin the game. \n\n" +
							"Step 4: Click on the computer's board and try and sink all 5 ships. \n\n"+
							"Step 5: You win if you sink all of the computer's ships before it sinks yours.");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				howToDialog.setVisible(true);
				
			}
			
		});
		
		fileButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser(".");
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Battleship File", "battle");
				fileChooser.setFileFilter(filter);
				int returnVal = fileChooser.showOpenDialog(GameGUI.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
			        file = fileChooser.getSelectedFile();
			        filename.setText("File: " + file.getName());
			        game.storeBoard(file);
				}
				if(game.fullShips() && file != null){
					startButton.setEnabled(true);
					canPlace = false;
				} else if(game.fullShips()){
					canPlace = false;
				} else {
					startButton.setEnabled(false);
				}
			}	
		});
		/************************************/
		for(int i = 0; i < (ROW-1); i++){
			for(int j = 1; j < COLUMN; j++){
				playerGrid[i][j].addMouseListener(new MouseAdapter(){
		
					public void mouseClicked(MouseEvent me) {	
						if(canPlace){
							//getting the row and column of click
						    JLabel currLabel = (JLabel) me.getSource();
						    String[] labelName = currLabel.getName().split("");
						    int[] index = {Integer.parseInt(labelName[0]), Integer.parseInt(labelName[1])};
						    
						    if(currLabel.getName().length() > 2){
						    	index[1] = (Integer.parseInt(labelName[1] + labelName[2]));
						    }
						    
						    //if this index has a ship on it...
						    if(!((GameIcon) playerGrid[index[0]][index[1]].getIcon()).getMarker().equals("?")){
						    //if(!playerGrid[index[0]][index[1]].getText().equals("?")){						    	
						    	eraseGUIShip(index[0], index[1], playerGrid[index[0]][index[1]].getText());
						    	game.eraseShip(index[0], index[1]);
						    }
						    
						    //creating dialog pop up box
							JDialog shipWindow = new JDialog(GameGUI.this);
							shipWindow.setTitle("Select ship at " + game.position(index[0], index[1]-1));
							shipWindow.setSize(300,160);
							shipWindow.setLocationRelativeTo(GameGUI.this);
							//shipWindow.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
							shipWindow.setModal(true);
							
							//creating gui components
							String[] availShips = game.getPlayerShips();
							JLabel shipLabel = new JLabel("Select Ship: ");
							JComboBox<String> shipChoice = new JComboBox<String>(availShips);
							JRadioButton northButton = new JRadioButton("North");
							JRadioButton southButton = new JRadioButton("South");
							JRadioButton eastButton = new JRadioButton("East");
							JRadioButton westButton = new JRadioButton("West");
							northButton.setActionCommand( northButton.getText() );
							southButton.setActionCommand( southButton.getText() );
							eastButton.setActionCommand( eastButton.getText() );
							westButton.setActionCommand( westButton.getText() );
							JButton placeButton = new JButton("Place Ship");
							ButtonGroup bg = new ButtonGroup();
							bg.add(northButton);
							bg.add(southButton);
							bg.add(eastButton);
							bg.add(westButton);
							
							//outer Container
							JPanel outBox = new JPanel();
							outBox.setLayout(new BorderLayout());
							
							//center Container
							JPanel centerBox1 = new JPanel();
							JPanel centerBox2 = new JPanel();
							centerBox2.setLayout(new GridLayout(2,2));
							centerBox2.add(northButton);
							centerBox2.add(southButton);						
							centerBox2.add(eastButton);
							centerBox2.add(westButton);
							centerBox1.add(centerBox2);
							
							//top Container
							JPanel topBox = new JPanel();
							topBox.add(shipLabel);
							topBox.add(shipChoice);
							
							//add everything to the window
							outBox.add(topBox, BorderLayout.NORTH);
							outBox.add(centerBox1, BorderLayout.CENTER);
							outBox.add(placeButton, BorderLayout.SOUTH);
							shipWindow.add(outBox);
	
							//setting default behavior
							placeButton.setEnabled(false);
							
							//creating action listener for radio buttons
							northButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {
									if(placeValid(index[0], index[1], (String)shipChoice.getSelectedItem(), bg.getSelection().getActionCommand())){
										placeButton.setEnabled(true);
									} else {
										placeButton.setEnabled(false);
									}
								}		
							});
							southButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {
									if(placeValid(index[0], index[1], (String)shipChoice.getSelectedItem(), bg.getSelection().getActionCommand())){
										placeButton.setEnabled(true);
									} else {
										placeButton.setEnabled(false);
									}
								}		
							});
							westButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {
									if(placeValid(index[0], index[1], (String)shipChoice.getSelectedItem(), bg.getSelection().getActionCommand())){
										placeButton.setEnabled(true);
									} else {
										placeButton.setEnabled(false);
									}
								}		
							});
							eastButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {
									if(placeValid(index[0], index[1], (String)shipChoice.getSelectedItem(), bg.getSelection().getActionCommand())){
										placeButton.setEnabled(true);
									} else {
										placeButton.setEnabled(false);
									}
								}		
							});
							shipChoice.addItemListener(new ItemListener(){
								public void itemStateChanged(ItemEvent e) {
									if(bg.getSelection() != null){
										if(placeValid(index[0], index[1], (String)shipChoice.getSelectedItem(), bg.getSelection().getActionCommand())){
											placeButton.setEnabled(true);
										} else {
											placeButton.setEnabled(false);
										}
									}
								}	
							});
	
							//creating action listener for place button
							placeButton.addActionListener(new ActionListener(){
								public void actionPerformed(ActionEvent e) {		
									game.placeShip(index[0], index[1], (String)shipChoice.getSelectedItem(), bg.getSelection().getActionCommand());								
									makeGUIPlacement(index[0], index[1], (String)shipChoice.getSelectedItem(), bg.getSelection().getActionCommand());	
									
									if(game.fullShips() /*&& file != null*/){
										startButton.setEnabled(true);
										canPlace = false;
									} else if(game.fullShips()){
										canPlace = false;
									} else {
										startButton.setEnabled(false);
									}
									
									shipWindow.dispose();
								}						
							});
							
							shipWindow.setVisible(true);
						}
					}
					
				});
				
			}
		}
		/*************************************/
		
		//creating action listener for start button
		startButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
				//get the map file to send to server
				if(!mapCheckBox.isSelected()){
					File mapFile = game.getPlayerMap(nameTextField.getText());
					try {
						oos.writeObject("file:" + nameTextField.getText());
						oos.flush();
						
						oos.writeObject(mapFile);
						oos.flush();
						
						while(notStart){
							//loop
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						System.out.println("Starting game...");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				//set Edit Mode off
				editMode = false;
				
				//changing the bottom JPanel to the log JPanel
				logPanel.setPreferredSize(new Dimension(50,100));
				CardLayout cl = (CardLayout)(cards.getLayout());
			    if(mapCheckBox.isSelected()){
			    	cl.show(cards, "second");
			    } else {
			    	cl.show(cards, "third");
			    }
				
				//starting the count down timer
				resetRound();
				roundCounter = 1;
		 	    
		 	    //setting up the computer Thread
				if(mapCheckBox.isSelected()){
					ComputerThread ct = new ComputerThread();
					ct.start();
				} else {
					PlayerThread pt = new PlayerThread();
					pt.start();
				}
				
				//assigning the task to check if anyone has won
		 		winTimer.schedule(new TimerTask(){
		 			public void run(){
		 				if(animHasEnded && (game.playerWon())){
		 					winMessage();
		 				} else if (animHasEnded && game.computerWon()){
		 					loseMessage();
		 				}
		 			}
		 		}, 0, 500);
		 		
			}
		});
		
		/*************************************/
		
		for(int i = 0; i < (ROW-1); i++){
			for(int j = 1; j < COLUMN; j++){
				computerGrid[i][j].addMouseListener(new MouseAdapter(){
		
					public void mouseClicked(MouseEvent me) {	
						
						if(!editMode && !playerGuessed && !game.computerWon()){
							
							//getting the row and column of click
						    JLabel currLabel = (JLabel) me.getSource();
						    String[] labelName = currLabel.getName().split("");
						    int[] index = {Integer.parseInt(labelName[0]), Integer.parseInt(labelName[1])};
						    playerGuessed = true;
						    GuessObject result;
						    
						    if(currLabel.getName().length() > 2){
						    	index[1] = Integer.parseInt(labelName[1]+labelName[2]);
						    }
						    
						    //if this index has a ship on it...
						    if(!((GameIcon) computerGrid[index[0]][index[1]].getIcon()).getMarker().equals("?")){
						    	return;
						    }
						    
						    /**************************************************/ //TODO
						    if(!mapCheckBox.isSelected()){
							    //PLAYER'S GUESS SENT TO SERVER
							    playerCanMove = false;
//							    pw.println("guess:" + index[0] + ":" + index[1]);
//							    pw.flush();
							    
							    try {
									oos.writeObject("guess:" + index[0] + ":" + index[1]);
								    oos.flush();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							    
							    while(!playerCanMove){
							    	//loop until received guess from server
							    	try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							    }			
							    //guessLock.lock();
							    result = playerGuess;							   
							    //guessLock.unlock();
						    } else {
						    	result = game.playerGuess(index[0], index[1]);
						    }
						    
						    playSound(cannonSound);
						    String shipName = "";
						    String url = "";
						    
						    if(result.marker.equals("A")){
						    	url = "assets/A.png";
						    	shipName = "Aircraft Carrier";
						    } else if (result.marker.equals("B")){
						    	url = "assets/B.png";
						    	shipName = "Battleship";
						    } else if (result.marker.equals("C")){
						    	url = "assets/C.png";
						    	shipName = "Cruiser";
						    } else if (result.marker.equals("D")){
						    	url = "assets/D.png";
						    	shipName = "Destroyer";
						    }
						    
					    	//storing the time of the hit
						    String time = "";
						    if(countDown >= 10){
						    	time = "(0:" + countDown + ")";
						    } else if (countDown < 10){
						    	time = "(0:0" + countDown + ")";	
						    }
						    
						    if(result.hit){
						    	computerGrid[index[0]][index[1]].setIcon(new GameIcon(url, result.marker));
						    	
						    	//setting explosion animation
						    	animationTimer.schedule(new ExplTask("Player", url, index[0], index[1]), EXPLODEDELAY+CANNONDELAY, EXPLODELENGTH);
						    	soundTimer.schedule(new TimerTask(){
						    		public void run(){
						    			playSound(explodeSound);
						    		}
						    	}, EXPLODEDELAY+CANNONDELAY);
						    	
							    //logging the player's hit
						    	gameLog.append("\nPlayer hit " + game.position(index[0], index[1]-1) + " and hit a " + shipName + "! " + time);
						    	
						    	if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
						    		GameGUI.this.logEvent(playerName + " hit " + game.position(index[0], index[1]-1) + " and hit a " + shipName + "! " + time);
						    	}
						    	
						    	//logging if the player sunk a ship
						    	if(result.sunk){
						    		gameLog.append("\nPlayer sunk Computer's " + shipName + "!");
						    		String coors[] = result.sunkShip.getCoordinates();
						    		
						    		if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
							    		GameGUI.this.logEvent(playerName + " sunk Computer's " + shipName + "!");
							    	}
						    		
						    		for(int a = 0; a < coors.length; a++){
						    			String curr = coors[a];
					    				String [] parse = curr.split("");
					    				
					    				if(curr.length() > 2){
					    					parse[1] = parse[1]+parse[2];
					    				}
					    				
					    				int letter = game.row(parse[0]);
					    				int number = Integer.parseInt(parse[1]);
					    				
					    				if(a == (coors.length - 1)){
					    					animationTimer.schedule(new SplashTask("Player", url, letter, number, true), SINKDELAY+CANNONDELAY, SINKLENGTH);
					    				} else {
					    					animationTimer.schedule(new SplashTask("Player", url, letter, number, false), SINKDELAY+CANNONDELAY, SINKLENGTH);
					    				}
					    				
						    			soundTimer.schedule(new TimerTask(){
						    				public void run(){
						    					playSound(sinkSound);
						    				}
						    			}, SINKDELAY+CANNONDELAY);
						    		}
						    		
						    	}
						    	
						    } else {
						    	computerGrid[index[0]][index[1]].setIcon(new GameIcon("assets/M.png", "M"));
						    	
						    	//setting splash animation
						    	animationTimer.schedule(new SplashTask("Player", "assets/M.png", index[0], index[1], false), SPLASHDELAY+CANNONDELAY, SPLASHLENGTH);
						    	soundTimer.schedule(new TimerTask(){
						    		public void run(){
						    			playSound(splashSound);
						    		}
						    	}, SPLASHDELAY+CANNONDELAY);
						    	
						    	gameLog.append("\nPlayer hit " + game.position(index[0], index[1]-1) + " and missed! " + time);
						    	
						    	if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
						    		GameGUI.this.logEvent(playerName + " hit " + game.position(index[0], index[1]-1) + " and missed! " + time);
						    	}
						    }

						    //Indicating that the player has guessed this round
						    playerGuessed = true;
						    if(playerGuessed && compGuessed){
						    	resetRound();
						    	roundCounter++;
						    	gameLog.append("\nRound " + roundCounter);
						    	
						    	if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){ //TODO - fix since probably going to do rounds on the server
						    		GameGUI.this.logEvent("Round " + roundCounter);
						    	}
						    }
						    
						}
					}
				});
			}
		}
	}
	
	private void createNetworkMenu(){
		//setting up the network menu
 		networkMenu.setSize(300, 220);
		networkMenu.setLocation(350, 50); 
		networkMenu.setResizable(false);
		
		networkMenu.addWindowListener(new WindowAdapter() { 
 		    @Override 
 		    public void windowClosing(WindowEvent e) { 
 		      System.exit(0);
 		    }
 		});
		
		//creating all needed GUI Objects
 		JPanel outerBox = new JPanel();
 		JPanel firstRow = new JPanel();
 		JPanel secondRow = new JPanel();
 		JPanel thirdRow = new JPanel();
 		JPanel fourthRow = new JPanel();
 		JPanel fifthRow = new JPanel();
 		JPanel sixthRow = new JPanel(); 		
 		yourIpLabel = new JLabel("Your IP: ");
 		JLabel nameLabel = new JLabel("Name: ");
 		JLabel hostIpLabel = new JLabel("Enter an IP: ");
 		JLabel portLabel = new JLabel("Port: ");
 		JLabel onlineMaps = new JLabel("201Maps: ");
 		JLabel hostGameLabel = new JLabel("Host Game");
 		JLabel customPortLabel = new JLabel("Custom Port");
 		JButton connectButton = new JButton("Connect");
 		JButton refreshButton = new JButton("Refresh");
 		JTextField hostTextField = new JTextField(8);
 		JTextField portTextField = new JTextField(8);
 		JTextField mapTextField = new JTextField(8);
 		hostCheckBox = new JCheckBox();
 		JCheckBox portCheckBox = new JCheckBox();
 		nameTextField = new JTextField(10);
 		mapCheckBox = new JCheckBox();
 		
 		//setting layout
 		outerBox.setLayout(new BoxLayout(outerBox, BoxLayout.Y_AXIS)); //TODO
 		
 		//adding the GUI objects
 		firstRow.add(yourIpLabel);
 		secondRow.add(nameLabel);
 		secondRow.add(nameTextField);
 		thirdRow.add(hostCheckBox);
 		thirdRow.add(hostGameLabel);
 		thirdRow.add(hostIpLabel);
 		thirdRow.add(hostTextField);
 		fourthRow.add(portCheckBox);
 		fourthRow.add(customPortLabel);
 		fourthRow.add(portLabel);
 		fourthRow.add(portTextField);
 		fifthRow.add(mapCheckBox);
 		fifthRow.add(onlineMaps);
 		fifthRow.add(mapTextField);
 		sixthRow.add(refreshButton);
 		sixthRow.add(connectButton);		
 		outerBox.add(firstRow);
 		outerBox.add(secondRow);

 		outerBox.add(thirdRow);
 		outerBox.add(fourthRow);
 		outerBox.add(fifthRow);
 		outerBox.add(sixthRow);
 		
 		//setting place-holders and default values
 		hostTextField.setText("localhost");
 		portTextField.setText("5678");
 		yourIpLabel.setText("Your IP: " + getIP());
 		portTextField.setEnabled(false);
 		
 		//get the mapCheckBox to disable (toggle) all the other inputs
 		mapCheckBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(mapCheckBox.isSelected()){
					hostCheckBox.setEnabled(false);
					hostTextField.setEnabled(false);
					portCheckBox.setEnabled(false);
					portTextField.setEnabled(false);
					hostGameLabel.setEnabled(false);
					customPortLabel.setEnabled(false);
					portLabel.setEnabled(false);
					hostIpLabel.setEnabled(false);
				} else if (!mapCheckBox.isSelected()){
					hostCheckBox.setEnabled(true);
					hostTextField.setEnabled(true);
					portCheckBox.setEnabled(true);
					portTextField.setEnabled(true);
					hostGameLabel.setEnabled(true);
					customPortLabel.setEnabled(true);
					portLabel.setEnabled(true);
					hostIpLabel.setEnabled(true);
				}
			}		
 		});
 		
 		portCheckBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!portCheckBox.isSelected()){
					portTextField.setEnabled(false);
				} else {
					portTextField.setEnabled(true);
				}		
			}	
 		});
 		
 		hostCheckBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(hostCheckBox.isSelected()){
					hostTextField.setEnabled(false);
					hostTextField.setText("localhost");
				} else {
					hostTextField.setEnabled(true);
				}		
			}			
 		});
 		
 		chatCheckBox.addActionListener(new ActionListener(){
 			@Override
 			public void actionPerformed(ActionEvent e){
 				GameGUI.this.setChatBox();
 			}
 		});
 		
 		eventCheckBox.addActionListener(new ActionListener(){
 			@Override
 			public void actionPerformed(ActionEvent e){
 				GameGUI.this.setChatBox();
 			}
 		});
 		
 		//when connect button is clicked
 		connectButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				boolean serverConnected = true;
				playerLabel.setText(nameTextField.getText());
				playerName = nameTextField.getText();
				
				if(nameTextField.getText().equals("") || nameTextField.getText() == null){
					JOptionPane.showMessageDialog(GameGUI.this, "You must enter your name!", "Semantic Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if(hostCheckBox.isSelected() && !mapCheckBox.isSelected()){
					//create server on given port
					gameServer = new BattleshipServer(Integer.parseInt(portTextField.getText()));
					
					//check if port is already being used
					ServerSocket ss = null;
					try {
						ss = new ServerSocket(Integer.parseInt(portTextField.getText()));
					} catch (NumberFormatException e1) {
						JOptionPane.showMessageDialog(GameGUI.this, "Port can only be a number!", "Connection Error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						serverConnected = false;
					} finally {
						try {
							if(ss != null){
								ss.close();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
					//start server thread
					gameServer.start();
				} 
				
				//if the server isn't connected, have pop up error message and return
				if(!serverConnected){
					JOptionPane.showMessageDialog(GameGUI.this, "Port is already being used!", "Connection Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (!mapCheckBox.isSelected()){ //TODO
					//if host is localhost leave as string, if not local host parse int
					try {
						//creating connection variables
						s = new Socket(hostTextField.getText(), Integer.parseInt(portTextField.getText()));
						oos = new ObjectOutputStream(s.getOutputStream());
						ois = new ObjectInputStream(s.getInputStream());
						System.out.println("Battleship Client Connected.");
						
						//switch to the waiting screen
						outerContainer.getLayout();
						CardLayout screenSwitch = (CardLayout)(outerContainer.getLayout());
						screenSwitch.show(outerContainer, "waiting");
						
						//start listening for info
						ChatThread chat = new ChatThread();
						chat.start();
						InternetThread inter = new InternetThread();
						inter.start();
						
						//setting up correct GUI display
						networkMenu.setVisible(false);
						GameGUI.this.setVisible(true);
						waitTimer = new Timer();
						waitTimer.schedule(new WaitingTask(), 1000, 1000);
						
					} catch (NumberFormatException e1) {
						System.out.println("Number Format Exception");
						JOptionPane.showMessageDialog(GameGUI.this, "Port can only be a number!", "Connection Error", JOptionPane.ERROR_MESSAGE);
					} catch (UnknownHostException e1) {
						System.out.println("Unknown Host Exception");
						JOptionPane.showMessageDialog(GameGUI.this, "Connection to the host failed!", "Connection Error", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e1) {
						System.out.println("IO Exception");
						JOptionPane.showMessageDialog(GameGUI.this, "Connection to the host failed!", "Connection Error", JOptionPane.ERROR_MESSAGE);
					}
				} else if (mapCheckBox.isSelected()){
					//load given map from website
					try {
						//stores the address of the page			
						String address = "http://www-scf.usc.edu/~csci201/assignments/" + mapTextField.getText() + ".battle";
						URL url = new URL(address);
					    String webAddress = url.getHost();
					    String file = url.getFile();
					    
					    //make sure the map name is correct
					    if(!mapTextField.getText().equals("map1") && !mapTextField.getText().equals("map2") &&
					       !mapTextField.getText().equals("map3") && !mapTextField.getText().equals("map4") &&
					       !mapTextField.getText().equals("map5")){
					    	JOptionPane.showMessageDialog(GameGUI.this, "Enter an existing map name!", "Connection Error", JOptionPane.ERROR_MESSAGE);
					    	return;
					    }
					    
					    //creating networking i.o. variables
					    s = new Socket(webAddress, 80);
					    BufferedReader serverResponse = new BufferedReader(new InputStreamReader(s.getInputStream()));
					    OutputStreamWriter outWriter = new OutputStreamWriter(s.getOutputStream());
					    outWriter.write("GET " + file + " HTTP/1.0\r\n\n");
					    outWriter.flush();
					    BufferedWriter out = new BufferedWriter(new FileWriter("assets/temp.battle"));
					    
					    //reading in the data from the web page
					    boolean more = true;
					    String input;
					    int lineCount = 0;
					    
					    //iterate through all data
					    while (more) {
						    input = serverResponse.readLine();
						    if(lineCount > 9){
							    if (input == null)
							        more = false;
							    else {
							        out.write(input+"\n");
							    }
						    }
						    lineCount++;
					    }
					    
					    //close the streams
					    out.close();
					    outWriter.close();
					    serverResponse.close();
					    s.close();
					    
					    //store chosen map in game board
					    File computerFile = new File("assets/temp.battle");
					    game.storeBoard(computerFile);
					    
					    //switch screen
					    CardLayout screenSwitch = (CardLayout)(outerContainer.getLayout());
						screenSwitch.show(outerContainer, "playing");
					    
					    //switch out windows
					    networkMenu.setVisible(false);
						GameGUI.this.setVisible(true);
						
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				
				if(!mapCheckBox.isSelected()){
					GameGUI.this.addWindowListener(new WindowAdapter(){
		                public void windowClosing(WindowEvent e){
		                	
		                	try {
		                		oos.writeObject("closing");
								oos.flush();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		                    System.exit(0);
		                }
		            });
				}
				
			}			
 		});
 		
 		//when refresh button is clicked
 		refreshButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
		 		yourIpLabel.setText("Your IP: " + getIP());
			}			
 		});
 		
 		networkMenu.add(outerBox);
 		networkMenu.setVisible(true);
	}
	
	public void logEvent(String ev){
		try{
			oos.writeObject("event:" + ev);
			oos.flush();
		} catch (IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void setChatBox(){
		chatLog.setText("");
		if(chatCheckBox.isSelected() && eventCheckBox.isSelected()){
				for(int i = 0; i < logArray.size(); i++){
					int index = logArray.get(i).indexOf(":");
					String chatStr = logArray.get(i).substring(index+1);
					chatLog.append(chatStr+"\n");
				}
		} else if (chatCheckBox.isSelected() && !eventCheckBox.isSelected()){
			for(int i = 0; i < logArray.size(); i++){
				int index = logArray.get(i).indexOf(":");
				String check = logArray.get(i).substring(0, index);
				String chatStr = logArray.get(i).substring(index+1);
				if(check.equals("chat")){
					chatLog.append(chatStr+"\n");
				}
			}
		} else if (!chatCheckBox.isSelected() && eventCheckBox.isSelected()){
			for(int i = 0; i < logArray.size(); i++){
				int index = logArray.get(i).indexOf(":");
				String check = logArray.get(i).substring(0, index);
				String chatStr = logArray.get(i).substring(index+1);
				if(check.equals("event")){
					chatLog.append(chatStr+"\n");
				}
			}	
		} 
		
	}
	
	private String getIP(){
		String ip = null;
		try{
	 		URL toCheckIp = new URL("http://checkip.amazonaws.com");
	 		BufferedReader in = new BufferedReader(new InputStreamReader(toCheckIp.openStream()));
	 		ip = in.readLine();
 		} catch (MalformedURLException e) {
 			System.out.println(e.getMessage());
 		} catch (IOException e) {
 			ip = "Error";
			System.out.println(e.getMessage());
		}
		return ip;
	}
	
	private void resetGame(){
		//resetting player's board to "?"'s 		
		for(int i = 0; i < (ROW-1); i++){
			for(int j = 1; j < COLUMN; j++){
				playerGrid[i][j].setIcon(new GameIcon("assets/Q.png", "?"));
				playerGrid[i][j].reset();
				//playerGrid[i][j].setText("?");
			}
		}

 		//resetting player's board to "?"'s
		for(int i = 0; i < (ROW-1); i++){
			for(int j = 1; j < COLUMN; j++){
				computerGrid[i][j].setIcon(new GameIcon("assets/Q.png", "?"));
				computerGrid[i][j].reset();
				//computerGrid[i][j].setText("?");
			}
		}
		
		//reset GameAction variables
		game.reset();
		
		//reset class variables
		editMode = true;
		canPlace = true;
		file = null;
		filename.setText("File: ");
		startButton.setEnabled(false);
		timerLabel.setText("Time - 0:15"); 
 		countDown = 15;
 		playerGuessed = false;
 		compGuessed = false;
 		roundCounter = 1;
		gameLog.setText("Round " + roundCounter);
		animHasEnded = false;
		gameEnded = false;
		notStart = true;
 		notReady = true;
		
		//resetting the timers
		timer.cancel();
		timer.purge();
 		timer = new Timer();
 		animationTimer.cancel();
 		animationTimer.purge();
 		animationTimer = new Timer();
 		winTimer.cancel();
 		winTimer.purge();
 		winTimer = new Timer();
 		soundTimer.cancel();
 		soundTimer.purge();
 		soundTimer = new Timer();
 		
 		//changing the bottom JPanel to the edit JPanel
 		logPanel.setPreferredSize(new Dimension(50, 30));
		CardLayout cl = (CardLayout)(cards.getLayout());
	    cl.show(cards, "first");
	}
	
	private void eraseGUIShip(int row, int column, String marker){
		//revert the ship's markers back to "?'s"
		String [] index = game.getShipIndices(row, column);
		for(int i = 0; i < index.length; i++){
			String curr = index[i];
			String [] parse = curr.split("");
			
			if(curr.length() > 2){
				parse[1] = parse[1]+parse[2];
			}
			
			int letter = game.row(parse[0]);
			int number = Integer.parseInt(parse[1]);		
			playerGrid[letter][number].setIcon(new GameIcon("assets/Q.png", "?"));
			playerGrid[letter][number].reset();
		}
	}
	
	private void makeGUIPlacement(int row, int column, String shipName, String direction){
		int rowStart = row;
		int colStart = column;
		int rowStop = row+1;
		int colStop = column+1;
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
			colStart = column;
			colStop = column+size;
		} else if (direction.equals("West")){
			colStart = column-size+1;
			colStop = column+1;
		}
		
		String url = null;
		
		if(size == 2){
			marker = "D";
			url = "assets/D.png";
		} else if (size == 3){
			marker = "C";
			url = "assets/C.png";
		} else if (size == 4){
			marker = "B";
			url = "assets/B.png";
		} else if (size == 5){
			marker = "A";
			url = "assets/A.png";
		}
		
		try{
			for(int i = rowStart; i < rowStop; i++){
				for(int j = colStart; j < colStop; j++){
					//playerGrid[i][j].setText(marker);
					playerGrid[i][j].setIcon(new GameIcon(url, marker));
					playerGrid[i][j].setMarker(url);
				}	
			}
		} catch(IndexOutOfBoundsException e){
			System.out.println(e.getMessage());
		}
	}
	
	private boolean placeValid(int row, int column, String shipName, String orientation){
		boolean isGood = false;
		int shipSize = 0;
		
		if(shipName.equals("Destroyer")){
			shipSize = 2;
		} else if (shipName.equals("Cruiser")){
			shipSize = 3;
		} else if (shipName.equals("Battleship")){
			shipSize = 4;
		} else if (shipName.equals("Aircraft Carrier")){
			shipSize = 5;
		}
		
		isGood = game.isValid(row, column, shipSize, orientation);
		
		return isGood;
	}
	
	public void playerHit(int row, int column, String marker){
		String url = "";
		
		if(marker.equals("A")){
			url = "asset/A.png";
		} else if (marker.equals("B")){
			url = "asset/B.png";
		} else if (marker.equals("C")){
			url = "asset/C.png";
		} else if (marker.equals("D")){
			url = "asset/D.png";
		}
		
		//computerGrid[row][column].setText(marker);
		computerGrid[row][column].setIcon(new GameIcon(url, marker));
	}
	
	public void playerMiss(int row, int column){
		//computerGrid[row][column].setText("MISS!");
		computerGrid[row][column].setIcon(new GameIcon("asset/M.png", "M"));
	} 
	
	public void computerHit(int row, int column){
		//playerGrid[row][column].setText("HIT!");
		playerGrid[row][column].setIcon(new GameIcon("asset/X.png", "X"));
		playerGrid[row][column].setMarker("asset/X.png");
	}
	
	public void computerMiss(int row, int column){
		//playerGrid[row][column].setText("MISS!");
		playerGrid[row][column].setIcon(new GameIcon("asset/M.png", "M"));
		playerGrid[row][column].setMarker("asset/M.png");
	}
	
	private void resetTimer(){
		//kill the current task thread
     	timer.cancel();
     	timer.purge();      	
     	//reset the countDown Timer
     	countDown = 15;
     	//create a new Count Down Task
     	timerLabel.setText("Time - 0:15");
     	timer = new Timer();
     	timer.scheduleAtFixedRate(new CountDownTimer(), 1000, 1000);
	}
	
	private void resetRound(){
		resetTimer();
		playerGuessed = false;
		compGuessed = false;
		playerCanMove = false;
		opponentCanMove = false;
		playerGuess = null;
		opponentGuess = null;
	}
	
	private void playSound(File soundFile){
		try {			
			Clip clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
		    clip.open(ais);
		    clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		}
	}
	
	private void winMessage(){
		gameLog.append("\nPlayer won the game!");
		
		if(!mapCheckBox.isSelected()){
    		GameGUI.this.logEvent(playerName + " won the game!");
    	}
		
	    gameEnded = true;
	    
	    if(mapCheckBox.isSelected()){
		    //open pop up dialog
	    	JOptionPane.showMessageDialog(GameGUI.this,
		    	"Congratulations, you won!",
		    	"The game has ended!",
		    	JOptionPane.INFORMATION_MESSAGE);
		    
		    //reset everything
		    resetGame();
		    resetNetworkMenu();
	    } else {    
		    int result = JOptionPane.showConfirmDialog(GameGUI.this, 
		    	"Congratulations, you won! Would you like a rematch?", "The game has ended!", JOptionPane.YES_NO_OPTION);
		    if(result == JOptionPane.YES_OPTION) {
		    	resetGame();
		    	try {
					oos.writeObject("rematch:true");
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    } else {
		    	try {
		    		oos.writeObject("rematch:false");
		    		oos.flush();
		    	} catch (IOException e){
		    		e.printStackTrace();
		    	}
		    	resetGame();
		    	resetNetworkMenu();
		    }
	    }
	}
	
	private void loseMessage(){
		gameLog.append("\nComputer won the game!");
		
		if(!mapCheckBox.isSelected()){
    		GameGUI.this.logEvent(opponentName + " won the game!");
    	}
		
	    gameEnded = true;
	    
	    if(mapCheckBox.isSelected()){
		    JOptionPane.showMessageDialog(GameGUI.this,
		    	"Sorry, you lost!",
		    	"The game has ended!",
		    	JOptionPane.INFORMATION_MESSAGE);
		    	
		    //reset the board
		    resetGame();
		    resetNetworkMenu();
	    } else {
	    	int result = JOptionPane.showConfirmDialog(GameGUI.this, 
			    "Sorry, you lost! Would you like a rematch?", "The game has ended!", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION) {
				try {
					oos.writeObject("rematch:true");
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			    resetGame();
			} else {
				try {
					oos.writeObject("rematch:false");
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			    resetGame();
			    resetNetworkMenu();
			}
	    }
	}
	
	private void resetNetworkMenu(){
 		waitLabel.setText("Waiting for another player... 30 seconds until timeout.");
 		waitingCount = 30;
 		isReady = false;
 		gameServer = null;
 		s = null;
 		GameGUI.this.setVisible(false);
 		networkMenu.setVisible(true);
	}
	
	/********************************************************/
	/******************  Inner Classes  *********************/
	/********************************************************/
	
	/***************************************/
	//       WaitingTimer Class  
	/***************************************/
	
	class WaitingTask extends TimerTask {
		
		public void run(){
			
			if(isReady){ //TODO
				CardLayout screenSwitch = (CardLayout)(outerContainer.getLayout());
			    screenSwitch.show(outerContainer, "playing");
			    waitTimer.cancel();
			    waitTimer.purge();
			} if(waitingCount <= 0){
				waitTimer.cancel();
				waitTimer.purge();
				resetNetworkMenu();
			} else if (waitingCount > 0){
				waitingCount--;				
				waitLabel.setText("Waiting for another player... " + waitingCount + " seconds until timeout.");
			}
		}
		
	}
	
	/***************************************/
	//       CountDownTimer Class  
	/***************************************/
	
	class CountDownTimer extends TimerTask {

 	    public void run() {
 	        
 	    	if(!game.playerWon() && !game.computerWon()){
 	    		
	 	    	if (countDown <= 0){
		 	    	 timer.cancel();
		 	         timer.purge();        	
		 	    } else if (countDown > 0){
		 	        countDown--;
		 	        
		 	        //log time warning
		 	        if(countDown == 3){
		 	        	gameLog.append("\nWarning - 3 seconds left in the round!");
		 	        	if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
			 	        	GameGUI.this.logEvent("Warning - 3 seconds left in the round!");
			 	       	}
		 	        }	 	        
		 	        
		 	        //update timer or reset round
		 	        if(countDown <= 0) {
		 	        	resetRound();
		 	        	roundCounter++;
		 	        	gameLog.append("\nRound " + roundCounter);
		 	        	
			 	        if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
			 	        	GameGUI.this.logEvent("Round " + roundCounter);
			 	       	}
		 	        	
		 	        } else if (countDown >= 10){
		 	        	timerLabel.setText("Time - 0:" + countDown);
		 	        } else if (countDown > 0 && countDown < 10){
		 	        	timerLabel.setText("Time - 0:0" + countDown);
		 	        }
		 	        	
		 	    }
	 	    	
 	    	}

 	    }
		
	} // closing CountDownTimer class
	
	/***************************************/
	//          Water Task Class   
	/***************************************/
	
	class WaterTask extends TimerTask {

 	    public void run() {
 	 		//repainting water
 			for(int i = 0; i < (ROW); i++){
 				for(int j = 0; j < COLUMN; j++){
 					computerGrid[i][j].paintWater();
 					playerGrid[i][j].paintWater();
 				}
 			}

 	    }
		
	} // closing WaterTask class
	
	/***************************************/
	//       Explosion Task Class
	/***************************************/
	
	class ExplTask extends TimerTask {
		ImageLabel il;
		String url;
		int row;
		int column;
		
		public ExplTask(String tempCaller, String tempURL, int tempRow, int tempColumn){
			url = tempURL;
			row = tempRow;
			column = tempColumn;
			
			if(tempCaller.equals("Player")){
				il = computerGrid[row][column];
			} else if (tempCaller.equals("Computer")){
				il = playerGrid[row][column];
			}				
		}

		public void run(){
			
			if(gameEnded){
				return;
			}
			
			boolean running = il.paintExplosion(explImages);
			if(running == false){
				il.setMarker(url);
				this.cancel();
			}

		}

	}
	
	/***************************************/
	//         Splash Task Class 
	/***************************************/
	
	class SplashTask extends TimerTask {
		ImageLabel il;
		String url;
		int row;
		int column;
		boolean shipSunk;
		
		public SplashTask(String tempCaller, String tempURL, int tempRow, int tempColumn, boolean sunk){
			url = tempURL;
			row = tempRow;
			column = tempColumn;
			shipSunk = sunk;
			animHasEnded = false;
			
			if(tempCaller.equals("Player")){
				il = computerGrid[row][column];
			} else if (tempCaller.equals("Computer")){
				il = playerGrid[row][column];
			}		
		}

		public void run(){
			
			if(gameEnded && animHasEnded){
				try {
					Thread.sleep(500);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			boolean running = il.paintSplash(splashImages);			
			if(running == false){
				
				if(shipSunk){
					animHasEnded = true;
				}
				
				il.setMarker(url);
				this.cancel();
			}
		}

	}
	
	/***************************************/
	//        Computer Execution  
	/***************************************/
	
	public class ComputerThread extends Thread{

		private long sleepTime;
		private int minimum = 2000; 
		private int maximum = 13000;
		
		public void run(){
			
			while(!game.playerWon() && !game.computerWon()){
				
				//makes the thread sleep for .5 seconds
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				//if the computer hasn't already guessed then guess
				if(!compGuessed && (!game.playerWon())){
					
					//generates a random time to sleep before guessing
					Random random = new Random();
					sleepTime = random.nextInt(maximum - minimum) + minimum;

					//generating the computer's guess
			    	GuessObject result = game.computerGuess();
			    	
			    	if(result.alreadyGuessed){
			    		continue;
			    	}
			    	
			    	int row = result.row;		
			    	int column = result.column;
			    	
			    	//finding type of ship hit
			    	String shipName = "";
			    	
			    	if(result.marker.equals("A")){
			    		shipName = "Aircraft Carrier";
			    	} else if (result.marker.equals("B")){
			    		shipName = "Battleship";
			    	} else if (result.marker.equals("C")){
			    		shipName = "Cruiser";
			    	} else if (result.marker.equals("D")){
			    		shipName = "Destroyer";
			    	}
			    	
			    	//sleeping for the determined amount of time then changing game icons
			    	try {
		    			if((sleepTime/1000) < countDown){
		    				Thread.sleep(sleepTime);
		    			}
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

			    	//Problem is that the thread is going to sleep and while it is asleep, we reset everything
			    	//so when it wakes up, it doesn't recognize that anybody won because the flags have been reset.
			    	//The solution is to check if they're still playing the same game (gameCounter) or raise a flag
			    	// indicating the game has ended (gameEnded = true). I've decided to go with the latter here.
			    	if(gameEnded){
			    		break;
			    	}
			    	
			    	//play cannon fire
			    	playSound(cannonSound);
			    	
			    	//hit or miss
			    	if(result.hit){
			    		//marking spot as hit
			    		playerGrid[row][column].setIcon(new GameIcon("assets/X.png", "X"));
			    		
			    		//setting explosion animation
				    	animationTimer.schedule(new ExplTask("Computer", "assets/X.png", row, column), EXPLODEDELAY+CANNONDELAY, EXPLODELENGTH);
				    	soundTimer.schedule(new TimerTask(){
				    		public void run(){
				    			if(gameEnded){
				    				return;
				    			}
				    			playSound(explodeSound);
				    		}
				    	}, EXPLODEDELAY+CANNONDELAY);
			    		
			    		//storing the time of the hit
					    String time = "";
					    if(countDown >= 10){
					    	time = "(0:" + countDown + ")";
					    } else if (countDown < 10){
					    	time = "(0:0" + countDown + ")";	
					    }
					    
					    gameLog.append("\nComputer hit " + game.position(row, column-1) + 
					    		" and hit a " + shipName + "! " + time);
					    
					    if(result.sunk){
					    	gameLog.append("\nComputer sunk Player's " + shipName + "!");
					    	String coors[] = result.sunkShip.getCoordinates();
				    		for(int a = 0; a < coors.length; a++){
				    			String curr = coors[a];
			    				String [] parse = curr.split("");
			    				
			    				if(curr.length() > 2){
			    					parse[1] = parse[1]+parse[2];
			    				}
			    				
			    				int letter = game.row(parse[0]);
			    				int number = Integer.parseInt(parse[1]);		
			    				
			    				if(a == (coors.length -1)){
			    					animationTimer.schedule(new SplashTask("Computer", "assets/X.png", letter, number, true), SINKDELAY+CANNONDELAY, SINKLENGTH);
			    				} else {
			    					animationTimer.schedule(new SplashTask("Computer", "assets/X.png", letter, number, false), SINKDELAY+CANNONDELAY, SINKLENGTH);
			    				}
			    				
				    			soundTimer.schedule(new TimerTask(){
				    				public void run(){
				    					if(gameEnded){
				    						return;
				    					}
				    					playSound(sinkSound);
				    				}
				    			}, SINKDELAY+CANNONDELAY);
				    		}
					    }
			    		
			    	} else {
			    		//marking spot as miss
			    		playerGrid[row][column].setIcon(new GameIcon("assets/M.png", "M"));
			    		animationTimer.schedule(new SplashTask("Computer", "assets/M.png", row, column, false), SPLASHDELAY+CANNONDELAY, SPLASHLENGTH);
			    		soundTimer.schedule(new TimerTask(){
				    		public void run(){
				    			if(gameEnded){
				    				return;
				    			}
				    			playSound(splashSound);
				    		}
				    	}, SPLASHDELAY+CANNONDELAY);
			    		
			    		//storing the time of the hit
					    String time = "";
					    if(countDown >= 10){
					    	time = "(0:" + countDown + ")";
					    } else if (countDown < 10){
					    	time = "(0:0" + countDown + ")";	
					    }
					    
					    gameLog.append("\nComputer hit " + game.position(row, column-1) + 
					    		" and missed! " + time);
			    	} 
				    
				    //computer just guessed
				    compGuessed = true;
				    //if player and computer have guessed, reset the round!
				    if(playerGuessed && compGuessed){
				    	resetRound();
				    	roundCounter++;
				    	gameLog.append("\nRound " + roundCounter);
				    }
			    	
			    } 

			}
			
		}
		
	} // closing the ComputerThread Inner Class
	
	/*************** Internet Thread *****************/
	
	public class InternetThread extends Thread{
		
		public void run(){
			while(true){			
				String ip = GameGUI.this.getIP();
				if(ip.equals("Error")){
					if(isReady == false){
						waitTimer.cancel();
						waitTimer.purge();
					}
					JOptionPane.showMessageDialog(GameGUI.this, "You have lost the connection!", "Connection Warning", JOptionPane.WARNING_MESSAGE);
					yourIpLabel.setText("Your IP: Error");
					GameGUI.this.resetGame();
					GameGUI.this.resetNetworkMenu();
				}
			}
		}
		
	}
	
	/*************** Chat Thread *****************/
	
	public class ChatThread extends Thread {
		
		@SuppressWarnings("unchecked")
		public void run(){
			while(!game.playerWon() && !game.computerWon()){
				String line = null;
				try {
					//line = br.readLine();
					line = (String) ois.readObject();
				} catch (IOException e) {
					JOptionPane.showMessageDialog(GameGUI.this, "Socket has been closed!", "Connection Error", JOptionPane.ERROR_MESSAGE);
					GameGUI.this.resetGame();
					GameGUI.this.resetNetworkMenu();
					System.out.println("IOE: " + e.getMessage());
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				if(line.equals("closing")){
					JOptionPane.showMessageDialog(GameGUI.this, "The other player has quit!", "Connection Error", JOptionPane.ERROR_MESSAGE);
					GameGUI.this.resetGame();
					GameGUI.this.resetNetworkMenu();
				} else if (line.equals("start")){
					notStart = false;
				} else if (line.equals("ready")){
					notReady = false;
					isReady = true;
					try {
						oos.writeObject(nameTextField.getText());
						oos.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						//computerLabel.setText(br.readLine());
						computerLabel.setText((String)ois.readObject());
						opponentName = computerLabel.getText();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else if (line.equals("chat")){
					try {
						ArrayList<String> strArray = (ArrayList<String>) ois.readObject();
						logArray = strArray;
						/*update the chatLog*/
						GameGUI.this.setChatBox();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else if (line.equals("guess")){
					GuessObject result = null;
					
					try {
				    	result = (GuessObject) ois.readObject();
						
						if(result.player.equals(playerName)){
							//this is the player's move
							playerGuess = result;
							playerCanMove = true;
						} else if (result.player.equals(opponentName)){
							//this is opponent's move
							opponentGuess = result;
							opponentCanMove = true;
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				} else if (line.equals("won")){
					GameGUI.this.winMessage();
				} else if (line.equals("lose")){
					GameGUI.this.loseMessage();
				} else if (line.equals("true")){
					//do nothing and wait for game to start
				} else if (line.equals("false")){
					GameGUI.this.resetNetworkMenu();
				}
			}
		}
		
	}
	
	/*************** Player Thread *****************/
	
	public class PlayerThread extends Thread{
		
		public void run(){
			
			while(!game.playerWon() && !game.computerWon()){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			if(!editMode && !compGuessed && !game.playerWon()){				
				compGuessed = true;
				opponentCanMove = false;
			    while(!opponentCanMove){
			    	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			    
			    //play cannon fire sound
			    playSound(cannonSound);
			    
			    GuessObject result = opponentGuess;
			    String shipName = "";
			    String url = "";		
			    int[] index = new int[2];
			    index[0] = result.row;
			    index[1] = result.column;
				    
			    if(result.marker.equals("A")){
			    	url = "assets/A.png";
			    	shipName = "Aircraft Carrier";
			    } else if (result.marker.equals("B")){
			    	url = "assets/B.png";
			    	shipName = "Battleship";
			    } else if (result.marker.equals("C")){
			    	url = "assets/C.png";
			    	shipName = "Cruiser";
			    } else if (result.marker.equals("D")){
			    	url = "assets/D.png";
			    	shipName = "Destroyer";
			    }
			    
		    	//storing the time of the hit
			    String time = "";
			    if(countDown >= 10){
			    	time = "(0:" + countDown + ")";
			    } else if (countDown < 10){
			    	time = "(0:0" + countDown + ")";	
			    }
			    
			    if(result.hit){
			    	playerGrid[index[0]][index[1]].setIcon(new GameIcon("assets/X.png", "X"));
			    	
			    	//setting explosion animation
			    	animationTimer.schedule(new ExplTask("Computer", "assets/X.png", index[0], index[1]), EXPLODEDELAY+CANNONDELAY, EXPLODELENGTH);
			    	soundTimer.schedule(new TimerTask(){
			    		public void run(){
			    			playSound(explodeSound);
			    		}
			    	}, EXPLODEDELAY+CANNONDELAY);
			    	
				    //logging the player's hit
			    	gameLog.append("\nComputer hit " + game.position(index[0], index[1]-1) + " and hit a " + shipName + "! " + time);
			    	
			    	if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
		 	        	GameGUI.this.logEvent(opponentName + " hit " + game.position(index[0], index[1]-1) + " and hit a " + shipName + "! " + time);
		 	       	}
			    	
			    	//logging if the player sunk a ship
			    	if(result.sunk){
			    		gameLog.append("\nComputer sunk Player's " + shipName + "!");
			    		
			    		if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
			 	        	GameGUI.this.logEvent(opponentName + " sunk Player's " + shipName + "!");
			 	       	}
			    		
			    		String coors[] = result.sunkShip.getCoordinates();
			    		
			    		for(int a = 0; a < coors.length; a++){
			    			String curr = coors[a];
		    				String [] parse = curr.split("");
		    				
		    				if(curr.length() > 2){
		    					parse[1] = parse[1]+parse[2];
		    				}
		    				
		    				int letter = game.row(parse[0]);
		    				int number = Integer.parseInt(parse[1]);
		    				
		    				if(a == (coors.length - 1)){
		    					animationTimer.schedule(new SplashTask("Computer", "assets/X.png", letter, number, true), SINKDELAY+CANNONDELAY, SINKLENGTH);
		    				} else {
		    					animationTimer.schedule(new SplashTask("Computer", "assets/X.png", letter, number, false), SINKDELAY+CANNONDELAY, SINKLENGTH);
		    				}
		    				
			    			soundTimer.schedule(new TimerTask(){
			    				public void run(){
			    					playSound(sinkSound);
			    				}
			    			}, SINKDELAY+CANNONDELAY);
			    		}
			    		
			    	}
			    	
			    } else {
			    	playerGrid[index[0]][index[1]].setIcon(new GameIcon("assets/M.png", "M"));
			    	
			    	//setting splash animation
			    	animationTimer.schedule(new SplashTask("Computer", "assets/M.png", index[0], index[1], false), SPLASHDELAY+CANNONDELAY, SPLASHLENGTH);
			    	soundTimer.schedule(new TimerTask(){
			    		public void run(){
			    			playSound(splashSound);
			    		}
			    	}, SPLASHDELAY+CANNONDELAY);
			    	
			    	gameLog.append("\nComputer hit " + game.position(index[0], index[1]-1) + " and missed! " + time);
			    	
			    	if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
		 	        	GameGUI.this.logEvent(opponentName + " hit " + game.position(index[0], index[1]-1) + " and missed! " + time);
		 	       	}
			    	
			    }

			    //Indicating that the player has guessed this round
			    compGuessed = true;
			    if(playerGuessed && compGuessed){
			    	resetRound();
			    	roundCounter++;
			    	gameLog.append("\nRound " + roundCounter);
			    	
			    	if(!mapCheckBox.isSelected() && hostCheckBox.isSelected()){
		 	        	GameGUI.this.logEvent("Round " + roundCounter);
		 	       	}
			    	
			    }
			    
			    }
			    
			}
			
		}
		
	} // closing the ComputerThread Inner Class
	
	
} // closing the GameGUI Class
