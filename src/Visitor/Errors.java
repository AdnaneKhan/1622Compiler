package Visitor;


/**
 * Created by adnankhan on 4/1/15.
 */
public abstract class Errors {
    public static void identifierError(int line, int column, String name) {
        System.out.println("Use of undefined identifier " + name + " at line " + line + ", character " + column);
    }

    public static void multiplyDefinedError(int line, int column, String name) {
        System.out.println("Multiply defined identifier " + name + " at line " + line + " character" + column);
    }

    public static void classMethodAssign(int line, int column, String id1, String id2) {
        System.out.println("Invalid l-value, " + id1 + " is a " + id2 + ", at line " + line + ", character " + column);
    }

    public static void assignFromMethodClass(int line, int column, String id1, String id2) {
        System.out.println("Invalid r-value, " + id1 + " is a " + id2 + ", at line " + line + ", character " + column);
    }

    public static void methClassOp(int line, int column, String id) {
        System.out.println("Invalid operands for " + id + " operator, at line " + line + ", character " + column + "");
    }

    public static void badCall(int line, int column) {
        System.out.println("Attempt to call a non-method at line " + line + ", character " + column + "");
    }

    public static void argCount(int line, int column, String id) {
        System.out.println("Call of method " + id + " does not match its declared number of arguments at line " + line + ", character " + column + "");
    }

    public static void argTypeMismatch(int line, int column, String id) {
        System.out.println("Call of method " + id + " does not match its declared signature at line " + line + ", character " + column + "");
    }

    public static void nonIntegerOperand(int line, int column, char op) {
        System.out.println("Non-integer operand for operator "+op+" at line " + line + ", character " + column + "");
    }

    public static void nonBooleanOperand(int line, int column, String id) {
        System.out.println("Attempt to use boolean operator " + id + " on non-boolean operands at line " + line + ", character " + column + "");
    }

    public static void nonArrayLength(int line, int column) {
        System.out.println("Length property only applies to arrays, line " + line + ", character " + column + "");
    }

    public static void nonBoolIf(int line, int column, String id) {
        System.out.println("Non-boolean expression used as the condition of " + id + " statement at line " + line + ", character " + column + "");
    }

    public static void typeMismatch(int line, int column) {
        System.out.println("Type mismatch during assignment at line " + line + ", character " + column + "");
    }

    public static void thisInMain(int line, int column) {
        System.out.println("Illegal use of keyword ‘this’ in static method at line " + line + ", character " + column + "");
    }
    public static void badType(int line, int column) {
        System.out.println("Identifier is not a type at line" + line + ", character " + column);
    }

}
