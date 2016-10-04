/*
 * Heather McCabe
 * Brett Hansen
 * Randall Rood
 * CST 338
 * 10/1/16
 * Module 5: GUI Cards - Phase One
 */

import javax.swing.*;
import java.awt.*;
   
class Assig5 
{
   // static for the 57 icons and their corresponding labels
   // normally we would not have a separate label for each card, but
   // if we want to display all at once using labels, we need to.
   
   static final int NUM_CARD_IMAGES = 57; // 52 + 4 jokers + 1 back-of-card image
   static Icon[] icon = new ImageIcon[NUM_CARD_IMAGES];
   
   private static String[] values = {"2","3","4","5","6","7","8","9","T","J","Q","K","A","X"};
   private static String[] suits = {"C","D","H","S"};

   static boolean iconsLoaded = false;
   //private static Icon[][] iconCards = new ImageIcon[values.length][suits.length];
   //private static Icon iconBack;   
   
   static public void loadCardIcons() 
   {
	   if (iconsLoaded == false) {
		   for (int suit = 0; suit < suits.length; suit++) {
			   int suitSkip = suit * (values.length);
			   for (int value = 0; value < values.length; value++) {
				   icon[suitSkip + value] = new ImageIcon("src/images/" + turnIntIntoCardValue(value) + turnIntIntoCardSuit(suit) + ".gif"); 
			   }
		   }
	   }
       icon[56] = new ImageIcon("src/images/BK.gif");
       
       iconsLoaded = true;
	   
   }
   
   // turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
   static String turnIntIntoCardValue(int k)
   {
	   
	   
	   if (k >= 0 && k < 14) {
	         // Return the card value at the given index
	         return values[k];
	      } else {
	         // Index was invalid
	         return "";
	      }
   }
   
   // turns 0 - 3 into "C", "D", "H", "S"
   static String turnIntIntoCardSuit(int k)
   {
	   
	   
	   if (k >= 0 && k < 4) {
	         // Return the card suit at the given index
	         return suits[k];
	      } else {
	         // Index was invalid
	         return "";
	      }
   }
   
   // a simple main to throw all the JLabels out there for the world to see
   public static void main(String[] args)
   {
      int k;
      
      // prepare the image icon array
      loadCardIcons();
      
      // establish main frame in which program will run
      JFrame frmMyWindow = new JFrame("Card Room!");
      frmMyWindow.setSize(1150, 650);
      frmMyWindow.setLocationRelativeTo(null);
      frmMyWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // set up layout which will control placement of buttons, etc.
      FlowLayout layout = new FlowLayout(FlowLayout.CENTER, 5, 20);   
      frmMyWindow.setLayout(layout);
      
      // prepare the image label array
      JLabel[] labels = new JLabel[NUM_CARD_IMAGES];
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         labels[k] = new JLabel(icon[k]);
      
      // place your 3 controls into frame
      for (k = 0; k < NUM_CARD_IMAGES; k++)
         frmMyWindow.add(labels[k]);

      // show everything to the user
      frmMyWindow.setVisible(true);
   }
}