package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public abstract class ClassDecl extends ASTNode {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
}
