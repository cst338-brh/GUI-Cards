/*
 * Heather McCabe
 * Brett Hansen
 * Randall Rood
 * CST 338
 * 10/1/16
 * Module 5: GUI Cards - Phase Two
 */

import javax.swing.*;
import java.awt.*;
import java.lang.Math;

public class Assig5B {
   
   static final int NUM_CARDS_PER_HAND = 7;
   static final int NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 

   public static void main(String[] args) {
      // Create the game window
      CardTable table = new CardTable("High Card",NUM_CARDS_PER_HAND,NUM_PLAYERS);
      
      // Set up the card images
      GUICard.loadCardIcons();
      
      // Create the card deck and shuffle
      Deck deck = new Deck(1);
      deck.shuffle();
      
      // Deal cards and display on table
      Hand playerHand = new Hand();
      Hand computerHand = new Hand();
      for (int i = 0; i < NUM_CARDS_PER_HAND; i++) {
         // Deal to human player and put card image on table
         playerHand.takeCard(deck.dealCard());
         humanLabels[i] = new JLabel(GUICard.getIcon(playerHand.inspectCard(i)));
         table.pnlHumanHand.add(humanLabels[i]);
         
         // Deal to computer and put card back image on table
         computerHand.takeCard(deck.dealCard());
         computerLabels[i] = new JLabel(GUICard.getBackCardIcon());
         table.pnlComputerHand.add(computerLabels[i]);
      }
      
      // Put 2 cards in the play area just see we can see what it looks like
      playedCardLabels[0] = new JLabel(GUICard.getIcon(deck.dealCard()));
      playedCardLabels[1] = new JLabel(GUICard.getIcon(deck.dealCard())); 
      table.pnlPlayArea.add(playedCardLabels[0]);
      table.pnlPlayArea.add(playedCardLabels[1]);
      
      // Label the play area cards
      playLabelText[0] = new JLabel("Computer",JLabel.CENTER);
      playLabelText[1] = new JLabel("You",JLabel.CENTER);
      table.pnlPlayArea.add(playLabelText[0]);
      table.pnlPlayArea.add(playLabelText[1]);
      
      // Show the window
      table.setVisible(true);
   }
}

class CardTable extends JFrame {
      static final int MAX_CARDS_PER_HAND = 56;
      static final int MAX_PLAYERS = 2;
      
      private int numCardsPerHand;
      private int numPlayers;
      
      public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
      
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
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            // Set up the computer player JPanel
            if (numPlayers > 1) {
               pnlComputerHand = new JPanel();
               pnlComputerHand.setBorder(BorderFactory.createTitledBorder("Computer Hand"));
               add(pnlComputerHand,BorderLayout.NORTH);
            }
            
            // Set up the play area JPanel
            pnlPlayArea = new JPanel();
            pnlPlayArea.setLayout(new GridLayout(2,numPlayers));
            pnlPlayArea.setBorder(BorderFactory.createTitledBorder("Playing Area"));
            pnlPlayArea.setPreferredSize(new Dimension(85 * numCardsPerHand,450));
            add(pnlPlayArea,BorderLayout.CENTER);
            
            // Set up the human player JPanel
            pnlHumanHand = new JPanel();
            pnlHumanHand.setBorder(BorderFactory.createTitledBorder("Your Hand"));
            add(pnlHumanHand,BorderLayout.SOUTH);
            
            pack();
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

class GUICard {
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
      // Get the correct card Icon from the array
      return iconCards[cardValueToInt(card.getValue())][cardSuitToInt(card.getSuit())];
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

class Card {
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

class Hand {
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
   public Card playCard(){
      // Create a copy of the Card to be returned
      Card nextCard = new Card(myCards[numCards-1].getValue(), myCards[numCards-1].getSuit());
      
      // Remove the Card from the array and decrement the card count
      myCards[numCards-1] = null;
      numCards--;
      
      return nextCard;
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

class Deck {
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


