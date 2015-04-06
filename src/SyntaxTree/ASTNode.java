package SyntaxTree;

/**
 * Created by adnankhan on 3/28/15.
 *
 * This represents a base AST node that other nodes can inherit from so that they can encode
 * src file structure information (which otherwise would be lost in the AST) for purposes
 * of error reporting.
 */
public abstract class ASTNode {
    public boolean erroneous = false;
    private int srcLine;
    private int srcCol;

    protected void setValues(int srcLine, int srcCol) {
        this.srcLine = srcLine +1;
        this.srcCol = srcCol +1;
    }
    public int lineNum() {
        return this.srcLine;

    }
    public int charNum() {
        return this.srcCol;
    }
}
