package SymTable;


import SyntaxTree.*;


/**
 * This represents a "leaf" entry in the symbol table
 * this can represent identifiers that do not lead to new scopes themselves
 * (anything that is not a method or a class)
 */
class SymbolEntry extends TableEntry{

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
}