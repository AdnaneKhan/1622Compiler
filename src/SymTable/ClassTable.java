package SymTable;

import SyntaxTree.ASTNode;
import SyntaxTree.MethodDecl;

import java.util.HashMap;

/**
 * Created by adnankhan on 3/29/15.
 */
public class ClassTable extends TableEntry {



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
