package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

/**
 * Created by adnankhan on 4/3/15.
 */
public class ErroneousClassDecl extends ClassDecl {

    public ErroneousClassDecl() {
        super.erroneous = true;
    }
    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

    @Override
    public Type accept(TypeVisitor v) {
        return v.visit(this);
    }
}
