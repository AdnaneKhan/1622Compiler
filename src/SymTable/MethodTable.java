package SymTable;

import SyntaxTree.*;

import java.util.HashMap;
/**
 * Created by adnankhan on 3/29/15.
 */
public class MethodTable extends TableEntry {



    public MethodTable(ASTNode makeFrom) {
        super(makeFrom);
    }

    public int entryType() {
        return METHOD_ENTRY;
    }

    @Override
    public boolean isEntry(int entryType) {
        return entryType == METHOD_ENTRY;
    }

    // Var Declarations belonging to method
    /**
     *
     * @param toAdd Var declaration for this method
     */
    public void putVariable(VarDecl toAdd) {
        SymbolEntry newLeaf = new SymbolEntry(toAdd.i.toString(),toAdd);
        newLeaf.parent = this;
        hash.put(toAdd.i.toString(),newLeaf);
    }


    public void putVariable(Formal toAdd) {
        SymbolEntry newLeaf = new SymbolEntry(toAdd.i.toString(),toAdd);
        newLeaf.parent = this;
        hash.put(toAdd.i.toString(),newLeaf);
    }
    // Do we need to add statements? Or dont we just work with them at tree level
    // Now we add the statements belonging to this method
//    /**
//     *
//     * @param toAdd Statement of type Assign
//     */
//    public void putLeaf(Assign toAdd) {
//        SymbolEntry newLeaf = new SymbolEntry(toAdd.i.toString(),toAdd);
//        newLeaf.parent = this;
//        hash.put(toAdd.i.toString(),newLeaf);
//    }
//
//    /**
//     *
//     * @param toAdd Statement of type ArrayAssign
//     */
//    public void putLeaf(ArrayAssign toAdd) {
//        SymbolEntry newLeaf = new SymbolEntry(toAdd.i.toString(),toAdd);
//        newLeaf.parent = this;
//        hash.put(toAdd.i.toString(),newLeaf);
//    }
}
