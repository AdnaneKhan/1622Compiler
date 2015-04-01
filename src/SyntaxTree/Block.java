package SyntaxTree;
import Visitor.Visitor;
import Visitor.TypeVisitor;

public class Block extends Statement  {
  public StatementList sl;

  public Block(StatementList asl,int col,int line) {
    sl=asl;
      setValues(col,line);
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}

