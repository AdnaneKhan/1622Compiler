package CodeGeneration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by adnankhan on 4/14/15.
 */
public class InterferenceGraph {
    Stack<InterferenceNode> istack;
    List<InterferenceNode> igraph;

    public InterferenceGraph() {
        igraph = new LinkedList<InterferenceNode>();
    }

    public void buildGraph(ArrayList<Row> inOut, ArrayList<Row> useDefs) {

    }

    public void colorGraph() {

    }

}
