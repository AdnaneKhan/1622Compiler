package IR;

import SymTable.*;

public class Quadruple {

    public Quadruple(int type) {
        arg1 = "";
        arg2 = "";
        op = "";
        result = "";

        label = "";
        this.type = type;
    }

    private String resolveVars(TableEntry theVar) {

        String varReturn = theVar.getSymbolName();

        if (theVar.parent.isEntry(SymbolTable.CLASS_ENTRY)) {

            String className = theVar.parent.getSymbolName();
            varReturn = className + "_" + theVar.getSymbolName();

        } else if (theVar.parent.isEntry(SymbolTable.METHOD_ENTRY)) {

            String methName = theVar.parent.getSymbolName();
            String className = theVar.parent.parent.getSymbolName();

            varReturn = className + "_" + methName + "_" + theVar.getSymbolName();
        }

        return varReturn;
    }

    public final static int ASSIGNMENT = 1;
    public final static int UNARY_ASSIGNMENT = 2;
    public final static int COPY = 3;
    public final static int UNCONDITIONAL_JUMP = 4;
    public final static int CONDITIONAL_JUMP = 5;
    public final static int PARAMETER = 6;
    public final static int CALL = 7;
    public final static int RETURN_3AC = 8;
    public final static int INDEXED_ASSIGNMENT = 9;
    public final static int NEW_3AC = 10;
    public final static int NEW_ARRAY = 11;
    public final static int LENGTH_3AC = 12;
    public final static int PRINT = 13;
    public final static int INDEXED_LOOKUP = 14;
    public final static int LABEL = 15;

    ///public String arg1;
    ///public String arg2;

    public String op;
    public String result;

    private boolean resultLiteral;
    public int intResult;

    public int type;
    public String label;

    public String arg1;
    public String arg2;

    private TableEntry arg1_entry;
    private TableEntry arg2_entry;
    private TableEntry resVar;


    public Quadruple() {
        arg1 = "";
        arg2 = "";
        op = "";
        result = "";
        type = 0;
        label = "";
    }

    public void setIntResult(int toSet) {
        intResult = toSet;
        this.resultLiteral = true;
    }

    public boolean isLiteral() {
        return resultLiteral;
    }

    public String getResult() {
        if (resultLiteral) {
            return Integer.toString(this.intResult);
        } else {
            return result;
        }
    }


    public void transferResult(Quadruple origin) {
        if (origin.isLiteral()) {
            this.resultLiteral = true;
            this.setIntResult(origin.intResult);
        } else {
            this.result = origin.getResult();
        }
    }

    public void setResEntry(TableEntry res) {
        resVar = res;
        result = resolveVars(res);
    }

    public void setArg1(TableEntry arg) {
        arg1_entry = arg;
        arg1 = resolveVars(arg);

    }

    public void setArg2(TableEntry arg) {
        arg2_entry = arg;
        arg2 = resolveVars(arg);
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