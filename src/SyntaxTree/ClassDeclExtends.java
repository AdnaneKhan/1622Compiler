package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class ClassDeclExtends extends ClassDecl {

    public Identifier j;

    public MethodDeclList ml;

    public ClassDeclExtends(Identifier ai, Identifier aj,
                            VarDeclList avl, MethodDeclList aml, int col, int line) {
        i = ai;
        j = aj;
        vl = avl;
        ml = aml;

        setValues(col, line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
