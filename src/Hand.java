/**
 * The Hand class is a subclass of the CardList class and is used to model a hand of cards. It has 
 * a private instance variable for storing the player who plays this hand. It also has methods for 
 * getting the player of this hand, checking if it is a valid hand, getting the type of this hand, 
 * getting the top card of this hand, and checking if it beats a specified hand.
 * 
 * @author DaveMatthew
 *
 */
public abstract class Hand extends CardList {
	
	/**
	 * the player who plays this hand.
	 */
	private final CardGamePlayer player;
	
	/**
	 * a constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player the specified player who play this hand
	 * @param cards the list of cards
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
		this.sort();
	}
	
	/**
	 * a method for retrieving the player of this hand.
	 * 
	 * @return player object which is the player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * a method for retrieving the top card of this hand.
	 * 
	 * @return card object which is the top card of this hand
	 */
	public Card getTopCard() {
		if (!this.isEmpty()) {
			this.sort();
			return (this.getCard(this.size()-1));
		} else {
			return null;
		}
		
	}
	
	/**
	 * a method for checking if this hand beats a specified hand.
	 * 
	 * @param hand the hand that wants to be compared
	 * @return boolean value that specify whether this hand beats the hand in the argument
	 */
	public boolean beats(Hand hand) {
		if (hand == null || !hand.isValid() || !this.isValid() || this.getType() != hand.getType()) {
			return false;
		} else {
			return (this.getTopCard().compareTo(hand.getTopCard()) > 0);
		}
	}
	
	/// abstract function below
	
	/**
	 * an abstract method for checking if this is a valid hand.
	 * 
	 * @return boolean value that specify whether the hand is valid or not
	 */
	public abstract boolean isValid();
	
	/**
	 * an abstract method for returning a string specifying the type of this hand.
	 * 
	 * @return string that specify the type of this hand
	 */
	public abstract String getType();
	
	
}
