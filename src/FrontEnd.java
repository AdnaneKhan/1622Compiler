
import SymTable.SymbolEntry;
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
            
            // Symbol table constructed
            SymbolTable compilerTable = new SymbolTable(minJProgram);

            NameAnalysisVisitor visitor = new NameAnalysisVisitor(compilerTable);
            visitor.visit(minJProgram);

            TypeCheckingVisitor typeVisir = new TypeCheckingVisitor(compilerTable);
            typeVisir.visit(minJProgram);
            // now we can use our samme symbol table


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Exit
    }
}