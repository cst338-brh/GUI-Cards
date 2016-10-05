/*
 * Heather McCabe
 * Brett Hansen
 * Randall Rood
 * CST 338
 * 10/1/16
 * Module 5: GUI Cards - Phase Three
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Math;

public class Assig5C
{
   static final private int NUM_CARDS_PER_HAND = 7;
   static final private int NUM_PLAYERS = 2;
   static private JLabel[] playedCardLabels, playLabelText, computerLabels; 
   static private JButton[] humanButtons;
   static private JButton messageButton;
   static private JLabel messageLabel;
   static private CardTable table;
   static private CardGameFramework highCardGame;
   static final private int computer = 0;
   static final private int human = 1;
   static private boolean gameOver;
   static private int computerCardIndex;
   static private int firstPlayer = 1;
   static private int humanScore = 0;
   static private int computerScore = 0;
   static private boolean cardChoosable = true;

   public static void main(String[] args) {
      // Set up the card images
      GUICard.loadCardIcons();
      
      // Create the game window
      table = new CardTable("High Card",NUM_CARDS_PER_HAND,NUM_PLAYERS);
      
      resetGame(); // Sets up a fresh game and starts it
      
      // Show the window
      table.setVisible(true);          
   }
   
   /**
    * Sets up the High Card game and components for a new deck/deal.
    */
   public static void resetGame() {      
      // Set up the game
      int numPacksPerDeck = 1;
      int numJokersPerPack = 0;
      int numUnusedCardsPerPack = 0;
      Card[] unusedCardsPerPack = null;

      highCardGame = new CardGameFramework( 
            numPacksPerDeck, numJokersPerPack,  
            numUnusedCardsPerPack, unusedCardsPerPack, 
            NUM_PLAYERS, NUM_CARDS_PER_HAND);
      
      // Reset scores and game status
      gameOver = false;
      cardChoosable = true;
      humanScore = 0;
      computerScore = 0;
      
      // Start the game         
      highCardGame.newGame();
      highCardGame.deal();
      
      // Reset the players' card image arrays
      computerLabels = new JLabel[NUM_CARDS_PER_HAND];
      humanButtons = new JButton[NUM_CARDS_PER_HAND];
      
      // Display the players' cards on the table
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
         // The computer's cards (backs only)
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         table.pnlComputerHand.add(computerLabels[i]);
         
         // The human's cards (face-up)
         humanButtons[i] = new JButton(GUICard.getIcon(highCardGame.getHand(human).inspectCard(i)));
         humanButtons[i].setMargin(new Insets(1,1,1,1));
         humanButtons[i].setActionCommand(Integer.toString(i));   // For action listener to know which button this is
         humanButtons[i].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if (cardChoosable){
                  playRound(Integer.parseInt(e.getActionCommand()));
               }
            }
         });
         
         table.pnlHumanHand.add(humanButtons[i]);
      }
      
      // Display blank cards in the play area panel
      playedCardLabels = new JLabel[NUM_PLAYERS];
      playedCardLabels[0] = new JLabel();
      playedCardLabels[1] = new JLabel();
      playLabelText = new JLabel[NUM_PLAYERS];
      playLabelText[0] = new JLabel("Computer: " + computerScore,JLabel.CENTER);
      playLabelText[1] = new JLabel("You: " + humanScore,JLabel.CENTER);
      table.pnlPlayArea.add(playedCardLabels[0]);
      table.pnlPlayArea.add(playedCardLabels[1]);
      table.pnlPlayArea.add(playLabelText[0]);
      table.pnlPlayArea.add(playLabelText[1]);
      
      // Set the round winner messages
      // Human won the round
      messageLabel = new JLabel();
      // Computer won round, create a button for them to start the next round
      messageButton = new JButton("I win this round! Click here to start the next round.");
      messageButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            if (cardChoosable == false){  
               cardChoosable = true;
               playRound(-1);
            }
         }
      });
      
      // Reset the turn; human always plays first
      firstPlayer = human;
      
      // Redraw the panels
      updateTable();
      
      // Set the human-wins message. This is set after the table is updated so it won't show at the start of the game.
      messageLabel = new JLabel("You won this round! Select a card to start the next round.");
   }

 
  /**
   * Plays a round of the game. If humanCardIndex is -1, the computer player will select a card first and 
   * this method will not continue. When the human player picks a card and humanCardIndex is not -1, this method will
   * calculate the winner. The cards will then be removed from the players' hands and displays and then will be 
   * displayed in the center play area. Finally, this method will call updateTable to redraw the game window.
   * @param humanCardIndex   the index of the card that the human player is playing; -1 if the computer should go first
   */
   public static void playRound(int humanCardIndex) {
      computerPlay(humanCardIndex);
      
      if (humanCardIndex != -1) {
         // Get Cards
         Card computerCard = highCardGame.playCard(computer,computerCardIndex);
         Card humanCard = highCardGame.playCard(human,humanCardIndex);
         
         // Determine the winner
         if (Card.valueIndex(humanCard.getValue()) > Card.valueIndex(computerCard.getValue())) {
            // Human wins
            humanScore++;
            firstPlayer = human;
         } else if (Card.valueIndex(humanCard.getValue()) < Card.valueIndex(computerCard.getValue())) {
            // Computer wins
            computerScore++;
            cardChoosable = false;
            firstPlayer = computer;
         } else {
            // Tie
            if (firstPlayer == human) {
               // Computer wins
               computerScore++;
               cardChoosable = false;
               firstPlayer = computer;
            } else {
               // Human wins
               humanScore++;
               firstPlayer = human;
            }
         }
         
         // Update the play area labels
         playedCardLabels[0] = new JLabel(GUICard.getIcon(computerCard));
         playedCardLabels[1] = new JLabel(GUICard.getIcon(humanCard));
         playLabelText[0] = new JLabel("Computer: " + computerScore,JLabel.CENTER);
         playLabelText[1] = new JLabel("You: " + humanScore,JLabel.CENTER);
         
         // Remove the computer's card from their labels
         for (int i = computerCardIndex; i < computerLabels.length - 1; i++) {
            computerLabels[i] = computerLabels[i+1];
         }
      
         computerLabels[computerLabels.length - 1] = null;
         
         // Remove the human's card from their buttons
         for (int j = humanCardIndex; j < humanButtons.length - 1; j++) {
            humanButtons[j] = humanButtons[j + 1];
            
            // Update the action command for button indexing
            if (humanButtons[j] != null) humanButtons[j].setActionCommand(Integer.toString(j));
         }
         humanButtons[humanButtons.length-1] = null;
         
         // Check if the game is over (hands are empty)
         if (humanButtons[0] == null){
            // The game is over, update the message button
            gameOver = true;
            messageButton = new JButton();
            
            if (humanScore > computerScore) {
               messageButton.setText("You won! Click to play again.");
            } else {
               messageButton.setText("Sorry, you lost! Click to play again.");
            }
            
            messageButton.addActionListener(new ActionListener() {
               public void actionPerformed(ActionEvent e)
               {
                  resetGame();
               }
            });
         }
         
         updateTable();
      }
   }
   
   /**
   * Re-populates and paints the panels in the game window. 
   */
   private static void updateTable() {
      // Update the play area panel
      table.pnlPlayArea.removeAll();
      for (JLabel label : playedCardLabels) table.pnlPlayArea.add(label);
      for (JLabel label : playLabelText) table.pnlPlayArea.add(label);
      
      // Update the message area panel
      table.pnlMessageArea.removeAll();
      if (messageButton != null && (firstPlayer == computer || gameOver)) {
         // Show a button to start the next round if the computer plays first or the game is over
         table.pnlMessageArea.add(messageButton);
      } else if (messageLabel != null) {
         // Show a message label that states that the human won
         table.pnlMessageArea.add(messageLabel);
      }
      
      // Update the human and computer hand panels
      table.pnlHumanHand.removeAll();
      table.pnlComputerHand.removeAll();
      for (JLabel label : computerLabels) if (label != null) table.pnlComputerHand.add(label);
      for (JButton button : humanButtons) if (button != null) table.pnlHumanHand.add(button);           
      
      // Redraw the table
      table.pnlPlayArea.revalidate();
      table.pnlMessageArea.revalidate();
      table.pnlComputerHand.revalidate();
      table.pnlHumanHand.revalidate();
      table.validate();
      table.repaint();
   }
   
   /**
    * Lets the computer player choose a card. If the computer is playing first in a round,
    * it will always choose its highest card. If the computer is playing second in a round,
    * it will attempt to choose the lowest-value card that can win. If it does not have a winning card, 
    * it will choose its lowest card.
    * @param value   the value of the card that was played first, or -1 if the computer is playing first.
    * @return        the index of the card to be played, or -1 if the computer has no cards.
    */
   public static void computerPlay(int humanCardIndex) {
      // Get the computer's hand and the number of cards in the computer's hand
      Hand compHand = highCardGame.getHand(computer);
      int numCards = compHand.getNumCards();
      
      if (numCards > 0) {
         if (humanCardIndex == -1) {
            // The computer is playing first, pick the highest card
            computerCardIndex = numCards - 1;    
            
            // Show the computer's card
            playedCardLabels[computer] = new JLabel(GUICard.getIcon(highCardGame.getHand(computer).inspectCard(computerCardIndex)));
            playedCardLabels[human] = new JLabel();
            
            updateTable();
         } else {
            // The computer is playing 2nd, pick the lowest card that can win
            int humanValue = Card.valueIndex(highCardGame.getHand(human).inspectCard(humanCardIndex).getValue());
            // If no card can win, just pick the lowest card
            computerCardIndex = 0;
            for (int i = 0; i < numCards; i++) {
               if (Card.valueIndex(compHand.inspectCard(i).getValue()) > humanValue) {
                  computerCardIndex = i;
                  break;
               }
            }
            
            // Go back to the round
            //playRound(humanCardIndex);
         }        

      } else {
         // The computer has no cards
         computerCardIndex = -1;
         }
   }
  
   private static class CardGameFramework
   {
      private static final int MAX_PLAYERS = 50;
   
      private int numPlayers;
      private int numPacks;            // # standard 52-card packs per deck
                                        // ignoring jokers or unused cards
      private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
      private int numUnusedCardsPerPack;  // # cards removed from each pack
      private int numCardsPerHand;        // # cards to deal each player
      private Deck deck;               // holds the initial full deck and gets
                                        // smaller (usually) during play
      private Hand[] hand;             // one Hand for each player
      private Card[] unusedCardsPerPack;   // an array holding the cards not used
                                            // in the game.  e.g. pinochle does not
                                         // use cards 2-8 of any suit
   
      public CardGameFramework( int numPacks, int numJokersPerPack,
            int numUnusedCardsPerPack,  Card[] unusedCardsPerPack,
            int numPlayers, int numCardsPerHand)
      {
         int k;
      
          // filter bad values
         if (numPacks < 1 || numPacks > 6)
            numPacks = 1;
         if (numJokersPerPack < 0 || numJokersPerPack > 4)
            numJokersPerPack = 0;
         if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
            numUnusedCardsPerPack = 0;
         if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
            numPlayers = 4;
         // one of many ways to assure at least one full deal to all players
         if  (numCardsPerHand < 1 ||
            numCardsPerHand >  numPacks * (52 - numUnusedCardsPerPack)
            / numPlayers )
            numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;
      
         // allocate
         this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
         this.hand = new Hand[numPlayers];
         for (k = 0; k < numPlayers; k++)
            this.hand[k] = new Hand();
         deck = new Deck(numPacks);
      
         // assign to members
         this.numPacks = numPacks;
         this.numJokersPerPack = numJokersPerPack;
         this.numUnusedCardsPerPack = numUnusedCardsPerPack;
         this.numPlayers = numPlayers;
         this.numCardsPerHand = numCardsPerHand;
         for (k = 0; k < numUnusedCardsPerPack; k++)
            this.unusedCardsPerPack[k] = unusedCardsPerPack[k];
      
      // prepare deck and shuffle
          newGame();
       }
      
       // constructor overload/default for game like bridge
       public CardGameFramework()
       {
          this(1, 0, 0, null, 4, 13);
       }
      
       public Hand getHand(int k)
       {
          // hands start from 0 like arrays
      
      // on error return automatic empty hand
          if (k < 0 || k >= numPlayers)
             return new Hand();
      
          return hand[k];
       }
      
       public Card getCardFromDeck() { return deck.dealCard(); }
      
       public int getNumCardsRemainingInDeck() { return deck.getNumCards(); }
      
       public void newGame()
       {
          int k, j;
      
          // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();
      
      // restock the deck
      deck.init(numPacks);
      
      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard( unusedCardsPerPack[k] );
      
      // add jokers
      for (k = 0; k < numPacks; k++)
         for ( j = 0; j < numJokersPerPack; j++)
            deck.addCard( new Card('X', Card.Suit.values()[j]) );
      
      // shuffle the cards
          deck.shuffle();
       }
      
       public boolean deal()
       {
          // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;
      
      // clear all hands
          for (j = 0; j < numPlayers; j++)
             hand[j].resetHand();
      
          enoughCards = true;
          for (k = 0; k < numCardsPerHand && enoughCards ; k++)
          {
             for (j = 0; j < numPlayers; j++)
                if (deck.getNumCards() > 0)
                   hand[j].takeCard( deck.dealCard() );
                else
                {
                   enoughCards = false;
                   break;
                }
          }
      
          return enoughCards;
       }
      
       void sortHands()
       {
          int k;
      
          for (k = 0; k < numPlayers; k++)
             hand[k].sort();
       }
      
       Card playCard(int playerIndex, int cardIndex)
       {
          // returns bad card if either argument is bad
      if (playerIndex < 0 ||  playerIndex > numPlayers - 1 ||
          cardIndex < 0 || cardIndex > numCardsPerHand - 1) {
         //Creates a card that does not work
         return new Card('M', Card.Suit.SPADES);      
      }
       
          // return the card played
          return hand[playerIndex].playCard(cardIndex);
       
       }
         
       boolean takeCard(int playerIndex)
       {
          // returns false if either argument is bad
          if (playerIndex < 0 || playerIndex > numPlayers - 1)
             return false;
         
           // Are there enough Cards?
           if (deck.getNumCards() <= 0)
              return false;
      
           return hand[playerIndex].takeCard(deck.dealCard());
       }
   }
   
   private static class Card {
      static public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES };
      static private char[] valueRanks = {'2','3','4','5','6','7','8','9','T','J','Q','K','A','X'};
      private char value;
      private Suit suit;
      private boolean errorFlag;
      
      /** 
       * Default Constructor. Initializes Card with value 'A' and suit SPADES.
       */
      public Card() {
         this('A',Suit.SPADES);
      }
      
      /** 
       * Constructor. Initializes Card with given value and Suit.
       * @param value   a char from 1-9,T,J,Q,K,A,'X'.
       * @param suit    a value from the Suit enum (CLUBS,DIAMONDS,HEARTS,SPADES).
       */
      public Card(char value, Suit suit) {
         set(value,suit);
      }
      
      /** 
       * Provides a String representation of the Card.
       * @return        a String in the form of "[VALUE] of [SUIT]".
       */
      public String toString() {
         if (errorFlag == true) {
            return "[ invalid ]";
         }else {
            return value + " of " + suit;
         }
      }
      
      /** 
       * Updates the suit and value of the Card after validating data. 
       * The errorFlag is set to true if the provided data is not valid.
       * @param value   a char from 1-9,T,J,Q,K,A,'X'.
       * @param suit    a value from the Suit enum (CLUBS,DIAMONDS,HEARTS,SPADES).
       * return         true if valid value and suit were set, otherwise false.
       */
      public boolean set(char value, Suit suit) {
         boolean isValid = isValid(value,suit);
         
         if (isValid) {
            this.value = Character.toUpperCase(value);
            this.suit = suit;
         }
         
         errorFlag = !isValid; // errorFlag is true if Card is not valid
         
         return isValid;
      }
      
      /**
       * Gets the Card's value.
       * @return        the Card's value.
       */
      public char getValue() {
         return value;
      }
      
      /**
       * Gets the Card's Suit.
       * @return        the Card's Suit.
       */
      public Suit getSuit() {
         return suit;
      }
      
      /**
       * Gets the status of the errorFlag.
       * @return        true if Card is invalid, otherwise false.
       */
      public boolean getErrorFlag() {
         return errorFlag;
      }
      
      /**
       * Compares two Card objects for equality.
       * @param card    a Card to compare to this Card.
       * @return        true if the Cards have equal value and Suit, otherwise false.
       */
      public boolean equals(Card card) {
         return (this.getSuit() == card.getSuit()) && (this.getValue() == card.getValue());
      }
      
      /**
       * Sorts an array of Cards by value from smallest to largest. 
       * This is done using a bubble sort algorithm.
       * @param cards   the cards to be sorted.
       */
      public static void arraySort(Card[] cards) {
         boolean sorted;
         
         // Keep looping through the cards and sorting until a full pass without any swaps.
         do {
            sorted = true; // This will be set to false if any cards are out of order.
            
            for (int i = 0; i < cards.length - 1; i++) {
               if (valueIndex(cards[i].getValue()) > valueIndex(cards[i+1].getValue())) {
                  // The first card is larger, swap them
                  Card tempCard = cards[i];
                  cards[i] = cards[i+1];
                  cards[i+1] = tempCard;
                  sorted = false;
               }
            }
         } while (!sorted);
      }
      
      /**
       * Gets the index of a value in the valueRanks array.
       * @param value   the value to search for.
       * @return        the index of the value in the valueRanks array.
       */
      private static int valueIndex(char value) {
         for (int i = 0; i < valueRanks.length; i++) {
            if (value == valueRanks[i]) return i;
         }
         return -1; // Returned if value not found in array
      }
      
      /**
       * A helper method to validate Card data.
       * @param value   a char representing the value of the card.
       * @param suit    a Suit enum object.
       * @return        true if data is valid, otherwise false.
       */
      private boolean isValid(char value, Suit suit) {
         boolean isValidValue = false;
         
         // Check if value is one of the valid characters using the valueRanks array
         for (char v : valueRanks) {
            if (Character.toUpperCase(value) == v) {
               isValidValue = true;
               break;
            }
         }
         
         return isValidValue; // Note: Suit is an enum type and does not need to be validated
      }
      
   }
   
   private static class Hand {
      public static final int MAX_CARDS = 57;
      private Card[] myCards;                 
      private int numCards;                   
      
      /**
       * Default Constructor. Initializes empty Hand.
       */
      public Hand(){
         resetHand(); //  Initializes empty hand
      }
      
      /**
       * Empties the array of Cards and resets the card counter.
       */
      public void resetHand(){
         myCards = new Card[MAX_CARDS];
         numCards = 0;
      }
      
      /**
       * Adds a given Card to the array if there is space.
       * @param card    a Card to add to the Hand.
       * @return        true if the Card was added to the Hand, otherwise false.
       */
      public boolean takeCard(Card card){
         if(numCards < MAX_CARDS){
            myCards[numCards] = new Card(card.getValue(),card.getSuit());  // Creates a copy of the Card to add to the array
            numCards++;
            return true;
         } else {
            // The Hand is full
            return false;
         }
      }
      
      /**
       * Gets the last Card in the array. Removes the Card from the array.
       * and decrements the counter.
       * @return        the Card to play.
       */
      public Card playCard() {
         // Create a copy of the Card to be returned
         Card nextCard = new Card(myCards[numCards-1].getValue(), myCards[numCards-1].getSuit());
         
         // Remove the Card from the array and decrement the card count
         myCards[numCards-1] = null;
         numCards--;
         
         return nextCard;
      }
      
      /**
       * Gets the Card at the given index and removes it from the array. This also decrements
       * the counter. If the given index is invalid, returns an invalid card and does not remove
       * anything.
       * @param k    the index of the Card to be removed and returned
       * @return     a copy of the Card
       */
      public Card playCard(int k) {
         if (k < numCards) {
            // Index k is valid, remove card from array, decrement card count, and return card
            Card returnCard = new Card(myCards[k].getValue(), myCards[k].getSuit());
            
            // Move cards down to fill in "space" of removed card
            for (int i = k; i < numCards - 1; i++) {
               myCards[i] = myCards[i+1];
            }
            myCards[numCards - 1] = null;
            
            numCards--;
            
            return returnCard;
         } else {
            // Index k was invalid, return invalid card
            return new Card('0',Card.Suit.SPADES);
         }
      }
      
      /**
       * Returns a String representation of the Cards in the Hand, separated by commas.
       * @return     a String representation of all of the Cards in the Hand.
       */
      public String toString(){
         String returnString = "";
         
         if (numCards > 0) {
            // Get the String version of each card in the array and add to returnString
            for(int i = 0; i < numCards; i++) {
               returnString += myCards[i].toString() + ", ";
            }      
            // Remove the last space and comma from the String for cleaner output
            returnString = returnString.substring(0, returnString.length()-2);
         }
         
         return returnString;
      }
      
      /**
       * Gets the number of Cards in the Hand
       * @return        the number of Cards in the Hand.
       */
      public int getNumCards(){
         return numCards;
      }
      
      /**
       * Gets the Card at a specific position in the Hand.
       * @param k       the position of the desired Card in the Hand's array.
       * @return        the Card.
       */
      public Card inspectCard(int k){
         // Return a copy of the Card if the given index is valid
         if (k < numCards) {
            return new Card(myCards[k].getValue(), myCards[k].getSuit());   
         } else {
            // Given index is invalid, generate and return an invalid card
            return new Card('-',Card.Suit.SPADES);
         }
      }
      
      /**
       * Sorts the Cards in this Hand by value from least to greatest.
       */
      public void sort() {
         Card.arraySort(myCards);
      }
   
   }
   
   private static class Deck {
      private static Card[] masterPack = new Card[56];
      private Card[] cards;
      private int topCard;
      private int numPacks;
      
      /**
       * Default constructor. Creates a Deck with 1 pack of Cards.
       */
      public Deck(){
         this(1);
      }
      
      /**
       * Constructor. Creates a Deck with a given number of packs of Cards.
       * @param numPacks   the number of packs to use for the Deck.
       */
      public Deck (int numPacks) {
         allocateMasterPack();
         init(numPacks);
      }
      
      /**
       * Sets up the Deck with the given number of packs of cards.
       */
      public void init(int numPacks){
         cards = new Card[numPacks * 52];
         
         for (int i = 0; i < cards.length; i++){
            cards[i] = masterPack[i % 52];
         }
         
         this.numPacks = numPacks;
         topCard = cards.length - 1;
      }
      
      /**
       * Shuffles the Deck of Cards by randomly swapping the position of Cards.
       */
      public void shuffle(){
         for (int i = 0; i < numPacks*52; i++) {
            // Generate a random int from 0 to the total number of Cards
            int randInt = (int)(Math.random() * numPacks * 52);
            
            // Swap the position of the current Card with another random Card
            Card tmpCard = cards[i];
            cards[i] = cards[randInt];
            cards[randInt] = tmpCard;
         }
      }
      
      /**
       * Deals the top Card of the Deck and removes it from the Deck.
       * @return     a Card.
       */
      public Card dealCard(){
         if (cards.length > 0) {
            // Create a copy of the Card to be returned
            Card dealtCard = new Card(cards[topCard-1].getValue(), cards[topCard-1].getSuit());
            
            // "Remove" the Card from the cards array by setting it to null
            cards[topCard] = null;
            topCard--;
            
            return dealtCard;
         } else {
            // The deck is empty, return an invalid card
            return new Card('X',Card.Suit.SPADES);
         }      
      }
      
      /**
       * Gets the position "top" Card in the Deck. This is the last Card in the cards array.
       */
      public int getTopCard() {
         return topCard;
      }
      
      /**
       * Gets the Card at the given position in the Deck. Returns an invalid Card if the given position 
       * is invalid.
       * @param k    the index of the Card to be returned.
       * @return     the Card at the given index, or an "invalid" Card if the index was invalid.
       */
      public Card inspectCard(int k) {
         if (k <= topCard){
            // Index was valid, return a copy of the Card
            return new Card(cards[k].getValue(),cards[k].getSuit());
         }
         else{
            // Index was invalid, return an invalid card
            return new Card('X',Card.Suit.SPADES);
         }
      }   
      
      /**
       * Adds a card to this deck. If the given card is already in this deck, it
       * will not be added.
       * @param card    a Card to add to this Deck.
       * @return        true if the card was added, otherwise false.
       * @see Card
       */
      public boolean addCard(Card card) {
         // Check if the Card is already in the deck.
         // There can be one of each Card per pack of cards.
         int cardsFound = 0;
         for (int i = 0; i <= topCard; i++) {
            if (card.equals(cards[i])) cardsFound++;
         }
         if (cardsFound >= numPacks) return false;
         
         // Card wasn't already in this deck, so it can be added to the top
         topCard++;
         cards[topCard] = card; 
         return true;
      }
      
      /**
       * Removes a given Card from this Deck. The current top Card of this Deck 
       * takes its place.
       * @param card       a Card to remove from this Deck.
       * @return           true if the Card was found and removed, otherwise false.
       */
      public boolean removeCard(Card card) {
         // Find the position of the Card to be removed
         for (int i = 0; i <= topCard; i++) {
            if (card.equals(cards[i])) {
               // The Card was found, put the top Card in this position instead
               cards[i] = cards[topCard];
               topCard--;
               return true;
            }
         }
         return false; // The Card was not found in this Deck
      }
      
      /**
       * Sorts the Cards in this Deck by value, from lowest to highest. This sorting
       * is handled by arraySort method of the Card class.
       * @see Card
       */
      public void sort() {
         Card.arraySort(cards);
      }
      
      /**
       * Gets the number of cards in this pack.
       * @return        the number of cards.
       */
      public int getNumCards() {
         return topCard + 1;
      }
      
      /**
       * Generates a "master pack" of Cards so that Cards don't need to be created repeatedly. If this 
       * has already been done, nothing happens.
       */
      private static void allocateMasterPack() {
         if (masterPack[0] == null) {
            char[] possValues = {'2','3','4','5','6','7','8','9','T','J','Q','K','A'};
            Card.Suit[] possSuits = {Card.Suit.HEARTS, Card.Suit.CLUBS, Card.Suit.DIAMONDS, Card.Suit.SPADES};
            int cardCount = 0;
            
            for (int i = 0; i < 4; i++) {         
               for (int j = 0; j < 13; j++){
                  masterPack[cardCount] = new Card(possValues[j],possSuits[i]);
                  cardCount++;      
               }
            }
         }
      }
   }
   
   private static class GUICard {
      private static String[] values = {"2","3","4","5","6","7","8","9","T","J","Q","K","A","X"};
      private static String[] suits = {"C","D","H","S"};
      private static Icon[][] iconCards = new ImageIcon[values.length][suits.length];
      private static Icon iconBack;
      private static boolean iconsLoaded = false;
            
      /**
       * Loads the card images as Icons. If this has already been done, nothing happens.
       */
      static public void loadCardIcons() {
         if (!iconsLoaded) {
            for (int suit = 0; suit < suits.length; suit++) {
               for (int value = 0; value < values.length; value++) {
                  iconCards[value][suit] = new ImageIcon("src/images/" + intToCardValue(value) + intToCardSuit(suit) + ".gif");
               }
            }
            
            iconBack = new ImageIcon("src/images/BK.gif");
               
            iconsLoaded = true;
         }
      }
      
      /**
       * Gets the Icon for the given Card's face.
       * @param card    the Card to get the Icon for.
       * @return        the card face Icon.
       */
      static public Icon getIcon(Card card) {
         
         int cardVal = cardValueToInt(card.getValue());
         int suitVal = cardSuitToInt(card.getSuit());
         
         // Get the correct card Icon from the array
         if (cardVal <= 13 && cardVal >= 0 && suitVal >= 0 && suitVal <= 3) {
            return iconCards[cardValueToInt(card.getValue())][cardSuitToInt(card.getSuit())];
         }
         else
            return iconBack;
      }
         
      /**
       * Gets the Icon for the card back.  
       * @return  the card back Icon.
       */
      static public Icon getBackCardIcon() {
          return iconBack;
      }
         
      /**
       * Converts an int into a a card suit. This is useful for looping through all suits. 
       * @param k    the position of the card suit in the internal suits array.
       * @return     a String representing the suit, or an empty string if the position was invalid.
       */
      static private String intToCardSuit(int k) {
         if (k >= 0 && k < 4) {
            // Return the card suit at the given index
            return suits[k];
         } else {
            // Index was invalid
            return "";
         }
      }
         
      /**
       * Converts an int into a a card value. This is useful for looping through all values. 
       * @param k    the position of the card value in the internal values array.
       * @return     a String representing the value, or an empty string if the position was invalid.
       */
      static private String intToCardValue(int k) {
         if (k >= 0 && k < 14) {
            // Return the card value at the given index
            return values[k];
         } else {
            // Index was invalid
            return "";
         }
      }
         
         /**
       * Converts a card suit into an int representing its position in the internal suits array.
       * @param value   a Suit enum object from the Card class.
       * @return        the index of the suit in the internal suits array.
       * @see Card
       */
      static private int cardSuitToInt(Card.Suit suit) {
         for (int i = 0; i < suits.length; i++) {
            // Look for the first letter of the suit in the suits array
            if (suits[i].equals(suit.name().substring(0,1))) return i;
         }
         return -1; // Suit was invalid
      }
      
      /**
       * Converts a card value into an int representing its position in the internal values array.
       * @param value   a char representing the card's value.
       * @return        the index of the value in the internal values array.
       */
      static private int cardValueToInt(char value) {
         for (int i = 0; i < values.length; i++) {
            if (values[i].equals(String.valueOf(value))) return i;
         }
         return -1; // Value was invalid
      }
   
   }
   
   private static class CardTable extends JFrame {
      static final int MAX_CARDS_PER_HAND = 56;
      static final int MAX_PLAYERS = 2;
      
      private int numCardsPerHand;
      private int numPlayers;
      
      public JPanel pnlComputerHand, pnlHumanHand, pnlCenter, pnlPlayArea, pnlMessageArea;
      
      /**
       * Constructor. Sets up the main JFrame with JPanels for each player 
       * and a play area. 
       * If numPlayers is 1, there will only be
       * a human player. If numPlayers is 2, there will be a human player
       * and a computer player. Any other value of numPlayers will result in
       * an empty default JFrame.
       * @param title            the title for the game window.
       * @param numCardsPerHand  the number of cards each player will have on the table.
       * @param numPlayers       the number of players.
       */
      public CardTable(String title, int numCardsPerHand, int numPlayers) {
         super(title);
         
         if (numPlayers <= MAX_PLAYERS && numPlayers > 0 
               && numCardsPerHand <= MAX_CARDS_PER_HAND && numCardsPerHand > 0) {
            
            // Set up the main window
            setLayout(new BorderLayout());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Set up the computer player JPanel
            if (numPlayers > 1) {
               pnlComputerHand = new JPanel();
               pnlComputerHand.setBorder(BorderFactory.createTitledBorder("Computer Hand"));
               add(pnlComputerHand,BorderLayout.NORTH);
            }
            
            // Set up the center area JPanel to manage the play/message areas
            pnlCenter = new JPanel();
            pnlCenter.setLayout(new BorderLayout());
            add(pnlCenter,BorderLayout.CENTER);
            
            // Set up the play area JPanel
            pnlPlayArea = new JPanel();
            pnlPlayArea.setLayout(new GridLayout(3,numPlayers));
            pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));
            pnlPlayArea.setPreferredSize(new Dimension(90 * numCardsPerHand,600));
            //add(pnlPlayArea,BorderLayout.CENTER);
            pnlCenter.add(pnlPlayArea,BorderLayout.CENTER);
            
            // Set up a message area JPanel
            pnlMessageArea = new JPanel();
            pnlMessageArea.setPreferredSize(new Dimension(90 * numCardsPerHand,50));
            //add(pnlMessageArea,BorderLayout.LINE_END);
            pnlCenter.add(pnlMessageArea,BorderLayout.SOUTH);
            
            // Set up the human player JPanel
            pnlHumanHand = new JPanel();
            pnlHumanHand.setBorder(BorderFactory.createTitledBorder("Your Hand"));
            add(pnlHumanHand,BorderLayout.SOUTH);
            
            pack();
            setLocationRelativeTo(null);
         }
      }
      
      /**
       * Gets the number of cards per hand for this CardTable.
       * @return     the number of cards per hand.
       */
      public int getNumCardsPerHand() {
         return numCardsPerHand;
      }
      
      /**
       * Gets the number of players for this CardTable.
       * @return     the number of players.
       */
      public int getNumPlayers() {
         return numPlayers;
      }
   }
}