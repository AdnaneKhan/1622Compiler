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


    public void output() {
        System.out.println(".text");
        for (Quadruple quad : programIR) {
            if (quad.type == Quadruple.LABEL) {
                System.out.println(quad);
            }

            if (quad.type == Quadruple.PARAMETER) {
                System.out.println( "addi" + getTReg() + quad.result );
            }

            if (quad.type == Quadruple.CALL) {
                System.out.print("jal " + quad.arg1);
            }


        }


    }

    /**
     * Prints the standard library at the end of the program.
     */
    private void printLibrary() {

    }
}