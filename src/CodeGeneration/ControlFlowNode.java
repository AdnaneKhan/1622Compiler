package CodeGeneration;

import IR.Quadruple;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by adnankhan on 4/14/15.
 *
 *
 *  This is a single node of the conrol flow graph used in liveless analysis.
 */
public class ControlFlowNode {
    static HashMap<String,ControlFlowNode> labelMap = new HashMap<String, ControlFlowNode>();
    int index;

    public ControlFlowNode(Quadruple toWrap, int index) {
        this.index = index;
        this.irLine = toWrap;
    }

    // We store sucessors and predecssors since we are doing top to bottom when generating, but need
    // predecessors when we do reverse dfs liveness analysis
    Set<ControlFlowNode> sucessors;
    Set<ControlFlowNode> predecessors;

    private String lhsReg;
    private String rhs1Reg;
    private String rhs2Reg;

    private Quadruple irLine;

    public String getLhs() {
        return irLine.getResult();
    }

    public boolean isRhsTuple() {
        if (irLine.arg2.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getRhsOne() {
        return irLine.getArg1();
    }

    public String getRhsTwo() {
        return irLine.getArg2();
    }
}