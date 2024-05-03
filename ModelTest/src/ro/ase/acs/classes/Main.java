package ro.ase.acs.classes;

import ro.ase.acs.models.BlackJackPlayingCard;
import ro.ase.acs.contracts.*;

public class Main {
    public static void main(String[] args) {
        // Test the constructor and toString method
        BlackJackPlayingCard card1 = new BlackJackPlayingCard("A", "S", 1);
        BlackJackPlayingCard card2 = new BlackJackPlayingCard("2", "H", 2);
        
        System.out.println(card1.toString());
        System.out.println(card2.toString());

        // Test the drawCard method
        BlackJackPlayingCard.drawCard(card1);
        BlackJackPlayingCard.drawCard(card1);
        BlackJackPlayingCard.drawCard(card2);

        // Test the getStatus method
        System.out.println(BlackJackPlayingCard.getStatus());

        // Test the setSuit and setQuantity methods
        card1.setSuit("D");
        card1.setQuantity(3);
        System.out.println(card1.toString());

        // Test the isGameOver method
        BlackJackPlayingCard card3 = new BlackJackPlayingCard("10", "C", 1);
        System.out.println("Is game over? " + card1.isGameOver(card3));
    }
}
