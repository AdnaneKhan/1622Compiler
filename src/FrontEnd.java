
import java_cup.runtime.Symbol;
import syntaxtree.*;
import visitor.PrettyPrintVisitor;

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

            PrettyPrintVisitor visitor = new PrettyPrintVisitor();

            // Visit is being given the start symbol, which
            // will now go and properly print everything.
            visitor.visit(minJProgram);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Exit

    }
}