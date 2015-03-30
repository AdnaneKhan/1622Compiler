package SymTable;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import syntaxtree.*;


/**
 * This represents a "leaf" entry in the symbol table
 * this can represent identifiers that do not lead to new scopes themselves
 * (anything that is not a method or a class)
 */
class SymbolEntry extends TableEntry{

    String symbolName;
    // Reference to the parent scope which this symbol entry resides in, the parent scope can be
    // accessed to check for duplicates, etc.
    public TableEntry parent;
    // Holds mappings fromm symmbol strings to their actual objects



    /**
     *
     * @param symbolName of the symbol that this entry will represent
     * @param constructFrom Abstract syntax node that this symbol table entry
     * will represent.
     *
     */
    public SymbolEntry(String symbolName, ASTNode constructFrom) {
        super(constructFrom);
        this.symbolName = symbolName;

    }

    public int entryType() {
        return LEAF_ENTRY;
    }

    /***
     *
     * @param s symbol ID string to add/check
     * @return
     *
    public static SymbolEntry symbol(String s) {
        String handle = s.intern();

        SymbolEntry resolvedEntry = (SymbolEntry)dict.get(handle);
        if (resolvedEntry == null) {
            resolvedEntry = new SymbolEntry(handle);
            dict.put(handle,resolvedEntry);
        }

        return resolvedEntry;
    }*/
}