package Visitor;

import SymTable.*;
import SyntaxTree.*;

import java.util.LinkedList;
import java.util.Stack;


public class NameAnalysisVisitor extends DepthFirstVisitor {

    SymbolTable base;
    String extendsClass= null;

    TableEntry swapper = null;


    public NameAnalysisVisitor(SymbolTable toPopulate) {
        base = toPopulate;
    }

    /**
     * Simple utility memthods checks the actual object and gets teh idenifier ffrom it
     *
     * @param toExtract abstract class declaration
     * @return name of class
     */
    private String extractClassName(ClassDecl toExtract) {

        String className;
        if (toExtract instanceof ClassDeclSimple) {
            className = ((ClassDeclSimple) toExtract).i.toString();
        } else  {
            className = ((ClassDeclExtends) toExtract).i.toString();
        }
        return className;
    }

    private boolean checkSuper(String s,int type) {
        boolean retValue =false;

        if (extendsClass != null) {
            ClassTable superClass = base.getClassTable(extendsClass);

            if (superClass != null) {
                retValue = superClass.hasEntry(s,type);
            }
        }
        return retValue;
    }


    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {

        Stack<ClassDecl> supers = new Stack<ClassDecl>();
        // Checks that classes do not have the same name while adding them to symbol table
        for (int i = 0; i < n.cl.size(); i++) {
            ClassDecl cursor = n.cl.elementAt(i);

            if (!cursor.erroneous) {
                String className = extractClassName(cursor);

                if (base.hasEntry(className,SymbolTable.CLASS_ENTRY)) {
                    Errors.multiplyDefinedError(cursor.lineNum(), cursor.charNum(), className);
                } else {
                    if (cursor instanceof ClassDeclExtends) {
                        supers.push(cursor);
                    } else {
                        base.putClass(cursor);
                    }
                }
            } else {
                Errors.clear = false;
            }
        }

        for (ClassDecl defLast : supers) {
            base.putClass(defLast);
        }


        // Checks that super classes exist where 'extends' called
        for (int i = 0; i < n.cl.size(); i++) {

            ClassDecl cursor = n.cl.elementAt(i);

            if (!cursor.erroneous) {
                String className = extractClassName(cursor);

                if (cursor instanceof ClassDeclExtends) {
                    String extendsName = ((ClassDeclExtends) cursor).j.toString();
                    if (!(base.hasEntry(extendsName, SymbolTable.CLASS_ENTRY))) {
                        Errors.identifierError(cursor.lineNum(), cursor.charNum(), className);
                    }
                } else {
                    cursor.accept(this);
                }
            } else {
                Errors.clear = false;
            }
        }

        // Now actually accept hte ones that hav esuper calsses
        for (ClassDecl defLast : supers) {
            defLast.accept(this);
        }


        // go to the main class scope
        // Add the main class to symbol
        base.putClass(n.m);

        n.m.accept(this);
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        base.descendScope(n.i1.s, SymbolTable.CLASS_ENTRY);
        // We are in the main class scope now
        if (base.getCurrentScope().isEntry(SymbolTable.CLASS_ENTRY)) {
            ClassTable current = (ClassTable) base.getCurrentScope();

            n.i1.accept(this);

            // Now we need to descend into the main method scope
            // Add this id to the main static method variable list
            SymbolEntry stringID = new SymbolEntry(n.i2.toString(),new IdentifierType("String[]",n.i2.lineNum(),n.i2.charNum()), n.i2);
            stringID.parent = base.getCurrentScope();
            current.put(n.i2.toString(), stringID,SymbolTable.LEAF_ENTRY); // note we need to set parent
            n.i2.accept(this);
            n.s.accept(this);
        } else {
            System.err.println("PROBLEM WITH PROGRAM!!");
            // CODE PROBLEM SHOULD NEVER REACH HERE UNDER NORMMAL
            // FUNCTIONALITY
        }
        base.ascendScope();
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        base.descendScope(n.i.s,SymbolTable.CLASS_ENTRY);

        ClassTable current;
        if (base.getCurrentScope().isEntry(SymbolEntry.CLASS_ENTRY)) {
            current = (ClassTable) base.getCurrentScope();

            n.i.accept(this);

            // Check for duplicate variable names in class
            for (int i = 0; i < n.vl.size(); i++) {
                if (!(n.vl.elementAt(i) instanceof ErroneousDecl)) {
                    if (current.hasEntry(n.vl.elementAt(i).i.toString(),SymbolTable.LEAF_ENTRY)) {
                        Errors.multiplyDefinedError(n.vl.elementAt(i).lineNum(), n.vl.elementAt(i).charNum(), n.vl.elementAt(i).i.s);

                    } else {
                        current.putVariable(n.vl.elementAt(i));
                        n.vl.elementAt(i).accept(this);
                    }
                }
            }

            // Check for duplicate method names in class
            for (int i = 0; i < n.ml.size(); i++) {
                if (current.hasEntry(n.ml.elementAt(i).i.toString(),SymbolTable.METHOD_ENTRY)) {
                    Errors.multiplyDefinedError(n.ml.elementAt(i).lineNum(), n.ml.elementAt(i).charNum(), n.ml.elementAt(i).i.s);
                } else {
                    current.putMethod(n.ml.elementAt(i));
                }
            }

            // Now actually accept them
            for (int i = 0; i < n.ml.size(); i++) {
                if (current.hasEntry(n.ml.elementAt(i).i.toString(), SymbolTable.METHOD_ENTRY)) {
                    n.ml.elementAt(i).accept(this);
                }
            }
        } else {
            System.err.println("PROBLEM WITH PROGRAM!!");
            // CODE PROBLEM SHOULD NEVER REACH HERE UNDER NORMMAL
            // FUNCTIONALITY
        }
        base.ascendScope();
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        base.descendScope(n.i.s,SymbolTable.CLASS_ENTRY);
        extendsClass = n.j.s;


        ClassTable current;
        if (base.getCurrentScope().isEntry(SymbolEntry.CLASS_ENTRY)) {
            current = (ClassTable) base.getCurrentScope();

            // Check the parent class
            n.i.accept(this);
            n.j.accept(this);

            // Check for duplicate variable names in class
            for (int i = 0; i < n.vl.size(); i++) {
                if (!(n.vl.elementAt(i) instanceof ErroneousDecl)) {
                    if (current.hasEntry(n.vl.elementAt(i).i.toString(), SymbolTable.LEAF_ENTRY)) {
                        Errors.multiplyDefinedError(n.vl.elementAt(i).lineNum(), n.vl.elementAt(i).charNum(), n.vl.elementAt(i).i.s);
                    } else {
                        current.putVariable(n.vl.elementAt(i));
                        n.vl.elementAt(i).accept(this);
                    }
                }
            }

            // Check for duplicate method names in class
            for (int i = 0; i < n.ml.size(); i++) {
                if (current.hasEntry(n.ml.elementAt(i).i.toString(), SymbolTable.METHOD_ENTRY)) {
                    Errors.multiplyDefinedError(n.ml.elementAt(i).i.lineNum(), n.ml.elementAt(i).i.charNum(), n.ml.elementAt(i).i.s);
                } else {
                    current.putMethod(n.ml.elementAt(i));
                }
            }

            // Now actually accept them
            for (int i = 0; i < n.ml.size(); i++) {
                if (current.hasEntry(n.ml.elementAt(i).i.toString(), SymbolTable.METHOD_ENTRY)) {
                    n.ml.elementAt(i).accept(this);
                }
            }
        } else {
            System.err.println("PROBLEM WITH PROGRAM!!");
            // CODE PROBLEM SHOULD NEVER REACH HERE UNDER NORMMAL
            // FUNCTIONALITY
        }

        extendsClass = null;
        base.ascendScope();

    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        base.descendScope(n.i.s,SymbolTable.METHOD_ENTRY);

        if (base.getCurrentScope().isEntry(SymbolTable.METHOD_ENTRY)) {
            MethodTable current = (MethodTable) base.getCurrentScope();

            n.t.accept(this);
            n.i.accept(this);

            // Checks formal list for duplicate variable names
            for (int i = 0; i < n.fl.size(); i++) {
                    if (current.hasEntry(n.fl.elementAt(i).i.toString(), SymbolTable.LEAF_ENTRY)) {
                        Errors.multiplyDefinedError(n.fl.elementAt(i).lineNum(), n.fl.elementAt(i).charNum(), n.fl.elementAt(i).i.s);
                    } else {
                        current.putVariable(n.fl.elementAt(i));
                        n.fl.elementAt(i).accept(this);
                    }
            }

            // Checks variable decls for duplicate names
            for (int i = 0; i < n.vl.size(); i++) {
                if (!(n.vl.elementAt(i) instanceof ErroneousDecl)) {
                    if (current.hasEntry(n.vl.elementAt(i).i.toString(), SymbolTable.LEAF_ENTRY)) {
                        Errors.multiplyDefinedError(n.vl.elementAt(i).lineNum(), n.vl.elementAt(i).charNum(), n.vl.elementAt(i).i.s);

                    } else {
                        current.putVariable(n.vl.elementAt(i));
                        n.vl.elementAt(i).accept(this);
                    }
                }
            }

            for (int i = 0; i < n.sl.size(); i++) {
                n.sl.elementAt(i).accept(this);
            }
            n.e.accept(this);
        }

        base.ascendScope();
    }


    // String s;
    public void visit(IdentifierType n) {
        boolean identifierFound = false;

        // Check the base scope for the existence of a class with
        // The same identifier
        if (base.hasEntry(n.s, SymbolTable.CLASS_ENTRY)) {
            identifierFound = true;
        }

        if (!identifierFound && !n.erroneous) {

            Errors.identifierError(n.lineNum(), n.charNum(), n.s);
        }

        if (n.erroneous) {
            Errors.clear = false;
        }
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        boolean identifierFound = false;


        if (extendsClass != null) {
            ClassTable superClass = base.getClassTable(extendsClass);

            if (superClass != null) {
                if (superClass.hasEntry(n.i.s,SymbolTable.LEAF_ENTRY)) {
                    identifierFound = true;
                }
            }
        }

        if (base.getCurrentScope().hasEntryWalk(n.i.s,SymbolTable.LEAF_ENTRY)) {
            identifierFound = true;
        }

        if (!identifierFound) {
            Errors.identifierError(n.i.lineNum(), n.i.charNum(), n.i.s);
        } else {
            n.i.accept(this);
        }
        n.e.accept(this);

    }



    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        boolean identifierFound;

        identifierFound = checkSuper(n.i.s,SymbolTable.LEAF_ENTRY);
        if (!identifierFound) {
            identifierFound = base.getCurrentScope().hasEntryWalk(n.i.s, SymbolTable.LEAF_ENTRY);
        }

        if (!identifierFound) {
            Errors.identifierError(n.i.lineNum(), n.i.charNum(), n.i.s);
        } else {
            n.i.accept(this);
        }

        n.e1.accept(this);
        n.e2.accept(this);
    }


    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        n.i.accept(this);


        for (int i = 0; i < n.el.size(); i++) {
            n.el.elementAt(i).accept(this);
        }
    }

    // String s;
    public void visit(IdentifierExp n) {
        boolean identifierFound = false;

        identifierFound = checkSuper(n.s,SymbolTable.LEAF_ENTRY);
        if (!identifierFound) {
            checkSuper( n.s,SymbolTable.LEAF_ENTRY);
        }

        // Check the keys for the identifier, if found set to true, else ascend scope
        if (base.getCurrentScope().hasEntryWalk(n.s, SymbolTable.LEAF_ENTRY) ||
            base.getCurrentScope().hasEntryWalk(n.s,SymbolTable.METHOD_ENTRY) ||
            base.getCurrentScope().hasEntryWalk(n.s,SymbolTable.CLASS_ENTRY)) {
                identifierFound = true;
        }

        if (!identifierFound) {
            Errors.identifierError(n.lineNum(), n.charNum(), n.s);
        }
    }

    // Identifier i;
    public void visit(NewObject n) {
        boolean identifierFound = false;

        // Check if keys contain class name of new object
        if (base.hasEntry(n.i.toString(), SymbolTable.CLASS_ENTRY)) {
            identifierFound = true;
            // Set flag
            swapper = base.getClassTable(n.i.toString());
        }

        if (!identifierFound) {

            Errors.identifierError(n.i.lineNum(), n.i.charNum(), n.i.s);
        }
    }


}
