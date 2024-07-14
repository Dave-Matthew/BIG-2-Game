/**
 * Triple is a subclass of the Hand class and is used to model a hand of triple in a Big Two card game, respectively. 
 * They should override methods of the Hand class as appropriate. In particular, 
 * the getType() method should return the name of the class as a String object 
 * in these classes modelling legal hands in a Big Two card game
 * 
 * @author DaveMatthew
 *
 */
public class Triple extends Hand {

	/**
	 * a constructor for building a hand of triple with the specified player and list of cards.
	 * 
	 * @param player the specified player who play this hand
	 * @param cards the list of cards
	 */
	public Triple(CardGamePlayer player, CardList cards) {
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
			return "Triple";
		}
		return null;
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return boolean value that specify whether the hand is valid or not
	 */
	public boolean isValid() {
		if (this.size() != 3) {
			return false;
		} else {
			if (this.getCard(0).getRank() != this.getCard(1).getRank() || 
				this.getCard(1).getRank() != this.getCard(2).getRank()) {
				return false;
			} else {
				return true;
			}
		}
	}

}
