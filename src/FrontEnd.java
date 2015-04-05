
import SymTable.SymbolTable;
import Visitor.*;
import java_cup.runtime.Symbol;
import SyntaxTree.*;

import java.io.*;


public class FrontEnd {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please entire a file as the argument!");
            System.exit(0);
        }

        String fileName = args[0];
        Reader fileRead;
        try {

            fileRead = new BufferedReader(new FileReader(fileName));

            // Initialize Lexer
            MiniJavaLexer miniLex = new MiniJavaLexer(fileRead);

            // Initialize Parserz
            parser p = new parser(miniLex);
            Symbol parse_tree = p.parse();

            // Since we defined the root non terminal as the executable
            // that is what the parser will report
            Program minJProgram = (Program)parse_tree.value;

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
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Exit
    }
}