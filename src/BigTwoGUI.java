import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;


/**
 * The BigTwoGUI class implements the CardGameTable interface. It is used to build a GUI for the Big Two card game 
 * and handle all user actions.
 * 
 * @author DaveMatthew
 */
public class BigTwoGUI implements CardGameUI {
	private final static int MAX_CARD_NUM = 13;
	
	/**
	 * a constructor for creating a BigTwoGUI. The parameter game 
	 * is a reference to a card game associates with this GUI.
	 * 
	 * @param game takes the BigTwo object as the argument to repaint GUI.
	 */
	public BigTwoGUI(BigTwo game) {
		this.game = game;
		this.avatars = new Image[4];
		// load all the card picture including the player icon
		avatars[0] = new ImageIcon("src/p0.png").getImage();
		avatars[1] = new ImageIcon("src/p1.png").getImage();
		avatars[2] = new ImageIcon("src/p2.png").getImage();
		avatars[3] = new ImageIcon("src/p3.png").getImage();
		this.playerPanel = new PlayerPanel[4];
		this.selected = new boolean[MAX_CARD_NUM];
		this.cardImages = new Image[4][13];
		for (int i = 0; i<4; i++) {
			for (int j = 0; j<13; j++) {
				if (i == 0) {
					this.cardImages[i][j] = new ImageIcon("src/" + Integer.toString(j+1) + "d.gif").getImage();
				}
				if (i == 1) {
					this.cardImages[i][j] = new ImageIcon("src/" + Integer.toString(j+1) + "c.gif").getImage();
				}
				if (i == 2) {
					this.cardImages[i][j] = new ImageIcon("src/" + Integer.toString(j+1) + "h.gif").getImage();
				}
				if (i == 3) {
					this.cardImages[i][j] = new ImageIcon("src/" + Integer.toString(j+1) + "s.gif").getImage();
				}
			}
		}
	
		frame = new JFrame("Big Two (" + game.getPlayerName() + ')');
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		msgArea = new JTextArea(30, 30);
		msgArea.setLineWrap(true);
		msgArea.setEditable(false);
		
		chatArea = new JTextArea(30,30);
		chatArea.setLineWrap(true);
		chatArea.setEditable(false);
		
		this.textField = new JTextField(10);
		textField.addActionListener(new EnterListener());
		textField.setPreferredSize(new Dimension(400, 30));
		
		MenuBar menu = new MenuBar(frame);
		
		this.playButton = new JButton("Play");
		this.passButton = new JButton("Pass");
		
		this.passButtonListener = new PassButtonListener();
		this.playButtonListener = new PlayButtonListener();
		
		passButton.addActionListener(passButtonListener);
		playButton.addActionListener(playButtonListener);
		
		this.disable();
			
	}
	
	/**
	 * a variable for storing the textField of the chat.
	 */
	private JTextField textField;
	
	/**
	 * a variable for storing the chat area.
	 */
	private JTextArea chatArea;
	
	/**
	 * a variable for storing the value of PassButtonListener.
	 */
	private PassButtonListener passButtonListener;
	
	/**
	 * a variable for storing the value of PlayButtonListener.
	 */
	private PlayButtonListener playButtonListener;
	
	/**
	 * a card game associates with this GUI.
	 */
	private BigTwo game;
	
	/**
	 * a boolean array indicating which cards are being selected.
	 */
	private boolean[] selected;
	
	/**
	 * an integer specifying the index of the active player.
	 */
	private int activePlayer;
	
	/**
	 * the main window of the application.
	 */
	private JFrame frame;
	
	/**
	 * a panel for showing the cards of each player and the cards played on the table.
	 */
	private JPanel bigTwoPanel;
	
	/**
	 * a Play button for the active player to play the selected cards.
	 */
	private JButton playButton;
	
	/**
	 * a Pass button for the active player to pass his/her turn to the next player.
	 */
	private JButton passButton;
	
	/**
	 * a text area for showing the current game status as well as end of game messages.
	 */
	private JTextArea msgArea;
	
	/**
	 * a 2D array storing the images for the faces of the cards.
	 */
	private Image[][] cardImages;
	
	/**
	 * an image for the backs of the cards.
	 */
	private Image cardBackImage;
	
	/**
	 * an array storing the images for the avatars.
	 */
	private Image[] avatars;
	
	/**
	 * an array for storing the playerPanels.
	 */
	private PlayerPanel[] playerPanel;
	
	
	/**
	 * a method for setting the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer an integer representing the index of the active player
	 */
	@Override
	public void setActivePlayer(int activePlayer) {
		
		if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
			this.activePlayer = -1;
		} else {				// setting active Player
			this.activePlayer = activePlayer;     
		}
		
	}
	
	/**
	 * a method for receiving the active player of the table.
	 * 
	 * @return an integer referring to the active player of the table.
	 */
	public int getActivePlayer() {
		return this.activePlayer;
	}
	
	/**
	 * a method for getting an array of indices of the cards selected.
	 * 
	 * @return an integer array containing the indexes of the card/s that is/are being selected
	 */
	public int[] getSelected() {
		
		CardGamePlayer player = this.game.getPlayerList().get(game.getCurrentPlayerIdx());
		
		int[] cardIndex = null;
		
		int count = 0;
		for (int i = 0; i<player.getNumOfCards(); i++) {
			if (this.selected[i] == true) {
				count++;
			}
		}
		
		cardIndex = new int[count];
		
		int index = 0;
		for (int i = 0; i<this.selected.length; i++) {
			if (this.selected[i] == true) {
				cardIndex[index] = i;
				index++;
			}
		}
		return cardIndex;
	}
	
	/**
	 * a method to get the cardImages.
	 * 
	 * @return cardImages which is a 2d array of type Image that stores the images of the cards' front.
	 */
	public Image[][] getCardImages() {
		return this.cardImages;
	}
	
	/**
	 * a method for resetting the list of selected cards.
	 */
	public void resetSelected() {
		
		for (int i = 0; i < this.selected.length; i++) {
			this.selected[i] = false;
		}
	}
	
	/**
	 * a method for repainting the GUI.
	 */
	@Override
	public void repaint() {
		//reset the selected array to false.
		this.resetSelected();
		
		//a method to reset the frame.
		frame.setContentPane( new JPanel( new BorderLayout() ) );
		
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setLayout(new BorderLayout());
		bigTwoPanel.setPreferredSize(new Dimension(frame.getWidth()*7/10, frame.getHeight()));
		
		JPanel buttonPanel = new JPanel();
		
		cardBackImage = new ImageIcon("src/b.gif").getImage();
		
		JPanel container = new JPanel();
		container.setLayout(new GridLayout(5,1));
		container.setOpaque(false);
		
		//creates panels for the non active players.
		for (int i = 0; i<playerPanel.length; i++) {
			JLabel playerLabel;
			if (i==game.getCurrentPlayerIdx() && game.getClient().getIsStarted()) {
				playerLabel = new JLabel("   " + game.getPlayerList().get(i).getName());
				playerLabel.setForeground(Color.YELLOW);
			}
			else if (this.game.getPlayerList().get(i).getName() != null) {
				playerLabel = new JLabel("   " + this.game.getPlayerList().get(i).getName());
				playerLabel.setForeground(new Color(0, 50, 50));
			}
			else {
				playerLabel = new JLabel("   ");
				playerLabel.setForeground(new Color(0, 50, 50));
			}
			playerLabel.setFont(new Font("Comic Sans", Font.BOLD, 16));
			if ( i!=this.activePlayer ) {
				playerPanel[i] = new PlayerPanel(avatars[i], game.getPlayerList().get(i), this);
				playerPanel[i].setLayout(new FlowLayout(FlowLayout.LEFT));
				playerPanel[i].add(playerLabel);
				playerPanel[i].setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()*4/21));
				playerPanel[i].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
			}
		}
		
		//creates a panel holding the cards on table
		CardsOnTable cardsOnTablePanel = new CardsOnTable(this.game.getHandsOnTable());
		cardsOnTablePanel.setBackground(new Color(10, 150, 0, 0));
		cardsOnTablePanel.setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()*4/21));
		cardsOnTablePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		
		//set the layout for the button panel, and add passButton along with playButton to the panel.
		buttonPanel.setLayout(new GridLayout());
		buttonPanel.setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()*1/24));
		
		buttonPanel.add(playButton);
		buttonPanel.add(passButton);
		
		//an if else logic to implement the active playerPanel.
		
		if ( game.endOfGame() ) {
			JLabel playerLabel = new JLabel("    " + "You    ");
			playerLabel.setForeground(new Color(20, 180, 180));
			playerLabel.setFont(new Font("Comic Sans", Font.BOLD, 16));
			playerPanel[activePlayer] = new PlayerPanel(avatars[activePlayer], game.getPlayerList().get(activePlayer), this);
			playerPanel[activePlayer].setLayout(new FlowLayout(FlowLayout.LEFT));
			playerPanel[activePlayer].add(playerLabel);
			playerPanel[activePlayer].setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()*4/21));
			playerPanel[activePlayer].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		}
		else {
			JPanel tempContainer = new JPanel();
			tempContainer.setLayout(new BoxLayout(tempContainer, BoxLayout.Y_AXIS));
			
			playerPanel[activePlayer] = new PlayerPanel(avatars[activePlayer], game.getPlayerList().get(activePlayer), this);
			playerPanel[activePlayer].setLayout(new BoxLayout(playerPanel[activePlayer], BoxLayout.X_AXIS));
			playerPanel[activePlayer].setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()*4/21));
			playerPanel[activePlayer].setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
			JLabel playerLabel = new JLabel("    " + "You    ");
			
			if (activePlayer == game.getCurrentPlayerIdx() && (game.getClient()).getIsStarted()) {
				playerLabel.setForeground(Color.YELLOW);
				playerLabel.setFont(new Font("Comic Sans", Font.BOLD, 16));
			} else {
				playerLabel.setForeground(new Color(20, 180, 180));
				playerLabel.setFont(new Font("Comic Sans", Font.BOLD, 16));
			}
			
			//creates a panel for the current active player.
			PlayerCardPanel activePanel = new PlayerCardPanel(this, this.playerPanel[this.activePlayer]);
			JPanel fillerPanel = new JPanel();
			fillerPanel.setOpaque(false);
			
			JPanel fillerPanel2 = new JPanel();
			fillerPanel2.setOpaque(false);
			fillerPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));
			fillerPanel2.setPreferredSize(new Dimension(100,100));
			fillerPanel2.setMaximumSize(new Dimension(100, 100));
			fillerPanel2.setMinimumSize(new Dimension(100, 100));
			
			JPanel containerTwo = new JPanel();
			containerTwo.setOpaque(false);
			containerTwo.setLayout(new BoxLayout(containerTwo, BoxLayout.Y_AXIS));
			containerTwo.add(activePanel);
	
			tempContainer.add(playerLabel);
			tempContainer.add(fillerPanel);
			tempContainer.setOpaque(false);
			containerTwo.setPreferredSize(new Dimension((int)Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()));
			playerPanel[activePlayer].add(tempContainer);
			playerPanel[activePlayer].add(fillerPanel2);
			playerPanel[activePlayer].add(containerTwo);
		}
		
		//add the player panels to the container, which will be inserted into the bigTwoPanel.
		container.add(playerPanel[0], BorderLayout.CENTER);
		container.add(playerPanel[1], BorderLayout.CENTER);
		container.add(playerPanel[2], BorderLayout.CENTER);
		container.add(playerPanel[3], BorderLayout.CENTER);
		
		container.add(cardsOnTablePanel, BorderLayout.CENTER);
		
		//add the contents of bigTwoPanel.
		bigTwoPanel.add(container, BorderLayout.CENTER);
		bigTwoPanel.add(buttonPanel, BorderLayout.SOUTH);

		frame.add(bigTwoPanel, BorderLayout.CENTER);
		
		//set the message area to be auto scrolled and add it to the frame.
		msgArea.setCaretPosition(msgArea.getDocument().getLength());
		JScrollPane sp = new JScrollPane(msgArea);
		
		chatArea.setCaretPosition(chatArea.getDocument().getLength());
		chatArea.setForeground(Color.BLUE);
		JScrollPane cht = new JScrollPane(chatArea);
		
		JPanel msgChatHolder = new JPanel();
		msgChatHolder.setLayout(new BorderLayout());
		
		JPanel msgAndChatArea = new JPanel();
		msgAndChatArea.setLayout(new GridLayout(2,1));
		
		JPanel textFieldHolder = new JPanel();
		textFieldHolder.setPreferredSize(new Dimension((int)msgChatHolder.getWidth(), (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight()*1/24));
		textFieldHolder.setLayout(new BorderLayout());
		textFieldHolder.add(new JLabel("Message:"), BorderLayout.WEST);
		textFieldHolder.add(textField);
		textFieldHolder.setOpaque(false);
		
		msgAndChatArea.add(sp);
		msgAndChatArea.add(cht);
		
		msgChatHolder.add(msgAndChatArea, BorderLayout.CENTER);
		msgChatHolder.add(textFieldHolder, BorderLayout.SOUTH);
		
		//setting the buttons to not be able to be clicked when the game is over, and print players' names to the JTextArea if the game is not over.
		if (!game.endOfGame()) {
			if (this.activePlayer != game.getCurrentPlayerIdx()) {
				this.disable();
			} else {
				BigTwoClient tempGame = game.getClient();
				if (tempGame.getIsStarted() == true) {
					this.enable();
				}
				else {
					this.disable();
				}
			}

		}
		else {
			(game.getClient()).setIsConnected(false);
			String messageDialog = "The game has ended! \n";
			int decoy = 0;
			for (int i = 0; i<game.getNumOfPlayers(); i++) {
				if (i == this.activePlayer) {
					messageDialog += ("You have " + game.getPlayerList().get(i).getNumOfCards() + " cards left. \n");
				} else {
					messageDialog += (game.getPlayerList().get(i).getName() + " has " + game.getPlayerList().get(i).getNumOfCards() + " cards left. \n");
				}
				decoy += game.getPlayerList().get(i).getNumOfCards();
			}
		
			if (this.activePlayer == this.game.getCurrentPlayerIdx()){
				JOptionPane.showMessageDialog(frame, messageDialog, "You Win!", JOptionPane.INFORMATION_MESSAGE);
			}
			else {
				JOptionPane.showMessageDialog(frame, messageDialog, "You Lose!", JOptionPane.INFORMATION_MESSAGE);
			}
			(game.getClient()).sendMessage(new CardGameMessage(4, -1, null));
			this.printMsg("\n");
			game.getHandsOnTable().clear();
			for (int i = 0; i<game.getPlayerList().size(); i++) {
				if (i == game.getNumOfPlayers()) {
					this.printMsg(game.getPlayerList().get(i).getName() + " wins the game." + '\n');
				} else {
					this.printMsg(game.getPlayerList().get(i).getName() + " has " + game.getPlayerList().get(i).getNumOfCards() + " cards in hand." + '\n');
				}
			}
			
		}
		
		frame.getContentPane().add(msgChatHolder, BorderLayout.EAST);
		
		frame.setSize(1500, 900);
		frame.setVisible(true);
		
		
	}
	
	/**
	 * a method for printing the specified string to the message area of the GUI.
	 * 
	 * @param msg a string that contains the message to be appended into the msgArea.
	 */
	@Override
	public void printMsg(String msg) {
		this.msgArea.append(msg);	
	}
	
	/**
	 * a method for printing the specified string to the chat area of the GUI.
	 * 
	 * @param chat a string that contains the chat to be appended into the chatArea.
	 */
	public void printChat(String chat) {
		this.chatArea.append(chat + '\n');
	}
	
	/**
	 * a method for clearing the message area of the GUI.
	 */
	@Override
	public void clearMsgArea() {
		this.msgArea.selectAll();
		this.msgArea.replaceSelection("");
	}
	
	/**
	 * a method for clearing the chat area of the GUI.
	 */
	public void clearChatArea() {
		this.chatArea.selectAll();
		this.chatArea.replaceSelection("");
	}
	
	/**
	 * a method for resetting the GUI.
	 */
	@Override
	public void reset() {
		clearMsgArea();
		resetSelected();
		this.enable();
		
		
	}
	/**
	 * a method for enabling user interactions with the GUI.
	 */
	@Override
	public void enable() {
		this.passButton.setEnabled(true);
		this.playButton.setEnabled(true);
	}
	/**
	 * a method for disabling user interactions with the GUI.
	 */
	@Override
	public void disable() {
		
		passButton.setEnabled(false);
		playButton.setEnabled(false);
		
	}
	
	/**
	 * a method for prompting the active player to select cards and make his/her move. 
	 * A message should be displayed in the message area showing it is the active player’s turn.
	 */
	@Override
	public void promptActivePlayer() {
		//this.printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn:" + '\n');
		this.printMsg("Your turn:" + '\n');
	}
	
	/**
	 * An inner class that extends the JPanel class and implements MOuseListener interface.
	 * Overrides the paintComponent() method inherited from the JPanel class to draw the background for the table.
	 * It will store the different panels of the players, button panel which stores play and pass button, 
	 * and also the HandsOnTable panel which shows the last hand played on the table.
	 *  
	 * @author DaveMatthew
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JFrame frame;
		JButton button;
		
		BigTwoPanel() {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		}

		/**
		 *
		 */
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		/**
		 *
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			
			
		}

		/**
		 *
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			
			
		}

		/**
		 *
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			
			
		}

		/**
		 *
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			
			
		}
		
		
		/**
		 * A method to paint the background of the BigTwoGUI panel.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Image table = new ImageIcon("src/bg.jpg").getImage();

			g.drawImage(table, 0, 0, this.getWidth(), this.getHeight() , this);
		}
	}
	
	/**
	 * An inner class for constructing a menu bar on the top left part of the frame.
	 * 
	 * @author DaveMatthew
	 *
	 */
	class MenuBar {
		JMenu game;
		JMenu message;
		JMenuItem connect;
		JMenuItem quit;
		JMenuItem clearGameMsg;
		JMenuItem clearChatMsg;
		
		/**
		 * A constructor to develop the menu bars and its components, and add them to the frame.
		 * 
		 * @param frame to add the menuBar to the frame.
		 */
		MenuBar(JFrame frame) {
			JMenuBar mb = new JMenuBar();
			game = new JMenu("Game");
			connect = new JMenuItem("Connect");
			quit = new JMenuItem("Quit");
			
			message = new JMenu("Message");
			clearGameMsg = new JMenuItem("Clear game messages");
			clearChatMsg = new JMenuItem("Clear chat messages");
			
			connect.addActionListener(new ConnectMenuItemListener());
			quit.addActionListener(new QuitMenuItemListener());
			game.add(connect);
			game.add(quit);
			
			clearGameMsg.addActionListener(new ClearGameMsgMenuItemListener());
			clearChatMsg.addActionListener(new ClearChatMsgMenuItemListener());
			message.add(clearGameMsg);
			message.add(clearChatMsg);
			
			mb.add(game);
			mb.add(message);
			frame.setJMenuBar(mb); //auto added in the top right corner
		}
	}
	
	/**
	 * An inner class that extends the JPanel class. 
	 * Overrides paintComponent() method inherited from the JPanel class to draw a panel for a player
	 * 
	 * @author DaveMatthew
	 *
	 */
	class PlayerPanel extends JPanel {
		private Image[][] frontCards;
		private Image character;
		private CardGamePlayer player;
		private Image backCards;
		private CardGamePlayer currentPlayer;
		
		/**
		 * A constructor to fill the instance variables of the inner class.
		 * 
		 * @param character to get the characters of each player.
		 * @param player to get the player of each panel.
		 * @param gui to access instance variables stored in the outer class.
		 */
		PlayerPanel (Image character, CardGamePlayer player, BigTwoGUI gui) {
			this.character = character;
			this.player = player;
			this.backCards = gui.cardBackImage;
			this.currentPlayer = gui.game.getPlayerList().get(game.getCurrentPlayerIdx());
			this.frontCards = gui.getCardImages();
		}
		
		/**
		 * @return player with a type of CardGamePlayer.
		 */
		public CardGamePlayer getPlayer() {
			return this.player;
		}
		
		/**
		 * A method to paint characters and cards of the players, paint back of the cards if the player is not active, and paint all the cards each player has to the panel
		 * when the game is over.
		 */
		public void paintComponent(Graphics g) {
			if (player.getName() == null || player.getName().isEmpty()) {
				
			}
			else {
				g.drawImage(character, 5, 20, 140, 140, this);
				
				if (game.endOfGame()) {
					for (int i = 0; i<player.getNumOfCards(); i++) {
						g.drawImage(frontCards[player.getCardsInHand().getCard(i).getSuit()][player.getCardsInHand().getCard(i).getRank()], 180+i*40, 50, 73, 97, this);
					}
				}
				else {
					if (game.getPlayerList().get((activePlayer))!=this.player)
					for (int i = 0; i<player.getNumOfCards(); i++) {
						g.drawImage(backCards, 180+i*40, 50, 73, 97, this);
					}
				}
			}
		}
		
	}
	
	/**
	 * An inner class that extends the JLayeredPane class.
	 * it will hold the card button inside each player's panel.
	 * 
	 * @author DaveMatthew
	 *
	 */
	class PlayerCardPanel extends JLayeredPane {
		private CardGamePlayer player;
		
		/**
		 * A constructor to create the cardButtons which are obtained from the cards in hand of the players, add them to the panel of the active player.
		 * 
		 * @param table to access the cardImages stored in the instance variable of outer class.
		 * @param playerPanel to access the instance variables of the PlayerPanel so that the constructor can identify whose card to be made cardButtons.
		 */
		PlayerCardPanel(BigTwoGUI table, PlayerPanel playerPanel) {
			this.setPreferredSize(new Dimension( (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), 500));
			this.player = playerPanel.getPlayer();
			for (int i = 0; i<this.player.getNumOfCards(); i++) {
				Icon cardImage = new ImageIcon(table.cardImages[this.player.getCardsInHand().getCard(i).getSuit()][this.player.getCardsInHand().getCard(i).getRank()]);
				CardButton cardImageTemp = new CardButton(cardImage, i);
				cardImageTemp.setIcon(cardImage);
				cardImageTemp.setBounds(10 + 40*i, 50, 73, 97);
				
				this.add(cardImageTemp, new Integer(i));
			}
		}
	}
	
	/**
	 * An inner class that extends the JPanel class.
	 * Overrides the painComponent() method to paint the handsOnTable to the south component of the BigTwoGUI panel.
	 * 
	 * @author DaveMatthew
	 *
	 */
	class CardsOnTable extends JPanel {
		private ArrayList<Hand> handOnTable;
		
		/**
		 * A constructor for creating the CardsOnTable panel on the south component of the bigTwoPanel. Handling handsOnTable from the game instance variable as the argument. The constructor also 
		 * creates a label for articulating the type of the last hand played on table and the owner of the hand.
		 * 
		 * @param lastHandOnTable handsOnTable which is acquired from the game instance variable.
		 */
		CardsOnTable(ArrayList<Hand> lastHandOnTable){
			JLabel label;
			this.setLayout(new FlowLayout(FlowLayout.LEFT));
			if (lastHandOnTable!=null) {
				if (lastHandOnTable.size()!=0) {
					String string;
					if (lastHandOnTable.get(lastHandOnTable.size()-1).getPlayer() == game.getPlayerList().get(activePlayer)) {
						string = "Played by you";
						label = new JLabel(string);
						label.setForeground(Color.BLUE);
					}
					else {
						string = "  Played by " + lastHandOnTable.get(lastHandOnTable.size()-1).getPlayer().getName() + " (" + lastHandOnTable.get(lastHandOnTable.size()-1).getType() + ")";
						label = new JLabel(string);
						label.setForeground(new Color(20, 20, 80));
					}		
				}
				else if (!game.getStarted()) {
					String string = "  Waiting for player...";
					label = new JLabel(string);
				}
				else {
					String string = "  Start of game";
					label = new JLabel(string);
				}
			}
			else {
				String string = "  Start of game";
				label = new JLabel(string);
			}
			label.setFont(new Font("Helvetica", Font.BOLD, 18));
			this.add(label);
			this.handOnTable = lastHandOnTable;

		}
		
		/**
		 * A method to automatically paint the panel whilst it is being constructed.
		 */
		public void paintComponent(Graphics g) {
			if (handOnTable.size()>0) {
				// print the back of the cards to represent the pile of the previous handOnTable if there is more than 1 hand played.
				if (handOnTable.size() > 1) {
					g.drawImage(cardBackImage, 40, 50, 73, 97, this);
				}
				for (int i = 0; i < handOnTable.get(handOnTable.size()-1).size(); i++) {
					g.drawImage(cardImages[handOnTable.get(handOnTable.size()-1).getCard(i).getSuit()][handOnTable.get(handOnTable.size()-1).getCard(i).getRank()], 180+i*40, 50, 73, 97, this);
				}
			}
		}
	}
	
	/**
	 * An inner class that  extends the JButton class and implements the MouseListener interface.
	 * It will make the card buttons that are going to be inserted in the PlayerCardPanel component.
	 * 
	 * @author DaveMatthew
	 *
	 */
	class CardButton extends JButton implements MouseListener {
		private Icon cardFace;
		private int position;
		private boolean cardClicked;
		
		/**
		 * An inner class constructor for obtaining the instance variables needed to make the moving animation of the card when clicked, and
		 * implementing the actionPerformed method when clicked.
		 *
		 * @param cardImage
		 * @param position
		 */
		CardButton(Icon cardImage, int position) {
			this.position = position;
			this.cardFace = cardImage;
			addMouseListener(this);
			cardClicked = false;
		}
		
		/**
		 *	implements the logic if one of the card in the PlayerCardPanel is clicked. Changing the location of the card upwards if clicked by the user
		 *  and set the selected in the specified position of the class among the cards in hand of the player to be true, and vice-versa.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			
			if (cardClicked == true) {
				cardClicked = false;
				this.setBorder(null);
				for (int i = 1; i<241; i++) {
					this.setBounds(10+position*40, 20+i/8, cardFace.getIconWidth(), cardFace.getIconHeight());
					this.repaint();
					frame.repaint();
				}
				selected[position] = false;
			}
			else {
				cardClicked = true;
				this.setBorder(null);
				for (int i = 1; i<241; i++) {
					this.setBounds(10+position*40, 50-i/8, cardFace.getIconWidth(), cardFace.getIconHeight());
					this.repaint();
					frame.repaint();
				}
				selected[position] = true;
			}
		}

		/**
		 *
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			
			
		}

		/**
		 *
		 */
		@Override
		public void mouseReleased(MouseEvent e) {
			
			
		}

		/**
		 * gives a border when the player hovers over the card.
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			this.setBorder(BorderFactory.createLineBorder(Color.RED));
			
			
		}

		/**
		 * removes the border of the card when the player removes the cursor from the card.
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			
			this.setBorder(null);
		}
		
	}
	
	class EnterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			BigTwoClient tempGame = game.getClient();
			tempGame.sendMessage(new CardGameMessage(7, -1, textField.getText()));
			textField.setText("");
		}
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle button-click events for the Play button. When the Play button is clicked, 
	 * it should call the makeMove() method of your BigTwo object to make a move.
	 * 
	 * @author DaveMatthew
	 *
	 */
	class PlayButtonListener implements ActionListener {
		
		/**
		 * implementing the makeMove method when clicked if the method getSelected does not return null, else print the message on the textArea
		 * that the move is not legal.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			game.makeMove(activePlayer, getSelected());
		}
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle button-click events for the Pass button. When the Pass button is clicked, 
	 * it should call the makeMove() method of your BigTwo object to make a move.
	 * 
	 * @author DaveMatthew
	 *
	 */
	class PassButtonListener implements ActionListener {
		
		/**
		 * implementing the makeMove method when clicked, entering a null as an argument for the array.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			game.makeMove(activePlayer, null);
		}
		
	}
	/**
	 *  an inner class that implements the ActionListener interface. Implements the actionPerformed() 
	 *  method from the ActionListener interface to handle menu-item-click events for the Connect menu 
	 *  item.
	 *  
	 * @author DaveMatthew
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(game.getClient().getIsConnected() == true) {
				printMsg("You are already connected to the server!");
				frame.repaint();
			}
			else {
				game.getClient().connect();
			}
		}
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface 
	 * to handle menu-item-click events for the Quit menu item. When the Quit menu 
	 * item is selected, it should terminate the application. (it may use System.exit()
	 * to terminate the application.)
	 * 
	 * @author DaveMatthew
	 *
	 */
	class QuitMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			System.exit(0);
		}
		
	}
	
	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle 
	 * menu-item-click events for the Clear game messages menu item. 
	 * When the Clear game message menu item is selected, it should call clearMsgArea() method
	 * 
	 * @author DaveMatthew
	 *
	 */
	class ClearGameMsgMenuItemListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent event) {
			clearMsgArea();
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface. 
	 * Implements the actionPerformed() method from the ActionListener interface to handle 
	 * menu-item-click events for the Clear chat messages menu item. 
	 * When the Clear chat messages menu item is selected, it should call clearChatArea() method
	 * 
	 * @author DaveMatthew
	 *
	 */
	class ClearChatMsgMenuItemListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent event) {
			clearChatArea();
		}
	}
	
}