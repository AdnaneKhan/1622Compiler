package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class Assign extends Statement{
  public Identifier i;
  public Exp e;

  public Assign(Identifier ai, Exp ae,int col,int line) {
    i=ai; e=ae;

      // Calls setter of ASTNode to log column and line from parser
      setValues(col,line);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}

