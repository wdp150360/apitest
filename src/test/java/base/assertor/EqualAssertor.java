package base.assertor;

import static org.testng.Assert.assertEquals;

import java.text.MessageFormat;


public class EqualAssertor extends Assertor {

    private int result;
    private int expect;

    private String resultString;
    private String expectString;

    public EqualAssertor(int result, int expect) {
        this.result = result;
        this.expect = expect;
    }

    public EqualAssertor(String expectString, String resultString) {
        this.resultString = resultString;
        this.expectString = expectString;
    }

    @Override
    public void doAssertion() {
        assertEquals(result, expect,
                MessageFormat.format("Case Name: {0}\n Expect is {1}, but now is {2}", caseName, expect, result));
        assertEquals(resultString, expectString, MessageFormat
                .format("Case Name: {0}\n Expect is {1}, but now is {2}", caseName, expectString, resultString));
    }
}
