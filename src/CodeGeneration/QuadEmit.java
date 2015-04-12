package CodeGeneration;

import IR.Quadruple;
import SymTable.ClassTable;
import SymTable.MethodTable;
import SymTable.TableEntry;
import SyntaxTree.ASTNode;
import SyntaxTree.ClassDecl;
import SyntaxTree.Formal;
import SyntaxTree.MethodDecl;

import java.util.HashMap;

/**
 * Created by adnankhan on 4/10/15.
 */
public class QuadEmit {
    private static final int GP_REG_COUNT = 22;
    int tRegC;
    int sRegC;
    int fRegC;

    // a very simple register map that works
    // its purposes prior to true regisster allocation
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

    // This function prints code to naively save
    // all registers
    private String printSaveAll() {
        StringBuilder instruction = new StringBuilder();
        int regCount = GP_REG_COUNT;
        // get the original position of the heap
        instruction.append("addi $sp, $sp,").append(regCount * -4).append('\n');

        // First grow the heap to accommodate all the new instructions
        int offSet = GP_REG_COUNT-1;
        // Save all temporaries
        for (int i = 0; i < 10; i++) {
            instruction.append("sw").append(' ').append("$t").append(i).append(',').append(' ').append(offSet*4);
            // note here we use the return value straight from the original syscall we did because
            // nothing changed in it
            instruction.append("($v0)").append('\n');

            offSet--;
        }

        // Save all s registers

        for (int i =0; i < 8; i++) {
            instruction.append("sw").append(' ').append("$s").append(i).append(',').append(' ').append(offSet*4);
            // note here we use the return value straight from the original syscall we did because
            // nothing changed in it
            instruction.append("($sp)").append('\n');

            offSet--;
        }

        // save everything else

        instruction.append("sw").append(' ').append("$at").append(',').append(' ').append(offSet*4);
        instruction.append("($sp)").append('\n');
        offSet--;

        instruction.append("sw").append(' ').append("$k0").append(',').append(' ').append(offSet*4);
        instruction.append("($sp)").append('\n');
        offSet--;

        instruction.append("sw").append(' ').append("$k1").append(',').append(' ').append(offSet*4);
        instruction.append("($sp)").append('\n');
        offSet--;

        instruction.append("sw").append(' ').append("$ra").append(',').append(' ').append(offSet*4);
        instruction.append("($sp)").append('\n');
        offSet--;

        return instruction.toString();
    }


    private String printRestoreAll() {
        StringBuilder instruction = new StringBuilder();
        int regCount = GP_REG_COUNT;
        int startPoint = 0;

        instruction.append("lw").append(' ').append("$ra").append(',').append(' ').append(startPoint*4);
        instruction.append("($sp)").append('\n');
        startPoint++;

        instruction.append("lw").append(' ').append("$k1").append(',').append(' ').append(startPoint*4);
        instruction.append("($sp)").append('\n');
        startPoint++;

        instruction.append("lw").append(' ').append("$k0").append(',').append(' ').append(startPoint*4);
        instruction.append("($sp)").append('\n');
        startPoint++;

        instruction.append("lw").append(' ').append("$at").append(',').append(' ').append(startPoint*4);
        instruction.append("($sp)").append('\n');
        startPoint++;

        for (int i = 0; i < 8; i ++) {
            instruction.append("lw").append(' ').append("$s").append(i).append(',').append(' ').append(startPoint*4);
            // note here we use the return value straight from the original syscall we did because
            // nothing changed in it
            instruction.append("($sp)").append('\n');

            startPoint++;
        }

        // Save all temporaries
        for (int i = 0; i < 10; i++) {
            instruction.append("lw").append(' ').append("$t").append(i).append(',').append(' ').append(startPoint*4);
            // note here we use the return value straight from the original syscall we did because
            // nothing changed in it
            instruction.append("($sp)").append('\n');

            startPoint++;
        }

        // Shrink the stack by passing a negative offffset
        instruction.append("addi $sp, $sp, ").append(regCount * 4).append('\n');

        return instruction.toString();
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
        }
        else if ( quad.isBoolean() ) {
            instruction.append("li ");
            instruction.append(getAReg());
            instruction.append(", ");
            if (quad.getResult().equals("$TRUE")) {
                instruction.append("1");
            } else {
                instruction.append("0");
            }
        }
        else {
            String paramVar;
            if (!quad.getResult().equals("this")) {
                 paramVar = regMap.get(quad.getResult());
            } else {
                // if its this then we know we are in a class and the first argument will be the class location
               paramVar = "$a0";
            }

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
        fRegC = 0;

        return instruction.toString();
    }

    public String handleCall(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Add reference to method for calls

        // Get class name
        ///String className = quad.arg1_entry.parent.getSymbolName();

        instruction.append(printSaveAll());
        instruction.append("jal").append(' ').append(quad.getArg1()).append("\n");
        instruction.append(printRestoreAll());
        printRestoreAll();
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
        } else if (!quad.arg2Literal() && !quad.isTempArg1() && !quad.isTempArg2() && !quad.arg1Literal()) {
                if (quad.arg1_entry.parent.isEntry(TableEntry.METHOD_ENTRY) && quad.arg2_entry.parent.isEntry(TableEntry.METHOD_ENTRY)) {
                    MethodDecl arg1Table = (MethodDecl) quad.arg1_entry.parent.getNode();

                    int arg1Pos = -1;
                    int arg2Pos = -1;
                    int i;

                    for ( i = 0; i < arg1Table.fl.size(); i++) {
                        if ( arg1Table.fl.elementAt(i).i.s.equals(quad.arg1)) {
                            arg1Pos = i + 1;
                        }

                        if ( arg1Table.fl.elementAt(i).i.s.equals(quad.arg2)) {
                            arg2Pos = i + 1;
                        }
                    }

                    instruction.append(generateOpInst(quad.op, quad.getResult(),"$a" +arg1Pos,"$a" +arg2Pos));

                } else if (quad.arg1_entry.parent.isEntry(TableEntry.CLASS_ENTRY)) {
                    // TODO
                    System.err.println(" We have class scoope variable we tried to reference!");
                }
        } else if (quad.isTempArg1() &&  quad.arg2_entry != null) {

            String arg1Reg = regMap.get(quad.getArg1());

            if (quad.arg2_entry.parent.isEntry(TableEntry.METHOD_ENTRY)) {
                MethodDecl arg2Table = (MethodDecl) quad.arg2_entry.parent.getNode();

                int arg2Pos = -1;
                int i;

                for ( i = 0; i < arg2Table.fl.size(); i++) {
                    if ( arg2Table.fl.elementAt(i).i.s.equals(quad.getArg2())) {
                        arg2Pos = i + 1;
                    }
                }

                instruction.append(generateOpInst(quad.op, quad.getResult(), arg1Reg, "$a" + arg2Pos));

            } else {
                // TODO
                System.err.println(" We have class scoope variable we tried to reference!");
            }

        } else if (quad.isTempArg2() && quad.arg1_entry != null) {
            String arg2Reg = regMap.get(quad.getArg2());

            if (quad.arg1_entry.parent.isEntry(TableEntry.METHOD_ENTRY)) {
                MethodDecl arg1Table = (MethodDecl) quad.arg1_entry.parent.getNode();

                int arg1Pos = -1;
                int i;

                for ( i = 0; i < arg1Table.fl.size(); i++) {
                    if ( arg1Table.fl.elementAt(i).i.s.equals(quad.arg1)) {
                        arg1Pos = i + 1;
                    }
                }

                instruction.append(generateOpInst(quad.op, quad.getResult(),arg2Reg, "$a"+arg1Pos));

            } else {
                // TODO
                System.err.println(" We have class scoope variable we tried to reference!");
            }
        }

        return instruction.toString();
    }

    private String generateOpInst(String op, String res, String reg1, String reg2) {
        StringBuilder instruction = new StringBuilder();
        String opCode = "";

        if (op.equals("+")) {
            opCode = "add";
        } else if (op.equals("-")) {
            opCode = "sub";
        } else if (op.equals("&&")) {
            opCode = "and";
        } else if (op.equals("<")) {
            opCode = "slt";
        }

        if (op.equals("*")) {
            opCode = "mult";
        } else{
            String tempReg;
            if (regMap.containsKey(res)) {
                tempReg = regMap.get(res);
            } else {
                tempReg = getTReg();
                regMap.put(res,tempReg);
            }

            instruction.append(opCode).append(' ').append(tempReg).append(", ").append(reg1).append(',').append(reg2);
        }

        return instruction.toString();
    }

    public String handleCopy(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // For copy we need to figure out which registers each respective value was in


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
         ClassTable classTable = (ClassTable) quad.arg1_entry;

         ClassDecl classNode = (ClassDecl) classTable.getNode();

        // Using size we can get our allocation count for new
        int varCount = classNode.vl.size();

        // Now we have to make the object itself

        // Load the size into the argument zero register
        // yes, our max class size is the size of 16 bit immediate, god help us if that is tested...
        instruction.append("li").append(' ').append("$a0").append(',').append( varCount * 4 ).append('\n');
        // now we call the object maker
        instruction.append("jal").append(" _new_object").append('\n');


        // But right now since we aren't doing classes -- curr time Fri, April 10 7PM
        // we just make a variable and shove zero in it

        String tempVar = quad.getResult();
        String tempReg = getTReg();

        regMap.put(tempVar,tempReg);
        // Now we move the address into the temporary, this is our new object address.
        instruction.append("move").append(' ').append(tempReg).append(", ").append("$v0");

        return instruction.toString();
    }
    public String handleNewArray(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // This will function in a manner that is similar to the new object instantiation

        // Get the size of the array from the node in the quadruple (may have to augment in the IR visitor)

        // then set up the instructions to call the new array function

        // and then we take the valueu that came fromm $v0 and assign it into whichever register is holding the array
        // pointer


        return instruction.toString();
    }

    public String handleIndexedAssignment(Quadruple quad){
        StringBuilder instruction = new StringBuilder();

        // In indexed assignment since we are saving a value to an index of an array what we have to do
        // is use the register that is holding the value and pull the pointer out of it
        /// if the value has been spilled to the stack (we need to use soem sort of spill table to hold variables
        // that have had their values spilled to the stack ex: the combination of a varaible and the register it was
        // last in should return either that register or will allow us to do the appropriate memory reference to get that
        // value



        return instruction.toString();
    }

    public String handleIndexedLookup(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // very simiilar to indexed assignment in how we have to get the pointer ,but then we make a lookup from mmeory

        // and save it into its destination register

        return instruction.toString();

    }
}
