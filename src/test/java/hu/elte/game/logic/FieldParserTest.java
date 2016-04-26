package hu.elte.game.logic;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import hu.elte.game.logic.data.CardField;
import hu.elte.game.logic.data.Field;
import hu.elte.game.logic.data.LandField;
import hu.elte.game.logic.data.PurchasableField;
import hu.elte.game.logic.data.TaxField;
import hu.elte.game.logic.interfaces.IField;
import hu.elte.game.logic.parsers.FieldParser;
import hu.elte.game.logic.parsers.JSONParser;
import net.sf.json.JSONArray;

@RunWith(Parameterized.class)
public class FieldParserTest {

	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
        	{"JSONTests/testField.json", Field.class},
        	{"JSONTests/testLandField.json", LandField.class},
        	{"JSONTests/testPurchasableField.json", PurchasableField.class},
        	{"JSONTests/testCardField.json", CardField.class},
        	{"JSONTests/testTaxField.json", TaxField.class}
       	 });
    }
    
    private String fileName;
    private Class<?> clazz;
    
    public FieldParserTest(String fileName, Class<?> clazz) {
    	this.fileName = fileName;
    	this.clazz = clazz;
    }
    
	@Test
	public void testCorrecctClass() {
		JSONArray jsonFields = JSONParser.parse(this.fileName, FieldParserTest.class, "../../../../resources/");
		List<IField> fields = FieldParser.parse(jsonFields);
		
		// Test the method
		assertTrue(fields.size() == 1);
		
		IField field = fields.get(0);
		
		// Test the correctness
		assertTrue(field.getClass().equals(this.clazz));
	}

}
