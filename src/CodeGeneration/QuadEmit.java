package CodeGeneration;

import IR.Quadruple;
import SymTable.ClassTable;
import SymTable.MethodTable;
import SymTable.SymbolEntry;
import SymTable.TableEntry;
import SyntaxTree.ClassDecl;
import SyntaxTree.Formal;
import SyntaxTree.MainClass;
import SyntaxTree.MethodDecl;

import java.awt.geom.Rectangle2D;
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

    int fRegC;

    /**
     * @return mips instruction loading the parameter into appropriate variable
     */
    public String handleParameter(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        if (quad.printParam) {
            instruction.append(saveVandA());
        }else if (functParam ) {
            instruction.append(printSaveAll());
            functParam = false;
        }

        if (quad.isLiteral()) {
            instruction.append("li ").append(getAReg()).append(", ").append(quad.getResult());
        } else if (quad.isBoolean()) {
            instruction.append("li ").append(getAReg()).append(", ");
            if (quad.getResult().equals("$TRUE")) {
                instruction.append("1");
            } else {
                instruction.append("0");
            }
        } else if (quad.isResultMethodVar()) {

            instruction.append("move ").append(getAReg()).append(COMMA_SPACE).append(prettyRegister(quad.getResRegister()));
        } else if (quad.isResultClassVar()) {
            instruction.append("move ").append(getAReg()).append(COMMA_SPACE).append(prettyRegister(quad.getResRegister()));
        }

        return instruction.toString();
    }

    public String handlePrint(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        instruction.append("jal _system_out_println").append('\n');
        instruction.append(loadVandA());

        fRegC = 0;

        return instruction.toString();
    }

    public String handleCall(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();


        instruction.append("jal").append(' ').append(quad.getArg1()).append("\n");
        functParam = true;
        instruction.append(printRestoreAll());


        if (quad.isResultClassVar() && !(quad.getNode().parent.getNode() instanceof MainClass)) {
            int offSet = ( (ClassTable) quad.getNode().parent).getVariableOffset(((SymbolEntry)quad.getNode()));
            instruction.append(putVarIntoClass(offSet,Registers.V0));
        } else {
            instruction.append("move").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append("$v0");
        }


        fRegC = 0;

        return instruction.toString();
    }

    /**
     * This method handles assignments into method scope variables that are held in registers (or perhaps spilled onto the stack if needed)
     * @param quad
     * @return
     */
    public String handleAssignment(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        String arg1Reg = null;
        String arg2Reg = null;

        if (quad.isArg1MethodVar()) {
            arg1Reg = prettyRegister(quad.getArg1Register());
        } else if (quad.isArg1ClassVar()) {

           int offSet = ( (ClassTable) quad.arg1_entry.parent).getVariableOffset(((SymbolEntry)quad.arg1_entry));
            instruction.append(extractVarFromClass(offSet, Registers.K1));
            arg1Reg = prettyRegister(Registers.K1);
        }

        if (quad.isArg2MethodVar()) {
            arg2Reg = prettyRegister(quad.getArg2Register());
        } else if(quad.isArg2ClassVar()) {
            int offSet = ( (ClassTable) quad.arg2_entry.parent).getVariableOffset(((SymbolEntry)quad.arg2_entry));

            instruction.append(extractVarFromClass(offSet, Registers.K0));
            arg2Reg =prettyRegister(Registers.K0);
        }

        // LITERAL LITERAL
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

            if (quad.isResultMethodVar()) {
                quad.dRes();
                instruction.append("li").append(" ").append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(Integer.toString(result));
            } else {

                int offSet = ( (ClassTable) quad.getNode().parent).getVariableOffset(((SymbolEntry)quad.getNode()));
                instruction.append("li").append(" ").append(prettyRegister(Registers.GP)).append(COMMA_SPACE).append(Integer.toString(result));
                instruction.append(putVarIntoClass(offSet, Registers.GP));
            }

        } else {
            if (quad.arg2Literal()) {

                setImmediateRegister(Registers.GP  , quad.getArg2(), instruction);
                arg2Reg = prettyRegister(Registers.GP);

            }
            else if (quad.arg1Literal()) {

                setImmediateRegister(Registers.GP, quad.getArg1(), instruction);
                arg1Reg = prettyRegister(Registers.GP);

            }

            if (quad.isResultClassVar()) {
                int offSet = ( (ClassTable) quad.getNode().parent).getVariableOffset(((SymbolEntry)quad.getNode()));
                instruction.append(generateOpInst(quad.op, prettyRegister(Registers.GP), arg1Reg, arg2Reg));
                instruction.append(putVarIntoClass(offSet, Registers.GP));
            } else {
                quad.dRes();
                String resReg = prettyRegister(quad.getResRegister());
                instruction.append(generateOpInst(quad.op, resReg, arg1Reg, arg2Reg));

            }
        }

        return instruction.toString();
    }

    /**
     *
     * @param quad
     * @return
     */
    public String handleCopy(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        if (quad.arg1Literal() && quad.isResultMethodVar()) {
            quad.dRes();
            instruction.append("li").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append(quad.getArg1());
            /**
             *
             */
        } else if (quad.arg1Literal() && quad.isResultClassVar()) {
            int offSet = ( (ClassTable) quad.getNode().parent ).getVariableOffset(((SymbolEntry)quad.getNode()));
            instruction.append("li").append(" ").append(prettyRegister(Registers.GP)).append(COMMA_SPACE).append(Integer.toString(quad.arg1Int)).append('\n');
            instruction.append(putVarIntoClass(offSet, Registers.GP));

        } else if (quad.isArg1MethodVar() && quad.isResultMethodVar()) {
            quad.dRes();
            // Check the symbol entries if they are both move related, at this point any pairs of entries thaat are move related are
            // properly set
            if (!((SymbolEntry) quad.arg1_entry).isPreColor() && (((SymbolEntry) quad.arg1_entry).getLinked().dominated && (quad.getNode()).equals(((SymbolEntry) quad.arg1_entry).coalesceBridge)) ||
                    (((SymbolEntry) quad.getNode()).getLinked().dominated && (((SymbolEntry) quad.arg1_entry)).equals(((SymbolEntry) quad.getNode()).coalesceBridge))) {
                instruction.append("# ELIMINATED VIA COALESCING");
            }

            String rhsReg = prettyRegister(quad.getArg1Register());
            instruction.append("move").append(" ").append(prettyRegister(quad.getResRegister())).
                    append(COMMA_SPACE).append(rhsReg);

            /**
             *
             */
        } else if (quad.isArg1ClassVar() && quad.isResultMethodVar()) {
            quad.dRes();
            int variableOffset = ((ClassTable) quad.arg1_entry.parent).getVariableOffset((SymbolEntry) quad.getNode());
            instruction.append(extractVarFromClass(variableOffset,quad.getResRegister()));

            /**
             *
             */
        } else if (quad.isArg1MethodVar() && quad.isResultClassVar()) {
            int variableOffset = ((ClassTable) quad.getNode().parent).getVariableOffset((SymbolEntry) quad.getNode());
            instruction.append(putVarIntoClass(variableOffset, quad.getArg1Register()));

        }

        return instruction.toString();
    }

    public String handleReturn(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        /**
         *
         */
        if (quad.isResultMethodVar()) {
            String resRegister = prettyRegister(quad.getResRegister());


            instruction.append("move").append(' ').append("$v0").append(COMMA_SPACE).append(resRegister).append('\n');
            /**
             *
             */
        } else if (quad.isResultClassVar()) {
            int variableOffset = ((ClassTable) quad.getNode().parent).getVariableOffset((SymbolEntry) quad.getNode());
            instruction.append(extractVarFromClass(variableOffset, Registers.V0));

            /**
             *
             */
        } else if (quad.isLiteral()) {
            instruction.append("li").append(" ").append("$v0").append(COMMA_SPACE).append(quad.getResult()).append('\n');
        }

        // Print out the default jr $r
        instruction.append("jr").append(' ').append("$ra").append('\n');

        return instruction.toString();
    }

    public String handleUnaryAssignment(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();
        String resRegister = prettyRegister(quad.getResRegister());

        /**
         *  Enumerate the case where arg1 is a literal and the result is in method scope
         */
        if (quad.arg1Literal() && quad.isResultMethodVar()) {
            int res = 0;
            if (0 == quad.arg1Int) {
                res = 1;
            }
            quad.dRes();
            instruction.append("li").append(' ').append(resRegister).append(COMMA_SPACE).append(res).append('\n');
            instruction.append("slti").append(' ').append(resRegister).append(COMMA_SPACE).
                    append(resRegister).append(COMMA_SPACE).append("1");
            /**
             * Enumerate the case whre teh arg is method scopes and the result is also method scope
             */
        } else if (quad.isArg1MethodVar() && quad.isResultMethodVar()) {
            quad.dRes();

            instruction.append("slti").append(' ').append(resRegister).append(COMMA_SPACE).
                    append(prettyRegister(quad.getArg1Register())).append(COMMA_SPACE).append("1");

            /**
             * Enumerate the case were the argument is lcass scope and the result is method scope
             */
        } else if (quad.isArg1ClassVar() && quad.isResultMethodVar()) {
            // The rhs from class and lhs is a method scope
            quad.dRes();

            int variableOffset = ((ClassTable) quad.arg1_entry.parent).getVariableOffset((SymbolEntry) quad.arg1_entry);
            instruction.append(extractVarFromClass(variableOffset, quad.getResRegister()));
            instruction.append("slti").append(' ').append(resRegister).append(COMMA_SPACE).append(resRegister).append(COMMA_SPACE).append("1");

            // TODO
            //  System.err.println(" We have class scope variable we tried to reference!");
            /**
             * Enumerate the case where the argument is method scope and result is class scope
             */
        } else if (quad.isArg1MethodVar() && quad.isResultClassVar()) {
            int variableOffset = ((ClassTable) quad.getNode().parent).getVariableOffset((SymbolEntry) quad.getNode());
            instruction.append(putVarIntoClass(variableOffset, quad.getArg1Register()));
            instruction.append("slti").append(' ').append(resRegister).append(COMMA_SPACE).append(resRegister).append(COMMA_SPACE).append("1");

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
        String resRegister;
        if (quad.isArg1MethodVar()) {
           resRegister = prettyRegister(quad.getArg1Register());

        } else  {
            int offSet =  ((ClassTable) quad.arg1_entry.parent).getVariableOffset((SymbolEntry) quad.arg1_entry);
            instruction.append(extractVarFromClass(offSet,Registers.GP)).append('\n');
            resRegister = prettyRegister(Registers.GP);
        }
        instruction.append("beq ").append(resRegister).append(COMMA_SPACE).append("$zero").append(COMMA_SPACE).append(quad.getArg2());
        return instruction.toString();
    }

    public String handleNew(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Steps:
        ClassTable classTable = (ClassTable) quad.arg1_entry;

        // get size of the class
        int varCount = classTable.trueSize();

        // Load the size into the argument zero register
        // yes, our max class size is the size of 16 bit immediate, god help us if that is tested...
        instruction.append(saveVandA());
        instruction.append("li").append(' ').append("$a0").append(',').append(varCount * 4).append('\n');
        // now we call the object maker
        instruction.append("jal").append(" _new_object").append('\n');
        instruction.append(loadVandA() );


        this.functParam = true;


        if (quad.isResultMethodVar()) {
            quad.dRes();

            instruction.append("move").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).append("$v0");
        } else {
            quad.dRes();
            int offSet = ( (ClassTable) quad.getNode().parent).getVariableOffset(((SymbolEntry)quad.getNode()));
            instruction.append(putVarIntoClass(offSet,Registers.V0));
        }

        return instruction.toString();
    }

    public String handleNewArray(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        instruction.append(saveVandA());
        if (quad.arg1_entry != null) {
            instruction.append("move").append(' ').append("$a0").append(COMMA_SPACE).
                    append(prettyRegister(quad.getArg1Register())).append('\n');
            instruction.append("sll").append(' ').append("$a0").append(COMMA_SPACE).append("$a0").append(COMMA_SPACE).append(2).append('\n');

        } else {
            int varCount = quad.arg1Int;
            instruction.append("li").append(' ').append("$a0").append(',').append(varCount * 4).append('\n');
        }

        instruction.append("jal").append(" _new_array").append("\n");
        instruction.append(loadVandA());
        this.functParam = true;

        String resReg = prettyRegister(quad.getResRegister());
        quad.dRes();

        if (quad.isResultMethodVar()) {
            instruction.append("move").append(' ').append(resReg).append(COMMA_SPACE).append("$v0");
        } else {
            int offSet = ( (ClassTable) quad.getNode().parent).getVariableOffset(((SymbolEntry)quad.getNode()));
            instruction.append(putVarIntoClass(offSet, Registers.V0));
        }


        return instruction.toString();
    }

    public String handleIndexedAssignment(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Move the offset into a register
        if (quad.isArg2MethodVar()) {
            instruction.append("move").append(' ').append(prettyRegister(Registers.GP)).append(COMMA_SPACE).
                    append(prettyRegister(quad.getArg2Register())).append('\n');

        } else if( quad.isArg2ClassVar()) {
            int offSet = ( (ClassTable) quad.arg2_entry.parent).getVariableOffset(((SymbolEntry)quad.arg2_entry));
            instruction.append(extractVarFromClass(offSet, Registers.GP));
        }

        // get address of the ARRAY and put it into K0
        if (quad.isResultClassVar()) {
            int offSet = ( (ClassTable) quad.getNode().parent).getVariableOffset(((SymbolEntry) quad.getNode()));
            instruction.append( extractVarFromClass(offSet, Registers.K0));
        } else if (quad.isResultMethodVar()) {
            instruction.append("move").append(' ').append(prettyRegister(Registers.K0)).append(COMMA_SPACE).
                    append(prettyRegister(quad.getResRegister())).append('\n');
        }

        // Deal with the value being assigned
        if (quad.isArg1MethodVar()) {
            instruction.append("move").append(' ').append(prettyRegister(Registers.K1)).append(COMMA_SPACE).
                    append(prettyRegister(quad.getArg1Register())).append('\n');

        } else if (quad.arg1Literal()) {
            String assignLit = quad.getArg1();
            instruction.append("li").append(' ').append(prettyRegister(Registers.K1)).append(',').append(assignLit).append('\n');
        } else if (quad.isArg1ClassVar()) {
            int offSet = ( (ClassTable) quad.arg1_entry.parent).getVariableOffset(((SymbolEntry)quad.arg1_entry));
            instruction.append(extractVarFromClass(offSet ,Registers.K1 ));
        }


        if (!quad.arg2Literal()){
            // mult by 4
            instruction.append("sll").append(' ').append(prettyRegister(Registers.GP)).append(COMMA_SPACE).append(prettyRegister(Registers.GP)).append(COMMA_SPACE).append(2).append('\n');
            /// Add offset adn address
            instruction.append("add").append(' ').append(prettyRegister(Registers.K0)).append(COMMA_SPACE).append(prettyRegister(Registers.K0)).append(COMMA_SPACE).append(prettyRegister(Registers.GP)).append('\n');
            /// LOAD THE WORD <FINALLY>
            quad.dRes();
            // at this point we use the calculated address the word immediate offset as zero to laod into out target, and we are done
            instruction.append("sw").append(' ').append(prettyRegister(Registers.K1)).append(COMMA_SPACE).
                    append('4').append('(').append(prettyRegister(Registers.K0)).append(')').append('\n');


        } else {
            instruction.append("sw").append(' ').append(prettyRegister(Registers.K1)).append(COMMA_SPACE).
                    append((quad.arg2Int * 4) + 4).append('(').append(prettyRegister(Registers.K0)).append(')').append('\n');
        }


        return instruction.toString();
    }

    public String handleIndexedLookup(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();

        // Move the offset into a register
        if (quad.isArg2MethodVar()) {
            instruction.append("move").append(' ').append(prettyRegister(Registers.GP)).append(COMMA_SPACE).
                    append(prettyRegister(quad.getArg2Register())).append('\n');

        } else if( quad.isArg2ClassVar()) {
            int offSet = ( (ClassTable) quad.arg2_entry.parent).getVariableOffset(((SymbolEntry)quad.arg2_entry));
            instruction.append(extractVarFromClass(offSet, Registers.GP));
        }

        // get address and put it into K0
        if (quad.isArg1ClassVar()) {
            int offSet = ( (ClassTable) quad.arg1_entry.parent).getVariableOffset(((SymbolEntry) quad.arg1_entry));
           instruction.append( extractVarFromClass(offSet, Registers.K0));
        } else if (quad.isArg1MethodVar()) {
            instruction.append("move").append(' ').append(prettyRegister(Registers.K0)).append(COMMA_SPACE).
                    append(prettyRegister(quad.getArg1Register())).append('\n');
        }

        if (!quad.arg2Literal()){
            // mult by 4
            instruction.append("sll").append(' ').append(prettyRegister(Registers.GP)).append(COMMA_SPACE).append(prettyRegister(Registers.GP)).append(COMMA_SPACE).append(2).append('\n');
            /// Add offset adn address
            instruction.append("add").append(' ').append(prettyRegister(Registers.K0)).append(COMMA_SPACE).append(prettyRegister(Registers.K0)).append(COMMA_SPACE).append(prettyRegister(Registers.GP)).append('\n');
            /// LOAD THE WORD <FINALLY>
            quad.dRes();
            // at this point we use the calculated address the word immediate offset as zero to laod into out target, and we are done
            instruction.append("lw").append(' ').append(prettyRegister(Registers.GP)).append(COMMA_SPACE).
                    append('4').append('(').append(prettyRegister(Registers.K0)).append(')').append('\n');


        } else {
            instruction.append("lw").append(' ').append(prettyRegister(Registers.GP)).append(COMMA_SPACE).
                    append((quad.arg2Int * 4) + 4).append('(').append(prettyRegister(Registers.K0)).append(')').append('\n');
        }

        /// Move the result to the destination
        if (quad.isResultMethodVar()) {
            // GP will hold the result
            instruction.append("move").append(' ').append(prettyRegister(quad.getResRegister())).append(COMMA_SPACE).
                    append(prettyRegister(Registers.GP)).append('\n');
        } else if (quad.isResultClassVar()) {
            int offSet = ( (ClassTable) quad.getNode().parent).getVariableOffset(((SymbolEntry) quad.getNode()));

            instruction.append(putVarIntoClass(offSet, Registers.GP));
        }

        return instruction.toString();

    }

    public String handleLength(Quadruple quad) {
        StringBuilder instruction = new StringBuilder();


        if (quad.isArg1ClassVar()) {

            int offSet = ( (ClassTable) quad.arg1_entry.parent).getVariableOffset(((SymbolEntry)quad.arg1_entry));
            instruction.append(extractVarFromArray(offSet, Registers.GP, quad.getArg1Register()));
        } else if (quad.isArg1MethodVar()) {
            instruction.append("move").append(prettyRegister(Registers.GP)).append(COMMA_SPACE).append(quad.getArg1Register()).append('\n');
        }


        // Check is the lhs is of the class or the method
        if (quad.isResultClassVar()) {
            int offSet = ( (ClassTable) quad.getNode().parent).getVariableOffset(((SymbolEntry) quad.getNode()));
           // instruction.append(extractVarFromClass(0, Registers.K0, quad.getResRegister()));
            instruction.append(putVarIntoClass(offSet,Registers.K0));
        } else if (quad.isResultMethodVar()) {
            quad.dRes();
          //  instruction.append(extractVarFromClass(0,quad.getResRegister(),Registers.GP));
        }

        return instruction.toString();
    }


    /*
     * ******************************************************************************************
     *   Begin Private Methods
     * ******************************************************************************************
     */

    /**
     * @param classOffset sets up instructionf to laod a variable from a class
     */
    private String extractVarFromClass(int classOffset, int targetRegister) {
        StringBuilder toRet = new StringBuilder();

        toRet.append("lw").append(' ').append(prettyRegister(targetRegister)).append(COMMA_SPACE).append(Integer.toString(classOffset)).append('(').append(prettyRegister(Registers.ARG0)).append(')').append('\n');

        return toRet.toString();
    }


    /**
     * @param classOffset sets up instructionf to laod a variable from a class
     */
    private String extractVarFromArray(int classOffset, int targetRegister,int arrayAddrReg) {
        StringBuilder toRet = new StringBuilder();

        toRet.append("lw").append(' ').append(prettyRegister(targetRegister)).append(COMMA_SPACE).append(Integer.toString(classOffset)).append('(').append(prettyRegister(arrayAddrReg)).append(')').append('\n');

        return toRet.toString();
    }

    /**
     * @param classAdress sets up instructions to save a variable into a class
     */
    private String putVarIntoClass(int classOffset, int sourceRegister) {
        StringBuilder toRet = new StringBuilder();


        toRet.append("sw").append(' ').append(prettyRegister(sourceRegister)).append(COMMA_SPACE).
                append(Integer.toString(classOffset)).append('(').append(prettyRegister(Registers.ARG0)).append(')').append('\n');


        return toRet.toString();
    }

    private String prettyRegister(int uglyRegister) {
        StringBuilder prettyRegister = new StringBuilder();
        // TODO make a method that returns the proper letter given the number
        prettyRegister.append("$");

        if (uglyRegister >= 8 && uglyRegister <= 15) {
            prettyRegister.append("$t").append(uglyRegister - 8);
        } else if (uglyRegister >= 16 && uglyRegister <= 23) {
            prettyRegister.append("$s").append(uglyRegister - 16);
        } else if (uglyRegister == 26){
            prettyRegister.append("$k0");
        } else if (uglyRegister == 27) {
            prettyRegister.append("$k1");
        } else if (uglyRegister == 28) {
            prettyRegister.append("$gp");
        }


        return prettyRegister.toString();
    }

    private String saveVandA() {
        StringBuilder i = new StringBuilder();

        i.append("addi").append(' ').append("$sp").append(COMMA_SPACE).append("$sp").append(COMMA_SPACE).append("-16").append('\n');

        i.append("sw").append(' ').append("$a0").append(COMMA_SPACE).append(12).append("($sp)").append('\n');        // save v
        i.append("sw").append(' ').append("$t1").append(COMMA_SPACE).append(8).append("($sp)").append('\n');
        i.append("sw").append(' ').append("$t0").append(COMMA_SPACE).append(4).append("($sp)").append('\n');
        i.append("sw").append(' ').append("$ra").append(COMMA_SPACE).append(0).append("($sp)").append('\n');
        // save a



        return i.toString();
    }


    private String loadVandA() {
        StringBuilder i = new StringBuilder();


        i.append("lw").append(' ').append("$ra").append(COMMA_SPACE).append(0).append("($sp)").append('\n');
        i.append("lw").append(' ').append("$t0").append(COMMA_SPACE).append(4).append("($sp)").append('\n');
        i.append("lw").append(' ').append("$t1").append(COMMA_SPACE).append(8).append("($sp)").append('\n');
        i.append("lw").append(' ').append("$a0").append(COMMA_SPACE).append(12).append("($sp)").append('\n');
        i.append("addi").append(' ').append("$sp").append(COMMA_SPACE).append("$sp").append(COMMA_SPACE).append("16").append('\n');



        return i.toString();
    }
    private void setImmediateRegister(int resRegister, String arg, StringBuilder instruction) {

        instruction.append("li").append(" ");
        instruction.append(prettyRegister(resRegister)).append(COMMA_SPACE);
        instruction.append(arg).append("\n");
    }

    private String generateOpInst(String op, String res, String reg1, String reg2) {
        StringBuilder instruction = new StringBuilder();
        String opCode = "";

        if (op.equals("+")) {
            opCode = "add";
        } else if (op.equals("-")) {
            opCode = "sub";
        } else if (op.equals("&&")) {
            opCode = "seq";
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
