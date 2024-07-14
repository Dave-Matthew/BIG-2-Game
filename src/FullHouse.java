/**
 * FullHouse is a subclass of the Hand class and is used to model a hand of full house in a Big Two card game, respectively. 
 * They should override methods of the Hand class as appropriate. In particular, 
 * the getType() method should return the name of the class as a String object 
 * in these classes modelling legal hands in a Big Two card game
 * 
 * @author DaveMatthew
 *
 */
public class FullHouse extends Hand {

	/**
	 * a constructor for building a hand of full house with the specified player and list of cards.
	 * 
	 * @param player the specified player who play this hand
	 * @param cards the list of cards
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
		this.sort();
	}
	
	/**
	 * a Card object representing the top card in the FullHouse
	 */
	private Card highTriplet;
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 * @return string that specify the type of this hand
	 */
	public String getType() {
		if (this.isValid() == true) {
			return "FullHouse";
		}
		return null;
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return boolean value that specify whether the hand is valid or not
	 */
	public boolean isValid() {
		if (this.size() != 5) {
			return false;
		} else {
			// if the triplet in the front of the hand
			if (this.getCard(0).getRank() == this.getCard(1).getRank() && 
				this.getCard(1).getRank() == this.getCard(2).getRank() && 
				this.getCard(2).getRank() != this.getCard(3).getRank() && 
				this.getCard(3).getRank() == this.getCard(4).getRank()) {
				highTriplet = this.getCard(2);
				return true;
			} 
			// if the triplet in the back of the hand
			else if (this.getCard(0).getRank() == this.getCard(1).getRank() && 
					 this.getCard(1).getRank() != this.getCard(2).getRank() && 
					 this.getCard(2).getRank() == this.getCard(3).getRank() && 
					 this.getCard(3).getRank() == this.getCard(4).getRank()) {
				highTriplet = this.getCard(4);
				return true;
			}
			return false;
		}
	}
	
	/**
	 * a method for checking if this hand beats a specified hand.
	 * 
	 * @param hand the hand that wants to be compared
	 * @return boolean value that specify whether this hand beats the hand in the argument
	 */
	public boolean beats(Hand hand) {
		if (hand == null || !hand.isValid() || !this.isValid()) {
			return false;
		} else if (this.getType() == hand.getType()) {
			if (hand.getCard(1).getRank() != hand.getCard(2).getRank()) {
				return (highTriplet.compareTo(hand.getCard(4)) > 0);
			} else { 
				return (highTriplet.compareTo(hand.getCard(2)) > 0);
			}
		} else if (hand.getType() == "Straight" || hand.getType() == "Flush") {
			return true;	
		} else {
			return false;
		}
	}

}
