package SymTable;

import SyntaxTree.ASTNode;
import SyntaxTree.ClassDeclExtends;
import SyntaxTree.ClassDeclSimple;
import java.util.HashMap;
import java.util.Set;

/**
 * This is the root symbol table node for our tree of hash-tables.
 *
 * The children of this node will be the main static class and then any additional user
 * defined classes.
 */
public class SymbolTable extends TableEntry{
    protected HashMap<String, TableEntry> hash;
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