package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class Identifier extends ASTNode {
    public String s;

    public Identifier(String as, int col, int line) {
        s = as;
        setValues(col, line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }

    public String toString() {
        return s;
    }
}
