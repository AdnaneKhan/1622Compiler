package IR;


import SymTable.TableEntry;

public class Return3AC extends ThreeAddressStatement {
    private TableEntry returnVar;

    public Return3AC(TableEntry toReturn) {
        returnVar = toReturn;
    }

    @Override
    public String statementType() {
        return null;
    }

    @Override
    public String toString() {
        return "return " + returnVar.getSymbolName();
    }
}