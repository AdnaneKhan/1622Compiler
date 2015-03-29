package SymTable;

import syntaxtree.ASTNode;

import java.util.HashMap;

/**
 * Created by adnankhan on 3/29/15.
 */
public class ClassTable extends TableEntry {
    // Reference to the parent scope which this symbol entry resides in, the parent scope can be
    // accessed to check for duplicates, etc.
    public TableEntry parent;

    protected HashMap<SymbolEntry, TableEntry> hash;

    public ClassTable(ASTNode makeFrom) {
        super(makeFrom);
    }


}
