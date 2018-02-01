package base.assertor;

import static org.testng.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.util.JsonParse;


public class JsonXPathMatchCompareAssertor extends Assertor {
    private static final Logger logger = LoggerFactory.getLogger(JsonXPathMatchCompareAssertor.class);

    private String jsonPath;
    private String jsonString;
    private Map<String, String> jsonKeyValue;

    public JsonXPathMatchCompareAssertor(String jsonPath,
                                         String jsonString,
                                         Map<String, String> jsonKeyValue) {
        this.jsonPath = jsonPath;
        this.jsonString = jsonString;
        this.jsonKeyValue = jsonKeyValue;
    }

    @Override
    /**
     * 仅仅比较xpath给出的路径下面的值
     *
     */
    public void doAssertion() {
        Object result = JsonParse.getJsonXPathResult(jsonPath, jsonString);

        if (result instanceof List) {
            /**
             * 返回的是List
             * 1. 如果没有一项是Map， 说明没有Key value
             * 2. 如果有Map， 则循环比较每一项Map的jsonKeyValue是否和Map中对应的Key value 相等
             *    如果有一项所有map key的value都相等那么就符合要求
             */
            boolean hasMapElement = false;
            boolean keyValueMatches = false;
            for (Object element : (List<Object>) result) {
                if (element instanceof Map) {
                    hasMapElement = true;
                    if (jsonKeyValueMatch((Map<String, Object>) element)) {
                        keyValueMatches = true;
                        break;
                    }
                }
            }
            // 判断是否含有Map元素，比较必须是map元素
            assertTrue(hasMapElement, MessageFormat.format("Case Name: {0}\n Does not have any key value\n", caseName));
            // map的list元素是否有一个符合给定的Map值
            assertTrue(keyValueMatches, MessageFormat.format("Case Name: {0}\n Key value does not match\n", caseName));
        } else if (result instanceof Map) {
            /**
             * 返回的是Map，直接比较Map的jsonKeyValue是否和Map中对应的Key value 相等
             */
            assertTrue(jsonKeyValueMatch((Map<String, Object>) result),
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

    private boolean jsonKeyValueMatch(Map<String, Object> result) {
        for (Map.Entry entry : jsonKeyValue.entrySet()) {
            if (!result.containsKey(entry.getKey())) {
                logger.warn("result does not contains key {}", entry.getKey());
                return false;
            }

            if ((result.get(entry.getKey()) instanceof Map) || (result.get(entry.getKey()) instanceof List)) {
                logger.warn("{}'s value is not Map or List", entry.getKey());
                return false;
            }

            try {
                if (!String.valueOf(result.get(entry.getKey())).equals(String.valueOf(entry.getValue()))) {
                    logger.warn("{}'s value does not match {}, actual value is {}", entry.getKey(), entry.getValue(),
                            String.valueOf(result.get(entry.getKey())));
                    return false;
                }
            } catch (Exception ex) {
                logger.warn("{}'s value is not string! {}", entry.getKey(), ex);
                return false;
            }

        }
        return true;
    }

}
