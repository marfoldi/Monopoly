package hu.elte.game.logic.parsers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

public class JSONParser {
	
	private static final String defaultPath = "../../../../../resources/";
	
	public static <T> T parse(String fileName) {
		return parse(fileName, JSONParser.class);
	}
	
	public static <T> T parse(String fileName, Class<?> clazz) {
		return parse(fileName, clazz, null);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T parse(String fileName, Class<?> clazz, String path) {
		InputStream is = clazz.getResourceAsStream((path == null ? defaultPath : path) + fileName);
        try {
        	Reader reader = new InputStreamReader(is, "UTF-8");
        	String jsonTxt = IOUtils.toString(reader);
        	Object json = JSONSerializer.toJSON( jsonTxt );
        	return (T) json;
        } catch (Exception e) {
        	return null;
        }
	}

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> parseJSONArray(JSONArray array) {
		//ArrayList<T> retVal = JSONArray.toList(array); -- deprecated
		ArrayList<T> retVal = new ArrayList<T>();
		for (int i=0; i<array.size(); i++) {
			try {
				retVal.add((T) array.get(i));
			} catch (Exception e) {
				// no-operation
			}
		}
		return retVal;
	}
}