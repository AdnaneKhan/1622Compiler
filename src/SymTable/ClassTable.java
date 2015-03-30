package SymTable;

import syntaxtree.ASTNode;
import syntaxtree.MethodDecl;

import java.util.HashMap;

/**
 * Created by adnankhan on 3/29/15.
 */
public class ClassTable extends TableEntry {
    protected HashMap<String, TableEntry> hash;
    // Reference to the parent scope which this symbol entry resides in, the parent scope can be
    // accessed to check for duplicates, etc.

    public TableEntry parent;

    public ClassTable(ASTNode makeFrom) {
        super(makeFrom);
    }

    public int entryType() {
        return CLASS_ENTRY;
    }

    public void put(String key, TableEntry value) {
        hash.put(key,value);
    }

    public void putMethod( MethodDecl methodNode) {
            MethodTable methTable = new MethodTable(methodNode);
            methTable.parent = this;
            put ( methodNode.i.toString(),methTable);

    }
}
