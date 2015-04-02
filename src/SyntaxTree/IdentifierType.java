package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class IdentifierType extends Type {
  public String s;
    public boolean methClass = false;

  public IdentifierType(String as,int col,int line) {
    s=as;
      setValues(col,line);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
