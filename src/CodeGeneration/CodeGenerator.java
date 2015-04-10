package CodeGeneration;

import IR.Quadruple;
import SymTable.SymbolTable;

import java.util.List;

/**
 * Created by adnankhan on 4/9/15.
 */
public class CodeGenerator {
    private static final boolean DEBUG = true;

    List<Quadruple> programIR;
    SymbolTable programTable;

    int tRegC;
    int sRegC;
    int fRegC;

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
            tempReg = "$t" + fRegC;
            fRegC++;
        }
        return tempReg;
    }



    public CodeGenerator ( List<Quadruple> interMediateRep, SymbolTable symTable) {
        programIR = interMediateRep;
        programTable = symTable;

        if(DEBUG) {
            System.out.println(" -------------- SOURCE INTERMEDIATE REPRESENTATION --------------");
            for (Quadruple quadCode : this.programIR) {
                System.out.println(quadCode);
            }
            System.out.println(" -------------- END INTERMEDIATE REPRESENTATION -----------------");
        }
    }

    /**
     *
     * @return mips instruction loading the parameter into appropriate variable
     */
    private String handleParameter(Quadruple quad) {
         StringBuilder instruction = new StringBuilder();


        if ( quad.isLiteral() ) {
            instruction.append("addi ");
            instruction.append(getAReg());
            instruction.append(",");
            instruction.append(quad.getResult());
        }
        return instruction.toString();
    }

    public void output() {
        System.out.println(".text");
        for (Quadruple quad : programIR) {

            switch (quad.type) {

                case (Quadruple.ASSIGNMENT): break;
                case (Quadruple.CALL): break;
                case (Quadruple.CONDITIONAL_JUMP): break;
                case (Quadruple.COPY): break;
                case (Quadruple.INDEXED_ASSIGNMENT): break;
                case (Quadruple.INDEXED_LOOKUP): break;
                case (Quadruple.LABEL): break;
                case (Quadruple.LENGTH_3AC): break;
                case (Quadruple.NEW_3AC): break;
                case (Quadruple.NEW_ARRAY): break;
                case (Quadruple.PARAMETER):
                    System.out.println(handleParameter(quad));
                    break;
                case (Quadruple.PRINT):
                    System.out.println("jal _system_out_println");
                    break;
                case (Quadruple.RETURN_3AC): break;
                case (Quadruple.UNARY_ASSIGNMENT): break;
                case (Quadruple.UNCONDITIONAL_JUMP): break;

            }


        }


    }



    /**
     * Prints the standard library at the end of the program.
     */
    private void printLibrary() {

    }
}