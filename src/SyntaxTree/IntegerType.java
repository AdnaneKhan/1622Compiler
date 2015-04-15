package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class IntegerType extends Type {
    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
