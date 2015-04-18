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


    private List<InterferenceNode> neighbors;
    private List<InterferenceNode> dynamicNeighbors;

    public InterferenceNode(SymbolEntry node) {

        neighbors = new LinkedList<InterferenceNode>();
        dynamicNeighbors = new LinkedList<InterferenceNode>();
        variable = node;

        variable.setINode(this);
        color = -5;
    }


    public List<InterferenceNode> getNeighbors() {
        if (dominated) {
            return variable.coalesceBridge.getLinked().getNeighbors();
        }
        return neighbors;
    }

    public List<InterferenceNode> getDynamicNeighbors() {
        if (dominated) {
            return variable.coalesceBridge.getLinked().getDynamicNeighbors();
        }
        return dynamicNeighbors;
    }

    public void merge(InterferenceNode toMerge) {


        // combine the list of neighbors, remove duplicates
        for (int i = 0; i < toMerge.getNeighbors().size(); i++) {
            if (!this.getNeighbors().contains(toMerge.getNeighbors().get(i))) {
                this.getNeighbors().add(toMerge.getNeighbors().get(i));
            }
        }

        // do the same thing for the dynamic neighbors
        for (int i = 0; i < toMerge.getDynamicNeighbors().size(); i++) {
            if (!this.getDynamicNeighbors().contains(toMerge.getDynamicNeighbors().get(i))) {
                this.getDynamicNeighbors().add(toMerge.getDynamicNeighbors().get(i));
            }
        }

        toMerge.dominated = true;

        toMerge.getVariable().buildBridge(this.getVariable());
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
