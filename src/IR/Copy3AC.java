package IR;

import SymTable.TableEntry;

public class Copy3AC extends ThreeAddressStatement {
    private TableEntry lhsVar;
    private TableEntry rhsVar;


    public Copy3AC(TableEntry lhsVar, TableEntry rhsVar) {
        this.lhsVar = lhsVar;
        this.rhsVar = rhsVar;
    }

    @Override
    public String statementType() {

        return null;
    }

    @Override
    public String toString() {
        return lhsVar + " := " + rhsVar;
    }
}