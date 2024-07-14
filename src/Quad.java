/**
 * Quad is a subclass of the Hand class and is used to model a hand of quad in a Big Two card game, respectively. 
 * They should override methods of the Hand class as appropriate. In particular, 
 * the getType() method should return the name of the class as a String object 
 * in these classes modelling legal hands in a Big Two card game
 * 
 * @author DaveMatthew
 *
 */
public class Quad extends Hand {

	/**
	 * a constructor for building a hand of quad with the specified player and list of cards.
	 * 
	 * @param player the specified player who play this hand
	 * @param cards the list of cards
	 */
	public Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
		this.sort();
	}
	
	/**
	 * a Card object representing the top card of the Quad
	 */
	private Card highQuadruplet ;
	
	/**
	 * a method for returning a string specifying the type of this hand.
	 * 
	 * @return string that specify the type of this hand
	 */
	public String getType() {
		if (this.isValid() == true) {
			return "Quad";
		}
		return null;
	}
	
	/**
	 * a method for checking if this is a valid hand.
	 * 
	 * @return boolean value that specify whether the hand is valid or not
	 */
	public boolean isValid() {
		int same = 1;
		if (this.size() != 5) {
			return false;
		} else {
			if (this.getCard(0).getRank() == this.getCard(1).getRank() &&
				this.getCard(1).getRank() == this.getCard(2).getRank() &&
				this.getCard(2).getRank() == this.getCard(3).getRank()) {
				highQuadruplet = this.getCard(3);
				return true;
			}
			else if (this.getCard(1).getRank() == this.getCard(2).getRank() &&
					this.getCard(2).getRank() == this.getCard(3).getRank() &&
					this.getCard(3).getRank() == this.getCard(4).getRank()) {
				highQuadruplet = this.getCard(4);
				return true;
			}
			return false;
			
			
//			for (int i = 0; i < 4; i++) {
//				if (this.getCard(i).getRank() == this.getCard(i+1).getRank()) {
//					same++;
//				}
//				if (same == 4) {
//					highQuadruplet = this.getCard(i+1);
//					break;
//				}
//			}
//			return (same == 4);
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
			if (hand.getCard(0).getRank() != hand.getCard(1).getRank()) {
				return (highQuadruplet.compareTo(hand.getCard(4)) > 0);
			} else { 
				return (highQuadruplet.compareTo(hand.getCard(3)) > 0);
			}
		} else if (hand.getType() == "Straight" || hand.getType() == "Flush" || hand.getType() == "FullHouse") {
			return true;	
		} else {
			return false;
		}
	}

}
