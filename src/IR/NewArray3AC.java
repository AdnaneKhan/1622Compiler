package IR;

import SymTable.TableEntry;

public class NewArray3AC extends ThreeAddressStatement {


    private TableEntry lhsVar;
    private String newType;
    private int arraySize;

    public NewArray3AC( TableEntry lhs, String type, int sizeToSet) {
        this.lhsVar = lhs;
        this.arraySize = sizeToSet;

        this.newType = type;
    }

    @Override
    public String statementType() {
        return null;
    }


    @Override
    public String toString() {
        return lhsVar.getSymbolName() + " := new "+ newType+ "," + arraySize;
    }
}