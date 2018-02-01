package base.replacer;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class VariableReplacer extends Replacer {

    public VariableReplacer(String element) {
        super(element);
    }

    public VariableReplacer setVariableValueMap(Map<String, String> variableValueMap) {
        this.variableValueMap = variableValueMap;
        return this;
    }

    public VariableReplacer setElement(String element) {
        return this;
    }

    @Override
    public String doReplacement() {
        for (Map.Entry<String, String> entry : variableValueMap.entrySet()) {
            Pattern variablePattern =
                    Pattern.compile("\\$\\{" + entry.getKey() + "\\}", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            Matcher variableMattcher = variablePattern.matcher(element);
            element = variableMattcher.replaceAll(entry.getValue());
        }
        return element;
    }
}
