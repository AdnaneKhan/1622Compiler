package CodeGeneration;

import SymTable.SymbolEntry;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by adnankhan on 4/14/15.
 */
public class InterferenceNode {
    private SymbolEntry variable;
    private int color;

    public boolean moveRelated = false;
    public SymbolEntry moveAssoc;

    public void  setMoveAssoc(SymbolEntry toSet) {
        moveAssoc = toSet;
    }

    public List<InterferenceNode> neighbors;

    public InterferenceNode(SymbolEntry node) {

        neighbors = new LinkedList<InterferenceNode>();
        variable = node;
        color = -5;
    }


    public SymbolEntry getVariable() {
        return variable;
    }

    /**
     * gets real register and passes it to variable
     *
     * @param color
     */
    public void setColor(int color) {
        // set register in the symbol table entry
        this.variable.assignRegister(color);

        // set color value to color
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

}
