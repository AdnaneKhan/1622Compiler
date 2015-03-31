package IR;

import SymTable.TableEntry;

public class Parameter3AC extends ThreeAddressStatement {
    private TableEntry param;


    public Parameter3AC(TableEntry parameter) {
        this.param = parameter;
    }

    @Override
    public String statementType() {
        return null;
    }

    @Override
    public String toString() {
        return "param " + param.getSymbolName();
    }
}