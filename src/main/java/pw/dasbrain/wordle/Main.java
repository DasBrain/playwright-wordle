package pw.dasbrain.wordle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws IOException {
		List<String> words = loadWords();
		var game = new GameState(words, new RandomWordSelector());
		
		try (Driver driver = new Driver()) {
			try {
				driver.init();
				for (int i = 0; i < 6; i++) {
					String word;
					WordTester.CharResult[] result;
					do {
						word = game.nextWord();
						result = driver.typeWord(word);
						if (result == null) {
							game.remove(word);
						}
					} while (result == null);
					if (Arrays.stream(result)
							.allMatch(r -> r == WordTester.CharResult.CORRECT)) {
						break;
					}
					game.filter(word, result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			driver.waitForClose();
		}
	}
	
	private static List<String> loadWords() throws IOException {
		try (var is = Main.class.getResourceAsStream("/words");
				var isr = new InputStreamReader(is, StandardCharsets.UTF_8);
				var br = new BufferedReader(isr)) {
			return br.lines().filter(s -> s.length() == 5)
					.map(String::toLowerCase).toList();
		}
	}
}
