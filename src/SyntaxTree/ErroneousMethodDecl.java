package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class ErroneousMethodDecl extends MethodDecl{
    public Type t;
    public Identifier i;
    public FormalList fl;
    public VarDeclList vl;
    public StatementList sl;
    public Exp e;

    public ErroneousMethodDecl(int col,int line) {
        erroneous = true;
        t=new IdentifierType(":ERROR:",col,line);
        i= new Identifier("ERROR",col,line);
        fl= new ErroneousFormalList();
        vl = null;
        sl = null;
        e = null;

        setValues(col,line);
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
