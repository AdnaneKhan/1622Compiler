package IR;

import SymTable.TableEntry;

public class New3AC extends ThreeAddressStatement {

    private TableEntry lhsVar;
    private String newType;

    public New3AC( TableEntry lhs, String type) {
        this.lhsVar = lhs;
        this.newType = type;
    }

    @Override
    public String statementType() {
        return null;
    }


    @Override
    public String toString() {
        return lhsVar.getSymbolName() + " := new "+ newType;
    }
}