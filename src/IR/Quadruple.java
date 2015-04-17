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
        if (varReturn.charAt(0) != '_' && !varReturn.equals("this")) {
            if (theVar.parent.isEntry(SymbolTable.CLASS_ENTRY)) {

                String className = theVar.parent.getSymbolName();
                varReturn = className + "_" + varReturn;

            } else if (theVar.parent.isEntry(SymbolTable.METHOD_ENTRY)) {

                String methName = theVar.parent.getSymbolName();
                String className = theVar.parent.parent.getSymbolName();

                varReturn = className + "_" + methName + "_" + varReturn;
            }
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

    public String op;
    public String result;

    private boolean resultLiteral;
    private boolean resultBoolean;

    // NOT ACTUAL LITERALS
    private boolean isArg1Literal;
    private boolean isArg2Literal;


    private int intResult;


    public int arg1Int;
    public int arg2Int;

    public int type;
    public String label;

    public String arg1;
    public String arg2;

    public TableEntry arg1_entry;
    public TableEntry arg2_entry;
    private TableEntry resVar;


    public Quadruple() {
        arg1 = "";
        arg2 = "";
        op = "";
        result = "";
        type = 0;
        label = "";
    }

    public int getResRegister() {
        int reg = 0;
        if (resVar != null && this.resVar.isEntry(TableEntry.LEAF_ENTRY)) {
            reg = ((SymbolEntry) resVar).getRegister();
        } else {
            System.err.println("Attempted to get register for a non var " + result);
        }
        return reg;
    }

    public int getArg1Register() {
        int reg = 0;
        if (arg1_entry != null && this.arg1_entry.isEntry(TableEntry.LEAF_ENTRY)) {
            reg = ((SymbolEntry) arg1_entry).getRegister();
        } else {
            System.err.println("Attempted to get register for a non var " + arg1);
        }
        return reg;
    }

    public int getArg2Register() {
        int reg = 0;
        if (arg2_entry != null && this.arg2_entry.isEntry(TableEntry.LEAF_ENTRY)) {
            reg = ((SymbolEntry) arg2_entry).getRegister();
        } else {
            System.err.println("Attempted to get register for a non var " + arg2);
        }
        return reg;
    }
    /**
     * @param toSet integer literal to place in the result field
     */
    public void setIntResult(int toSet) {
        intResult = toSet;
        this.resultLiteral = true;
    }

    public void setIsBoolean() {

        this.resultBoolean = true;
    }

    public void setArg1(int toSet) {
        isArg1Literal = true;
        arg1Int = toSet;
    }

    public void setArg1(String toSet) {
        arg1 = toSet;
    }

    public String getArg1() {
        if (isArg1Literal) {
            return Integer.toString(arg1Int);
        } else {
            return arg1;
        }
    }

    public String getArg2() {
        if (isArg2Literal) {
            return Integer.toString(arg2Int);
        } else {
            return arg2;
        }
    }

    public void setArg2(int toSet) {
        isArg2Literal = true;
        arg2Int = toSet;
    }

    public void setArg2(String toSet) {
        arg2 = toSet;
    }


    public boolean isTempArg1() {
        if (!arg1Literal() && arg1.charAt(0) == '_') {
            return true;
        }

        return false;
    }

    public boolean isTempArg2() {
        if (!arg2Literal() && arg2.charAt(0) == '_') {
            return true;
        }

        return false;
    }

    /**
     * @return true if this quadruple represents an integer literal
     */
    public boolean isLiteral() {
        return resultLiteral;
    }

    public boolean isBoolean() {
        return resultBoolean;
    }

    public boolean arg1Literal() {
        return isArg1Literal;
    }

    public boolean arg2Literal() {
        return isArg2Literal;
    }


    /**
     * @return string representation off the result
     */
    public String getResult() {
        if (resultLiteral) {
            return Integer.toString(this.intResult);
        } else if (resVar != null && result.charAt(0) != '_' && !result.equals("this")) {
            return resVar.getHierarchyName();
        } else {
            return result;
        }
    }

    /**
     * IF THIS quadruple is associated with a node, like a class or method, then
     * return the node associated.
     * <p/>
     * Otherwise returns null
     *
     * @return
     */
    public TableEntry getNode() {
        return resVar;
    }


    /**
     * Transfers result from one quad to antoher, retaining the type iff it is an int
     *
     * @param origin
     */
    public void transferResult(Quadruple origin) {
        if (origin.isLiteral()) {
            this.resultLiteral = true;
            this.setIntResult(origin.intResult);
        } else {
            this.result = origin.getResult();
            this.resVar = origin.resVar;
        }
    }

    public void resToArg1(Quadruple origin) {
        if (origin.isLiteral()) {
            this.setArg1(origin.intResult);
        } else {
            this.arg1 = origin.getResult();
            this.arg1_entry = origin.resVar;
        }
    }

    public void resToArg2(Quadruple origin) {
        if (origin.isLiteral()) {
            this.setArg2(origin.intResult);
        } else {
            this.arg2 = origin.getResult();
            this.arg2_entry = origin.resVar;
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
            ret += result + " := " + getArg1() + " " + op + " " + getArg2();
        } else if (type == UNARY_ASSIGNMENT) {
            ret += result + " := " + op + getArg1();
        } else if (type == COPY) {
            ret += result + " := " + getArg1();
        } else if (type == PARAMETER) {
            ret += "param " + getResult();
        } else if (type == CALL) {
            ret += result + " := call " + this.arg1_entry.getSymbolName() + ", " + getArg2();
        } else if (type == RETURN_3AC) {

            String boolRes;
            if (this.isBoolean()) {
                if (intResult == 1) {
                    boolRes = "true";
                } else {
                    boolRes = "false";
                }
                ret += "return " + boolRes;
            } else {
                ret += "return " + this.getResult();
            }

        } else if (type == INDEXED_ASSIGNMENT) {
            ret += result + "[" + getArg2() + "]" + " := " + getArg1();
        } else if (type == NEW_3AC) {
            ret += result + " := " + op + " " + getArg1();
        } else if (type == NEW_ARRAY) {
            ret += result + " := " + op + "[" + getArg1() + "]";
        } else if (type == LENGTH_3AC) {
            ret += result + " := length " + getArg1();
        } else if (type == PRINT) {
            ret += "call print, 1";
        } else if (type == INDEXED_LOOKUP) {
            ret += result + " := " + getArg1() + "[" + getArg2() + "]";
        } else if (type == LABEL) {
            ret += result + ": ";
        } else if (type == UNCONDITIONAL_JUMP) {
            ret += "goto " + result;
        } else if (type == CONDITIONAL_JUMP) {
            ret += result + " " + getArg1() + " goto " + getArg2();
        }


        return ret;
    }

}