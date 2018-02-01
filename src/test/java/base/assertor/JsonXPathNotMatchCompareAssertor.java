package base.assertor;

import static org.testng.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.util.JsonParse;


public class JsonXPathNotMatchCompareAssertor extends Assertor {
    private static final Logger logger = LoggerFactory.getLogger(JsonXPathNotMatchCompareAssertor.class);

    private String jsonPath;
    private String jsonString;
    private Map<String, String> jsonKeyValue;

    public JsonXPathNotMatchCompareAssertor(String jsonPath, String jsonString,
                                            Map<String, String> jsonKeyValue) {
        this.jsonPath = jsonPath;
        this.jsonString = jsonString;
        this.jsonKeyValue = jsonKeyValue;
    }

    @Override
    public void doAssertion() {
        Object result = JsonParse.getJsonXPathResult(jsonPath, jsonString);

        if (result instanceof List) {
            /**
             * 返回的是List
             * 1. 如果没有一项是Map， 说明没有Key value，也不符合要求
             * 2. 如果有Map， 则循环比较每一项Map的jsonKeyValue是否在Map中对应的Key Value List中
             *    如果有一项在key的value list中那么就不符合要求
             *    所有项目都不在key 的value list中，才符合要求
             */
            boolean hasMapElement = false;
            boolean keyValueNoteMatch = true;
            for (Object element : (List<Object>) result) {
                if (element instanceof Map) {
                    hasMapElement = true;
                    if (!jsonKeyValueNotMatch((Map<String, Object>) element)) {
                        keyValueNoteMatch = false;
                        break;
                    }
                }
            }
            // 判断是否含有Map元素，比较必须是map元素
            assertTrue(hasMapElement, MessageFormat.format("Case Name: {0}\n Does not have any key value\n", caseName));
            // map的list元素是否都不在key的value list里面
            assertTrue(keyValueNoteMatch, MessageFormat.format("Case Name: {0}\n Key value is match\n", caseName));
        } else if (result instanceof Map) {
            /**
             * 返回的是Map，比较每一项Map的jsonKeyValue是否在Map中对应的Key Value List中
             * 如果有一项在key的value list中那么就不符合要求
             */
            assertTrue(jsonKeyValueNotMatch((Map<String, Object>) result),
                    MessageFormat.format("Case Name: {0}\n Key value does not match\n", caseName));
        } else {
            /**
             * 返回的不是Map和List类型，说明没有Key value
             * 直接返回错
             */
            assertTrue(false,
                    MessageFormat.format("Case Name: {0}\n Does not have any key value\n", caseName));

        }
    }

    boolean jsonKeyValueNotMatch(Map<String, Object> result) {
        for (Map.Entry entry : jsonKeyValue.entrySet()) {
            /**
             * 如果没有Key，则认为该map不是要比较的对象，直接继续下一个map的比较
             * 返回false是认为没有找到比较的对象
             */
            if (!result.containsKey(entry.getKey())) {
                break;
            }

            if ((result.get(entry.getKey()) instanceof Map) || (result.get(entry.getKey()) instanceof List)) {
                return true;
            }

            try {
                String jsonValue = String.valueOf(entry.getValue());
                if (jsonValue.startsWith("[") && jsonValue.endsWith("]")) {
                    String[] values = jsonValue.substring(1, jsonValue.length() - 1).split(",");
                    if (findIn(String.valueOf(result.get(entry.getKey())), values)) {
                        return false;
                    }
                } else {
                    if (String.valueOf(result.get(entry.getKey())).equals(jsonValue)) {
                        return false;
                    }
                }

            } catch (Exception ex) {
                logger.warn("{}'s value is not string! {}", entry.getKey(), ex);
                return true;
            }

        }
        return true;
    }

    private boolean findIn(String element, String[] list) {
        for (int listPos = 0; listPos < list.length; ++listPos) {
            if (element.equals(list[listPos])) {
                return true;
            }
        }
        return false;
    }
}
