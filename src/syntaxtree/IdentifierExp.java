package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class IdentifierExp extends Exp {
  public String s;
  public IdentifierExp(String as,int col,int line) {
    s=as;
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
