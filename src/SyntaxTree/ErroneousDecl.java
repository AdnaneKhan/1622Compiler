package SyntaxTree;

/**
 * Created by adnankhan on 4/3/15.
 */
public class ErroneousDecl extends VarDecl {

    public ErroneousDecl( int col, int line) {
        super(new IdentifierType(":ERORR:",col,line), new Identifier("ERROR",col,line), col, line);
    }
}
