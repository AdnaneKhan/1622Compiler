package SymTable;

import SyntaxTree.ASTNode;
import SyntaxTree.MethodDecl;
import SyntaxTree.VarDecl;
import javafx.scene.control.Tab;

import java.util.HashMap;

/**
 * Created by adnankhan on 3/29/15.
 */
public abstract class TableEntry {
    protected HashMap<KeyWrapper, TableEntry> hash;
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

    protected TableEntry(ASTNode actualNode) {

        if (!this.isEntry(LEAF_ENTRY)) {
            hash = new HashMap<KeyWrapper, TableEntry>();
        }

        this.actualNode = actualNode;
        this.lineNum = actualNode.lineNum();
        this.charNum = actualNode.charNum();
    }


    /**
     * @param key        string key to lookup, this is the identifier in the code
     * @param entry_type Class, Method, or Type (leaf
     *                   meaning int[], boolean, int or Object
     * @return true if the type exists in THIS SCOPE
     */
    public boolean hasEntry(String key, int entry_type) {

        if (!this.isEntry(LEAF_ENTRY)) {
            KeyWrapper checkWrap = new KeyWrapper(key, entry_type);

            if (this.hash.containsKey(checkWrap)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param key        string key to lookup, this is the identifier in the code
     * @param entry_type Class, Method, or Type (leaf
     *                   meaning int[], boolean, int or Object
     * @return null if entry does not exist with the specified type
     */
    public TableEntry getEntry(String key, int entry_type) {
        if (this.entryType() != LEAF_ENTRY) {
            KeyWrapper checkWrap = new KeyWrapper(key, entry_type);
            if (this.hash.containsKey(checkWrap)) {
                return hash.get(checkWrap);
            }
        }
        return null;
    }


    /**
     * @param key
     * @param entry_type
     * @return the entry if it exists in this scope OR any of its parent scopes
     */
    public TableEntry getEntryWalk(String key, int entry_type) {
        TableEntry retV = null;

        TableEntry cursor = this;
        KeyWrapper checkKey = new KeyWrapper(key, entry_type);

        do {
            if (cursor.entryType() != LEAF_ENTRY) {
                if (cursor.isEntry(CLASS_ENTRY)) {

                    if (entry_type == METHOD_ENTRY) {
                        retV = ((ClassTable) cursor).getMethod(key);

                    } else if (entry_type == LEAF_ENTRY) {
                        retV = ((ClassTable) cursor).getVariable(key);
                    }
                } else {
                    retV = cursor.hash.get(checkKey);
                }

                if (retV != null) {
                    break;
                }
            }
            cursor = cursor.parent;
        } while (cursor != null);

        return retV;
    }

    /**
     * @param key
     * @param entry_type
     * @return true if the entry if it exists in this scope OR any of its parent scopes
     */
    public boolean hasEntryWalk(String key, int entry_type) {

        boolean found = false;
        TableEntry cursor = this;
        KeyWrapper checkKey = new KeyWrapper(key, entry_type);

        do {
            if (cursor.entryType() != LEAF_ENTRY) {
                found = cursor.hash.containsKey(checkKey);
                if (found) {
                    break;
                }
            }
            cursor = cursor.parent;
        } while (cursor != null);

        return found;
    }


    /**
     * The string name of this symbol associated with the ID type
     * there may be more names in which case the getNode method must be used
     */
    public String getSymbolName() {
        return symbolName;
    }

    public String getHierarchyName() {
        StringBuilder hierarchy = new StringBuilder();

        if (this.isEntry(ROOT_ENTRY)) {
            // no op
        } else if (this.isEntry(CLASS_ENTRY)) {
            hierarchy.append(this.getSymbolName()).append('_');
        } else if (this.isEntry(METHOD_ENTRY)) {
            hierarchy.append(this.parent.getHierarchyName()).append(this.getSymbolName()).append('_');
        } else if (this.isEntry(LEAF_ENTRY)) {
            hierarchy.append(this.parent.getHierarchyName()).append(this.getSymbolName());
        }


        return hierarchy.toString();
    }

    /**
     * @return the actual AST node wrapped by this entry
     */
    public ASTNode getNode() {
        return actualNode;
    }

    /**
     * @return type of this entry which can be:
     * <p/>
     * Method
     * Class
     * Leaf Entry
     */
    public abstract int entryType();

    /**
     * @return whether entry type is equal to that passed in
     */
    public abstract boolean isEntry(int entryType);
}
