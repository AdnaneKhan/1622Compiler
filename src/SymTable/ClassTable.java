package SymTable;

import SyntaxTree.ASTNode;
import SyntaxTree.MethodDecl;
import SyntaxTree.VarDecl;

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

    @Override
    public boolean isEntry(int entryType) {
        return entryType == CLASS_ENTRY;
    }


    public void put(String key, TableEntry value, int type) {
        KeyWrapper keyWrap = new KeyWrapper(key, type);

        hash.put(keyWrap,value);
    }

    public void putMethod( MethodDecl methodNode) {
            MethodTable methTable = new MethodTable(methodNode);
            methTable.parent = this;
            put ( methodNode.i.toString(),methTable, METHOD_ENTRY);
    }

    public void putVariable( VarDecl newVar ) {
        SymbolEntry var = new SymbolEntry(newVar.i.toString(), newVar);
        var.parent = this;
        put (var.getSymbolName(), var, LEAF_ENTRY);
    }
}
