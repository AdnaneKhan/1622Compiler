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
//
//    /**
//     *  This method takes in a quadruple and
//     *
//     */
//    private void handleQuadruple(QuadR) {
//        if
//    }



    /**
     * Prints the standard library at the end of the program.
     */
    private void printLibrary() {

    }





}