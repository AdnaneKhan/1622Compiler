package SymTable;



import java.lang.Package;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Hashtable;

public class SymbolTable {
    protected Hashtable<SymbolEntry, SymbolTable> hash;
    public SymbolTable() {
        hash = new HashMap<>();
    }


    /**
     *
     * @param key
     * @param value
     * @return the HashTable belonging to the value (if it leads to a method/class) null otherwise
     */
    public SymbolTable put(SymbolEntry key, Object value) {
        hash.put(key,value);

        // Now we check if the value object has a scope of its own (is it a method or a class?)
        //

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
    public Enumeration keys() {
        return hash.keys();
    }
}