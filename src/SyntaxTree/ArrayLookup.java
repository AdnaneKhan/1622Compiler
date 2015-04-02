package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class ArrayLookup extends Exp  {
  public Exp e1,e2;
  
  public ArrayLookup(Exp ae1, Exp ae2,int col,int line) {
    e1=ae1; e2=ae2;

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
