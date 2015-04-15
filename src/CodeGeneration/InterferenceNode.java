package CodeGeneration;

import SymTable.SymbolEntry;

import java.util.List;

/**
 * Created by adnankhan on 4/14/15.
 */
public class InterferenceNode {
    private SymbolEntry variable;
    public List<InterferenceNode> neighbors;

    public InterferenceNode(SymbolEntry node) {
        variable = node;
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
    }


}
