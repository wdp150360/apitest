package base.assertor;

import static org.testng.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.Map;

import base.util.JsonParse;


public class JsonKeyValueCompareAssertor extends Assertor {

    private String jsonContent;
    private String key = "";
    private String value = "";
    private Map<String, String> jsonKeyValue;

    @Deprecated
    public JsonKeyValueCompareAssertor(String jsonContent, String key, String value) {
        this.jsonContent = jsonContent;
        this.key = key;
        this.value = value;
    }

    public JsonKeyValueCompareAssertor(String jsonContent, Map<String, String> jsonKeyValue) {
        this.jsonContent = jsonContent;
        this.jsonKeyValue = jsonKeyValue;
    }

    @Override
    public void doAssertion() {
        if (!"".equals(key) && !"".equals(value)) {
            jsonAssertion(key, value);
        } else {
            for (Map.Entry<String, String> entry : jsonKeyValue.entrySet()) {
                jsonAssertion(entry.getKey(), entry.getValue());
            }
        }
    }

    private void jsonAssertion(String key, String expectValue) {
        assertTrue(JsonParse.hasKey(key, jsonContent),
                MessageFormat.format("Case Name: {0}\n Does not have key {1}", caseName, key));
        Boolean hasEqualValue = false;
        String resultValue = "";
        for (String v : JsonParse.getValues(key, jsonContent)) {
            if (v.equals(expectValue)) {
                hasEqualValue = true;
                break;
            }
            if (resultValue.equals("")) {
                resultValue += v;
            } else {
                resultValue += "," + v;
            }
        }
        assertTrue(hasEqualValue,
                MessageFormat.format("Case Name: {0}\n No key:\"{1}\" value matches {2}, result is {3}",
                        caseName, key, expectValue, resultValue));
    }
}
