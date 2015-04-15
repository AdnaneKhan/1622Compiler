package CodeGeneration;

import IR.IRClass;
import IR.IRMethod;
import IR.Quadruple;
import SymTable.SymbolEntry;
import SymTable.SymbolTable;
import SymTable.TableEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;


/**
 * Created by adnankhan on 4/9/15.
 */
public class CodeGenerator {
    private static final boolean DEBUG = true;
    ArrayList<Row> useAndDefs = new ArrayList<Row>();
    private ArrayList<ControlFlowNode> baseNodes = new ArrayList<ControlFlowNode>();
    private HashMap<Quadruple, ControlFlowNode> cfgMap;
    private HashMap<String, ControlFlowNode> labelMap = new HashMap<String, ControlFlowNode>();
    ArrayList<IRClass> ir;
    SymbolTable programTable;
    QuadEmit instPrinter;
    Quadruple mainLast;


    public ArrayList<Row> generateLiveness() {
        int iteration = 0;
        boolean change;
        boolean clear = true;
        ArrayList<ArrayList<Row>> inOut = new ArrayList<ArrayList<Row>>();

        do {

            ArrayList<Row> cycle = new ArrayList<Row>();
            inOut.add(cycle);
            change = false;

            // pre instantiate the array of rows to prevent null pointers
            for (int i = 0; i < baseNodes.size(); i++) {
                Row temp = new Row();
                cycle.add(temp);
            }

            // Uses - in, defs - out
            for (int i = 0; i < baseNodes.size(); i++) {

                int oldInSize = 0;
                int oldOutSize = 0;

                if (iteration > 0) {
                    oldInSize = inOut.get(iteration - 1).get(i).uses.size();
                    oldOutSize = inOut.get(iteration - 1).get(i).defs.size();


                    for (SymbolEntry se : inOut.get(iteration - 1).get(i).uses) {
                        cycle.get(i).uses.add(se);
                    }

                    for (SymbolEntry se : inOut.get(iteration - 1).get(i).defs) {
                        cycle.get(i).defs.add(se);
                    }

                } else {

                    // Copy uses into in
                    for (SymbolEntry se : useAndDefs.get(i).uses) {
                        cycle.get(i).uses.add(se);
                    }
                }

                for (SymbolEntry outEntry : cycle.get(i).defs) {
                    if (!useAndDefs.get(i).defs.contains(outEntry)) {
                        cycle.get(i).uses.add(outEntry);
                    }
                }

                // Union Step in Algo
                for (ControlFlowNode predecessor : baseNodes.get(i).predecessors) {
                    int predIndex = predecessor.index;


                    for (SymbolEntry inValue : cycle.get(predIndex).uses) {

                        cycle.get(i).defs.add(inValue);
                    }
                }


                if (clear && oldInSize != inOut.get(iteration).get(i).uses.size() ||
                        oldOutSize != inOut.get(iteration).get(i).defs.size()) {
                    change = true;
                    clear = false;
                }
            }

            iteration++;
        } while (change);


        return inOut.get(iteration - 1);
    }


    public void generateDefUse() {
        Row newRow;

        for (ControlFlowNode cf : baseNodes) {
            newRow = new Row();
            if (cf.irLine.type == Quadruple.PARAMETER || cf.irLine.type == Quadruple.RETURN_3AC) {

                // Placing in uses due to exxceptiions
                newRow.uses.add((SymbolEntry) cf.irLine.getNode());
            } else {
                newRow.defs.add((SymbolEntry) cf.irLine.getNode());
            }

            if (cf.irLine.arg1_entry != null && cf.irLine.arg1_entry.isEntry(TableEntry.LEAF_ENTRY)) {

                newRow.uses.add((SymbolEntry) cf.irLine.arg1_entry);
            }

            if (cf.irLine.arg2_entry != null && cf.irLine.arg2_entry.isEntry(TableEntry.LEAF_ENTRY)) {
                newRow.uses.add((SymbolEntry) cf.irLine.arg2_entry);
            }
            useAndDefs.add(newRow);
        }
    }

    public void cfgRelations() {

        for (int i = 0; i < baseNodes.size(); i++) {
            ControlFlowNode cursor = baseNodes.get(i);

            switch (cursor.irLine.type) {

                case (Quadruple.UNCONDITIONAL_JUMP): {
                    ControlFlowNode successor = labelMap.get(cursor.irLine.result);
                    successor.predecessors.add(cursor);
                    cursor.sucessors.add(successor);
                }
                break;
                case (Quadruple.RETURN_3AC):
                    // no-op
                    break;

                case (Quadruple.CONDITIONAL_JUMP): {
                    ControlFlowNode successor = labelMap.get(cursor.irLine.arg2);
                    successor.predecessors.add(cursor);

                    cursor.sucessors.add(successor);
                }
                // This executes in every case EXCEPT unconditional and return
                default:

                    if (i + 1 < baseNodes.size()) {
                        ControlFlowNode next = baseNodes.get(i + 1);

                        if (!cursor.irLine.equals(mainLast)) {
                            next.predecessors.add(cursor);

                            cursor.sucessors.add(next);
                        }

                    }
            }
        }
    }


    /**
     * Generates all the cfg nodes, does label resolution for branch landing,
     * and places them in list according to ordering of IR
     */
    public void generateCfg() {
        Stack<Quadruple> lastLabel = new Stack<Quadruple>();
        int index = 0;

        int mainSize = ir.get(0).lines.get(0).getLength();
        mainLast = ir.get(0).lines.get(0).get(mainSize - 1);

        for (IRClass irClass : ir) {
            for (IRMethod irMethod : irClass.lines) {
                Quadruple labelQuad = new Quadruple(Quadruple.LABEL);
                labelQuad.result = irMethod.getName();
                lastLabel.push(labelQuad);
                for (Quadruple quad : irMethod.lines) {
                    if (quad.type == Quadruple.LABEL) {
                        lastLabel.push(quad);
                    } else {

                        ControlFlowNode newNode = new ControlFlowNode(quad, index);
                        baseNodes.add(newNode);

                        // while we have labels, bbind them to
                        while (lastLabel.size() > 0) {
                            labelMap.put(lastLabel.pop().result, newNode);
                        }
                    }

                    index++;
                }


            }
        }
    }

    public CodeGenerator(ArrayList<IRClass> classes, SymbolTable symTable) {
        programTable = symTable;
        ir = classes;
        instPrinter = new QuadEmit();

        if (DEBUG) {
            System.out.println(" -------------- SOURCE INTERMEDIATE REPRESENTATION --------------");


            for (IRClass quadCode : this.ir) {
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
                for (int i = 0; i < 4; i++) {
                    fileOut.append("move").append(' ').append("$s").append(i).append(", ").append("$a").append(i).append('\n');
                }

                for (Quadruple quad : irMethod.lines) {
                    quad.getResRegister();
                    // Switch based on the type of quadtruple
                    switch (quad.type) {

                        case (Quadruple.ASSIGNMENT):
                            fileOut.append(instPrinter.handleAssignment(quad)).append('\n');
                            break;
                        case (Quadruple.CALL):
                            fileOut.append(instPrinter.handleCall(quad)).append('\n');
                            break;
                        case (Quadruple.CONDITIONAL_JUMP):
                            fileOut.append(instPrinter.handleCondJump(quad)).append('\n');
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
                            fileOut.append(instPrinter.handleUnaryAssignment(quad)).append('\n');
                            break;
                        case (Quadruple.UNCONDITIONAL_JUMP):
                            fileOut.append(instPrinter.handleUncondJump(quad)).append('\n');
                            break;
                    }
                }

                if (labelQuad.getResult().equals("main")) ;
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