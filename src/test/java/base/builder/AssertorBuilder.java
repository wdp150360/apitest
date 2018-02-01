package base.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.assertor.Assertor;
import base.assertor.EqualAssertor;
import base.assertor.JsonChildKeyValueCompareAssertor;
import base.assertor.JsonKeyValueCompareAssertor;
import base.assertor.JsonXPathCheckListCountAssertor;
import base.assertor.JsonXPathMatchCompareAssertor;
import base.assertor.JsonXPathNotMatchCompareAssertor;


public class AssertorBuilder<T extends AssertorBuilder<T>> extends Assertor {
    protected String caseName;
    protected List<Assertor> assertors;
    protected String resultString;

    public AssertorBuilder(String caseName, String resultString) {
        assertors = new ArrayList<Assertor>();
        this.caseName = caseName;
        this.resultString = resultString;
    }

    public AssertorBuilder(String resultString) {
        assertors = new ArrayList<Assertor>();
        this.caseName = "";
        this.resultString = resultString;
    }

    public void doAssertion() {
        for (Assertor a : assertors) {
            a.doAssertion();
        }
    }

    protected void add(Assertor assertor) {
        assertor.setCaseName(caseName);
        assertors.add(assertor);
    }

    public T jsonKeyValueCompare(Map<String, String> jsonKeyValue) {
        add(new JsonKeyValueCompareAssertor(resultString, jsonKeyValue));
        return (T) this;
    }

    /**
     * Find a child Json node according to it's parent field name, and compare it with Key Value pairs.
     *
     * @param fieldName
     * @param jsonKeyValue
     *
     * @return
     */
    public T jsonChildKeyValueCompare(String fieldName, Map<String, String> jsonKeyValue) {
        add(new JsonChildKeyValueCompareAssertor(resultString, fieldName, jsonKeyValue));
        return (T) this;
    }

    /**
     * Find a child Json array node according to it's parent field name.
     * Check every sub Json node in the Json array node, if value of idKey in sub node matches expected value,
     * compare the sub node with Key Value pairs.
     *
     * @param fieldName
     * @param jsonKeyValue
     * @param idKey
     *
     * @return T
     */
    public T jsonChildKeyValueCompare(String fieldName,
                                      Map<String, String> jsonKeyValue, String idKey) {
        add(new JsonChildKeyValueCompareAssertor(resultString, fieldName,
                jsonKeyValue, idKey));
        return (T) this;
    }

    public T jsonXPathMatchCompare(String jsonPath, String jsonString, Map<String, String> keyValue) {
        add(new JsonXPathMatchCompareAssertor(jsonPath, jsonString, keyValue));
        return (T) this;
    }

    public T jsonXPathMatchCompare(String jsonPath, Map<String, String> keyValue) {
        return jsonXPathMatchCompare(jsonPath, resultString, keyValue);
    }

    public T jsonXPathNotMatchCompare(String jsonPath, String jsonString, Map<String, String>
            keyValue) {
        add(new JsonXPathNotMatchCompareAssertor(jsonPath, jsonString, keyValue));
        return (T) this;
    }

    public T jsonXPathNotMatchCompare(String jsonPath, Map<String, String> keyValue) {
        return jsonXPathNotMatchCompare(jsonPath, resultString, keyValue);
    }

    /**
     * JsonXPath路径jsonPath给出的子元素，如果是List，那么校验List的大小是否是给出的count值
     * JsonString默认为httpResponseBody
     *
     * @param jsonPath
     * @param count
     *
     * @return T
     */
    public T jsonXPathListCount(String jsonPath, int count) {
        add(new JsonXPathCheckListCountAssertor(jsonPath, resultString, count));
        return (T) this;
    }

    /**
     * @param expectString
     *
     * @return T
     * 对比HttpResponse和给出的expectString是否相等
     */
    public T expectEqual(String expectString) {
        add(new EqualAssertor(expectString, resultString));
        return (T) this;
    }
}
