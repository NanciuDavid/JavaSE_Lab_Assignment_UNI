package ro.ase.acs.models;

import ro.ase.acs.contracts.Playable;

public abstract class PlayingCard implements Playable{
	protected String rank; // 2, 3, 4, 5, 6, 7, 8, 9, T, J, Q, K, A (no validations are necessary)
	protected String suit; // S - Spade, D - Diamond, H - Heart, C - Club (if the suit is invalid an exception is thrown)
	protected int quantity; //quantity (integer) - how many cards of that type
	
	
	public PlayingCard(String rank, String suit, int quantity) {
		this.rank = rank;
		if(!(isValid(suit))) {
		throw new IllegalArgumentException("Invalid suit");
		} else {
		this.suit = suit;
		}
		this.quantity = quantity;
	}
	
	public boolean isValid(String suit) {
	    if(suit.equals("S") || suit.equals("D") || suit.equals("H") || suit.equals("C")) {
	        return true;
	    }
	    return false;
	}

	
	@Override
	public float getPoints() {
		return 0.0f;
	}
	
	public abstract boolean isGameOver(PlayingCard card);



	
	
}
