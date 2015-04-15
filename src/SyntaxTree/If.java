package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class If extends Statement {
    public Exp e;
    public Statement s1, s2;

    public If(Exp ae, Statement as1, Statement as2, int col, int line) {
        e = ae;
        s1 = as1;
        s2 = as2;
        setValues(col, line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}

