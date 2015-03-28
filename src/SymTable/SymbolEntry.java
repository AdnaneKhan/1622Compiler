package SymTable;

import java.util.Hashtable;

class SymbolEntry {
    String symbolName;
    int lineNum;
    int charNum;
    // Holds mappings fromm symmbol strings to their actual objects
    private static Dictionary<SymbolEntry, Object> dict = new HashMap<>();

    public int getLine() {
        return lineNum;
    }
    public int getColumn() {
        return charNum;
    }


    /**
     *
     * @param toStore
     * @param constructfrom
     */
    public SymbolEntry(String toStore, ASTNode constructFrom) {
        symbolName = toStore;
        this.lineNum = constructFrom.getLine();
        this.charNum = constructFrom.getCol();
    }

    /***
     *
     * @param s symbol ID string to add/check
     * @return
     */
    public static SymbolEntry symbol(String s) {
        String handle = n.intern();

        SymbolEntry resolvedEntry = dict.get(handle);
        if (resolvedEntry == null) {
            resolvedEntry = new SymbolEntry(handle);
            dict.put(handle,resolvedEntry);
        }

        return resolvedEntry;
    }
}