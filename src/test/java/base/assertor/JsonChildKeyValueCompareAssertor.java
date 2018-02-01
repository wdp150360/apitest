package base.assertor;

import static org.testng.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

import base.util.JsonParse;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonChildKeyValueCompareAssertor extends Assertor {
    private JsonNode childNode;
    private JsonNode subNode;
    private String key = "";
    private String value = "";
    private Map<String, String> jsonKeyValue;
    private String idKey = "";

    @Deprecated
    public JsonChildKeyValueCompareAssertor(String jsonContent, String childFieldName, String key, String value) {
        this.childNode = JsonParse.getJsonChildNode(childFieldName, jsonContent);
        this.key = key;
        this.value = value;
    }

    public JsonChildKeyValueCompareAssertor(String jsonContent, String childFieldName,
                                            Map<String, String> jsonKeyValue) {
        this.childNode = JsonParse.getJsonChildNode(childFieldName, jsonContent);
        this.jsonKeyValue = jsonKeyValue;
    }

    public JsonChildKeyValueCompareAssertor(String jsonContent, String childFieldName, Map<String, String> jsonKeyValue,
                                            String idKey) {
        this.childNode = JsonParse.getJsonChildNode(childFieldName, jsonContent);
        this.jsonKeyValue = jsonKeyValue;
        this.idKey = idKey;
    }

    @Override
    public void doAssertion() {
        Boolean idKeyMatchesCurrentSubNode = false;
        if (childNode.isArray()) {
            Iterator<JsonNode> fieldsIterator = childNode.elements();
            while (fieldsIterator.hasNext() && !idKeyMatchesCurrentSubNode) {
                subNode = fieldsIterator.next();
                for (String v : JsonParse.getValues(idKey, subNode.toString())) {
                    if (v.equals(jsonKeyValue.get(idKey))) {
                        idKeyMatchesCurrentSubNode = true;
                        doAssertionForSubNode();
                        break;
                    }
                }
            }
            assertTrue(idKeyMatchesCurrentSubNode, MessageFormat
                    .format("Case Name: {0}\n Can not find \"{1}\" equals {2} in Json array.", caseName, idKey,
                            jsonKeyValue.get(idKey)));
        } else {
            subNode = childNode;
            doAssertionForSubNode();
        }
    }

    private void doAssertionForSubNode() {
        if (!"".equals(key) && !"".equals(value)) {
            jsonAssertion(key, value);
        } else {
            for (Map.Entry<String, String> entry : jsonKeyValue.entrySet()) {
                jsonAssertion(entry.getKey(), entry.getValue());
            }
        }
    }

    private void jsonAssertion(String key, String value) {
        assertTrue(JsonParse.hasKey(key, subNode.toString()),
                MessageFormat.format("Case Name: {0}\n Does not have key {1}.", caseName, key));
        Boolean hasEqualValue = false;
        for (String v : JsonParse.getValues(key, subNode.toString())) {
            if (v.equals(value)) {
                hasEqualValue = true;
                break;
            }
        }
        assertTrue(hasEqualValue,
                MessageFormat.format("Case Name: {0}\n No key:\"{1}\" value matches {2}.", caseName, key, value));
    }
}
