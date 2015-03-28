package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public abstract class Type extends syntaxtree.ASTNode{
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
}
