package pw.dasbrain.wordle;

import java.util.Scanner;

import com.microsoft.playwright.*;

public class Example {
	public static void main(String[] args) {
		try (Playwright playwright = Playwright.create()) {
			Browser browser = playwright.firefox()
					.launch(new BrowserType.LaunchOptions().setHeadless(false));
			Page page = browser.newPage();
			page.navigate("https://www.powerlanguage.co.uk/wordle/");
			page.click("game-icon[icon='close']");
			typeWord(page, "toast");
			var row = page.querySelectorAll("#board game-row:nth-child(1) game-tile");
			for (var elem : row) {
				System.out.println(elem.getAttribute("evaluation"));
			}
			new Scanner(System.in).nextLine();
		}
	}
	
	static void typeWord(Page page, String word) {
		for (char c : word.toCharArray()) {
			page.click("button[data-key=" + c + "]");
		}
		page.click("button[data-key=↵]");
	}
}