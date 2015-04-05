package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class This extends Exp {
    public This(int col, int line) {
        setValues(col,line);
    }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
