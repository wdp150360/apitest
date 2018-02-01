package base.assertor;

public abstract class Assertor {
    protected String caseName = "";

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void doAssertion() {
    }
}
