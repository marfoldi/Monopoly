package hu.elte.game.logic;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@RunWith(Parameterized.class)
public class JSONParserTest {
	
	private static JSONArray json1;
	private static JSONObject json2;
	
	static {
		json1 = new JSONArray();
		json1.add(1);
		json1.add(2);
		
		JSONArray json2Array = new JSONArray();
		json2Array.add(1);
		json2Array.add(2);
		json2Array.add(3);
		json2 = new JSONObject();
		json2.put("field", "value");
		json2.put("list", json2Array);
	}
	
	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
        	{"JSONTests/json1.json", json1},
        	{"JSONTests/json2.json", json2}
       	 });
    }
    
	private String fileName;
	private Object json;
	
	public JSONParserTest(String fileName, Object json) {
		this.fileName = fileName;
		this.json = json;
	}
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCorrect() {
		try {
			Object result = JSONParser.parse(this.fileName, JSONParserTest.class);
			assertTrue(result.equals(this.json));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
