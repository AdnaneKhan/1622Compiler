package CodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import SymTable.SymbolEntry;
import sun.jvm.hotspot.asm.Register;

/**
 * Created by adnankhan on 4/14/15.
 */
public class InterferenceGraph {
    Stack<InterferenceNode> istack;
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

    public void buildGraph(ArrayList<Row> inOut, ArrayList<Row> useDefs) {
        // instantiate igraph
        for (int i = 0; i < useDefs.size(); i++) {
            InterferenceNode newNode;


            for (SymbolEntry var : useDefs.get(i).defs) {
                boolean exists = false;
                for (InterferenceNode existingNode : igraph) {
                    if (existingNode.getVariable().equals(var)) {
                        if (useDefs.get(i).moveRelated) {

                            for (SymbolEntry use : useDefs.get(i).uses) {
                                existingNode.setMoveAssoc(use);
                                existingNode.moveRelated = true;
                                break;
                            }


                        }
                        exists = true;
                        break;
                    }

                }
                if (!exists) {
                    newNode = new InterferenceNode(var);
                    if (useDefs.get(i).moveRelated) {

                        for (SymbolEntry use : useDefs.get(i).uses) {
                            newNode.setMoveAssoc(use);
                            newNode.moveRelated = true;
                            break;
                        }


                    }
                    igraph.add(newNode);
                }

            }
        }

        // add neighbors to nodes in graph
        for (int i = 0; i < useDefs.size(); i++) {
            InterferenceNode currentNode = null;
            for (SymbolEntry var : useDefs.get(i).defs) {
                for (int j = 0; j < igraph.size(); j++) {
                    if (igraph.get(j).getVariable().equals(var)) {
                        currentNode = igraph.get(j);
                        break;
                    }
                }


                for (SymbolEntry neighbor : inOut.get(i).defs) {
                    for (int j = 0; j < igraph.size(); j++) {
                        if (igraph.get(j).getVariable().equals(neighbor)) {
                            currentNode.neighbors.add(igraph.get(j));
                            break;
                        }
                    }
                }
            }
        }
    }

    public void colorGraph() {

        istack = new Stack<InterferenceNode>();

        

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
