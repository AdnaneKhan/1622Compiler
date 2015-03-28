package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;


public class Formal extends ASTNode {
  public Type t;
  public Identifier i;
 
  public Formal(Type at, Identifier ai,int col,int line) {
    t=at; i=ai;
      setValues(col,line);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
