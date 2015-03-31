package IR;


import SymTable.TableEntry;
import syntaxtree.MethodDecl;

public class Call3AC extends ThreeAddressStatement {

    private TableEntry lhsVar;
    private TableEntry callMethod;
    private int paramCount;

    public Call3AC (TableEntry lhsVar, TableEntry callMethod) {
        this.lhsVar = lhsVar;

        this.callMethod = callMethod;
        // Get the parameter count from the AST node associated with symbol table entry for the methods
        this.paramCount = ((MethodDecl)callMethod.getNode()).fl.size();
    }

    @Override
    public String statementType() {

        return null;
    }

    @Override
    public String toString() {
        return lhsVar + " := call " + callMethod+","+ paramCount;
    }
}