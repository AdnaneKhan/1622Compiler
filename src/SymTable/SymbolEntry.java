package SymTable;

import SyntaxTree.*;


/**
 * This represents a "leaf" entry in the symbol table
 * this can represent identifiers that do not lead to new scopes themselves
 * (anything that is not a method or a class)
 */
public class SymbolEntry extends TableEntry {
    Type symType;


    private int register= -1;

    public int getRegister() {
        return register;
    }

    public void setRegister(int new_regValue) {
        register = new_regValue;
    }

    /**
     *
     * @param symbolName of the symbol that this entry will represent
     * @param constructFrom Abstract syntax node that this symbol table entry
     * will represent.
     *
     */
    public SymbolEntry(String symbolName, Type t, ASTNode constructFrom) {
        super(constructFrom);

        symType = t;
        this.symbolName = symbolName;
    }

    public int entryType() {
        return LEAF_ENTRY;
    }

    public Type getType() {
        return symType;
    }

    public boolean isEntry( int entryType) {
        return entryType == LEAF_ENTRY;
    }

    @Override
    public String toString() {
        StringBuilder toRet = new StringBuilder();

        toRet.append("This is a leaf entry named: ").append(symbolName).append('\n');
        toRet.append("The parent is: ").append(this.parent.getSymbolName()).append('\n');
        toRet.append("The type is: ").append(this.symType.toString()).append("\n");

        return toRet.toString();
    }


}