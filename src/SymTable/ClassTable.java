package SymTable;

import SyntaxTree.*;

import java.util.HashMap;

/**
 * Created by adnankhan on 3/29/15.
 */
public class ClassTable extends TableEntry {
    private ClassTable parentClass;
private IdentifierType classType;

    /**
     *
     * @param makeFrom AST node that represents this class
     * @param parent parent of this class (the root node normally)
     */
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
            this.symbolName = actualNode.i.s;
            parentClass = (ClassTable) parent.getEntry(parentId.s,CLASS_ENTRY);
        } else if (makeFrom instanceof ClassDeclSimple) {
            this.symbolName = ((ClassDeclSimple)makeFrom).i.s;

        }

        this.classType = new IdentifierType(symbolName,0,0);



    }

    public Type getClassType() {
        return classType;
    }

    public int entryType() {
        return CLASS_ENTRY;
    }

    @Override
    public boolean isEntry(int entryType) {
        return entryType == CLASS_ENTRY;
    }

    /**
     *
     * @param key namme of varaible
     * @return symbol entry for that variable
     */
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

    /**
     *
     * @param key string id of method belonging to this class
     * @return symbol table for that method, null if not found
     */
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

    /**
     * Adds a method as a child of this class
     * @param methodNode the mmethod to add
     */
    public void putMethod( MethodDecl methodNode) {
            MethodTable methTable = new MethodTable(methodNode);
            methTable.parent = this;
            put ( methodNode.i.toString(),methTable, METHOD_ENTRY);
    }

    /**
     * Adds a variable declaration to this class
     * @param newVar the variable to add
     */
    public void putVariable( VarDecl newVar ) {
        SymbolEntry var = new SymbolEntry(newVar.i.s,newVar.t, newVar);
        var.parent = this;
        put (var.getSymbolName(), var, LEAF_ENTRY);
    }
}
