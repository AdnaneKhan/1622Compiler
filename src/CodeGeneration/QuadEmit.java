package CodeGeneration;

import IR.Quadruple;
import SymTable.ClassTable;
import SymTable.MethodTable;
import SymTable.SymbolEntry;
import SymTable.TableEntry;
import SyntaxTree.ClassDecl;
import SyntaxTree.Formal;
import SyntaxTree.MethodDecl;

import java.util.HashMap;

/**
 * Created by adnankhan on 4/10/15.
 */
public class QuadEmit {
    private static final int GP_REG_COUNT = 26;
    private static final String COMMA_SPACE = ", ";
    boolean functParam = true;
    // a very simple register map that works
    // its purposes prior to true regisster allocation
    private HashMap<String, String> regMap;

    int fRegC;


    public QuadEmit() {
        regMap = new HashMap<String, String>();
    }


    /**
     * @return mips instruction loading the parameter into appropriate variable
     */
    public String handleParameter(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();


        if (functParam) {

            instruction.append(printSaveAll());
        }

        functParam = false;

        if (quad.isLiteral()) {
            instruction.append("li ");
            instruction.append(getAReg());
            instruction.append(", ");
            instruction.append(quad.getResult());
        } else if (quad.isBoolean()) {
            instruction.append("li ");
            instruction.append(getAReg());
            instruction.append(", ");
            if (quad.getResult().equals("$TRUE")) {
                instruction.append("1");
            } else {
                instruction.append("0");
            }
        } else {
            String paramVar;

            paramVar = prettyRegister(quad.getResRegister());
            instruction.append("move ");
            instruction.append(getAReg());
            instruction.append(COMMA_SPACE);
            instruction.append(paramVar);
        }



        return instruction.toString();
    }

    public String handlePrint(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        instruction.append("jal _system_out_println");

        functParam = true;
        fRegC = 0;

        return instruction.toString();
    }

    public String handleCall(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Add reference to method for calls
        // Get class nam


        instruction.append("jal").append(' ').append(quad.getArg1()).append("\n");
        functParam = true;
        instruction.append(printRestoreAll());
        // Now for moving the return value

        // Now we need to output instruction to get the value that returns
        // Later when we are doing liveness analysis we need to be able to identify in the method
        // whether there is a return value at all (meaning that there is a def into $v0, otherwise we do
        // not need to pull the return value out

        instruction.append("move").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append("$v0");

        // reset the count since we are dealing with arguments within the function
        // Note we start with reg count at 1 since $a0 is for the this
        fRegC = 1;

        //  get the method and add any params to args
        MethodTable callTable = (MethodTable) quad.arg1_entry;
        MethodDecl methodNode = (MethodDecl) callTable.getNode();


        regMap.put(callTable.getEntry("this", TableEntry.LEAF_ENTRY).getSymbolName(), getAReg());
        for (int i = 0; i < methodNode.fl.size(); i++) {
            regMap.put(callTable.getEntry(methodNode.fl.elementAt(i).i.s, TableEntry.LEAF_ENTRY).getHierarchyName(), getAReg());
            if (fRegC == 4) {
                break;
            }
        }

        fRegC = 0;

        return instruction.toString();
    }

    public String handleAssignment(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();
        // If the lhs and rhs are literals then we need to get them and
        // first li to the reg, then add the second one to the reg
        if (quad.arg1Literal() && quad.arg2Literal()) {

            int result = 0;
            if (quad.op.equals("+")) {
                result = quad.arg1Int + quad.arg2Int;
            } else if (quad.op.equals("-")) {
                result = quad.arg1Int - quad.arg2Int;
            } else if (quad.op.equals("&&")) {
                boolean res = (quad.arg1Int == 1) && (1 == quad.arg2Int);
                if (res) {
                    result = 1;
                }
            } else if (quad.op.equals("<")) {
                boolean res = (quad.arg1Int < quad.arg2Int);
                if (res) {
                    result = 1;
                }
            } else if (quad.op.equals("*")) {
                result = quad.arg1Int * quad.arg2Int;
            }

            instruction.append("li").append(" ").append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(Integer.toString(result));


        } else if (!quad.arg2Literal() && !quad.isTempArg1() && !quad.isTempArg2() && !quad.arg1Literal()) {
            if (quad.isArg1MethodVar() && quad.isArg2MethodVar()) {
                String a1Reg = null;
                String a2Reg = null;
                if (quad.arg1_entry.getNode() instanceof Formal) {
                    int arg1Pos = getArgPos(quad.arg1_entry);
                    a1Reg = "$a" + arg1Pos;
                } else {
                    a1Reg = prettyRegister(quad.getArg1Register());
                }

                if (quad.arg2_entry.getNode() instanceof Formal) {
                    int arg2Pos = getArgPos(quad.arg2_entry);
                    a2Reg = "$a" + arg2Pos;
                } else {
                    a2Reg = prettyRegister(quad.getArg2Register());
                }

                instruction.append(generateOpInst(quad.op, prettyRegister(quad.getResRegister()), a1Reg, a2Reg));

            } else if (quad.isArg1ClassVar()) {
                // TODO

                int variableOffset = ((ClassTable)quad.arg1_entry.parent).getVariableOffset((SymbolEntry) quad.arg1_entry);
                // we need to get the position in the object that this variable resides in
                // this means we need to do the following:


                // Check if the class extends a prent




                System.err.println(" We have class scoope variable we tried to reference!");
            }
        } else if (quad.isTempArg1() && quad.arg2_entry != null) {

            String arg1Reg = regMap.get(quad.getArg1());

            if (quad.isArg2MethodVar()) {

                int arg2Pos = getArgPos(quad.arg2_entry);
                String arg2Reg = argRegStr(quad.getArg2(), arg2Pos);
                instruction.append(generateOpInst(quad.op, prettyRegister(quad.getResRegister()), arg1Reg, arg2Reg));

            } else if (quad.isArg2ClassVar()) {

                int variableOffset = ((ClassTable)quad.arg2_entry.parent).getVariableOffset((SymbolEntry) quad.arg2_entry);

                // TODO
                System.err.println(" We have class scoope variable we tried to reference!");
            }

        } else if (quad.isTempArg2() && quad.arg1_entry != null) {
            String arg2Reg = prettyRegister(quad.getArg2Register());

            if (quad.isArg1MethodVar()) {

                int arg1Pos = getArgPos(quad.arg1_entry);
                String arg1Reg = argRegStr(quad.getArg1(), arg1Pos);
                instruction.append(generateOpInst(quad.op, prettyRegister(quad.getResRegister()), arg1Reg, arg2Reg));

            } else {

                int variableOffset = ((ClassTable)quad.arg1_entry.parent).getVariableOffset((SymbolEntry) quad.arg1_entry);

                // TODO
                System.err.println(" We have class scope variable we tried to reference!");
            }
        } else if (quad.arg1Literal() && quad.arg2_entry != null) {
            if (quad.isArg2MethodVar()) {

                String arg2Reg = prettyRegister(quad.getArg2Register());
                setImmediateRegister(quad.getResRegister(), quad.getArg1(), instruction);
                instruction.append(generateOpInst(quad.op, prettyRegister(quad.getResRegister()), prettyRegister(quad.getResRegister()), arg2Reg));

            } else {


                int variableOffset = ((ClassTable)quad.arg2_entry.parent).getVariableOffset((SymbolEntry) quad.arg2_entry);

                // TODO
                System.err.println(" We have class scope variable we tried to reference!");
            }
        } else if (quad.arg2Literal() && quad.arg1_entry != null) {
            if (quad.isArg1MethodVar()) {


                String arg1Reg = prettyRegister(quad.getArg1Register());
                setImmediateRegister(quad.getResRegister(), quad.getArg2(), instruction);
                instruction.append(generateOpInst(quad.op, prettyRegister(quad.getResRegister()), arg1Reg,  prettyRegister(quad.getResRegister())));

            } else {
                int variableOffset = ((ClassTable)quad.arg1_entry.parent).getVariableOffset((SymbolEntry) quad.arg1_entry);


                // Steps for class allocation:

                // First start with saving necessary registers. For an assignment into a class variable this behav


                // TODO
                System.err.println(" We have class scope variable we tried to reference!");
            }
        }

        return instruction.toString();
    }

    public String handleCopy(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        if (quad.arg1Literal()) {
            instruction.append("li").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(quad.getArg1());
        } else if (quad.isArg1MethodVar()) {

            // Check the symbol entries if they are both move related, at this point any pairs of entries thaat are move related are
            // properly set
            if ( !((SymbolEntry) quad.arg1_entry).isPreColor() && (((SymbolEntry) quad.arg1_entry).getLinked().dominated && (quad.getNode()).equals(((SymbolEntry) quad.arg1_entry).coalesceBridge)) ||
                    (((SymbolEntry) quad.getNode()).getLinked().dominated && (((SymbolEntry)quad.arg1_entry)).equals(((SymbolEntry) quad.getNode()).coalesceBridge)   )) {
                instruction.append("# ELIMINATED VIA COALESCING");
            }

            String rhsReg = prettyRegister(quad.getArg1Register());
            instruction.append("move").append(" ").append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(rhsReg);

        }


        return instruction.toString();
    }

    public String handleReturn(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();


        // Move return value into first return register
        if (quad.isLiteral()) {
            instruction.append("li").append(" ").append("$v0").append(COMMA_SPACE).append(quad.getResult()).append('\n');
        } else if (quad.isResultMethodVar()) {
            String resRegister = prettyRegister(quad.getResRegister());

            instruction.append("move").append(' ').append("$v0").append(COMMA_SPACE).append(resRegister).append('\n');
        } else if (quad.isResultClassVar()) {

        }

        // Print out the default jr $r
        instruction.append("jr").append(' ').append("$ra").append('\n');

        return instruction.toString();
    }

    public String handleUnaryAssignment(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();
         String resRegister = prettyRegister(quad.getResRegister());

        if (quad.arg1Literal()) {
            int res = 0;
            if (0 == quad.arg1Int) {
                res = 1;
            }

            instruction.append("li").append(' ').append(resRegister).append(COMMA_SPACE).append(res).append('\n');
            instruction.append("slti").append(' ').append(resRegister).append(COMMA_SPACE).append(resRegister).append(COMMA_SPACE).append("1");
        } else if (quad.isArg1MethodVar()) {
            int arg1Pos = getArgPos(quad.arg1_entry);
            String arg1Reg = argRegStr(quad.getArg1(), arg1Pos);

            instruction.append("slti").append(' ').append(resRegister).append(COMMA_SPACE).append(arg1Reg).append(COMMA_SPACE).append("1");

        } else if (quad.isArg1ClassVar()) {


            int variableOffset = ((ClassTable)quad.arg1_entry.parent).getVariableOffset((SymbolEntry) quad.arg1_entry);

            // TODO
            System.err.println(" We have class scope variable we tried to reference!");
        }

        return instruction.toString();
    }

    public String handleUncondJump(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();
        instruction.append("j " + quad.getResult());
        return instruction.toString();
    }

    public String handleCondJump(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();
        String resRegister = prettyRegister(quad.getArg1Register());
        instruction.append("beq ").append(resRegister).append(COMMA_SPACE).append("$zero").append(COMMA_SPACE).append(quad.getArg2());
        return instruction.toString();
    }

    public String handleNew(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Steps:
        ClassTable classTable = (ClassTable) quad.arg1_entry;


        // get size of the class
        int varCount = classTable.trueSize();

        // Now we have to make the object itself

        // Load the size into the argument zero register
        // yes, our max class size is the size of 16 bit immediate, god help us if that is tested...
        instruction.append("li").append(' ').append("$a0").append(',').append(varCount * 4).append('\n');
        // now we call the object maker
        instruction.append("jal").append(" _new_object").append('\n');
        this.functParam = true;

        // But right now since we aren't doing classes -- curr time Fri, April 10 7PM
        // we just make a variable and shove zero in it

        String tempReg = prettyRegister(quad.getResRegister());

        // Now we move the address into the temporary, this is our new object address.
        instruction.append("move").append(' ').append(tempReg).append(", ").append("$v0");

        return instruction.toString();
    }

    public String handleNewArray(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();


        instruction.append("addi").append(' ').append("$sp").append(COMMA_SPACE).append("$sp").append(COMMA_SPACE).append("-4");
        // Save $a0 to stack
        instruction.append("sw").append(' ').append("$a0").append(COMMA_SPACE).append(0);
        instruction.append("($sp)").append("\n");

        if (quad.arg1_entry != null) {
                instruction.append("move").append(' ').append("$a0").append(COMMA_SPACE).append(prettyRegister(quad.getArg1Register())).append('\n');
                // Double the values twice to effectively multiply by 4 in place
                for (int i = 0; i < 2; i++) {
                    instruction.append("add").append(' ').append("$a0").append(COMMA_SPACE).append(prettyRegister(quad.getArg1Register())).append(COMMA_SPACE).append(prettyRegister(quad.getArg1Register())).append('\n');
                }

            } else {
                int varCount = quad.arg1Int;
                instruction.append("li").append(' ').append("$a0").append(',').append(varCount * 4).append('\n');
        }



        instruction.append("jal").append("_new_array").append("\n");
        this.functParam = true;


        // restore $a0
        instruction.append("lw").append(' ').append("$a0").append(COMMA_SPACE).append(0);
        instruction.append("($sp)").append("\n");
        instruction.append("addi").append(' ').append("$sp").append(COMMA_SPACE).append("$sp").append(COMMA_SPACE).append("4");

        String resReg = prettyRegister(quad.getResRegister());

        instruction.append("move").append(' ').append(resReg).append(COMMA_SPACE).append("$v0");


        return instruction.toString();
    }

    public String handleIndexedAssignment(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // the problem here is that there are no
        // defs that occur in this assignment,
        // this means that we need to get 1 or 2 temporary registers for this to be possible,
        // we need to add quadruples in the IR that can facilitate this operation so that they may be properly colored


        // Deal with the value being used
        if (quad.arg1_entry != null) {
            instruction.append("move").append(' ').append("$a0").append(COMMA_SPACE).append(prettyRegister(quad.getArg1Register())).append('\n');
            for (int i = 0; i < 2; i++) {
                instruction.append("add").append(' ').append("$a0").append(COMMA_SPACE).append(prettyRegister(quad.getArg1Register())).append(COMMA_SPACE).append(prettyRegister(quad.getArg1Register())).append('\n');
            }
         } else {
            String assignLit = quad.getArg1();
            instruction.append("li").append(' ').append("$a0").append(',').append(assignLit).append('\n');
        }

        // Deal with the offset
        if (quad.arg2_entry != null) {


        } else {

        }

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

        //deal with the offfset
        if (quad.arg2_entry != null) {
            // in this case the offset is a variable

            instruction.append("move").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(prettyRegister(quad.getArg2Register())).append('\n');
            for (int i = 0; i < 2; i++) {
                instruction.append("add").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(prettyRegister(quad.getResRegister())).append('\n');
            }

            // now add the offset and the actual memory address into the res address and save it to result, since we can not have register offffsets for loads
            instruction.append("add").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(prettyRegister(quad.getResRegister())).append(prettyRegister(quad.getArg1Register())).append('\n');

            // at this point we use the calculated address the word immediate offset as zero to laod into out target, and we are done
            instruction.append("lw").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append('0').append('(').append(prettyRegister(quad.getArg1Register())).append(')').append('\n');

        } else {

            /// if we simply have a literal offset and we load it fromm memory
            instruction.append("lw").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(quad.arg2Int).append('(').append(prettyRegister(quad.getArg1Register())).append(')').append('\n');
        }


        return instruction.toString();

    }

    public String handleLength(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();



        return instruction.toString();
    }


    /*
     * ******************************************************************************************
     *   Begin Private Methods
     * ******************************************************************************************
     */

    /**
     *
     * @param classOffset sets up instructionf to laod a variable from a class
     */
    private String extractVarFromClass(int classOffset, int targetRegister, int classAddress) {
        StringBuilder toRet = new StringBuilder();

        // Register saving phase


        toRet.append("addi $sp, $sp,").append(4).append('\n');
        // save $s0
        toRet.append("sw").append(' ').append("$s5").append(COMMA_SPACE).append('0');
        toRet.append("($sp)").append('\n');

        // now we can use $s0 for our purposes


        toRet.append("lw").append(' ').append(prettyRegister(targetRegister)).append(COMMA_SPACE).append(Integer.toString(classOffset)).append('(').append(prettyRegister(classAddress)).append(')').append('\n');




        toRet.append("lw").append(' ').append("$s5").append(COMMA_SPACE).append('0');
        // note here we use the return value straight from the original syscall we did because
        // nothing changed in it
        toRet.append("($sp)").append('\n');
        toRet.append("addi $sp, $sp,").append(4).append('\n');

        // Register loading phase


        return toRet.toString();
    }

    /**
     *
     * @param classOfffset sets up instructions to save a variable into a class
     */
    private String putVarIntoClass(int classOffset, int sourceRegister, int classAdress) {
        StringBuilder toRet = new StringBuilder();

        // Register saving phase


        toRet.append("addi $sp, $sp,").append(4).append('\n');
        // save $s0
        toRet.append("sw").append(' ').append("$s0").append(COMMA_SPACE).append('0');
        toRet.append("($sp)").append('\n');

        // now we can use $s0 for our purposes



        toRet.append("sw").append(' ').append(prettyRegister(sourceRegister)).append(COMMA_SPACE).append(Integer.toString(classOffset)).append('(').append(prettyRegister(classAdress)).append(')').append('\n');




        toRet.append("lw").append(' ').append("$s0").append(COMMA_SPACE).append('0');
        // note here we use the return value straight from the original syscall we did because
        // nothing changed in it
        toRet.append("($sp)").append('\n');
        toRet.append("addi $sp, $sp,").append(4).append('\n');

        // Register loading phase


        return toRet.toString();
    }

    private String prettyRegister(int uglyRegister) {
        StringBuilder prettyRegister = new StringBuilder();
        // TODO make a method that returns the proper letter given the number
        prettyRegister.append("$").append(uglyRegister);

        return prettyRegister.toString();
    }


    private void setImmediateRegister(int resRegister, String arg, StringBuilder instruction) {

        instruction.append("li").append(" ");
        instruction.append(prettyRegister(resRegister)).append(COMMA_SPACE);
        instruction.append(arg).append("\n");
    }

    private int getArgPos(TableEntry quad_entry) {
        MethodDecl arg1Table = (MethodDecl) quad_entry.parent.getNode();

        int argPos = -1;
        int i;

        for (i = 0; i < arg1Table.fl.size(); i++) {
            if (arg1Table.fl.elementAt(i).i.s.equals(quad_entry.getSymbolName())) {
                argPos = i + 1;
            }
        }
        return argPos;
    }

    private String argRegStr(String arg, int argPos) {
        String argReg;
        if (argPos >= 0) {
            argReg = "$a" + argPos;
        } else {
            argReg = regMap.get(arg);
        }
        return argReg;
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

            instruction.append(opCode).append(' ').append(reg1).append(COMMA_SPACE).append(reg2).append('\n');
            instruction.append("mflo").append(' ').append(res);
        } else {
            instruction.append(opCode).append(' ').append(res).append(COMMA_SPACE).append(reg1).append(COMMA_SPACE).append(reg2);
        }

        return instruction.toString();
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
        int offSet = GP_REG_COUNT - 1;
        // Save all temporaries
        for (int i = 0; i < 10; i++) {
            instruction.append("sw").append(' ').append("$t").append(i).append(COMMA_SPACE).append(offSet * 4);
            // note here we use the return value straight from the original syscall we did because
            // nothing changed in it
            instruction.append("($sp)").append('\n');

            offSet--;
        }

        // Save all s registers

        for (int i = 0; i < 8; i++) {
            instruction.append("sw").append(' ').append("$s").append(i).append(COMMA_SPACE).append(offSet * 4);
            // note here we use the return value straight from the original syscall we did because
            // nothing changed in it
            instruction.append("($sp)").append('\n');

            offSet--;
        }


        for (int i = 0; i < 4; i++) {
            instruction.append("sw").append(' ').append("$a").append(i).append(COMMA_SPACE).append(offSet * 4);
            instruction.append("($sp)").append("\n");
            
            offSet--;
        }

        // save everything else
        instruction.append("sw").append(' ').append("$at").append(COMMA_SPACE).append(offSet * 4);
        instruction.append("($sp)").append('\n');
        offSet--;

        instruction.append("sw").append(' ').append("$k0").append(COMMA_SPACE).append(offSet * 4);
        instruction.append("($sp)").append('\n');
        offSet--;

        instruction.append("sw").append(' ').append("$k1").append(COMMA_SPACE).append(offSet * 4);
        instruction.append("($sp)").append('\n');
        offSet--;

        instruction.append("sw").append(' ').append("$ra").append(COMMA_SPACE).append(offSet * 4);
        instruction.append("($sp)").append('\n');
        offSet--;

        return instruction.toString();
    }

    private String printRestoreAll() {
        StringBuilder instruction = new StringBuilder();
        int regCount = GP_REG_COUNT;
        int startPoint = 0;

        instruction.append("lw").append(' ').append("$ra").append(COMMA_SPACE).append(startPoint * 4);
        instruction.append("($sp)").append('\n');
        startPoint++;

        instruction.append("lw").append(' ').append("$k1").append(COMMA_SPACE).append(startPoint * 4);
        instruction.append("($sp)").append('\n');
        startPoint++;

        instruction.append("lw").append(' ').append("$k0").append(COMMA_SPACE).append(startPoint * 4);
        instruction.append("($sp)").append('\n');
        startPoint++;

        instruction.append("lw").append(' ').append("$at").append(COMMA_SPACE).append(startPoint * 4);
        instruction.append("($sp)").append('\n');
        startPoint++;
        
        
        for (int i = 3; i >= 0; i--) {
            instruction.append("lw").append(' ').append("$a").append(i).append(COMMA_SPACE).append(startPoint * 4);
            instruction.append("($sp)").append("\n");
            startPoint++;
        }

        for (int i = 7; i >= 0; i--) {
            instruction.append("lw").append(' ').append("$s").append(i).append(COMMA_SPACE).append(startPoint * 4);
            // note here we use the return value straight from the original syscall we did because
            // nothing changed in it
            instruction.append("($sp)").append('\n');

            startPoint++;
        }

        // Save all temporaries
        for (int i = 9; i >= 0; i--) {
            instruction.append("lw").append(' ').append("$t").append(i).append(COMMA_SPACE).append(startPoint * 4);
            // note here we use the return value straight from the original syscall we did because
            // nothing changed in it
            instruction.append("($sp)").append('\n');

            startPoint++;
        }

        // Shrink the stack by passing a negative offset
        instruction.append("addi $sp, $sp, ").append(regCount * 4).append('\n');

        return instruction.toString();
    }

}
