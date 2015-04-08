package SymTable;

import SyntaxTree.*;

import java.util.HashMap;

/**
 * Created by adnankhan on 3/29/15.
 */
public class ClassTable extends TableEntry {
    private ClassTable parentClass;

    public ClassTable(ASTNode makeFrom, TableEntry parent) {
        super(makeFrom);

        this.parent = parent;
        if (makeFrom instanceof MainClass) {
            this.symbolName = "main";
        }

        if (makeFrom instanceof ClassDeclExtends) {
            // Get the root class and link it in
            ClassDeclExtends actualNode = (ClassDeclExtends) makeFrom;
            Identifier parentId = actualNode.j;

            parentClass = (ClassTable) parent.getEntry(parentId.s,CLASS_ENTRY);
        }

    }

    public int entryType() {
        return CLASS_ENTRY;
    }

    @Override
    public boolean isEntry(int entryType) {
        return entryType == CLASS_ENTRY;
    }

    public SymbolEntry getVariable(String key) {
        SymbolEntry returnV = null;

        if (this.hasEntry(key,LEAF_ENTRY)) {
            returnV = (SymbolEntry) this.getEntry(key, LEAF_ENTRY);
        }

        if (returnV == null && this.parentClass != null) {
            returnV = (SymbolEntry) (this.parentClass.getEntry(key,LEAF_ENTRY));
        }


        return returnV;
    }

    public MethodTable getMethod(String key) {
        MethodTable returnV = null;

        if (this.hasEntry(key,METHOD_ENTRY)) {
            returnV = (MethodTable) this.getEntry(key, METHOD_ENTRY);
        }

        if (returnV == null && this.parentClass != null) {
            returnV = (MethodTable) (this.parentClass.getEntry(key,METHOD_ENTRY));
        }

        return returnV;
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
        SymbolEntry var = new SymbolEntry(newVar.i.s,newVar.t, newVar);
        var.parent = this;
        put (var.getSymbolName(), var, LEAF_ENTRY);
    }
}
