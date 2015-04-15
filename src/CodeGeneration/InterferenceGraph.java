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
    Stack<InterferenceNode> istack;
    List<InterferenceNode> igraph;
    List<Integer> colors;

    public InterferenceGraph() {
        igraph = new LinkedList<InterferenceNode>();
        colors = new LinkedList<Integer>();
        colors.add(1);
        colors.add(3);
        colors.add(8);
        colors.add(9);
        colors.add(10);
        colors.add(11);
        colors.add(12);
        colors.add(13);
        colors.add(14);
        colors.add(15);
        colors.add(24);
        colors.add(25);
        colors.add(16);
        colors.add(17);
        colors.add(18);
        colors.add(19);
        colors.add(20);
        colors.add(21);
        colors.add(22);
        colors.add(23);
        colors.add(26);
        colors.add(27);
        colors.add(28);
    }

    public void buildGraph(ArrayList<Row> inOut, ArrayList<Row> useDefs) {
    	// instantiate igraph
    	for (int i = 0; i < useDefs.size(); i++) {
    		InterferenceNode newNode;
    		for (SymbolEntry var : useDefs.get(i).defs) {
    			newNode = new InterferenceNode(var);
    			igraph.add(newNode);
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
