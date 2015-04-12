package CodeGeneration;

import IR.IRClass;
import IR.IRMethod;
import IR.Quadruple;
import SymTable.SymbolTable;

import java.util.ArrayList;


/**
 * Created by adnankhan on 4/9/15.
 */
public class CodeGenerator {
    private static final boolean DEBUG = true;


    ArrayList<IRClass> ir;
    SymbolTable programTable;
    QuadEmit instPrinter;




    public CodeGenerator (   ArrayList<IRClass> classes, SymbolTable symTable) {
      ///  programIR = interMediateRep;
        programTable = symTable;
        ir = classes;
        instPrinter = new QuadEmit();

        if(DEBUG) {
            System.out.println(" -------------- SOURCE INTERMEDIATE REPRESENTATION --------------");



            for (IRClass quadCode : this.ir)  {
                System.out.println(quadCode);
            }
            System.out.println(" -------------- END INTERMEDIATE REPRESENTATION -----------------");
        }
    }


    public String output() {
        StringBuilder fileOut = new StringBuilder();

      fileOut.append(".text\n");
        for (IRClass irClass : ir) {
            for (IRMethod irMethod : irClass.lines) {

                // We need to do some operations for each method,
                // in particular we need to look at the formal list and associate the variables names with the
                // parameter list going in.

                // every time we go over a method the formal list needs to be checked in the following manner

                // start with first index and bind that to argument $a1, remember because we intend to
                // support "newing" things we need to reserve $a0 for the either the object reference or this.

                // if we have exhausted four parameters, then we need to start spilling to the stack, this means saving
                // exisiting argument registers and then restoring them upon the return


                // this way if they are referenced we can know which $a0, $a1, $a2, $a3 to access
                // if there end up being more then we know that arguments were spilled and we must properly retrieve them

                Quadruple labelQuad = new Quadruple(Quadruple.LABEL);
                labelQuad.result = irMethod.getName();
                fileOut.append(labelQuad.getResult()).append(':').append('\n');


                for (Quadruple quad : irMethod.lines) {
                    // Switch based on the type of quadtruple
                    switch (quad.type) {

                        case (Quadruple.ASSIGNMENT):
                            fileOut.append(instPrinter.handleAssignment(quad)).append('\n');
                            break;
                        case (Quadruple.CALL):
                            fileOut.append(instPrinter.handleCall(quad)).append('\n');
                            break;
                        case (Quadruple.CONDITIONAL_JUMP):
                            break;
                        case (Quadruple.COPY):
                            fileOut.append(instPrinter.handleCopy(quad)).append('\n');
                            break;
                        case (Quadruple.INDEXED_ASSIGNMENT):
                            break;
                        case (Quadruple.INDEXED_LOOKUP):
                            break;
                        case (Quadruple.LABEL):
                            fileOut.append(quad.getResult()).append(':').append('\n');
                            break;
                        case (Quadruple.LENGTH_3AC):
                            break;
                        case (Quadruple.NEW_3AC):
                            fileOut.append(instPrinter.handleNew(quad)).append('\n');
                            break;
                        case (Quadruple.NEW_ARRAY):
                            break;
                        case (Quadruple.PARAMETER):
                            fileOut.append(instPrinter.handleParameter(quad)).append('\n');
                            break;
                        case (Quadruple.PRINT):
                            fileOut.append(instPrinter.handlePrint(quad)).append('\n');
                            break;
                        case (Quadruple.RETURN_3AC):
                            fileOut.append(instPrinter.handleReturn(quad)).append('\n');
                            break;
                        case (Quadruple.UNARY_ASSIGNMENT): 
                            break;
                        case (Quadruple.UNCONDITIONAL_JUMP): 
                            fileOut.append(instPrinter.handleUncondJump(quad)).append('\n');
                            break;
                    }
                }

                if (labelQuad.getResult().equals("main"));
                fileOut.append("jal _system_exit\n");

            }

        }


        fileOut.append(getLibrary());


        return fileOut.toString();
    }



    /**
     * Prints the standard library at the end of the program.
     */
    private static String getLibrary() {
        String library = "# main is testing the functions I've provided. You will include this code at the end\n" +
        "# of your output file so that you may call these system services.\n" +
        "\n" +
        "#main:\n" +
        "#\tli $a0, 100\n" +
        "#\tjal _new_array\n" +
        "#\tmove $s0, $v0\n" +
        "#\tmove $a0, $v0\n" +
        "#\tjal _system_out_println\n" +
        "#\tlw $a0, 0($s0)\n" +
        "#\tjal _system_out_println\n" +
        "#\tjal _system_exit\n" +
        "\n" +
        "_system_exit:\n" +
        "\tli $v0, 10 #exit\n" +
        "\tsyscall\n" +
        "\t\n" +
        "# Integer to print is in $a0. \n" +
        "# Kills $v0 and $a0\n" +
        "_system_out_println:\n" +
        "\t# print integer\n" +
        "\tli  $v0, 1 \n" +
        "\tsyscall\n" +
        "\t# print a newline\n" +
        "\tli $a0, 10\n" +
        "\tli $v0, 11\n" +
        "\tsyscall\n" +
        "\tjr $ra\n" +
        "\t\n" +
        "# $a0 = number of bytes to allocate\n" +
        "# $v0 contains address of allocated memory\n" +
        "_new_object:\n" +
        "\t# sbrk\n" +
        "\tli $v0, 9 \n" +
        "\tsyscall\n" +
        "\t\n" +
        "\t#initialize with zeros\n" +
        "\tmove $t0, $a0\n" +
        "\tmove $t1, $v0\n" +
        "_new_object_loop:\n" +
        "\tbeq $t0, $zero, _new_object_exit\n" +
        "\tsb $zero, 0($t1)\n" +
        "\taddi $t1, $t1, 1\n" +
        "\taddi $t0, $t0, -1\n" +
        "\tj _new_object_loop\n" +
        "_new_object_exit:\n" +
        "\tjr $ra\n" +
        "\t\n" +
        "# $a0 = number of bytes to allocate \n" +
        "# $v0 contains address of allocated memory (with offset 0 being the size)\t\n" +
        "_new_array:\n" +
        "\t# add space for the size (1 integer)\n" +
        "\taddi $a0, $a0, 4\n" +
        "\t# sbrk\n" +
        "\tli $v0, 9\n" +
        "\tsyscall\n" +
        "#initialize to zeros\n" +
        "\tmove $t0, $a0\n" +
        "\tmove $t1, $v0\n" +
        "_new_array_loop:\n" +
        "\tbeq $t0, $zero, _new_array_exit\n" +
        "\tsb $zero, 0($t1)\n" +
        "\taddi $t1, $t1, 1\n" +
        "\taddi $t0, $t0, -1\n" +
        "\tj _new_array_loop\n" +
        "_new_array_exit:\n" +
        "\t#store the size (number of ints) in offset 0\n" +
        "\taddi $t0, $a0, -4\n" +
        "\tsra $t0, $t0, 2\n" +
        "\tsw $t0, 0($v0)\n" +
        "\tjr $ra";

       // System.out.println(library);

        return library;
    }
}