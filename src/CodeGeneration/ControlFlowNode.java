package CodeGeneration;

import IR.Quadruple;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by adnankhan on 4/14/15.
 *
 *
 *  This is a single node of the conrol flow graph used in liveless analysis.
 */
public class ControlFlowNode {

    int index;

    public ControlFlowNode(Quadruple toWrap, int index) {
        this.index = index;
        this.irLine = toWrap;

        predecessors = new ArrayList<ControlFlowNode>();
        sucessors = new ArrayList< ControlFlowNode>();
    }

    // We store sucessors and predecssors since we are doing top to bottom when generating, but need
    // predecessors when we do reverse dfs liveness analysis
    public List<ControlFlowNode> sucessors;
    public List<ControlFlowNode> predecessors;

    private String lhsReg;
    private String rhs1Reg;
    private String rhs2Reg;

    public Quadruple irLine;

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
