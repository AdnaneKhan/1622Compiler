package IR;

import SymTable.TableEntry;

public class IndexedAssignment3AC extends ThreeAddressStatement {
    TableEntry lhsVar;
    TableEntry arrayVar;
    TableEntry index;

    public IndexedAssignment3AC( TableEntry lhsVar, TableEntry arrayVar, TableEntry index) {
        this.lhsVar = lhsVar;
        this.arrayVar = arrayVar;
        this.index = index;
    }
    @Override
    public String statementType() {
        return null;
    }

    @Override
    public String toString() {
        return lhsVar + " := " + arrayVar + "[" + index + "]";
    }
}