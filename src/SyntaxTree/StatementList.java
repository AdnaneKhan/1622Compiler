package SyntaxTree;

import java.util.Vector;

public class StatementList extends ASTNode {
    private Vector list;

    public StatementList() {
        list = new Vector();

    }

    public void addElement(Statement n) {

        list.add(0, n);
    }

    public Statement elementAt(int i) {
        return (Statement) list.elementAt(i);
    }

    public int size() {
        return list.size();
    }
}
