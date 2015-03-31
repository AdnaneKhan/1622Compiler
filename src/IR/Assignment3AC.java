package IR;


public class Assignment3AC extends ThreeAddressStatement {
    String lhsVar;
    String rhsVar;
    String operator;

    public Assignment3AC() {

    }

    @Override
    public String statementType() {
        return null;
    }

    @Override
    public String toString() {
        return lhsVar + " := " + operator + " " + rhsVar;
    }
}