package pw.dasbrain.wordle;

import java.util.List;

public final class GameState {
	
	private List<String> words;
	private final WordSelector selector;
	
	public GameState(List<String> words, WordSelector selector) {
		this.words = words;
		this.selector = selector;
	}
	
	public String nextWord() {
		return selector.selectWord(words);
	}
	
	public List<String> words() {
		return words;
	}
	
	public void filter(String word, WordTester.CharResult... result) {
		words = words.stream().filter(WordTester.makeFilter(word, result)).toList();
	}
	
	public void remove(String word) {
		words = words.stream().filter(s -> !s.equals(word)).toList();
	}
}
