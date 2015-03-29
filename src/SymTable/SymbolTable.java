package SymTable;

import com.sun.tools.javac.util.Name;
import syntaxtree.ASTNode;
import syntaxtree.ClassDecl;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;

import java.lang.Package;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Hashtable;
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

    public void putClass(ASTNode classNode) {
        if (classNode instanceof ClassDeclExtends) {
            ClassTable tableEntry = new ClassTable(classNode);
            // Set parent of the table entry to this class
            tableEntry.parent = this;
            put( ((ClassDeclExtends) classNode).i.toString(),tableEntry);
        } else if(classNode instanceof ClassDeclSimple) {
            ClassTable tableEntry = new ClassTable(classNode);
            // Set parent of the table entry to this class
            tableEntry.parent = this;

            put( ((ClassDeclSimple) classNode).i.toString(),tableEntry);
        } else {
            // We tried to add something that isn't a class to the program's decendents, parser should have prevneted this so we
            // should never reach this point.
        }
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