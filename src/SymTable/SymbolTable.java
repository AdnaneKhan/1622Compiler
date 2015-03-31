package SymTable;

import SyntaxTree.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This is the root symbol table node for our tree of hash-tables.
 *
 * The children of this node will be the main static class and then any additional user
 * defined classes.
 */
public class SymbolTable extends TableEntry{
    public TableEntry currentScope;

    /*

    The following methods are state control methods that help in tracking the scope

     */

    /**
     *
     * @param scopeID descends scope, if it is in highest scope it goes to a class, if it is in class scope
     *                it goes to method scope
     */
    public void descendScope(String scopeID) {
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
     * Goes up one level in scope, if it is already at root then it reports a problem
     */
    public void ascendScope() {
        if (currentScope.parent != null) {
            currentScope = currentScope.parent;
        } else {
            System.out.println("Problem! We tried to ascend scopes when there was no parent!");
        }
    }

    /**
     *
     * @return the hashtable associated with the CURRENT scope level which may be global (children are classes)
     *  class (children are methods or var declarations)
     *  or method (children are statements or var declarations)
     */
    public Map<String, TableEntry> currentScopeMap() {
        return currentScope.hash;
    }



    public SymbolTable(ASTNode root) {
        super(root);
        hash = new HashMap<String,TableEntry>();
    }

    /**
     *
     * @param key in this case the ID of the class
     * @param value Scope for the class
     * @return the HashTable belonging to the value (if it leads to a method/class) null otherwise
     */
    public void put(String key, TableEntry value) {
        hash.put(key,value);
        currentScope = this;
        // Given that what we will be adding here are classes we

    }



    public void putClass(ClassDeclExtends classNode) {
        ClassTable tableEntry = new ClassTable(classNode);
        // Set parent of the table entry to this class
        tableEntry.parent = this;
        put( classNode.i.toString(),tableEntry);
    }

    public void putClass(ClassDeclSimple classNode) {
        ClassTable tableEntry = new ClassTable(classNode);
           // Set parent of the table entry to this class
        tableEntry.parent = this;
        put(classNode.i.toString(),tableEntry);

    }

    public void putClass(MainClass classNode) {

        ClassTable tableEntry = new ClassTable(classNode);
        tableEntry.parent = this;
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

    /**
     *
     * @param key
     * @return Object associated with the key
     */
    public Object get(SymbolEntry key) {
        return hash.get(key);
    }

    /**
     *
     * @return enumeration of all keys in the symbol table
     */
    public Set keys() {
        return hash.keySet();
    }
}