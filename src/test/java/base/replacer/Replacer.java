package base.replacer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base.exception.ReplaceException;


public abstract class Replacer {

    protected String element;
    protected String functionPatternString;
    protected String parameterPatternString;
    protected Map<String, String> variableValueMap = new HashMap<String, String>();

    public Replacer(String element) {
        this.element = element;
    }

    public String doReplacement() {

        StringBuffer sb = new StringBuffer();
        Pattern functionPattern = Pattern.compile(functionPatternString, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher functionMatcher = functionPattern.matcher(element);

        while (functionMatcher.find()) {
            String function = functionMatcher.group(1);
            String variable = "";

            // 用“：”分割函数和函数结果对应的变量名
            if (2 == function.split(":").length) {
                variable = function.split(":")[1];
            }

            Pattern parameterPattern =
                    Pattern.compile(parameterPatternString, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            Matcher parameterMatcher = parameterPattern.matcher(function);
            if (!parameterMatcher.find()) {
                throw new ReplaceException(ReplaceException.REPLACE_NO_PARAMS_FOUND);
            }

            String parameter = parameterMatcher.group(1);
            String functionReturn = replaceFunction(parameter);
            functionMatcher.appendReplacement(sb, functionReturn);

            if (!variable.equals("")) {
                variableValueMap.put(variable, functionReturn);
            }
        }
        functionMatcher.appendTail(sb);
        return String.valueOf(sb);
    }

    public String replaceFunction(String parameter) {
        return "";
    }

    public Replacer setElement(String element) {
        this.element = element;
        return this;
    }

    public Map<String, String> getVariableValueMap() {
        return variableValueMap;
    }
}

