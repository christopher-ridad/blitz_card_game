import src.domain.cards.Card;
import src.domain.cards.Deck;
import src.domain.player.Player;

import java.util.*;


public class Game {

    /***** INSTANCE VARIABLES  *****/

    private final Deck deck; // The deck of cards
    private final Deck discardPile; // The discard pile
    private final List<Player> players; // Keeps a list of players in the game
    private boolean gameRunning; // Tracks if the game is running
    private boolean playerKnocked; // Tracks if a player has knocked

    /***** CONSTRUCTORS *****/

    public Game(Deck d, Deck disc, List<Player> p){
        this.deck = d;
        this.deck.shuffle(); // Shuffle the deck
        this.discardPile = disc;
        this.players = p;
        Collections.shuffle(players); // Randomize the turn order
        this.gameRunning = false; // Game is not running yet
        this.playerKnocked = false; // Knock condition initially set to false
    }

    /***** PRINTER METHODS ******/

    // displayGameInfo(String playerName, List<src.domain.cards.Card> hand, int score): Display's the player's turn information
    public void displayTurnInfo(Player player) {
        // Display player's name
        System.out.println("\n====================");
        System.out.println("It's " + player.getName() + "'s turn.");
        System.out.println("====================");
    
        // Display deck and discard pile information
        System.out.println("src.domain.cards.Deck: " + deck.getSize() + " cards remaining");
        System.out.println("Discard Pile: " + discardPile.peekTopCard());
    
        System.out.println("--------------------");
        
        // Display player's hand
        if(!(player instanceof AIPlayer)){
            System.out.println("Your hand:");
            for (Card card : player.hand) {
                System.out.println(" - " + card);
            }
            System.out.println("--------------------");
            
            // Display player's score
            System.out.println("Your score: " + player.getScore());
            System.out.println("--------------------");
        }
    }

    // promptPlayerChoice(): Prompts the user to make their next move
    public void promptPlayerChoice() {
        System.out.println("What would you like to do?");
        System.out.println("(1) Draw from the deck");
        System.out.println("(2) Draw from the discard pile");
        if(!this.playerKnocked){
            System.out.println("(3) Knock");
        }
    }

    // displayNewHand(): Display's the players new hand
    public void displayNewHand(Player player) {
        System.out.println("--------------------");
        System.out.println("Your new hand:");
        for (Card card : player.hand) {
            System.out.println(" - " + card);
        }
        System.out.println("--------------------");
        
        // Display player's score
        System.out.println("Your new score: " + player.getScore());
        System.out.println("--------------------\n");
    
        // Add delay for user to see the prompt before they choose
        delay();
    }

    /***** GAME METHODS *****/
    
    private void delay(){
        try {
            Thread.sleep(2000); // 2 seconds delay before the player can respond
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    

    // startGame(): Start the game by dealing 3 cards to each player
    public void startGame(){
        for (Player player : this.players){
            for(int i = 0; i < 3; i++){
                Card drawnCard = deck.removeCard();
                player.addCard(drawnCard);
            }
        }
        // Add the top card from the deck to the discard pile
        Card topCard = deck.removeCard();
        discardPile.addCard(topCard);
    }

    // nextPlayer(): Determine which player is next
    public int nextPlayerIndex(int playerIndex){
        playerIndex++;
        if (playerIndex >= this.players.size()) {
            playerIndex = 0; // Loop back to the first player
        }
        return playerIndex;
    }

    // drawDeckCard(src.domain.player.Player player, Scanner scan): src.domain.player.Player draws card from the deck
    public void drawDeckCard(Player player, Scanner scan){
        // Remove top card from the deck
        Card drawnCard = deck.removeCard();

        OUTER:
        while(true) {
            System.out.println("====================");
            System.out.println("src.domain.cards.Card Drawn!");
            System.out.println("====================");
            System.out.println("You drew: **" + drawnCard + "**");
            System.out.println("Do you want to keep this card? (y/n)");

            String choice = scan.next();

            switch (choice) {
                case "y" -> {
                    // Determine which card the player wants to discard from their hand
                    System.out.println("====================");
                    System.out.println("Discard a src.domain.cards.Card ");
                    System.out.println("====================");
                    System.out.println("Which card do you want to discard?");
                    System.out.println("(1) " + player.hand.get(0));
                    System.out.println("(2) " + player.hand.get(1));
                    System.out.println("(3) " + player.hand.get(2));
                    System.out.println("====================");
                    int discardChoice = Integer.parseInt(scan.next());
                    Card discardedCard = player.hand.get(discardChoice - 1);
                    // Add drawn card to player's hand and remove the discarded card
                    player.removeCard(discardedCard);
                    System.out.println("====================");
                    System.out.println("You discarded the " + discardedCard + ".");
                    System.out.println("====================");
                    player.addCard(drawnCard);
                    // Add discarded card to the discard pile
                    discardPile.addCard(discardedCard);
                    // Show new hand and score
                    displayNewHand(player);
                    break OUTER;
                }
                case "n" -> {
                    // Add the drawn card to the discard pile
                    System.out.println("====================");
                    System.out.println("You discarded the " + drawnCard + ".");
                    System.out.println("====================");
                    discardPile.addCard(drawnCard);
                    break OUTER;
                }
                default -> {
                    System.out.println("====================");
                    System.out.println("INVALID CHOICE. TRY AGAIN.");
                    System.out.println("====================");
                }
            }
        }
    }

    // drawDiscardCard(src.domain.player.Player player, Scanner scan): src.domain.player.Player draws card from the discard pile
    public void drawDiscardCard(Player player, Scanner scan){
        // Remove top card from the discard pile
        Card drawnCard = discardPile.removeCard();
        int discardChoice;

        while(true){
        System.out.println("You drew: **" + drawnCard + "**");
        
            // Determine which card the player wants to discard from their hand
            System.out.println("====================");
            System.out.println("Discard a src.domain.cards.Card ");
            System.out.println("====================");
            System.out.println("Which card do you want to discard?");
            System.out.println("(1) " + player.hand.get(0));
            System.out.println("(2) " + player.hand.get(1));
            System.out.println("(3) " + player.hand.get(2));
            System.out.println("====================");

            discardChoice = Integer.parseInt(scan.next());
            if(discardChoice == 1 || discardChoice == 2 || discardChoice == 3){
                break; // Break out of this loop if the choice is valid
            }
            // Loop continues if the choice was invalid
            System.out.println("====================");
            System.out.println("INVALID CHOICE. TRY AGAIN.");
            System.out.println("====================");
        }

        Card discardedCard = player.hand.get(discardChoice - 1);

        // Add drawn card to player's hand and remove the discarded card
        player.removeCard(discardedCard);
        System.out.println("====================");
        System.out.println("You discarded the " + discardedCard + ".");
        System.out.println("====================");
        player.addCard(drawnCard);

        // Add discarded card to the discard pile
        discardPile.addCard(discardedCard);

        // Show new hand and score
        displayNewHand(player);
    }

    // playerTurn(src.domain.player.Player player, Scanner scan): Handles the player's turn logic,
    // prompts the player for their action (draw from deck, draw from discard pile, or knock)
    public void playerTurn(Player player, Scanner scan){
        OUTER:
        while(true) {
            promptPlayerChoice();

            int choice = Integer.parseInt(scan.next());

            switch (choice) {
                case 1 -> {
                    // Draw from the deck
                    drawDeckCard(player, scan);
                    break OUTER;
                }
                case 2 -> {
                    // Draw from the discard pile
                    drawDiscardCard(player, scan);
                    break OUTER;
                }
                case 3 -> {
                    // src.domain.player.Player knocks if no other player has knocked (invalid knock otherwise)
                    if (this.playerKnocked) {
                        System.out.println("====================");
                        System.out.println("You have already knocked. Please choose another option.");
                        System.out.println("====================");
                        break; // Exit the current case and move to the default case
                    }
                    this.playerKnocked = true;
                    break OUTER;
                }
                default -> {
                    System.out.println("====================");
                    System.out.println("INVALID CHOICE. TRY AGAIN.");
                    System.out.println("====================");
                }
            }
        }
    } 

    // AITurn(AIPlayer aiPlayer, Scanner scan): Handles the AI's turn logic
    public void AITurn(AIPlayer aiPlayer, Scanner scan){
        // 1. If the AI player has a strong hand, the AI will knock if no other player has knocked
        if(!this.playerKnocked && aiPlayer.shouldKnock()){
            this.playerKnocked = true;
            delay();
            return;
        }

        // 2. If the AI decides to not knock, decide whether to take from discard or draw from the deck
        Card topDiscard = discardPile.peekTopCard();
        Card worstCard = aiPlayer.improveHand(topDiscard);

        // 3. If the card from the discard pile does not improve the hand, draw from the deck
        if(worstCard.equals(topDiscard)){
            System.out.println("\nAI src.domain.player.Player is drawing a card from the deck.");
            delay();

            Card drawnCard = deck.removeCard();
            worstCard = aiPlayer.improveHand(drawnCard);
            // If the drawn card does not improve the hand, discard it
            if(worstCard.equals(drawnCard)){ 
                // Add discarded card to the discard pile
                discardPile.addCard(drawnCard);
            }
            // Otherwise, exchange this card with the worst card in the AI's current hand
            else{
                aiPlayer.removeCard(worstCard);
                discardPile.addCard(worstCard);
                aiPlayer.addCard(drawnCard);
            }
            System.out.println("AI src.domain.player.Player is discarding the " + worstCard + ".");
            delay();
        }
        // 4. If the card from the discard pile does improve the hand, take it
        else{
            System.out.println("\nAI src.domain.player.Player is taking the " + discardPile.peekTopCard() + " from the discard pile.");
            delay();
            System.out.println("AI src.domain.player.Player is discarding the " + worstCard + ".");
            delay();
            discardPile.removeCard();
            aiPlayer.addCard(topDiscard);
            aiPlayer.removeCard(worstCard);
            discardPile.addCard(worstCard);
        }
        System.out.println("AI src.domain.player.Player's turn has ended.\n");
        delay();
    }

    // determineWinner(): Determine the winner based on which player has the highest score
    public Player determineWinner(){
        int best_score = -1;
        Player winner = null;
        for(Player player : this.players){
            if(player.getAutoWin()){ // Auto win condition
                winner = player;
                break;
            }
            if(player.getScore() > best_score){
                best_score = player.getScore();
                winner = player;
            }
        }
        return winner;
    }

    // playGame(): Main game loop where players take turns drawing cards until the game ends
    public void playGame(){
        startGame();
        this.gameRunning = true;
        int currentPlayerIndex = 0;
        Player currentPlayer = this.players.get(currentPlayerIndex);
        Scanner scan = new Scanner(System.in);

        // Main game loop (no player has knocked)
        while (this.gameRunning && !this.playerKnocked) {
            displayTurnInfo(currentPlayer);

            // Determine if the current player is an AI or a human player
            // src.domain.player.Player takes their turn
            if(currentPlayer instanceof AIPlayer aiPlayer){
                AITurn(aiPlayer, scan);
            }
            else{
                playerTurn(currentPlayer, scan);
            }

            // Check if a player has automatically won.
            if(currentPlayer != null && currentPlayer.getAutoWin()){
                this.gameRunning = false;
                break;
            }

            // Check if the deck is empty.
            if(deck.cards.isEmpty()){
                System.out.println("The deck is empty. The game is over.");
                this.gameRunning = false;
                break;
            }
            
            // Check if a player has knocked.
            if(this.playerKnocked){
                System.out.println("\n====================");
                System.out.println("A PLAYER HAS KNOCKED. THIS IS THE FINAL ROUND.");
                System.out.println("====================");
                delay();
                currentPlayerIndex = nextPlayerIndex(currentPlayerIndex);
                currentPlayer = this.players.get(currentPlayerIndex);
                break;
            }
            currentPlayerIndex = nextPlayerIndex(currentPlayerIndex);
            currentPlayer = this.players.get(currentPlayerIndex);
        }

        // Final round of the game (a player has knocked + game is still runnning)
        if(this.gameRunning && this.playerKnocked){
            for(int i = 0; i < players.size() - 1; i++){
                // Remaining players get one more turn
                displayTurnInfo(currentPlayer);
                if(currentPlayer instanceof AIPlayer aiPlayer){
                    AITurn(aiPlayer, scan);
                }
                else{
                    playerTurn(currentPlayer, scan);
                }

                // Check if the deck is empty.
                if(deck.cards.isEmpty()){
                    System.out.println("The deck is empty. The game is over.");
                    this.gameRunning = false;
                }

                currentPlayerIndex = nextPlayerIndex(currentPlayerIndex);
                currentPlayer = this.players.get(currentPlayerIndex);
            }
        }
    }
}