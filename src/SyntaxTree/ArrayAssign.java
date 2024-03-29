package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class ArrayAssign extends Statement {
    public Identifier i;
    public Exp e1, e2;

    public ArrayAssign(Identifier ai, Exp ae1, Exp ae2, int col, int line) {
        i = ai;
        e1 = ae1;
        e2 = ae2;

        // Calls setter of ASTNode to log column and line from parsers
        setValues(col, line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

}

