package CodeGeneration;

import IR.Quadruple;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adnankhan on 4/14/15.
 * <p/>
 * <p/>
 * This is a single node of the conrol flow graph used in liveless analysis.
 */
public class ControlFlowNode {

    int index;

    public ControlFlowNode(Quadruple toWrap, int index) {
        this.index = index;
        this.irLine = toWrap;

        predecessors = new ArrayList<ControlFlowNode>();
        sucessors = new ArrayList<ControlFlowNode>();
    }

    // We store sucessors and predecssors since we are doing top to bottom when generating, but need
    // predecessors when we do reverse dfs liveness analysis
    public List<ControlFlowNode> sucessors;
    public List<ControlFlowNode> predecessors;

    public Quadruple irLine;

}
