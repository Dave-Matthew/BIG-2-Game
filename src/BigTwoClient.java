import java.io.*;
import java.net.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * The BigTwoClient class implements the NetworkGame interface. 
 * It is used to model a Big Two game client that is responsible for establishing 
 * a connection and communicating with the Big Two game server.
 * 
 * @author DaveMatthew
 *
 */
public class BigTwoClient implements NetworkGame{
	
	/**
	 * a constructor for creating a Big Two client. 
	 * The first parameter is a reference to a BigTwo object associated with this client and 
	 * the second parameter is a reference to a BigTwoGUI object associated the BigTwo object.
	 * 
	 * @param game a BigTwo object that represent the game played
	 * @param gui a BigTwoGUI object that represent the GUI corresponding to the BigTwo game
	 * 
	 */
	public BigTwoClient(BigTwo game, BigTwoGUI gui) {
		this.game = game;
		this.gui = gui;
		
		
		game.setStarted(false);
		
		setPlayerName(game.getPlayerName());
		
		// hard code the IP address and server port
		setServerIP("127.0.0.1"); 
		setServerPort(2396);
		connect(); 
		
	}
	
	/**
	 * a BigTwo object for the Big Two card game.
	 */
	private BigTwo game; 
	
	/**
	 * a BigTwoGUI object for the Big Two card game.
	 */
	private BigTwoGUI gui;
	
	/**
	 * a socket connection to the game server.
	 */
	private Socket sock;
	
	/**
	 * an ObjectOutputStream for sending messages to the server.
	 */
	private ObjectOutputStream oos;
	
	/**
	 * an integer specifying the playerID (i.e., index) of the local player.
	 */
	private int playerID;
	
	/**
	 * a string specifying the name of the local player.
	 */
	private String playerName;
	
	/**
	 * a string specifying the IP address of the game server.
	 */
	private String serverIP;
	
	/**
	 * an integer specifying the TCP port of the game server.
	 */
	private int serverPort;
	
	/**
	 * a boolean value that represent whether the client is connected to the server or not.
	 */
	private boolean isConnected;
	
	/**
	 * a boolean value that represent whether the game has started or not.
	 */
	private boolean isStarted;
	
	
	// implements the NetworkGame interface methods.
	
	
	/**
	 * a method for getting the playerID (i.e., index) of the local player.
	 * 
	 * @return an integer representing the playerID.
	 */
	public int getPlayerID() {
		return this.playerID;
	}
	
	/**
	 * a method for setting the playerID (i.e., index) of the local player. 
	 * This method should be called from the parseMessage() method when a message of the type PLAYER_LIST is received from the game server. 
	 * 
	 * @param playerID an integer representing the playerID.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	
	/**
	 * a method for getting the name of the local player.
	 * 
	 * @return a string representing the player name.
	 */
	public String getPlayerName() {
		return this.playerName;
	}
		
	/**
	 * a method for setting the name of the local player. 
	 * 
	 * @param playerName a string representing the player name.
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	/**
	 * a method for getting the IP address of the game server.
	 * 
	 * @return  a string representing the IP address of the game server.
	 */
	public String getServerIP() {
		return this.serverIP;
	}
	
	/**
	 * a method for setting the IP address of the game server.
	 * 
	 * @param serverIP a string representing the location of the IP (IP address).
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	
	/**
	 * a method for getting the TCP port of the game server.
	 * 
	 * @return an integer representing the server port.
	 */
	public int getServerPort() {
		return this.serverPort;
	}
	
	/**
	 * a method for setting the TCP port of the game server.
	 * 
	 * @param serverPort an integer representing the server port.
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	/**
	 * a method for getting the value of isConnected of this player.
	 * 
	 * @return a boolean value of isConnected.
	 */
	public boolean getIsConnected() {
		return this.isConnected;
	}
	
	/**
	 * a method for setting the boolean value of isConnected.
	 * 
	 * @param connected a boolean value to be set into isConnected.
	 */
	public void setIsConnected(boolean connected) {
		this.isConnected = connected;
	}
	
	/**
	 * a method for getting the value of isStarted.
	 * 
	 * @return a boolean value if isStarted.
	 */
	public boolean getIsStarted() {
		return this.isStarted;
	}
	
	/**
	 * a method for setting the value of isStarted
	 * 
	 * @param started a boolean value to be set into isStarted.
	 */
	public void setIsStarted(boolean started) {
		isStarted = started;
	}
	
	
	/**
	 * a method for making a socket connection with the game server.
	 * Upon successful connection, it should (i) create an ObjectOutputStream for sending messages to the game server; 
	 * (ii) create a new thread for receiving messages from the game server.
	 */
	public void connect() {
		
		try {
			if(this.getIsConnected()) {
				gui.printMsg("You are already connected!" + '\n');
			} else {
				sock = new Socket(this.getServerIP(), this.getServerPort());
				oos = new ObjectOutputStream(sock.getOutputStream());
				Thread connection = new Thread(new ServerHandler());
				connection.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * a method for parsing the messages received from the game server. 
	 * This method should be called from the thread responsible for receiving messages from the game server. 
	 * Based on the message type, different actions will be carried out 
	 * 
	 * @param message a GameMessage object that wants to be parsed
	 */
	public void parseMessage(GameMessage message) {
		if (message.getType() == CardGameMessage.PLAYER_LIST) {
			
			String[] data = (String[]) message.getData();
			this.setPlayerID(message.getPlayerID());
			for (int i = 0; i<data.length; i++) {
				this.game.getPlayerList().get(i).setName(data[i]);
			}
			sendMessage(new CardGameMessage(1, -1, this.getPlayerName()));
			
		}
		else if (message.getType() == CardGameMessage.JOIN) {
			
			if (this.getPlayerID() == message.getPlayerID()) {
				sendMessage(new CardGameMessage(4, -1, null));
			}
			else {
				gui.printMsg((String)message.getData() + " joins the game." + '\n');
			}
			game.getPlayerList().get(message.getPlayerID()).setName((String)message.getData());
			this.setIsConnected(true);
			this.setIsStarted(false);
			gui.setActivePlayer(playerID);
			gui.repaint();
			
		}
		else if (message.getType() == CardGameMessage.FULL) {
			
			this.isConnected = false;
			gui.printMsg("The game is full!" + '\n');
			gui.repaint();
			
		}
		else if (message.getType() == CardGameMessage.QUIT) {
			
			gui.printMsg("Player "+ game.getPlayerList().get(message.getPlayerID()).getName() + " has left the game" + '\n');
			if (game.getStarted()) {
				gui.printMsg("Waiting for others to join" + '\n');
			}
			game.getPlayerList().get(message.getPlayerID()).setName("");
			this.setIsConnected(false);
			sendMessage(new CardGameMessage(4, -1, null));
			game.setStarted(false);
			setIsStarted(false);
			gui.repaint();
			
		}
		else if (message.getType() == CardGameMessage.READY) {
			
			int numOfConnected = 0;
			for (int i = 0; i<this.game.getNumOfPlayers(); i++) {
				try {
					if (this.game.getPlayerList().get(i).getName()!=null || !this.game.getPlayerList().get(i).getName().isEmpty()) {
						numOfConnected++;
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			if (numOfConnected == 4) {
				this.setIsStarted(true);
				this.setIsConnected(true);
				gui.setActivePlayer(playerID);
			}
			else {
				this.setIsStarted(false);
				gui.printMsg(this.game.getPlayerList().get(message.getPlayerID()).getName() + " is ready!" + '\n');
			}
			
		}
		else if (message.getType() == CardGameMessage.START) {
			
			this.game.start((BigTwoDeck) message.getData());
			if (this.game.getCurrentPlayerIdx() == gui.getActivePlayer()) {
				gui.printMsg("Your turn:" + '\n');
			}
			else {
				gui.printMsg(this.game.getPlayerList().get(this.game.getCurrentPlayerIdx()).getName() + "'s turn:" + '\n');
			}
			gui.repaint();
			
		}
		else if (message.getType() == CardGameMessage.MOVE) {
			
			this.game.checkMove(message.getPlayerID(),(int[]) message.getData());
			
		}
		else if (message.getType() == CardGameMessage.MSG) {
			
			gui.printChat((String)message.getData());
			
		}
		
	}
	

	/**
	 * a method for sending the specified message to the game server. 
	 * This method should be called whenever the client wants to communicate with the game server or other clients.
	 * 
	 * @param message a GameMessage object that is to be sent from the client.
	 */
	public synchronized void sendMessage(GameMessage message) {
		
		try {
			oos.writeObject(message);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * an inner class that implements the Runnable interface. It should implement the run() method from the Runnable interface 
	 * and create a thread with an instance of this class as its job in the connect() method from the NetworkGame interface 
	 * for receiving messages from the game server. Upon receiving a message, the parseMessage() method from the NetworkGame interface
	 * should be called to parse the messages accordingly.
	 *   
	 * @author DaveMatthew
	 */
	class ServerHandler implements Runnable {
		@Override
		public void run() {
			CardGameMessage input;
			try {
				ObjectInputStream streamReader = new ObjectInputStream(sock.getInputStream());
				while((input = (CardGameMessage)streamReader.readObject()) != null ) {
					parseMessage(input);
				}
			}
			catch(SocketException sockEx) {
				gui.printMsg("Your connection to the server has been lost. Press connect in the menu to reconnect" + '\n');
				setIsConnected(false);
				game.setStarted(false);
				gui.disable();
				sockEx.printStackTrace();
			}
			catch(Exception e) {
				gui.printMsg("Your connection to the server has been lost. Press connect in the menu to reconnect" + '\n');
				setIsConnected(false);
				e.printStackTrace();
			}
		}
	}
	
	
	
}
