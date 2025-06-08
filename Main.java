import src.domain.cards.Deck;
import src.domain.player.Player;

import java.util.*;

public class Main {
    // Prints the game description and rules
    public static void printGameDescription() {
        System.out.printf("""
        ===========================================
                      Blitz Card Game
        ===========================================
        Objective:
        Achieve the highest total value of cards of the same suit. The highest possible score is 31.
    
        How to Play:
        Each player is dealt 3 cards in their hand. At the start of the game, turn order is automatically randomized.
        On your turn, you can do one of the following:
        - Draw from the deck
        - Take the top card from the discard pile
        - Knock        

        If a player knocks, all other players get one more turn, and then the game ends. 
        The game also automatically ends if there are no more cards to draw from in the deck. 
        The player with the highest suit total at the end of the game wins!

        Card Values:
        - Number cards: Face value (e.g., 2 of Hearts = 2 points)
        - Face cards (Jack, Queen, King): 10 points each
        - Aces: 11 points

        Scoring:
        - Your score is determined by the highest total value of cards from a single suit.
        - All cards of the same suit in your hand are added together.
        
        Automatic Win Condition:
        The game will immediately end when a player has a 10, a face card, and an Ace of the same suit in their hand. It's an instant win!

        Get ready to play!    
        ===========================================
        """);
    }
    public static void main(String[] args){
        /* Set up */
        printGameDescription();

        System.out.println("Enter your name: ");
        Scanner scan = new Scanner(System.in);
        String name = scan.nextLine();
        Player user_player = new Player(name);

        AIPlayer ai_player = new AIPlayer("AI src.domain.player.Player");
        Game game = new Game(new Deck(true), new Deck(false), Arrays.asList(user_player, ai_player));

        game.playGame(); // Begin the game

        // Determine the winner.
        Player winner = game.determineWinner();
        if(winner.getAutoWin()){
            System.out.println("\n============================================================");
            System.out.println(winner.getName() + " has automatically won with the " + winner.hand.get(0) + ", " + winner.hand.get(1) + ", and " + winner.hand.get(2) + "!");
            System.out.println("============================================================");
        }
        else{
            System.out.println("\n============================================================");
            System.out.println("The winner is " + winner.getName() + " with a score of " + winner.getScore() + "!");
            System.out.println("============================================================");
        }
        
        // All player hands
        System.out.println(user_player.getName() + "'s hand: " + user_player.hand + " = " + user_player.getScore());
        System.out.println(ai_player.getName() + "'s hand: " + ai_player.hand + " = " + ai_player.getScore());
        System.out.println("============================================================\n");
    }
}
