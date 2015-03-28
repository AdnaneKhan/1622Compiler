package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class ArrayLength extends Exp {
  public Exp e;
  
  public ArrayLength(Exp ae,int col,int line) {
    e=ae;
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
