package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class Minus extends Exp {
    public Exp e1, e2;

    public Minus(Exp ae1, Exp ae2, int col, int line) {
        e1 = ae1;
        e2 = ae2;
        setValues(col, line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
