import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;

/**
 * The BigTwo class implements the CardGame interface. It is used to model a Big Two card game.
 * 
 * @author DaveMatthew
 */
public class BigTwo implements CardGame {

	/**
	 * a constructor for creating a Big Two card game.
	 */
	public BigTwo() {
		this.playerList = new ArrayList<CardGamePlayer>();
		CardGamePlayer[] playersTemp = {new CardGamePlayer(), new CardGamePlayer(), new CardGamePlayer(), new CardGamePlayer()};
		for (int i = 0; i<playersTemp.length; i++) {
			this.playerList.add(playersTemp[i]);
			
		}
		
		playerName = JOptionPane.showInputDialog(null,"Enter Name: ");

		if (playerName == null) {
			System.exit(0);
		}
		
		while(playerName.isEmpty()) {
			playerName = JOptionPane.showInputDialog("Enter A valid Name: ");
			if (playerName == null) {
			System.exit(0);
			}
		}

		setPlayerName(playerName);
		this.GUI = new BigTwoGUI(this);
		this.handsOnTable = new ArrayList<Hand>();
		this.client = new BigTwoClient(this, this.GUI);
		
		// load the GUI
		GUI.repaint();
			
	}
	
	/**
	 * a deck of cards.
	 */
	private Deck deck;
	/**
	 * a list of players.
	 */
	private ArrayList<CardGamePlayer> playerList;
	/**
	 * a list of hands played on the table.
	 */
	private ArrayList<Hand> handsOnTable;
	/**
	 * an integer specifying the index of the current player.
	 */
	private int currentPlayerIdx;
	/**
	 * a BigTwoGUI object which builds the GUI for the game and handles all user actions.
	 */
	private BigTwoGUI GUI;
	/**
	 * a string representing the player name.
	 */
	private String playerName;
	/**
	 * a BigTwoClient object that corresponds to the BigTwo object game.
	 */
	private BigTwoClient client;
	/**
	 * A boolean object to represent whether the game has started or not in the multi-player scenario
	 */
	private boolean started;
	
	
	/**
	 * a method for getting the value of started in the BigTwo game object
	 * 
	 * @return a boolean value of whether the game has started or not
	 */
	public boolean getStarted() {
		return this.started;
	}
	
	/**
	 * a method for setting the value of started in the BigTwo game object
	 * 
	 * @param started a boolean value of whether the game has started or not
	 */
	public void setStarted( boolean started ) {
		this.started = started;
	}
	
	/**
	 * a method for getting the player name that corresponds to the BigTwo object.
	 * 
	 * @return a string object that correspond to the player name of the BigTwo object.
	 */
	public String getPlayerName( ) {
		return this.playerName;
	}
	
	/**
	 * a method for setting the player name that corresponds to the BigTwo object.
	 * 
	 * @param name a string object that correspond to the player name of the BigTwo object.
	 */
	public void setPlayerName( String name ) {
		this.playerName = name;
	}
	/**
	 * a method for getting the client that corresponds to the BigTwo object.
	 * 
	 * @return a BigTwoClient object that correspond to the BigTwo object
	 */
	public BigTwoClient getClient( ) {
		return this.client;
	}
	
	/**
	 * a method for setting the client that corresponds to the BigTwo object.
	 * 
	 * @param client a BigTwoClient object that correspond to the BigTwo object.
	 */
	public void setClient( BigTwoClient client ) {
		this.client = client;
	}
	
	/**
	 * a method for getting the number of players.
	 * 
	 * @return an integer specifying the number of player
	 */
	@Override
	public int getNumOfPlayers() {
		
		return this.playerList.size();
	}

	/**
	 * a method for retrieving the deck of cards being used.
	 * 
	 * @return a Deck object that is being used
	 */
	@Override
	public Deck getDeck() {
		
		return this.deck;
	}

	/**
	 * a method for retrieving the list of players.
	 * 
	 * @return an ArrayList object of type CardGamePlayer containing the list of players
	 */
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		
		return this.playerList;
	}

	/**
	 * a method for retrieving the list of hands played on the table.
	 * 
	 * @return an ArrayList object of type Hand containing the list of hands played on the table
	 */
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		
		return this.handsOnTable;
	}

	/**
	 * a method for retrieving the index of the current player.
	 * 
	 * @return an integer specifying the index of the current player
	 */
	@Override
	public int getCurrentPlayerIdx() {
		
		return this.currentPlayerIdx;
	}
	
	/**
	 * a method for distributing a deck of cards to players
	 * 
	 * @param deck getting the deck that has been initialized for the game.
	 */
	public void distributeCard(Deck deck) {
		//startIndex to know which player should start (i.e. the player holding 3 diamond card)
		int startIndex = 0;
		
		//distribute the cards in the deck to the players using nested for loop
		for (int i = 0; i<deck.size()/this.getPlayerList().size(); i++) {
			for (int j = 0; j<this.getPlayerList().size(); j++) {
				if (deck.getCard(i*this.getPlayerList().size() + j).getRank() == 2 && deck.getCard(i*this.getPlayerList().size() + j).getSuit() == 0) {
					startIndex = j;
				}
				this.getPlayerList().get(j).addCard(deck.getCard((i)*this.getPlayerList().size() + j));
			}
		}
	
		//sort the cards that the players have by using the sort method for each player
		for (int i = 0; i<getPlayerList().size(); i++) {
			this.getPlayerList().get(i).sortCardsInHand();
		}
		this.currentPlayerIdx = startIndex;
	}

	/**
	 * a method for starting/restarting the game with a given shuffled deck of cards.
	 * 
	 * @param deck a Deck object containing shuffled deck of cards
	 */
	@Override
	public void start(Deck deck) {
		
		this.deck = deck;
		
		this.GUI.clearMsgArea();
		this.GUI.printMsg("All players are ready. Game starts." + '\n');
		
		// (i) remove all the cards from the players as well as from the table.
		this.handsOnTable.clear();
		for (int i = 0; i<this.getPlayerList().size(); i++) {
			this.getPlayerList().get(i).removeAllCards();
		}
		
		// (ii) distribute the cards to the players
		// (iii) identify the player who holds the 3 of Diamonds
		// (iv) set the currentIdx of the BigTwo instance to the playerID (i.e., index) of the player who holds the 3 of Diamonds
		// the distributeCard method will identify and set the player who holds the 3 of diamond as the currentPlayerIdx
		this.distributeCard(deck);
		
		// (v) set the activePlayer of the BigTwoGUI instance to the playerID (i.e., index) of the local player 
		// (i.e., only shows the cards of the local player and the local player can only select cards from his/her own hand)
		GUI.setActivePlayer(this.client.getPlayerID());
	
		started = true;
		
		//GUI.repaint();
	}

	/**
	 * a method for making a move by a player with the specified playerID using the cards 
	 * specified by the list of indices. This method should be called from the BigTwoGUI 
	 * when the active player presses either the Play or Pass button.
	 * 
	 * @param playerIdx an integer specifying the index of the player
	 * @param cardIdx an integer specifying the index of the cards
	 */
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		
		CardGameMessage move = new CardGameMessage(6, -1, cardIdx);
		this.client.sendMessage(move);
	}

	/**
	 * a method for checking a move made by a player. This method should be called from the makeMove() 
	 * method from the CardGame interface.
	 * 
	 * @param playerID an integer specifying the index of the player
	 * @param cardIdx an integer specifying the index of the cards
	 */
	@Override
	public synchronized void checkMove(int playerID, int[] cardIdx) {
		
		CardList cards = new CardList();
		if (cardIdx == null) {
			if (handsOnTable.size()!=0 && handsOnTable.get(handsOnTable.size()-1).getPlayer() != this.getPlayerList().get(this.currentPlayerIdx)) {
				this.currentPlayerIdx = (this.getCurrentPlayerIdx()+1)%4;
				GUI.printMsg("[pass]" + '\n');
				if (this.getCurrentPlayerIdx() == GUI.getActivePlayer()) {
					GUI.promptActivePlayer();
				}
				else {
					GUI.printMsg(this.getPlayerList().get(this.getCurrentPlayerIdx()).getName() + "'s turn:" + '\n');
				}
				GUI.resetSelected();
				GUI.repaint();
			}
			else {
				GUI.printMsg("Not a legal move!!!" +'\n');
			}
		}
		else {
			for (int i = 0; i<cardIdx.length; i++) {
				cards.addCard(this.getPlayerList().get(playerID).getCardsInHand().getCard(cardIdx[i]));
			}
			Hand hand = this.composeHand(this.getPlayerList().get(playerID), this.getPlayerList().get(playerID).play(cardIdx));
			if (hand != null) {
				if (handsOnTable.size() == 0) {
					if (hand.contains(this.getPlayerList().get(currentPlayerIdx).getCardsInHand().getCard(0)) && hand.isValid()) {
						this.handsOnTable.add(hand);
						this.getPlayerList().get(playerID).removeCards(hand);
						this.currentPlayerIdx = (this.getCurrentPlayerIdx()+1)%4;
						if (this.getCurrentPlayerIdx() == GUI.getActivePlayer()) {
							GUI.printMsg(hand.getType() + " " + hand.toString() + '\n' + "Your turn:" + '\n');
						}
						else {
							GUI.printMsg(hand.getType() + " " + hand.toString() + '\n' + this.getPlayerList().get(this.getCurrentPlayerIdx()).getName() + "'s turn:" + '\n');
						}
						GUI.resetSelected();
						GUI.repaint();
					}
					else {
						GUI.printMsg("Not a legal move!!!" + '\n');
					}
				}
				else if (hand.getPlayer() == handsOnTable.get(handsOnTable.size()-1).getPlayer()) {
					if (hand.isValid()) {
						this.handsOnTable.add(hand);
						this.getPlayerList().get(playerID).removeCards(hand);
						if (this.endOfGame()) {
							
							GUI.repaint();
							GUI.printMsg( '\n' + "Game has ended!!! Thanks for playing!!!" + "\n");
							GUI.resetSelected();
							
						}
						else {
							GUI.printMsg(hand.getType() + " " + hand.toString() + '\n');
							this.currentPlayerIdx = (this.getCurrentPlayerIdx()+1)%4;
							GUI.resetSelected();
							if (this.getCurrentPlayerIdx() == GUI.getActivePlayer()) {
								GUI.printMsg("Your turn:" + '\n');
							}
							else {
								GUI.printMsg(this.getPlayerList().get(this.getCurrentPlayerIdx()).getName() + "'s turn:" + '\n');
							}
							GUI.repaint();
						}
					}
					else {
						GUI.printMsg(" Not a legal move!!!" + '\n');
					}
				}
				else if (hand.size() == handsOnTable.get(handsOnTable.size()-1).size() && hand.isValid() && hand.beats(handsOnTable.get(handsOnTable.size()-1))) {
					this.handsOnTable.add(hand);
					this.getPlayerList().get(playerID).removeCards(hand);
					if (this.endOfGame()) {
						GUI.repaint();
						GUI.printMsg("Game has ended!!! Thanks for playing!!!");
						GUI.resetSelected();
						
					}
					else {
						this.currentPlayerIdx = (this.getCurrentPlayerIdx()+1)%4;
						if (this.getCurrentPlayerIdx() == GUI.getActivePlayer()) {
							GUI.printMsg(hand.getType() + " " + hand.toString() + '\n' + "Your turn:" + '\n');
						}
						else {
							GUI.printMsg(hand.getType() + " " + hand.toString() + '\n' + this.getPlayerList().get(this.getCurrentPlayerIdx()).getName() + "'s turn:" + '\n');
						}
						GUI.resetSelected();
						GUI.repaint();
					}
				}
				else {
					GUI.printMsg("Not a legal move!!!" + '\n');
				}
			}
			else {
				GUI.printMsg("Not a legal move!!!" + '\n');
			}
		}
	}

	/**
	 * a method for checking if the game ends.
	 * 
	 * @return a boolean value specifying whether the game ends or not
	 */
	@Override
	public boolean endOfGame() {
		
		if (this.getClient().getIsStarted() && this.getClient().getIsConnected()) {
			
			for (int i = 0; i<this.getNumOfPlayers(); i++) {
				if (this.getPlayerList().get(i).getNumOfCards() == 0) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * a method for returning a valid hand from the specified list of cards of the player. 
	 * Returns null if no valid hand can be composed from the specified list of cards
	 * 
	 * @param player a CardGamePlayer object specifying the player
	 * @param cards a CardList object specifying the list of cards
	 * @return a Hand object that can be composed from the cards (i.e. Single, Pair, Triple, etc.)
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		if (cards == null) {
			return null;
		}
		//creating new objects of the hand's subclasses
		Hand single = new Single(player, cards);
		Hand pair = new Pair(player, cards);
		Hand triple = new Triple(player, cards);
		Hand quad = new Quad(player, cards);
		Hand flush = new Flush(player, cards);
		Hand fullHouse = new FullHouse(player, cards);
		Hand straight = new Straight(player, cards);
		Hand straightFlush = new StraightFlush(player, cards);
		//checking if they are valid, and returns the hand if so
		if (cards.size() == 5) {
			if (straightFlush.isValid()) {
				straightFlush.getType();
				return straightFlush;
			}
			else if (quad.isValid()) {
				quad.getType();
				return quad;
			}
			else if (fullHouse.isValid()) {
				fullHouse.getType();
				return fullHouse;
			}
			else if (flush.isValid()) {
				flush.getType();
				return flush;
			} 
			else if (straight.isValid()) {
				straight.getType();
				return straight;
			} 
		}
		
		else if (cards.size() == 3) {
			if (triple.isValid()) {
				triple.getType();
				return triple;
			}
		} 
		else if (cards.size() == 2) {
			if (pair.isValid()) {
				pair.getType();
				return pair;
			}
 		} 
		else if (cards.size() == 1) {
			if (single.isValid()) {
				return single;
			}
 		}
 		return null;
		
	}
	
	
	/**
	 * the main function of the program to be run.
	 * 
	 * @param args not needed
	 */
	public static void main(String[] args) {
		
		BigTwo game = new BigTwo();
 
	}

}
