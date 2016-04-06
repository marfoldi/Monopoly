package hu.elte.game.logic;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

public class JSONParser {
	
	public static <T> T parse(String fileName) {
		return parse(fileName, JSONParser.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T parse(String fileName, Class<?> clazz) {
		InputStream is = clazz.getResourceAsStream("../../../../resources/" + fileName);
        try {
        	String jsonTxt = IOUtils.toString(is);
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