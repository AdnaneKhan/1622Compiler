package SyntaxTree;

import java.util.Vector;

public class ClassDeclList extends ASTNode {
    private Vector list;

    public ClassDeclList() {
        list = new Vector();

    }

    public void addElement(ClassDecl n) {
        list.addElement(n);
    }

    public ClassDecl elementAt(int i) {
        return (ClassDecl) list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}
