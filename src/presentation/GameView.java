package presentation;

import domain.player.Player;
import domain.card.Card;

import java.util.List;
import java.util.Scanner;

public class GameView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayGameSetup() {
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

    public void displayTurnInfo(Player player, int deckSize, Card topDiscard) {
        System.out.println("\nIt's " + player.getPlayerId() + "'s turn.");
        System.out.println("Deck has " + deckSize + " cards.");
        System.out.println("Top discard card: " + topDiscard);
        System.out.println("--------------------");

        if (!player.isBot()) {
            System.out.println("Your hand:");
            for (Card card : player.getHand().getCards()) {
                System.out.println(" - " + card);
            }
            System.out.println("Your score: " + player.getHand().getScore());
        }
    }

    public int promptPlayerChoice(boolean canKnock) {
        System.out.println("What would you like to do?");
        System.out.println("(1) Draw from deck");
        System.out.println("(2) Draw from discard");
        if (canKnock) {
            System.out.println("(3) Knock");
        }
        return Integer.parseInt(scanner.nextLine());
    }

    public int promptDiscardChoice(List<Card> hand) {
        System.out.println("Which card do you want to discard?");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println("(" + (i + 1) + ") " + hand.get(i));
        }
        return Integer.parseInt(scanner.nextLine()) - 1;
    }

    public boolean confirmCardKeep(Card drawnCard) {
        System.out.println("You drew: " + drawnCard);
        System.out.println("Keep this card? (y/n)");
        return scanner.nextLine().equalsIgnoreCase("y");
    }

    public void displayEndScreen(Player winner, List<Player> allPlayers) {
        System.out.println("\n=== GAME OVER ===");
        System.out.println("Winner: " + winner.getPlayerId() + " with score: " + winner.getHand().getScore());
        for (Player p : allPlayers) {
            System.out.println(p.getPlayerId() + "'s hand: " + p.getHand().getCards() + " = " + p.getHand().getScore());
        }
    }

    public void displayDelay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
