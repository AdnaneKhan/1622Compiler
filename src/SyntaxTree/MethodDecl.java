package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public class MethodDecl extends ASTNode {
    public Type t;
    public Identifier i;
    public FormalList fl;
    public VarDeclList vl;
    public StatementList sl;
    public Exp e;


    public MethodDecl() {

    }

    public MethodDecl(Type at, Identifier ai, FormalList afl, VarDeclList avl,
                      StatementList asl, Exp ae, int col, int line) {
        t = at;
        i = ai;
        fl = afl;
        vl = avl;
        sl = asl;
        e = ae;
        setValues(col, line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
