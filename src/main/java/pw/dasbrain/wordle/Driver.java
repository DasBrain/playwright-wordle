package pw.dasbrain.wordle;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.ElementState;

public class Driver implements AutoCloseable {
	
	Playwright playwright;
	Browser browser;
	Page page;
	int row = 1;
	
	public void init() {
		playwright = Playwright.create();
		browser = playwright.firefox()
				.launch(new BrowserType.LaunchOptions().setHeadless(false));
		page = browser.newPage();
		page.navigate("https://www.powerlanguage.co.uk/wordle/");
		page.click("game-icon[icon='close']");
	}
	
	public WordTester.CharResult[] typeWord(String word) {
		System.out.println("typing word \"" + word + "\"");
		for (char c : word.toCharArray()) {
			page.click("button[data-key=" + c + "]");
		}
		page.click("button[data-key=↵]");
		
		var elements = page.querySelectorAll("#board game-row:nth-child(" + row + ") game-tile");
		var result = elements
				.stream().map(e -> e.getAttribute("evaluation")).toList();
		if (result.contains(null)) {
			for (int i = 0; i < 5; i++) {
				page.click("button[data-key=←]");
			}
			page.waitForTimeout(1000);
			return null;
		}
		row++;
		page.waitForTimeout(2500);
		return result.stream().map(s -> WordTester.CharResult.valueOf(s.toUpperCase()))
			.toArray(WordTester.CharResult[]::new);
	}
	
	@Override
	public void close() {
		playwright.close();
	}

	public void waitForClose() {
		page.waitForClose(() -> {});
	}
	
}
