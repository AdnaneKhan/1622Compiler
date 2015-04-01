package Visitor;

/**
 * Created by adnankhan on 4/1/15.
 */
public abstract class Errors {
    public static void identifierError(int line, int column, String name) {
        System.out.println("Use of undefined identifier "+ name + " at line "+line+", character " + column);
    }

    public static void multiplyDefinedError(int line, int column, String name) {
        System.out.println("Multiply defined identifier " + name + " at line " + line + " character" + column);
    }


}
