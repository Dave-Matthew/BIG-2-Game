/**
 * Straight is a subclass of the Hand class and is used to model a hand of straight in a Big Two card game, respectively. 
 * They should override methods of the Hand class as appropriate. In particular, 
 * the getType() method should return the name of the class as a String object 
 * in these classes modelling legal hands in a Big Two card game
 * 
 * @author DaveMatthew
 *
 */
public class Straight extends Hand {

	/**
	 * a constructor for building a hand of straight with the specified player and list of cards.
	 * 
	 * @param player the specified player who play this hand
	 * @param cards the list of cards
	 */
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
		this.sort();
	}
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 * @return string that specify the type of this hand
	 */
	public String getType() {
		if (this.isValid() == true) {
			return "Straight";
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
			int rankCard1;
			int rankCard2;
			for (int i = 0; i < 4; i++) {
				rankCard1 = this.getCard(i).getRank();
				rankCard2 = this.getCard(i+1).getRank();
				
				if (rankCard1 < 2) {
					rankCard1 += 13;
				}
				
				if (rankCard2 < 2) {
					rankCard2 += 13;
				}
				
				if (rankCard1 + 1 != rankCard2) {
					return false;
				}
			}
			return true;
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
			return (this.getTopCard().compareTo(hand.getTopCard()) > 0);
		} else {
			return false;
		}
	}

}
