package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class False extends Exp {

    public False(int line, int column) {
        setValues(line, column);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
