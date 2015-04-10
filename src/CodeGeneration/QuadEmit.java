package CodeGeneration;

import IR.Quadruple;

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
