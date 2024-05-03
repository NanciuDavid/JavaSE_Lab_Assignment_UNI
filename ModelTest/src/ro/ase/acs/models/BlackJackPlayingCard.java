package ro.ase.acs.models;
import	ro.ase.acs.classes.DataSeriesOperation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.JUnit4TestCaseFacade;

public class BlackJackPlayingCard extends PlayingCard implements Cloneable{
	private int score;
	private static 	Map<BlackJackPlayingCard, Integer> drawnCards = new HashMap<BlackJackPlayingCard, Integer>();
	
	public BlackJackPlayingCard(String rank, String suit, int quantity) {
	    super(rank, suit, quantity);
	    switch (rank) {
	        case "2":
	        case "3":
	        case "4":
	        case "5":
	        case "6":
	        case "7":
	        case "8":
	        case "9":
	            this.score = Integer.parseInt(rank);
	            break;
	        case "T":
	            this.score = 10;
	            break;
	        case "J":
	            this.score = 11; // Set score for Jack
	            break;
	        case "Q":
	            this.score = 12; // Set score for Queen
	            break;
	        case "K":
	            this.score = 13; // Set score for King
	            break;
	        case "A":
	            this.score = 14; // Set score for Ace
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid rank");
	    }
	    this.drawnCards = new HashMap<>();
	}

	
	@Override 
	public boolean isGameOver(PlayingCard card) {
		BlackJackPlayingCard newCard = new BlackJackPlayingCard(card.rank, card.suit, card.quantity);
		
		if(this.score * this.quantity + newCard.score * newCard.quantity > 21) {
			return true;
		}
	
		
		return false;
	}
	
	@Override
	public float getPoints() {
	    return this.score * this.quantity * 1.0f;
	}

	
	@SuppressWarnings("static-access")
	protected Object clone() throws CloneNotSupportedException {
		BlackJackPlayingCard newCard = (BlackJackPlayingCard)super.clone();
		
		int quantity = this.quantity;
		int score = this.score;
		newCard.quantity = quantity;
		newCard.score = score;
		newCard.rank = new String(this.rank);
		newCard.suit = new String(this.suit);
		newCard.drawnCards = new HashMap<BlackJackPlayingCard, Integer>(this.drawnCards);
		
		return  newCard;
	}
	
	public String getSuit() {
		return new String(this.suit);
	}
	
	public int getQuantity() {
		int copy = this.quantity;
		return copy;
	}
	
	public void setSuit(String suit) {
		if(!isValid(this.suit)) {
			throw new IllegalArgumentException("Invalid suit");
		}
		this.suit = suit;
	}
	
	public void setQuantity(int quantity) {
		if(quantity < 0) {
			throw new IllegalArgumentException("Number is to low");
		}
		this.quantity = quantity;
	}
	
	
	@Override
	public String toString() {
	    return "> " + this.rank + " " + this.getSuit() + " " + this.getQuantity() + " " + this.getPoints();
	}

	
	public static void drawCard(BlackJackPlayingCard card) {
	
		if(!(drawnCards.containsKey(card))) { 
		drawnCards.put(card, 1);
		} else {
			int count = drawnCards.get(card);
			drawnCards.put(card, count + 1);
		}
	}
	
	public static String getStatus() {
		String status = new String(" ");
		int points = 0;
		for(Entry<BlackJackPlayingCard, Integer> drawnCard : drawnCards.entrySet()) {
			status = status +  drawnCard.toString() + "\n";
			points += drawnCard.getKey().getPoints();
		}
		
		if(points <= 21) {
			status = status + "\n" + "still in the game";
		} else {
			status = status + "\n" + "game over";
		}
	
	return status;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else if(!(o.getClass() == getClass())) {
			return false;
		}
		BlackJackPlayingCard newObj = (BlackJackPlayingCard)o;
		return this.score == newObj.score;
	}
	
	
}
	

