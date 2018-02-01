package base.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import base.replacer.Replacer;


public abstract class ReplacerBuilder extends Replacer {

    protected List<Replacer> replacerList = new ArrayList<Replacer>();

    public ReplacerBuilder(String element) {
        super(element);
    }

    @Override
    public String doReplacement() {
        for (Replacer replacer : replacerList) {
            replacer.setElement(element);
            element = replacer.doReplacement();
            if (!replacer.getVariableValueMap().isEmpty()) {
                for (Map.Entry<String, String> entry : replacer.getVariableValueMap().entrySet()) {
                    variableValueMap.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return element;
    }

    public void add(Replacer replacer) {
        replacerList.add(replacer);
    }
}
