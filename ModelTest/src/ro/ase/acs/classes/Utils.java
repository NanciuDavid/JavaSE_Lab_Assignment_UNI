package ro.ase.acs.classes;
import ro.ase.acs.models.BlackJackPlayingCard;

import java.util.List;
import java.util.stream.Stream;

public class Utils {
	

public static DataSeriesOperation lambdaOperantion = (arr) -> {
		
		int sum = 0 ;
		for(int value : arr) {
			sum += value;
		}
		return sum*1.0/arr.length;
	};
	
	public static Stream<String> getCardsByRank(List<String> cards, char rank) {
		
		return cards.stream().filter(card -> card.charAt(0) == rank).distinct().sorted();
	}
}
