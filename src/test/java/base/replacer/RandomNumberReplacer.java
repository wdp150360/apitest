package base.replacer;

import org.apache.commons.lang3.RandomStringUtils;

import base.exception.ReplaceException;


public class RandomNumberReplacer extends Replacer {

    public RandomNumberReplacer(String element) {
        super(element);
        functionPatternString = "\\$\\{(randomnumber\\(\\d+\\):{0,1}.*?)\\}";
        parameterPatternString = "\\((\\d+)\\)";
    }

    @Override
    public String replaceFunction(String parameter) {
        try {
            int counts = Integer.valueOf(parameter);
            return RandomStringUtils.randomNumeric(counts);
        } catch (NumberFormatException ex) {
            throw new ReplaceException(ReplaceException.REPLACE_PARAMS_TYPE_ERROR);
        }
    }
}
