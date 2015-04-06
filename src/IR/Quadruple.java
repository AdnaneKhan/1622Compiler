package IR;

import SymTable.SymbolEntry;
import SymTable.TableEntry;

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
    public final static int INDEXED_LOOKUP = 14;
    public final static int LABEL = 15;

	public String arg1;
    public String arg2;

	public String op;
	public String result;
	public int type;
	public String label;

	public Quadruple() {
		arg1 = "";
		arg2 = "";
		op = "";
		result = "";
		type = 0;
		label = "";
	}

    @Override
	public String toString() {
        String ret = "";

        if (label.compareTo("") != 0) {
        	ret += label + ": ";
        }


		if (type == ASSIGNMENT) {
			ret += result + " := " + arg1 + " " + op + " " + arg2;
		}
		else if (type == UNARY_ASSIGNMENT) {
			ret += result + " := " + op + arg1;
		}
		else if (type == COPY) {
			ret += result + " := " + arg1;
		}
		else if (type == PARAMETER) {
			ret += "param " + result;
		}
		else if (type == CALL) {
			ret += result + " := call " + arg1 + ", " + arg2;
		}
		else if (type == RETURN_3AC) {
			ret += "return " + result;
		}
		else if (type == INDEXED_ASSIGNMENT) {
			ret += result + "[" + arg2 + "]" + " := " + arg1;
		}
		else if (type == NEW_3AC) {
			ret += result + " := " + op + " " + arg1;
		}
		else if (type == NEW_ARRAY) {
			ret += result + " := " + op + "[" + arg1 + "]";
		}
		else if (type == LENGTH_3AC) {
			ret += result + " := length " + arg1;
		}
		else if (type == PRINT) {
			ret += "call print, 1";
		}
		else if (type == INDEXED_LOOKUP) {
			ret += result + " := " + arg1 + "[" + arg2 + "]";
		}
		else if (type == LABEL) {
			ret += result + ": ";
		}
		else if (type == UNCONDITIONAL_JUMP) {
			ret += "goto " + result;
		}
		else if (type == CONDITIONAL_JUMP) {
			ret += result + " " + arg1 + " goto " + arg2;
		}


        return ret;
	}

}