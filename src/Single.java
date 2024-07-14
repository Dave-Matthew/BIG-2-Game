/**
 * Single is a subclass of the Hand class and is used to model a hand of single in a Big Two card game, respectively. 
 * They should override methods of the Hand class as appropriate. In particular, 
 * the getType() method should return the name of the class as a String object 
 * in these classes modelling legal hands in a Big Two card game
 * 
 * @author DaveMatthew
 *
 */
public class Single extends Hand {

	/**
	 * a constructor for building a hand of single with the specified player and list of cards.
	 * 
	 * @param player the specified player who play this hand
	 * @param cards the list of cards
	 */
	public Single(CardGamePlayer player, CardList cards) {
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
			return "Single";
		}
		return null;
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return boolean value that specify whether the hand is valid or not
	 */
	public boolean isValid() {
		if (this.size() != 1) {
			return false;
		} else {
			return true;
		}
	}

}
