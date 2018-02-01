package base.builder;

import base.replacer.RandomNumberReplacer;
import base.replacer.RandomStringReplacer;


public class CSVReplacerBuilder extends ReplacerBuilder {

    public CSVReplacerBuilder(String element) {
        super(element);
    }

    public CSVReplacerBuilder buildReplacers() {
        // TODO add factory pre condition logic
        add(new RandomStringReplacer(element));
        add(new RandomNumberReplacer(element));
        return this;
    }
}
