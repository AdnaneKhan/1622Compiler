package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class IntegerLiteral extends Exp {
  public int i;

  public IntegerLiteral(int ai,int col,int line) {
    i=ai;
      setValues(col,line);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
