package hu.elte.game.logic;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.junit.Test;

public class JSONParserTest {
	@Test
	public void testFields(){
		try {
			List<IField> fields=JSONParser.readFields("src\\main\\java\\resources\\fields.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testChanceCards(){
		try {
			List<Card> cards=JSONParser.readCards("src\\main\\java\\resources\\chanceCards.json");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCommChestCards(){
		try {
			List<Card> cards=JSONParser.readCards("src\\main\\java\\resources\\commChestCards.json");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
}
