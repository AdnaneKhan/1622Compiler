package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class Call extends Exp {
  public Exp e;
  public Identifier i;
  public ExpList el;
  
  public Call(Exp ae, Identifier ai, ExpList ael,int col,int line) {
    e=ae; i=ai; el=ael;

      setValues(col,line);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
