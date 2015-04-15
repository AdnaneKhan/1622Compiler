package SyntaxTree;

import Visitor.Visitor;
import Visitor.TypeVisitor;

public abstract class Type extends SyntaxTree.ASTNode {
    public abstract void accept(Visitor v);

    public abstract Type accept(TypeVisitor v);
}
