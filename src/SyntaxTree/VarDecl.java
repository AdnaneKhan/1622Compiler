package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class VarDecl extends ASTNode{
  public Type t;
  public Identifier i;
  
  public VarDecl(Type at, Identifier ai,int col,int line) {
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
