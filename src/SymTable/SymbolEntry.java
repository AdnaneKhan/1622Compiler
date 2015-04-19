package SymTable;

import CodeGeneration.InterferenceNode;
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

    public SymbolEntry coalesceBridge = null;
    private InterferenceNode nodeLink;

    private boolean preColor = false;
    private boolean defed = false;
    private int preColorReg;
    private int register = DEAD_REG;

    public int getRegister() {

        if (coalesceBridge != null) {
            return coalesceBridge.getRegister();
        } else if (preColor && !defed) {
            return preColorReg;
        } else if (register == DEAD_REG) {
            System.err.println("We are trying to get a dead register for:\n" + this.toString());
        }

        return register;
    }

    public void defed() {
        // the param has been defed

        if (coalesceBridge != null) {
             coalesceBridge.defed();
        } else if (preColor) {
            defed = true;
        }
    }

    public void preColor(int reg) {
        if (coalesceBridge != null) {
            coalesceBridge.preColor(reg);
        } else {
            preColor = true;
            preColorReg = reg;
            assignRegister(reg);
        }

    }

    public boolean isPreColor() {
        if (coalesceBridge != null) {
            return coalesceBridge.isPreColor();
        } else{
            return preColor;
        }

    }

    /**
     *
     * @param inode set inode for this entry (happens in graph coloring phase)
     */
    public void setINode(InterferenceNode inode) {
        nodeLink = inode;
    }

    /**
     *
     * @return the interefrence node that is assocated with this variable
     */
    public InterferenceNode getLinked() {


        return nodeLink;
    }



    /**
     *
     *
     * @param toBridge symbol table entry to bridge this one with (the one we bridge with becomes the domminant
     *                 value)
     */
    public void buildBridge(SymbolEntry toBridge) {
        coalesceBridge = toBridge;
    }

    public void clearBridge() {
        coalesceBridge = null;
    }

    public void assignRegister(int new_regValue) {
        // If there is a coalesce bridge present then assign the register to the other value
        if (coalesceBridge != null) {
            coalesceBridge.assignRegister(new_regValue);
            // If we def a parameter (precolored) it loses its coloring
        } if (register != DEAD_REG) {
            if (preColor) {
                register = new_regValue;
            }  else {
                System.err.println("We tried to give a live register a new live value:\n" + new_regValue +"  " + this.toString());
            }

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