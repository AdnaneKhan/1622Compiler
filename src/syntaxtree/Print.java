package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class Print extends Statement {
  public Exp e;

  public Print(Exp ae,int col,int line) {
    e=ae;
      setValues(col,line);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
