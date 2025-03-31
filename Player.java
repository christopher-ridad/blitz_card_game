import java.util.*;

public class Player {

    /***** INSTANCE VARIABLES  *****/

    public ArrayList<Card> hand;
    private final String name;
    private int score; // Player's score
    private boolean autoWin; // Automatic win condition

    /***** CONSTRUCTORS *****/

    public Player(String name) {
        this.hand = new ArrayList<>();
        this.name = name;
        this.score = 0;
        this.autoWin = false;
    }

    /***** ACCESSOR METHODS *****/

    // Return the player's names
    public String getName(){
        return this.name;
    }

    // Return the player's score
    public int getScore(){
        return this.score;
    }

    // Return true or false if the player's hand automatically wins
    public boolean getAutoWin(){
        return this.autoWin;
    }

    /***** MUTATOR METHODS *****/

    // addCard(Card card): Add a card from the player's hand
    public void addCard(Card card) {
        hand.add(card);
        setScore(calculateScore(this.hand)); // Calculate score after adding a card
        setAutoWin(); // Update autoWin
    }

    // removeCard(Card card): Remove a card from the player's hand
    public void removeCard(Card card) {
        hand.remove(card);
        setScore(calculateScore(this.hand)); // Calculate score after removing a card
        setAutoWin(); // Update autoWin
    }

    // setScore(int newScore): Set the player's score to a new value
    public void setScore(int newScore){
        this.score = newScore; 
    }

    // setAutoWin(): Sets autoWin to true if their hand matches the automatic win condition
    private void setAutoWin(){
        Set<Integer> suits = new HashSet<>();
        Set<Integer> ranks = new HashSet<>();

        for (Card card : this.hand) {
            suits.add(card.getSuit());
            ranks.add(card.getRank());
        }

        if(suits.size() == 1 && ranks.contains(1) && ranks.contains(10) &&
               (ranks.contains(11) || ranks.contains(12) || ranks.contains(13))){
            this.autoWin = true;
        }
    }

    /***** PLAYER METHODS *****/

    // Returns the score of a given hand
    public int calculateScore(ArrayList<Card> player_hand){
        Map<Integer, Integer> suitSums = new HashMap<>();

        // Sum up the card values by suit
        for (Card card : player_hand) {
            int suit = card.getSuit();
            int value = card.getCardValue();

            suitSums.put(suit, suitSums.getOrDefault(suit, 0) + value);
        }

        // Find the highest sum among all suits
        int maxScore = 0;
        for (int sum : suitSums.values()) {
            maxScore = Math.max(maxScore, sum);
        }

        return maxScore; // Return the score of the player's hand
    }
}