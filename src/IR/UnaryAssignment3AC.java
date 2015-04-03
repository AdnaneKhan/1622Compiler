package IR;

import SymTable.TableEntry;

public class UnaryAssignment3AC extends ThreeAddressStatement {

    private TableEntry lhsVar;
    private TableEntry rhsVar;
    private String unaryOp;

    public UnaryAssignment3AC(TableEntry lhs,TableEntry rhs, String unOp) {
        this.lhsVar = lhs;
        this.rhsVar = rhs;
        this.unaryOp = unOp;
    }

    @Override
    public String statementType() {
        return null;
    }

    @Override
    public String toString() {
        return lhsVar.getSymbolName() + " := " + unaryOp + " " + rhsVar.getSymbolName();
    }
}