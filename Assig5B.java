/*
 * Heather McCabe
 * Brett Hansen
 * Randall Rood
 * CST 338
 * 10/1/16
 * Module 5: GUI Cards
 */

import javax.swing.*;
import java.lang.Math;


public class Assig5B {
   
   static int NUM_CARDS_PER_HAND = 7;
   static int NUM_PLAYERS = 2;
   static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
   static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];  
   static JLabel[] playedCardLabels  = new JLabel[NUM_PLAYERS]; 
   static JLabel[] playLabelText  = new JLabel[NUM_PLAYERS]; 

   public static void main(String[] args) {
      // Set up JFrame
      
      // Create labels for each player
      
      // Add labels to the panels
      // Use GridLayout to lay out the cards nicely
      
      // Pick 2 random cards to display in the middle for now using generateRandomCard()
      
      // Show the window
   }
   
   //static Card generateRandomCard() {
      // Return a new random Card for the main to use for its game
  // }
}

class CardTable extends JFrame {
      static final int MAX_CARDS_PER_HAND = 56;
      static final int MAX_PLAYERS = 2;
      
      private int numCardsPerHand;
      private int numPlayers;
      
      public JPanel pnlComputerHand, pnlHumanHand, pnlPlayArea;
      
      public CardTable() {
        // this.CardTable("High Card",5,2);
         
         // Set up JFrame
         // Add JPanels for each player to the JFrame...
         // ...(human player at the bottom, computer player at the top, PlayArea in the middle)
      }

      public CardTable(String title, int numCardsPerHand, int numPlayers) {
         // Another constructor to set up the game with non-default values
         // Validate input
         // See notes from default constructor above
      }
      
      public int getNumCardsPerHand() {
         return numCardsPerHand;
      }
      
      public int getNumPlayers() {
         return numPlayers;
      }
      
   }

class GUICard {
   private static String[] values = {"2","3","4","5","6","7","8","9","T","J","Q","K","X"};
   private static String[] suits = {"C","D","H","S"};
   private static Icon[][] iconCards = new ImageIcon[values.length][suits.length];
   private static Icon iconBack;
   private static boolean iconsLoaded = false;
      
   public GUICard() {
      loadCardIcons();
   }
   
   /**
    * Loads the card images as Icons. If this has already been done, nothing happens.
    */
   static void loadCardIcons() {
      if (!iconsLoaded) {
         for (int suit = 0; suit < 4; suit++) {
            for (int value = 0; value < 14; value++) {
               iconCards[suit][value] = new ImageIcon("CardsImages/" + intToCardValue(value) + intToCardSuit(suit) + ".gif");
            }
         }
          
         iconBack = new ImageIcon("CardImages/BK.gif");
            
         iconsLoaded = true;
      }
   }
   
   /**
    * Gets the Icon for the given Card's face.
    * @param card    the Card to get the Icon for
    * @return        the card face Icon
    */
   static public Icon getIcon(Card card) {
      // Get the correct card Icon from the array
      return iconCards[cardValueToInt(card.getValue())][cardSuitToInt(card.getSuit())];
   }
      
   /**
    * Gets the Icon for the card back.  
    * @return  the card back Icon
    */
   static public Icon getBackCardIcon() {
       return iconBack;
   }
      
   /**
    * Converts an int into a a card suit. This is useful for looping through all suits. 
    * @param k    the position of the card suit in the internal suits array
    * @return     a String representing the suit, or an empty string if the position was invalid
    */
   static private String intToCardSuit(int k) {
      if (k > 0 && k < 4) {
         // Return the card suit at the given index
         return suits[k];
      } else {
         // Index was invalid
         return "";
      }
   }
      
   /**
    * Converts an int into a a card value. This is useful for looping through all values. 
    * @param k    the position of the card value in the internal values array
    * @return     a String representing the value, or an empty string if the position was invalid
    */
   static private String intToCardValue(int k) {
      if (k > 0 && k < 14) {
         // Return the card value at the given index
         return values[k];
      } else {
         // Index was invalid
         return "";
      }
   }
      
      /**
    * Converts a card suit into an int representing its position in the internal suits array.
    * @param value   a Suit enum object from the Card class
    * @return        the index of the suit in the internal suits array
    * @see Card
    */
   static private int cardSuitToInt(Card.Suit suit) {
      for (int i = 0; i < suits.length; i++) {
         // Look for the first letter of the suit in the suits array
         if (suits[i] == suit.name().substring(0,1)) return i;
      }
      return -1; // Suit was invalid
   }
   
   /**
    * Converts a card value into an int representing its position in the internal values array.
    * @param value   a char representing the card's value
    * @return        the index of the value in the internal values array
    */
   static private int cardValueToInt(char value) {
      for (int i = 0; i < values.length; i++) {
         if (values[i] == String.valueOf(value)) return i;
      }
      return -1; // Value was invalid
   }

}

class Card {
   public enum Suit { CLUBS, DIAMONDS, HEARTS, SPADES };
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
    * @param value   A char from 1-9,T,J,Q,K,A
    * @param suit    A value from the Suit enum (CLUBS,DIAMONDS,HEARTS,SPADES)
    */
   public Card(char value, Suit suit) {
      set(value,suit);
   }
   
   /** 
    * Provides a String representation of the Card.
    * @return        A String in the form of "[VALUE] of [SUIT]"
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
    * @param value   a char from 1-9,T,J,Q,K,A
    * @param suit    a value from the Suit enum (CLUBS,DIAMONDS,HEARTS,SPADES)
    * @return        true if valid value and suit were set, otherwise false
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
    * @return        the Card's value
    */
   public char getValue() {
      return value;
   }
   
   /**
    * Gets the Card's Suit.
    * @return        the Card's Suit
    */
   public Suit getSuit() {
      return suit;
   }
   
   /**
    * Gets the status of the errorFlag.
    * @return        true if Card is invalid, otherwise false
    */
   public boolean getErrorFlag() {
      return errorFlag;
   }
   
   /**
    * Compares two Card objects for equality.
    * @param card    a Card to compare to this Card
    * @return        true if the Cards have equal value and Suit, otherwise false
    */
   public boolean equals(Card card) {
      return (this.getSuit() == card.getSuit()) && (this.getValue() == card.getValue());
   }
   
   /**
    * A helper method to validate Card data.
    * @param value   a char representing the value of the card
    * @param suit    a Suit enum object
    * @return        true if data is valid, otherwise false
    */
   private boolean isValid(char value, Suit suit) {
      boolean isValidValue = false;
      char[] values = {'1','2','3','4','5','6','7','8','9','T','J','Q','K','A'}; // Allowable values
      
      // Check if value is one of the valid characters
      for (char v : values) {
         if (Character.toUpperCase(value) == v) {
            isValidValue = true;
            break;
         }
      }
      
      return isValidValue; // Note: Suit is an enum type and does not need to be validated
   }
   
}

class Hand {
   public static final int MAX_CARDS = 57;   //Constant set for class to terminate program
   private Card[] myCards;                   //An array of cards
   private int numCards;                     //The number of cards in "Hand"
   
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
    * @param card    A Card to add to the Hand
    * @return        True if the Card was added to the Hand, otherwise false
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
    * Gets the last Card in the array. Removes the Card from the array
    * and decrements the counter.
    * @return        The Card to play
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
    * @return     a String representation of all of the Cards in the Hand
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
    * @return        The number of Cards in the Hand.
    */
   public int getNumCards(){
      return numCards;
   }
   
   /**
    * Gets the Card at a specific position in the Hand.
    * @param k       The position of the desired Card in the Hand's array
    * @return        The Card
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

}

class Deck {
   private static Card[] masterPack = new Card[52];
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
    * @param numPacks   the number of packs to use for the Deck
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
    * @return     a Card
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
    * @param k    the index of the Card to be returned
    * @return     the Card at the given index, or an "invalid" Card if the index was invalid
    */
   Card inspectCard(int k) {
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


