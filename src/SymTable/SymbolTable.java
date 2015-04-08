package SymTable;

import SyntaxTree.*;

/**
 * This is the root symbol table node for our tree of hash-tables.
 *
 * The children of this node will be the main static class and then any additional user
 * defined classes.
 */
public class SymbolTable extends TableEntry{
    private TableEntry currentScope;

    /*

    The following methods are state control methods that help in tracking the scope

     */

    /**
     *
     * @param scopeID descends scope, if it is in highest scope it goes to a class, if it is in class scope
     *                it goes to method scope
     */
    private void descendScope(KeyWrapper scopeID) {
        if (currentScope.hash.containsKey(scopeID)){
            TableEntry scopeCheck = currentScope.hash.get(scopeID);
            if (scopeCheck instanceof SymbolEntry) {
                System.out.println("Problem! We tried to descend scope to a leaf!");
            } else {
                currentScope = scopeCheck;
            }
        }
    }

    /**
     *
     * @param scopeID descends scope, if it is in highest scope it goes to a class, if it is in class scope
     *                it goes to method scope
     */
    public void descendScope(String scopeID, int scope_type ) {
        KeyWrapper key = new KeyWrapper( scopeID, scope_type);

        if (currentScope.hash.containsKey(new KeyWrapper(scopeID,scope_type))){
            TableEntry scopeCheck = currentScope.hash.get(key);
            if (scopeCheck instanceof SymbolEntry) {
                System.out.println("Problem! We tried to descend scope to a leaf!");
            } else {
                currentScope = scopeCheck;
            }
        } else {
            System.out.println("PROBLEM, we tried to descend to a scope that does not exist!");
        }
    }


    /**
     * Goes up one level in scope, if it is already at root then it reports a problem
     */
    public void ascendScope() {
        if (currentScope.parent != null) {
            currentScope = currentScope.parent;
        } else {
            System.out.println("Problem! We tried to ascend scopes when there was no parent!");
        }
    }

    public TableEntry getCurrentScope() {
        return this.currentScope;
    }


    public SymbolTable(ASTNode root) {
        super(root);
    }

    /**
     *
     * @param key in this case the ID of the class
     * @param value Scope for the class
     * @return the HashTable belonging to the value (if it leads to a method/class) null otherwise
     */
    protected void put(String key, TableEntry value) {
        KeyWrapper wrap = new KeyWrapper(key,CLASS_ENTRY);
        hash.put(wrap,value);
        currentScope = this;
        // Given that what we will be adding here are classes we

    }

    public void putClass(ClassDeclExtends classNode) {
        ClassTable tableEntry = new ClassTable(classNode,this);

        put( classNode.i.toString(),tableEntry);
    }

    public void putClass(ClassDeclSimple classNode) {
        ClassTable tableEntry = new ClassTable(classNode,this);

        put(classNode.i.toString(),tableEntry);

    }

    public void putClass(MainClass classNode) {

        ClassTable tableEntry = new ClassTable(classNode,this);
        put(classNode.i1.toString(),tableEntry);
    }

    public void putClass(ClassDecl classNode) {
        if (classNode instanceof ClassDeclSimple) {
            this.putClass((ClassDeclSimple)classNode);
        } else {
            this.putClass((ClassDeclExtends) classNode);
        }
    }

    public int entryType() {
        return ROOT_ENTRY;
    }

    @Override
    public boolean isEntry(int entryType) {
        return entryType == ROOT_ENTRY;
    }

    /**
     *
     * @param key string
     * @return Object associated with the key
     */
    public ClassTable getClassTable(String key) {

        KeyWrapper checkWrap = new KeyWrapper(key, CLASS_ENTRY);
        return (ClassTable) hash.get(checkWrap);
    }

}