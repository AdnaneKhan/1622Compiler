package SymTable;

import CodeGeneration.Registers;
import SyntaxTree.*;


/**
 * This represents a "leaf" entry in the symbol table
 * this can represent identifiers that do not lead to new scopes themselves
 * (anything that is not a method or a class)
 */
public class SymbolEntry extends TableEntry {
    public static final int DEAD_REG = -5;
    Type symType;

    private int register = DEAD_REG;

    public int getRegister() {

        if (register == DEAD_REG) {
            System.err.println("We are trying to get a dead register for:\n" + this.toString());
        }

        return register;
    }

    public void assignRegister(int new_regValue) {
        if (register != DEAD_REG) {
            System.err.println("We tried to give a live register a new live value!");
        } else {
            register = new_regValue;
        }
    }

    public int kill() {
        int oldValue = register;
        if (register == DEAD_REG) {
            System.err.println("We tried to kill a register that was already dead!");
        } else {
            register = DEAD_REG;
        }
        return register;
    }

    /**
     * @param symbolName    of the symbol that this entry will represent
     * @param constructFrom Abstract syntax node that this symbol table entry
     *                      will represent.
     */
    public SymbolEntry(String symbolName, Type t, ASTNode constructFrom) {
        super(constructFrom);

        symType = t;
        this.symbolName = symbolName;
        if (symbolName.equals("this")) {
            this.register = Registers.ARG0;
        }

    }

    public int entryType() {
        return LEAF_ENTRY;
    }

    public Type getType() {
        return symType;
    }

    public boolean isEntry(int entryType) {
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