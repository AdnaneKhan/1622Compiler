package Visitor;

import SymTable.SymbolTable;
import SyntaxTree.*;

public class TypeCheckingVisitor extends DepthFirstVisitor {

    SymbolTable base;
    public TypeCheckingVisitor(SymbolTable toUse) {

        // This symbol table has already been populated with appropriate values
        base = toUse;
    }



}
