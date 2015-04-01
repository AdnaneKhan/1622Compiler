package SymTable;

import SyntaxTree.ASTNode;

import java.util.HashMap;

/**
 * Created by adnankhan on 3/29/15.
 */
public abstract  class TableEntry {
    protected HashMap<String, TableEntry> hash;
    public final static int METHOD_ENTRY = 1;
    public final static int CLASS_ENTRY = 2;
    public final static int LEAF_ENTRY = 3;
    public final static int ROOT_ENTRY = 4;

    String symbolName;
    // Reference to the parent scope which this symbol entry resides in, the parent scope can be
    // accessed to check for duplicates, etc.
    public TableEntry parent;
    private ASTNode actualNode;

    int lineNum;
    int charNum;

    public int getLine() {
        return lineNum;
    }
    public int getColumn() {
        return charNum;
    }

     protected  TableEntry(ASTNode actualNode) {
        this.actualNode = actualNode;
        this.lineNum = actualNode.lineNum();
        this.charNum = actualNode.charNum();
    }

    public boolean hasEntry(String key, int entry_type) {
        if (this.hash.containsKey(key) && this.hash.get(key).isEntry(entry_type)) {
            return true;
        } else
        {
            return false;
        }
    }

    public String getSymbolName() {
        return symbolName;
    }

    public ASTNode getNode() {
        return actualNode;
    }

    /**
     *
     * @return type of this entry which can be:
     *
     *      Method
     *      Class
     *      Leaf Entry
     *
     */
    public abstract int entryType();


    /**
     *
     * @return whether entry type is equal to that passed in
     */
    public abstract boolean isEntry(int entryType);
}
