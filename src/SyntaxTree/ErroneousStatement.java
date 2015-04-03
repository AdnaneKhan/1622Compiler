package SyntaxTree;


import Visitor.Visitor;
import Visitor.TypeVisitor;


/**
 * Created by adnankhan on 4/3/15.
 */
public class ErroneousStatement extends Statement {

    public Identifier i;
    public Exp e;

    public ErroneousStatement(int line, int column) {
        setValues(column, line);
    }

    @Override
    public void accept(Visitor v) {

    }

    @Override
    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
