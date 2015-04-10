package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class ClassDeclSimple extends ClassDecl {

  public MethodDeclList ml;
 
  public ClassDeclSimple(Identifier ai, VarDeclList avl, MethodDeclList aml,int col,int line) {
    i=ai; vl=avl; ml=aml;
      setValues(col,line);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
