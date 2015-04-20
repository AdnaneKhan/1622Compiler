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
    private HashMap<String, ControlFlowNode> labelMap = new HashMap<String, ControlFlowNode>();
    ArrayList<IRClass> ir;
    SymbolTable programTable;
    QuadEmit instPrinter;
    Quadruple mainLast;

    public ArrayList<Row> generateLiveness(ArrayList<Row> useDefs) {
        int iteration = 0;
        boolean unchanged = false;
        ArrayList<ArrayList<Row>> inOut = new ArrayList<ArrayList<Row>>();
        ArrayList<Row> lastCycle;

        while (!unchanged) {
            unchanged = true;
            ArrayList<Row> cycle = new ArrayList<Row>();

            // Pre instantiate rows
            for (int i = 0; i < baseNodes.size(); i++) {
                Row temp = new Row();
                cycle.add(temp);
            }

            // Uses - in, Defs - out
            for (int i = baseNodes.size()-1; i >= 0; i--) {

                // Initial population of inOut
                if (iteration == 0) {

                    // Populate the ins
                    for (SymbolEntry se : useDefs.get(i).uses) {
                        cycle.get(i).uses.add(se);
                    }

                    // Populate the outs
                    if (baseNodes.get(i).sucessors.size() > 0) {
                        for (int j = 0; j < baseNodes.get(i).sucessors.size(); j++) {
                            for (SymbolEntry se : cycle.get(baseNodes.get(i).sucessors.get(j).index).uses) {
                                cycle.get(i).defs.add(se);
                            }
                        }
                    }

                }

                // If we are beyond initial population
                else {



                    // Populate the ins
                    for (SymbolEntry se : inOut.get(iteration - 1).get(i).defs) {
                        if (!useDefs.get(i).defs.contains(se)) {
                            cycle.get(i).uses.add(se);
                        }
                    }

                    // Populate the outs
                    if (baseNodes.get(i).sucessors.size() > 0) {

                        for (int j = 0; j < baseNodes.get(i).sucessors.size(); j++) {

                            for (SymbolEntry se : cycle.get(baseNodes.get(i).sucessors.get(j).index).uses) {
                                cycle.get(i).defs.add(se);
                            }

                        }


                    }
                    for (SymbolEntry se : useDefs.get(i).uses) {
                        cycle.get(i).uses.add(se);
                    }


                }

            }

            inOut.add(cycle);

            // Check to see if there was a change
            if (iteration == 0) {
                unchanged = false;
            }
            else {
                for (int j = 0; j < cycle.size(); j++) {
                    Row q = cycle.get(j);
                    Row p = inOut.get(iteration-1).get(j);
                    if (q.defs.size() != p.defs.size() || q.uses.size() != p.uses.size()) {
                        unchanged = false;
                        break;
                    }
                }
            }


//           System.out.println("-- iter number: " + iteration);
//            int k = 0;
//           for (Row r : cycle) {
//
//               System.out.print(k++ +" INs ");
//               for (SymbolEntry s : r.uses) {
//                   System.out.print(s.getSymbolName() + ",");
//
//               }
//               System.out.print(" OUTS ");
//
//               for (SymbolEntry s2 : r.defs) {
//                   System.out.print(s2.getSymbolName() + ",");
//               }
//
//               System.out.println("\n------------------\n");
//
//           }

            iteration++;


        }


        lastCycle = inOut.get(inOut.size()-1);
        return lastCycle;
    }


    public ArrayList<Row> generateDefUse() {
        Row newRow;

        for (ControlFlowNode cf : baseNodes) {
            newRow = new Row();
            if ((cf.irLine.type == Quadruple.PARAMETER || cf.irLine.type == Quadruple.RETURN_3AC) && cf.irLine.getNode() != null) {

                // Placing in uses due to exxceptiions
                newRow.uses.add((SymbolEntry) cf.irLine.getNode());
            } else if (cf.irLine.getNode() != null) {
                newRow.defs.add((SymbolEntry) cf.irLine.getNode());
            }

            if (cf.irLine.arg1_entry != null && cf.irLine.arg1_entry.isEntry(TableEntry.LEAF_ENTRY)) {

                newRow.uses.add((SymbolEntry) cf.irLine.arg1_entry);
            }

            if (cf.irLine.arg2_entry != null && cf.irLine.arg2_entry.isEntry(TableEntry.LEAF_ENTRY)) {
                newRow.uses.add((SymbolEntry) cf.irLine.arg2_entry);
            }

            if (cf.irLine.type == Quadruple.COPY && cf.irLine.arg1_entry != null) {
                if (DEBUG ) {
                    System.err.println("Adding move related row!");
                   
                }
                newRow.moveRelated = true;
            }

            useAndDefs.add(newRow);
        }
        System.out.println("USES AND DEFS");
        int k = 0;
        for (Row r : useAndDefs) {

            System.out.print(k++ + " USEs ");
            for (SymbolEntry s : r.uses) {
                System.out.print(s.getSymbolName() + ",");

            }
            System.out.print(" DEFs ");

            for (SymbolEntry s2 : r.defs) {
                System.out.print(s2.getSymbolName() + ",");
            }

            System.out.println("\n------------------\n");

        }

        return this.useAndDefs;
    }

    public ArrayList<ControlFlowNode> cfgRelations() {

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
        return baseNodes;
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

                        ControlFlowNode newNode = new ControlFlowNode(quad, index++);
                        baseNodes.add(newNode);

                        // while we have labels, bbind them to
                        while (lastLabel.size() > 0) {
                            labelMap.put(lastLabel.pop().result, newNode);
                        }
                    }
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
                            fileOut.append(instPrinter.handleCondJump(quad)).append('\n');
                            break;
                        case (Quadruple.COPY):
                            fileOut.append(instPrinter.handleCopy(quad)).append('\n');
                            break;
                        case (Quadruple.INDEXED_ASSIGNMENT):
                            fileOut.append(instPrinter.handleIndexedAssignment(quad)).append('\n');
                            break;
                        case (Quadruple.INDEXED_LOOKUP):
                            fileOut.append(instPrinter.handleIndexedLookup(quad)).append('\n');
                            break;
                        case (Quadruple.LABEL):
                            fileOut.append(quad.getResult()).append(':').append('\n');
                            break;
                        case (Quadruple.LENGTH_3AC):
                            fileOut.append(instPrinter.handleLength(quad)).append('\n');
                            break;
                        case (Quadruple.NEW_3AC):
                            fileOut.append(instPrinter.handleNew(quad)).append('\n');
                            break;
                        case (Quadruple.NEW_ARRAY):
                            fileOut.append(instPrinter.handleNewArray(quad)).append('\n');
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