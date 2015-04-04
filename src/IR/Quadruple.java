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
    public final static int PRINT = 13;

	public String arg1;
	public String arg2;
	public String op;
	public String result;
	public int type;

	public Quadruple() {
		arg1 = "";
		arg2 = "";
		op = "";
		result = "";
		type = 0;
	}

    @Override
	public String toString() {
        String ret = "";
		if (this.type == 1) {
			ret = result + " := " + arg1 + op + arg2;
		}
		else if (this.type == 2) {
			ret = result + " := " + op + arg1;
		}
		else if (this.type == 3) {
			ret = result + " := " + arg1;
		}

        return ret;
	}

}