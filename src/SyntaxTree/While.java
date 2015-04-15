package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class While extends Statement {
    public Exp e;
    public Statement s;

    public While(Exp ae, Statement as, int col, int line) {
        e = ae;
        s = as;
        setValues(col, line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}

