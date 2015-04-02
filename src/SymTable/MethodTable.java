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
     * @param toAdd Var declaration for this method
     */
    public void putVariable(VarDecl toAdd) {
        SymbolEntry newLeaf = new SymbolEntry(toAdd.i.toString(),toAdd.t, toAdd);
        newLeaf.parent = this;

        hash.put(new KeyWrapper(toAdd.i.s, LEAF_ENTRY), newLeaf);
    }


    public void putVariable(Formal toAdd) {
        SymbolEntry newLeaf = new SymbolEntry(toAdd.i.s,toAdd.t, toAdd);
        newLeaf.parent = this;

        hash.put(new KeyWrapper(toAdd.i.s, LEAF_ENTRY), newLeaf);
    }
}