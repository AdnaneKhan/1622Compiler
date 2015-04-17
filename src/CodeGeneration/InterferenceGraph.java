package CodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import SymTable.SymbolEntry;

/**
 * Created by adnankhan on 4/14/15.
 */
public class InterferenceGraph {
    List<InterferenceNode> igraph;
    List<Integer> colors;

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


    public Stack<InterferenceNode> coalesceGraph(ArrayList<Row> inOut, ArrayList<Row> useDefs) {
        Stack<InterferenceNode> coalesceStack = new Stack<InterferenceNode>();

        for (int i = 0; i < igraph.size(); i++) {

            InterferenceNode cursor = igraph.get(i);
            if (!cursor.moveRelated && cursor.neighbors.size() < colors.size()) {
                // if less than signficant degree and not move related
                // remove from the graph and push onto stack
                igraph.remove(cursor);
                coalesceStack.push(cursor);
            }
        }

        Stack<InterferenceNode> toRemove = new Stack<InterferenceNode>();
        for (InterferenceNode inode : igraph ){

            // Check if the neighbors lists of all coalesce candidate pairs are less than 2
            if (inode.moveRelated && inode.moveAssoc.getLinked().neighbors.size() < 23
                    && inode.neighbors.size() < 23) {
                /// We can link
                System.err.println("Ready to link");
               toRemove.push(inode.moveAssoc.getLinked());
                inode.getVariable().buildBridge(inode.moveAssoc);
            }

        }

        // this is a little hack to allow us to store nodes that get linked, and then
        // wait until we have all the coalesce candidates to remove them so we only remove
        // one side
        while (toRemove.size() > 0) {
            InterferenceNode value = toRemove.pop();
            igraph.remove(value);
            toRemove.remove(value.moveAssoc.getLinked());
            value.moveAssoc.clearBridge();

        }
        // At this point coalescing is ready to occur

        // logic for coalescing:

        // the RHS becomes the delegate of the new combined node

        // The move quadruple is flagged as bad, we might be able to do this
        // by saying that hte lhs has been dominated, and add that
        // check when we try to emit the dominated quadruple, so the symbol entry
        // will pass the info back, this might be the cleanest way to do it without modfying
        // program structure too much



        return coalesceStack;
    }


    // This is the standard simplification call used without coalescing
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
                if (igraph.get(i).neighbors.size() < 23) {
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
                igraph.remove(toStack);

                // Put that node on the stack
                istack.push(toStack);

            }
        }

        return istack;
    }


    public void buildGraph(ArrayList<Row> inOut, ArrayList<Row> useDefs) {
        // instantiate igraph
        for (int i = 0; i < useDefs.size(); i++) {
            InterferenceNode newNode;

            for (SymbolEntry var : useDefs.get(i).defs) {
                boolean exists = false;
                for (InterferenceNode existingNode : igraph) {
                    if (existingNode.getVariable().equals(var)) {
                        if (useDefs.get(i).moveRelated) {
                            // If we find it in the duplicate check
                            existingNode.setMoveAssoc( useDefs.get(i));
                        }
                        exists = true;
                        break;
                    }
                }

                // Handle the standard case
                if (!exists) {
                    newNode = new InterferenceNode(var);
                    if (useDefs.get(i).moveRelated) {
                            newNode.setMoveAssoc(useDefs.get(i));
                    }
                    igraph.add(newNode);
                }
            }
        }
    }

    public void colorGraph(Stack<InterferenceNode> simplifyStack) {
    Stack<InterferenceNode> istack = simplifyStack;

        // Now we pop these nodes off the stack one at a time while assigning colors and add them back to the graph
        while (istack.size() > 0) {
            // Pop a node
            InterferenceNode inode = istack.pop();

            // Begin color assignment
            for (int i = 0; i < colors.size(); i++) {
                boolean colorFound = true;
                for (InterferenceNode n : inode.neighbors) {
                    // Set color found to false if neighbor has color already
                    if (n.getColor() == colors.get(i)) {
                        colorFound = false;
                        break;
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
    }
}
