package pw.dasbrain.wordle;

import java.util.List;
import java.util.Random;

public class RandomWordSelector implements WordSelector {
	
	private final Random random = new Random();
	
	@Override
	public String selectWord(List<String> words) {
		return words.get(random.nextInt(words.size()));
	}
	
}
