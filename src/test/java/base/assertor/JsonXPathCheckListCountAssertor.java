package base.assertor;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.util.JsonParse;


public class JsonXPathCheckListCountAssertor extends Assertor {

    private static final Logger logger = LoggerFactory.getLogger(JsonXPathCheckListCountAssertor.class);

    private String jsonString;
    private String jsonPath;
    private int count = 0;

    public JsonXPathCheckListCountAssertor(String jsonPath, String jsonString, int count) {
        this.jsonString = jsonString;
        this.jsonPath = jsonPath;
        this.count = count;
    }

    @Override
    public void doAssertion() {
        Object result = JsonParse.getJsonXPathResult(jsonPath, jsonString);
        assertTrue(result instanceof List, MessageFormat
                .format("Case Name: {0}\n {1} does not have any list member in path {2}\n", caseName, jsonPath,
                        jsonString));
        int listCount = ((List<Object>) result).size();
        assertEquals(listCount, count, MessageFormat
                .format("Case Name: {0}\n Expect list size is {1} but now is {2}\n", caseName, count, listCount));
    }
}
