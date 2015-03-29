package SymTable;

import syntaxtree.ASTNode;

/**
 * Created by adnankhan on 3/29/15.
 */
public abstract  class TableEntry {

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
    public int entryType() {
        return 0;

    }
}
