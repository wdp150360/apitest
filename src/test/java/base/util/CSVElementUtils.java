package base.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import base.builder.CSVReplacerBuilder;
import base.exception.CSVException;


public final class CSVElementUtils {
    /**
     * 把["key1":"value1","key2":"value2"]的String转化为Map<String, String>
     *
     * @param element
     *
     * @return
     */
	private static final Logger logger = LoggerFactory.getLogger(CSVElementUtils.class);
	
    public static Map<String, String> convertToStringMap(String element) {
    
    	System.out.println("element2:"+element);
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            Matcher keyValueMatcher = Pattern.compile("(\"(.*?)\":\"(.*?)\")", Pattern.MULTILINE).matcher(element);
            while (keyValueMatcher.find()) {
                resultMap.put(keyValueMatcher.group(2), keyValueMatcher.group(3));
            }
        } catch (Exception ex) {
            throw new CSVException(CSVException.CSV_FILE_PARSE_FAIL); 
            
        }

        return resultMap;
    }

    /**
     * 替换element中${function(param):variable}的元素
     * 并将function(param)的返回值和variable保存在variableValueMap中
     *
     * @param element
     *
     * @return Size为2的list，第一个是过滤后的element，第二个是variableValueMap
     */
    public static List<Object> replacePlaceHolder(String element) {
        CSVReplacerBuilder csvReplacerBuilder = new CSVReplacerBuilder(element);
        element = csvReplacerBuilder.buildReplacers().doReplacement();
        Map<String, String> variableValueMap = csvReplacerBuilder.getVariableValueMap();
        List<Object> result = new ArrayList<Object>();
        result.add(element);
        result.add(variableValueMap);
        return result;
    }

    public static List<Object> fileterElement(String element) {
    	System.out.println("element:"+element);
        List<Object> result = new ArrayList<Object>();
        List<Object> tempList = replacePlaceHolder(element);
        // filter element
        element = (String) tempList.get(0);
        // element is map
        if (element.startsWith("[") && element.endsWith("]")) {
            result.add(convertToStringMap(element));
        } else {
            result.add(element);
        }
        // 增加variableValueMap
        result.add(tempList.get(1));
        return result;
    }
}
