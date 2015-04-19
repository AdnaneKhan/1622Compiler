package CodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import SymTable.SymbolEntry;
import javafx.util.Pair;

/**
 * Created by adnankhan on 4/14/15.
 */
public class InterferenceGraph {
    List<InterferenceNode> igraph;
    List<Integer> colors;
    ArrayList<Pair<InterferenceNode, InterferenceNode>> assocs = new ArrayList<Pair<InterferenceNode, InterferenceNode>>();


    public InterferenceGraph() {
        igraph = new LinkedList<InterferenceNode>();
        colors = new LinkedList<Integer>();
        colors.add(Registers.AT);
        colors.add(Registers.V1);
        colors.add(Registers.TEMP0);
        colors.add(Registers.TEMP1);
        colors.add(Registers.TEMP2);
        colors.add(Registers.TEMP3);
        colors.add(Registers.TEMP4);
        colors.add(Registers.TEMP5);
        colors.add(Registers.TEMP6);
        colors.add(Registers.TEMP7);
        colors.add(Registers.TEMP8);
        colors.add(Registers.TEMP9);
        colors.add(Registers.SAVE0);
        colors.add(Registers.SAVE1);
        colors.add(Registers.SAVE2);
        colors.add(Registers.SAVE3);
        colors.add(Registers.SAVE4);
        colors.add(Registers.SAVE5);
        colors.add(Registers.SAVE6);
        colors.add(Registers.SAVE7);
        colors.add(Registers.K0);
        colors.add(Registers.K1);
        colors.add(Registers.GP);
    }


    /**
     * Starts off by simplifying nodes of flesser degre
     *
     * @return
     */
    public Stack<InterferenceNode> coalesceGraph(Stack<InterferenceNode> buildFrom) {
        Stack<InterferenceNode> coalesceStack = new Stack<InterferenceNode>();

        coalesceStack.addAll(buildFrom);

        // remove things of lesser degree
        for (int i = 0; i < igraph.size(); i++) {

            InterferenceNode cursor = igraph.get(i);
            if (!cursor.moveRelated && cursor.getNeighbors().size() < colors.size()) {
                // if less than signficant degree and not move related
                // remove from the graph and push onto stack
                removeFromGraph(cursor);
                coalesceStack.push(cursor);
            }
        }


            for (int i = 0; i < assocs.size(); i++) {
                if (assocs.get(i).getKey().getDynamicNeighbors().size() < colors.size() && assocs.get(i).getValue().getDynamicNeighbors().size() < colors.size()) {
                    if (!assocs.get(i).getKey().getNeighbors().contains(assocs.get(i).getValue())) {
                        removeFromGraph(assocs.get(i).getValue());
                        assocs.get(i).getKey().merge(assocs.get(i).getValue());


                        for (int j = 0; j < assocs.size(); j++) {
                            if (assocs.get(j).getKey().equals(assocs.get(i).getKey())) {

                                Pair<InterferenceNode, InterferenceNode> toAdd = new Pair<InterferenceNode, InterferenceNode>(assocs.get(i).getKey(), assocs.get(j).getValue());


                                assocs.add(j, toAdd);
                                assocs.remove(assocs.get(j));

                            }

                            if (assocs.get(j).getValue().equals(assocs.get(i).getKey())) {
                                Pair<InterferenceNode, InterferenceNode> toAdd = new Pair<InterferenceNode, InterferenceNode>(assocs.get(j).getKey(), assocs.get(i).getKey());

                                assocs.add(j, toAdd);
                                assocs.remove(assocs.get(j));
                            }

                            if (assocs.get(j).getKey().equals(assocs.get(i).getValue())) {
                                Pair<InterferenceNode, InterferenceNode> toAdd = new Pair<InterferenceNode, InterferenceNode>(assocs.get(i).getKey(), assocs.get(j).getValue());

                                assocs.add(j, toAdd);
                                assocs.remove(assocs.get(j));

                            }

                            if (assocs.get(j).getValue().equals(assocs.get(i).getValue())) {
                                Pair<InterferenceNode, InterferenceNode> toAdd = new Pair<InterferenceNode, InterferenceNode>(assocs.get(j).getKey(), assocs.get(i).getKey());

                                assocs.add(j, toAdd);
                                assocs.remove(assocs.get(j));
                            }

                        }
                } else {
                        assocs.get(i).getKey().moveRelated = false;
                        assocs.get(i).getValue().moveRelated = false;

                   }
                }

            }


        for (Pair<InterferenceNode,InterferenceNode> curr : assocs) {
            if (curr.getKey().equals(curr.getValue())) {
                assocs.remove(curr);
            }
        }

        // DEBUG: Print the Pairs
        /*System.out.println("PAIRS");
        for (Pair<InterferenceNode, InterferenceNode> curr : assocs) {
            System.out.print(curr.getKey().getVariable().getSymbolName() + ": ");
            if (curr.getKey().dominated) {
                System.out.print("Dominated, ");
            }
            else {
                System.out.print("Not Dominated, ");
            }
            System.out.print(curr.getValue().getVariable().getSymbolName() + ": ");
            if (curr.getKey().dominated) {
                System.out.print("Dominated");
            }
            else {
                System.out.print("Not Dominated");
            }
            System.out.println();
        }
        System.out.println();*/


        return coalesceStack;
    }


    /**
     * removes node from igraph
     *
     * @param toStack
     */
    public void removeFromGraph(InterferenceNode toStack) {
        for (InterferenceNode node : toStack.getDynamicNeighbors()) {
            if (node.getDynamicNeighbors().contains(toStack)) {
                node.getDynamicNeighbors().remove(toStack);
            }
        }
        igraph.remove(toStack);
    }


    /**
     * simplifies the graph
     *
     * @param oldStack if there is a stack that has old variables in it, this stack can transfer them in
     *                 other wise it is acceptabble to pass in an empty stack.
     * @return
     */
    public Stack<InterferenceNode> simplifyGraph(Stack<InterferenceNode> oldStack) {
        Stack<InterferenceNode> istack = new Stack<InterferenceNode>();
        // Add all the elements from the old stack (in cases where we had nodes
        // of less than significant degree popped off in coalesce simplify stage
        istack.addAll(oldStack);


        // While the graph still contains nodes
        while (igraph.size() > 0) {

            // Find a node we can push to the stack
            InterferenceNode toStack = null;
            for (int i = 0; i < igraph.size(); i++) {
                if (igraph.get(i).getDynamicNeighbors().size() < 23) {
                    toStack = igraph.get(i);
                }
            }

            // If there is no node, spill (or abort)
            if (toStack == null) {
                System.out.println("A spill has been detected. Please exit the program.");
                System.exit(1);
            }
            // Else we continue on
            else {

                // Remove the interference node from the graph and...
                removeFromGraph(toStack);


                // Put that node on the stack
                istack.push(toStack);

            }
        }

        return istack;
    }

    private void addPair(InterferenceNode first, InterferenceNode second) {
        Pair<InterferenceNode, InterferenceNode> toAdd = new Pair<InterferenceNode, InterferenceNode>(first, second);
        assocs.add(toAdd);

    }

    /**
     * Builds the interference graph from the uses and defs
     *
     * @param useDefs
     */
    public void buildGraph(ArrayList<Row> useDefs, ArrayList<Row> inOut) {
        // instantiate igraph
        for (int i = 0; i < useDefs.size(); i++) {
            InterferenceNode newNode;

            for (SymbolEntry var : useDefs.get(i).defs) {

                boolean exists = false;
                for (InterferenceNode existingNode : igraph) {
                    if (existingNode.getVariable().equals(var)) {
                        if (useDefs.get(i).moveRelated) {
                            for (SymbolEntry se : useDefs.get(i).uses) {
                                if (!(se.isPreColor())) {
                                    InterferenceNode otherNode = se.getLinked();
                                    otherNode.moveRelated = true;
                                    addPair(existingNode, otherNode);
                                    break;
                                }
                            }
                        }
                        exists = true;
                        break;
                    }
                }

                // Handle the standard case
                if (!exists) {

                    newNode = new InterferenceNode(var);
                    if (useDefs.get(i).moveRelated) {
                        for (SymbolEntry se : useDefs.get(i).uses) {
                            if (!(se.isPreColor())) {
                                InterferenceNode otherNode = se.getLinked();
                                otherNode.moveRelated = true;
                                addPair(newNode, otherNode);
                                break;
                            }
                        }
                    }
                    igraph.add(newNode);
                }
            }
        }

        for (int i = 0; i < igraph.size(); i++) {
            InterferenceNode currentNode = igraph.get(i);
            for (int j = 0; j < inOut.size(); j++) {
                if (inOut.get(j).defs.contains(currentNode.getVariable())) {
                    for (SymbolEntry s : inOut.get(j).defs) {
                        if (!(s.isPreColor()) && !s.equals(currentNode.getVariable())) {
                            InterferenceNode n = s.getLinked();


                            // fix for non defed class variables
                            if (n == null) {
                                n = new InterferenceNode(s);
                                igraph.add(n);
                            }
                            if ( !currentNode.getNeighbors().contains(n)) {
                                currentNode.getNeighbors().add(n);
                                if (!n.getNeighbors().contains(currentNode)) n.getNeighbors().add(currentNode);
                            }
                        }
                    }
                }
            }
        }

        for (int i = 0; i < igraph.size(); i++) {
            InterferenceNode currentNode = igraph.get(i);
            ArrayList<Integer> indexes = new ArrayList<Integer>();
            ArrayList<SymbolEntry> rhs = new ArrayList<SymbolEntry>();
            for (int j = 0; j < useDefs.size(); j++) {
                if (useDefs.get(j).defs.contains(currentNode.getVariable()) && useDefs.get(j).uses.size() == 1) {
                    indexes.add(j);
                    for (SymbolEntry s : useDefs.get(j).uses) {
                        rhs.add(s);
                        break;
                    }
                }
            }
            for (int j = 0; j < indexes.size(); j++) {
                for (SymbolEntry s : inOut.get(indexes.get(j)).defs) {
                    if (!s.equals(rhs.get(j)) && !(s.isPreColor()) && !s.equals(currentNode.getVariable())) {
                        InterferenceNode n = s.getLinked();

                        if (!currentNode.getNeighbors().contains(n)) {
                            currentNode.getNeighbors().add(n);
                            if (!n.getNeighbors().contains(currentNode)) n.getNeighbors().add(currentNode);
                        }
                    }
                }
            }
        }

        for (InterferenceNode node : igraph) {
            node.copyNeighbors();
        }

        // DEBUG: Print the uncolored graph
        /*System.out.println("UNCOLORED IGRAPH");
        for (InterferenceNode n : igraph) {
            System.out.print(n.getVariable().getSymbolName() + ": ");
            for (InterferenceNode i : n.getNeighbors()) {
                System.out.print(i.getVariable().getSymbolName() + ",");
            }
            System.out.println();
        }
        System.out.println();

        for (InterferenceNode node : igraph) {
            node.copyNeighbors();
        }*/

    }

    public void colorGraph(Stack<InterferenceNode> simplifyStack) {

        // Now we pop these nodes off the stack one at a time while assigning colors and add them back to the graph
        while (simplifyStack.size() > 0) {
            // Pop a node
            InterferenceNode inode = simplifyStack.pop();

            // Begin color assignment
            for (int i = 0; i < colors.size(); i++) {
                boolean colorFound = true;
                for (InterferenceNode n : inode.getNeighbors()) {
                    // Set color found to false if neighbor has color already
                    if (n.getColor() == colors.get(i)) {
                        colorFound = false;
                    }
                }
                // If color found is still true, set the color and break, else continue
                if (colorFound) {
                    inode.setColor(colors.get(i));
                    break;
                }
            }

            // Add the node back to the graph
            igraph.add(inode);
        }

        // DEBUG: Print the colored graph
        /*System.out.println("COLORED IGRAPH");
        for (InterferenceNode n : igraph) {
            System.out.print(n.getVariable().getSymbolName() + ": ");
            for (InterferenceNode i : n.getNeighbors()) {
                System.out.print(i.getVariable().getSymbolName() + ",");
            }
            System.out.println();
        }
        System.out.println();

        for (InterferenceNode node : igraph) {
            node.copyNeighbors();
        }*/

    }
}
