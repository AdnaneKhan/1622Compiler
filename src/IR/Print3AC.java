package IR;


import SymTable.TableEntry;
import SyntaxTree.MethodDecl;

public class Print3AC extends ThreeAddressStatement {

    private TableEntry lhsVar;
    private String callMethod;
    private int paramCount;

    public Print3AC () {
        this.lhsVar = lhsVar;

        this.callMethod = "println";
        // Get the parameter count from the AST node associated with symbol table entry for the methods
        this.paramCount = 1;
    }

    @Override
    public String statementType() {

        return null;
    }

    @Override
    public String toString() {
        return "call " + callMethod+","+ paramCount;
    }
}