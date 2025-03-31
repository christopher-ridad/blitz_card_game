public class Card {

    /***** INSTANCE VARIABLES  *****/

    private final int rank; // 1-13 (Ace to King)
    private final int suit; // 0-3 (Hearts, Diamonds, Clubs, Spades)
    private boolean isContributing; // Tracks if the card is contributing to the player's score

    /***** CONSTRUCTORS *****/

    public Card(int rank, int suit) {
        this.rank = rank;
        this.suit = suit;
        this.isContributing = false; 
    }

    /***** ACCESSOR METHODS *****/

    // getRank(): Returns the rank of a Card
    public int getRank() {
        return rank; // All other cards are worth their rank
    }

    public int getCardValue(){
        if(rank == 1){
            return 11; // Ace is worth 11 points
        }
        if(rank > 10){
            return 10; // Face cards (Jack, Queen, King) are worth 10 points
        }
        return rank; // All other cards are worth their rank
    }

    // getSuit(): Returns the suit of a Card
    public int getSuit() {
        return suit;
    }

    // isContributing(): Returns if the card is contributing to the player's score
    public boolean isContributing() {
        return this.isContributing;
    }

    /***** MUTATOR METHODS *****/

    public void setIsContributing(boolean bool){
        this.isContributing = bool;
    }

    /***** PRINTER METHODS *****/

    // toString(): Returns a string representation of a Card
    @Override
    public String toString() {
        String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        return ranks[rank - 1] + " of " + suits[suit];
    }
}
