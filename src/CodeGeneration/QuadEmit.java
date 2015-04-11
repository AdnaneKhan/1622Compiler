package CodeGeneration;

import IR.Quadruple;
import SymTable.MethodTable;
import SyntaxTree.ASTNode;
import SyntaxTree.Formal;
import SyntaxTree.MethodDecl;

import java.util.HashMap;

/**
 * Created by adnankhan on 4/10/15.
 */
public class QuadEmit {
    int tRegC;
    int sRegC;
    int fRegC;

    private HashMap<String,String> regMap;


    public QuadEmit() {
        regMap = new HashMap<String,String>();
    }

    private String getTReg() {
        String tempReg = null;

        if (tRegC == 9) {
            // Limit Reached
        } else {
            tempReg = "$t" + tRegC;
            tRegC++;
        }
        return tempReg;
    }

    private String getAReg() {
        String tempReg = null;

        if (fRegC == 4) {
            // Limit Reached
        } else {
            tempReg = "$a" + fRegC;
            fRegC++;
        }
        return tempReg;
    }

    /**
     *
     * @return mips instruction loading the parameter into appropriate variable
     */
    public String handleParameter(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        if ( quad.isLiteral() ) {
            instruction.append("li ");
            instruction.append(getAReg());
            instruction.append(", ");
            instruction.append(quad.getResult());
        } else {
            String paramVar = regMap.get(quad.getResult());

            instruction.append("move ");
            instruction.append(getAReg());
            instruction.append(", ");
            instruction.append(paramVar);
        }

        return instruction.toString();
    }

    public String handlePrint(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        instruction.append("jal _system_out_println");


        return instruction.toString();
    }

    public String handleCall(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Add reference to method for calls

        // Get class name
        ///String className = quad.arg1_entry.parent.getSymbolName();


        instruction.append("jal").append(' ').append(quad.getArg1()).append("\n");

        // Now for moving the return value

        // Now we need to output instruction to get the value that returns
        // Later when we are doing liveness analysis we need to be able to identify in the method
        // whether there is a return value at all (meaning that there is a def into $v0, otherwise we do
        // not need to pull the return value out


        String defRegister;
        if (regMap.containsKey(quad.getResult())) {
            defRegister = regMap.get(quad.getResult());
        } else {

            defRegister = getTReg();
            regMap.put(quad.getResult(),defRegister);
        }

        instruction.append("move").append(' ').append(defRegister).append(", ").append("$v0");


        // reset the count since we are dealing with arguments within the function
        // Note we start with reg count at 1 since $a0 is for the this
        fRegC = 1;

        //  get the method and add any params to args
        MethodTable callTable = (MethodTable) quad.arg1_entry;
        MethodDecl methodNode = (MethodDecl) callTable.getNode();

        for (int i = 0; i < methodNode.fl.size(); i++) {
            regMap.put(methodNode.fl.elementAt(i).i.s,getAReg());
            if (fRegC == 4) {
                break;
            }
        }


        fRegC = 0;

        return instruction.toString();
    }


    public String handleAssignment(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Grab a register for the LHS
        String var = quad.getResult();
        String reg;
        if (regMap.containsKey(var)) {
             reg = regMap.get(var);

        } else {
             reg = getTReg();
            // Add temporary to the map
            regMap.put(var,reg);
        }


        // If the lhs and rhs are literals then we need to get them and
        // first li to the reg, then add the second one to the reg
        if (quad.arg1Literal() && quad.arg2Literal()) {

            String resRegister= getTReg();
            regMap.put(quad.getResult(),resRegister);

            instruction.append("li").append(" ");
            instruction.append(resRegister).append(", ");
            instruction.append(quad.getArg1()).append("\n");


            instruction.append("addi").append(" ");
            instruction.append(resRegister).append(", ").append(resRegister);
            instruction.append(", ").append(quad.getArg2());
        }

        return instruction.toString();
    }

    public String handleCopy(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        return instruction.toString();
    }
    public String handleReturn(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();


        // Move return value into first return register
        if (quad.isLiteral()) {
            instruction.append("li").append(" ").append("$v0").append(',').append(quad.getResult()).append('\n');
        } else {
            String varLookup = quad.getResult();
            String resRegister = regMap.get(varLookup);

            instruction.append("move").append(' ').append("$v0").append(',').append(" ").append(resRegister).append('\n');
        }

        // Print out the default jr $r
        instruction.append("jr").append(' ').append("$ra").append('\n');

        return instruction.toString();
    }
    public String handleUnaryAssignment(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        return instruction.toString();
    }
    public String handleUncondJump(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        return instruction.toString();
    }
    public String handleCondJump(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        return instruction.toString();
    }
    public String handleNew(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Steps:
        String classString = quad.getArg1();

        // ClassTable classTable = (ClassTable) quad.getNode();

        // ClassDecl classNode = (ClassDecl) classTable.getNode();

        // Using size we can get our allocation count for new
        // int varCount = classNode.vl.size();

        // But right now since we aren't doing classes -- curr time Fri, April 10 7PM
        // we just make a variable and shove zero in it

        String tempVar = quad.getResult();
        String tempReg = getTReg();

        regMap.put(tempVar,tempReg);

        instruction.append("li").append(' ').append(tempReg).append(", ").append("0");

        return instruction.toString();
    }
    public String handleNewArray(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        return instruction.toString();
    }

    public String handleIndexedAssignment(Quadruple quad){
        StringBuilder instruction = new StringBuilder();

        return instruction.toString();
    }

    public String handleIndexedLookup(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        return instruction.toString();

    }
}
