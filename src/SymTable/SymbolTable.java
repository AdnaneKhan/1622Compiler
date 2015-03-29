package SymTable;

import java.lang.Package;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

public class SymbolTable {
    protected HashMap<SymbolEntry, TableEntry> hash;
    public SymbolTable() {
        hash = new HashMap<SymbolEntry,TableEntry>();
    }

    /**
     *
     * @param key in this case the ID of the class
     * @param value Scope for the class
     * @return the HashTable belonging to the value (if it leads to a method/class) null otherwise
     */
    public void put(SymbolEntry key, TableEntry value) {
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
    public Set keys() {
        return hash.keySet();
    }
}