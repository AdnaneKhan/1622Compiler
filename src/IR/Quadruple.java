package IR;

public class Quadruple {

	public final static int ASSIGNMENT = 1;
    public final static int UNARY_ASSIGNMENT= 2;
    public final static int COPY = 3;
    public final static int UNCONDITIONAL_JUMP = 4;
    public final static int CONDITIONAL_JUMP = 5;
    public final static int PARAMETER = 6;
    public final static int CALL = 7;
    public final static int RETURN_3AC = 8;
    public final static int INDEXED_ASSIGNMENT= 9;
    public final static int NEW_3AC = 10;
    public final static int NEW_ARRAY = 11;
    public final static int LENGTH_3AC = 12;

	TableEntry arg1;
	TableEntry arg2;
	String op;
	TableEntry result;
	int type;

	public Quadruple(int type) {
		arg1 = NULL;
		arg2 = NULL;
		result = NULL;
	}

}