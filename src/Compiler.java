
import CodeGeneration.CodeGenerator;
import CodeGeneration.ControlFlowNode;
import CodeGeneration.InterferenceGraph;
import CodeGeneration.InterferenceNode;
import CodeGeneration.Row;
import CodeGeneration.Optimizer;
import IR.IRClass;
import SymTable.SymbolTable;
import Visitor.*;
import java_cup.runtime.Symbol;
import SyntaxTree.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Stack;


public class Compiler {
    public static final boolean OPTIMIZE = true;
    private static boolean doOpt = false;

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please entire a file as the argument!");
            System.exit(0);
        } else {
            if (args.length == 2 && (args[0].equals("-O1") || args[0].equals("-o21"))) {
                doOpt = true;
            }
        }

        String fileName = args[0];
        Reader fileRead;
        try {

            fileRead = new BufferedReader(new FileReader(fileName));

            // Initialize Lexer
            MiniJavaLexer miniLex = new MiniJavaLexer(fileRead);

            Program minJProgram = null;
            // Initialize Parserz
            parser p = new parser(miniLex);
            try {
                Symbol parse_tree = p.parse();
                // Since we defined the root non terminal as the executable
                // that is what the parser will report
                minJProgram = (Program) parse_tree.value;

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }



            // Symbol table constructed, but its empty
            SymbolTable compilerTable = new SymbolTable(minJProgram);

            // Start the name analyser visitor
            NameAnalysisVisitor visitor = new NameAnalysisVisitor(compilerTable);
            visitor.visit(minJProgram);

            // Now we can use the same symbol table for type checking
            TypeCheckingVisitor typeVisir = new TypeCheckingVisitor(compilerTable);
            typeVisir.visit(minJProgram);


            // Only move forward with IR generation if fthere are NO ERRORS
            if (Errors.clear) {
                IRGeneratorVisitor irGen = new IRGeneratorVisitor(compilerTable);
                irGen.visit(minJProgram);

                ArrayList<IRClass> ir = irGen.getClasses();

                CodeGenerator codeGen = new CodeGenerator(ir, compilerTable);

                // generate cfg nodes
                codeGen.generateCfg();

                // making cfg relations
                ArrayList<ControlFlowNode> cfg = codeGen.cfgRelations();

                ArrayList<Row> defsAndUse = codeGen.generateDefUse();

                if (doOpt) {
                    Optimizer optimizer = new Optimizer();
                    ir = optimizer.constantConditions(ir, cfg, defsAndUse);
                    codeGen = new CodeGenerator(ir, compilerTable);
                    codeGen.generateCfg();
                    cfg = codeGen.cfgRelations();
                    defsAndUse = codeGen.generateDefUse();
                }

                ArrayList<Row> liveness = codeGen.generateLiveness(defsAndUse);

                InterferenceGraph ifGraph = new InterferenceGraph();

                ifGraph.buildGraph(defsAndUse,liveness);

                Stack<InterferenceNode> temp = new Stack<InterferenceNode>();
                //
                Stack<InterferenceNode> tempStack = ifGraph.coalesceGraph(temp);

                // Simplify the graph

                Stack<InterferenceNode> simpleStack = ifGraph.simplifyGraph(tempStack);

             ///   simpleStack = ifGraph.coalesceGraph(simpleStack);

              ///  simpleStack = ifGraph.simplifyGraph(simpleStack);

                ifGraph.colorGraph(simpleStack);


                String output = codeGen.output();
                System.out.println(output);
                FileWriter fileOut = new FileWriter(fileName + ".asm");
                fileOut.write(output);
                fileOut.flush();


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Exit
    }
}