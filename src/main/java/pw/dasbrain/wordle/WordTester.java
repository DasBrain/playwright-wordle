package pw.dasbrain.wordle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class WordTester {
	public enum CharResult {
		ABSENT,
		PRESENT,
		CORRECT;
	}
	
	public static Predicate<String> makeFilter(String word, CharResult... results) {
		assert word.length() == results.length;
		
		record Matcher(char c, int min, int max, List<Integer> positions, List<Integer> notPositionts) implements Predicate<String> {
			
			Matcher {
				positions = List.copyOf(positions);
				notPositionts = List.copyOf(notPositionts);
			}

			@Override
			public boolean test(String t) {
				long count = t.codePoints().filter(cp -> cp == c).count(); 
				return count >= min && count <= max &&
						positions.stream().allMatch(i -> t.charAt(i) == c) &&
						notPositionts.stream().allMatch(i -> t.charAt(i) != c);
			}
			
			static Matcher of(char c) {
				return new Matcher(c, 0, Integer.MAX_VALUE, List.of(), List.of());
			}
			
			Matcher absent() {
				return new Matcher(c, min, min, positions, notPositionts);
			}
			
			Matcher containsNotAt(int pos) {
				int newMax = max == Integer.MAX_VALUE ? Integer.MAX_VALUE : max + 1;
				ArrayList<Integer> newNotPos = new ArrayList<>(notPositionts);
				newNotPos.add(pos);
				return new Matcher(c, min + 1, newMax, positions, newNotPos);
			}
			
			Matcher correct(int pos) {
				int newMax = max == Integer.MAX_VALUE ? Integer.MAX_VALUE : max + 1;
				ArrayList<Integer> newPos = new ArrayList<>(positions);
				newPos.add(pos);
				return new Matcher(c, min + 1, newMax, newPos, notPositionts);
			}
			
			
		}
		
		HashMap<Character, Matcher> chars = new HashMap<>();
		
		for (int i = 0; i < results.length; i++) {
			char c = word.charAt(i);
			
			Matcher base = chars.computeIfAbsent(c, Matcher::of);
			
			Matcher newMatcher = switch (results[i]) {
				case ABSENT -> base.absent();
				case PRESENT -> base.containsNotAt(i);
				case CORRECT -> base.correct(i);
			};
			chars.put(c, newMatcher);
		}
		return chars.values().stream().<Predicate<String>>map(Function.identity())
				.reduce(Predicate::and).orElseThrow();
	}
}
