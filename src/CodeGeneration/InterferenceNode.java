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
    public boolean dominated = false;
    public SymbolEntry moveAssoc;

    public void  setMoveAssoc(Row toSet) {

        for (SymbolEntry use : toSet.uses) {
            moveAssoc = use;
            break;
        }
        moveAssoc.getLinked().moveRelated = true;
        moveAssoc.getLinked().moveAssoc = variable;
        toSet.moveRelated = true;
        moveRelated = true;
    }


    private List<InterferenceNode> neighbors;
    private List<InterferenceNode> dynamicNeighbors;

    public InterferenceNode(SymbolEntry node) {

        neighbors = new LinkedList<InterferenceNode>();
        variable = node;

        variable.setINode(this);
        color = -5;
    }

    public void merge(InterferenceNode toMerge) {
        // combine the list of neighbors, remove duplicates
        for (int i = 0; i < toMerge.neighbors.size(); i++) {
            if (!this.neighbors.contains(toMerge.neighbors.get(i))) {
                this.neighbors.add(toMerge.neighbors.get(i));
            }
        }

        // do the same thing for the dynamic neighbors
        for (int i = 0; i < toMerge.dynamicNeighbors.size(); i++) {
            if (!this.neighbors.contains(toMerge.dynamicNeighbors.get(i))) {
                this.dynamicNeighbors.add(toMerge.dynamicNeighbors.get(i));
            }
        }


        // The node given is can now be removed from the graph and marked as a bridge
    }

    public List<InterferenceNode> getNeighbors() {
        if (dominated) {
            return moveAssoc.getLinked().getNeighbors();
        }
        return neighbors;
    }

    public List<InterferenceNode> getDynamicNeighbors() {
        if (dominated) {
            return moveAssoc.getLinked().getDynamicNeighbors();
        }
        return dynamicNeighbors;
    }

    public void copyNeighbors() {
        dynamicNeighbors = new LinkedList<InterferenceNode>();
        dynamicNeighbors.addAll(neighbors);
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

    @Override
    public boolean equals(Object toCheck) {
        if (toCheck instanceof InterferenceNode) {
            return variable.equals(((InterferenceNode) toCheck).getVariable());
        }
        return false;
    }

}
