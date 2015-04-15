package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class Program extends ASTNode {
    public MainClass m;
    public ClassDeclList cl;

    public Program(MainClass am, ClassDeclList acl, int col, int line) {
        m = am;
        cl = acl;
        setValues(col, line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
