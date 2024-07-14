/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a card used in a 
 * Big Two card game. It should override the compareTo() method it inherits from the Card 
 * class to reflect the ordering of cards used in a Big Two card game.
 * 
 * @author DaveMatthew
 *
 */
public class BigTwoCard extends Card {
	
	/**
	 * a constructor for building a card with the specified suit and rank. 
	 * 
	 * @param suit an int value between 0 and 3 representing the suit of a card:
	 *             <p>
	 *             0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank an int value between 0 and 12 representing the rank of a card:
	 *             <p>
	 *             0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11 =
	 *             'Q', 12 = 'K'
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit,rank);
	}

	
	/**
	 * Compares this card with the specified card for order.
	 * 
	 * @param card the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is less
	 *         than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card) {
		// modifying the rank so that the ranking system will be: 
		// 0 = '3', 1 = '4', ... , 10 = 'K', 11 = 'A', 12 = '2'
		if ((this.rank+11)%13 > (card.rank+11)%13) {
			return 1;
		} else if ((this.rank+11)%13 < (card.rank+11)%13) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
