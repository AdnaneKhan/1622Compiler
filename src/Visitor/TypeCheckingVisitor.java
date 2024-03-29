package Visitor;

import SymTable.*;
import SyntaxTree.*;

public class TypeCheckingVisitor extends TypeDepthFirstVisitor {


    SymbolTable base;

    public TypeCheckingVisitor(SymbolTable toUse) {

        // This symbol table has already been populated with appropriate values
        base = toUse;
    }

    private void classOpCheck(Type toCheck, Exp e, String operator) {
        if (toCheck instanceof IdentifierType) {
            if (e instanceof IdentifierExp) {
                String nameCheck = ((IdentifierExp) e).s;

                if (base.getCurrentScope().hasEntryWalk(nameCheck, SymbolEntry.CLASS_ENTRY) ||
                        base.getCurrentScope().hasEntryWalk(nameCheck, SymbolEntry.METHOD_ENTRY)) {
                    Errors.methClassOp(e.lineNum(), e.charNum(), operator);
                }
            }
        }
    }


    // MainClass m;
    // ClassDeclList cl;
    public Type visit(Program n) {
        n.m.accept(this);
        for (int i = 0; i < n.cl.size(); i++) {
            n.cl.elementAt(i).accept(this);
        }
        return null;
    }

    // Identifier i1,i2;
    // Statement s;
    public Type visit(MainClass n) {
        base.descendScope(n.i1.s, SymbolTable.CLASS_ENTRY);

        n.i1.accept(this);
        n.i2.accept(this);
        n.s.accept(this);

        base.ascendScope();
        return null;
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public Type visit(ClassDeclSimple n) {
        base.descendScope(n.i.s, SymbolEntry.CLASS_ENTRY);


        n.i.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {
            n.ml.elementAt(i).accept(this);
        }


        base.ascendScope();
        return null;
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public Type visit(ClassDeclExtends n) {

        base.descendScope(n.i.s, SymbolTable.CLASS_ENTRY);

        n.i.accept(this);
        n.j.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {
            n.ml.elementAt(i).accept(this);
        }
        base.ascendScope();
        return null;
    }

    // Type t;
    // Identifier i;
    public Type visit(VarDecl n) {
        n.t.accept(this);
        n.i.accept(this);
        return null;
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public Type visit(MethodDecl n) {
        base.descendScope(n.i.s, SymbolTable.METHOD_ENTRY);

        n.t.accept(this);
        n.i.accept(this);
        for (int i = 0; i < n.fl.size(); i++) {
            n.fl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.elementAt(i).accept(this);
        }
        n.e.accept(this);

        base.ascendScope();
        return n.t;
    }

    // Type t;
    // Identifier i;
    public Type visit(Formal n) {
        n.t.accept(this);
        n.i.accept(this);
        return null;
    }

    public Type visit(IntArrayType n) {
        return null;
    }

    public Type visit(BooleanType n) {
        return null;
    }

    public Type visit(IntegerType n) {
        return null;
    }

    // String s;
    public Type visit(IdentifierType n) {

        // Look up this string in the symbol table
        String idCheck = n.s;


        if (!base.hasEntry(idCheck, SymbolTable.CLASS_ENTRY) && !n.erroneous) {
            Errors.badType(n.lineNum(), n.charNum());
        }

        return n;
    }

    // StatementList sl;
    public Type visit(Block n) {
        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.elementAt(i).accept(this);
        }
        return null;
    }

    // Exp e;
    // Statement s1,s2;
    public Type visit(If n) {

        Type ret1 = n.e.accept(this);

        if (!(ret1 instanceof BooleanType)) {
            Errors.nonBoolIf(n.e.lineNum(), n.e.charNum(), "if");
        }

        n.s1.accept(this);
        n.s2.accept(this);
        return null;
    }

    // Exp e;
    // Statement s;
    public Type visit(While n) {
        Type ret1 = n.e.accept(this);

        if (!(ret1 instanceof BooleanType)) {
            Errors.nonBoolIf(n.e.lineNum(), n.e.charNum(), "while");
        }

        n.s.accept(this);
        return null;
    }

    // Exp e;
    public Type visit(Print n) {
        Type check = n.e.accept(this);

        if (!(check instanceof IntegerType)) {
            Errors.badPrint(n.e.lineNum(), n.e.charNum());
        }

        return null;
    }

    // Identifier i;
    // Exp e;
    public Type visit(Assign n) {

        Type ret = n.i.accept(this);
        Type eRet = n.e.accept(this);


        // If the lhs is a method or class
        if (ret instanceof IdentifierType && ((IdentifierType) ret).methClass) {
            Errors.classMethodAssign(n.i.lineNum(), n.i.charNum(), n.i.s, ((IdentifierType) ret).s);
        }

        // If the rhs is a method or class
        if ((eRet instanceof IdentifierType) && ((IdentifierType) eRet).methClass) {
            if (n.e instanceof IdentifierExp) {
                Errors.assignFromMethodClass(n.e.lineNum(), n.e.charNum(), ((IdentifierExp) n.e).s, ((IdentifierType) eRet).s);
            }


            // If both sides are valid identifiers but there is a type mismatch between the two
        }

        if (ret instanceof IdentifierType && eRet instanceof IdentifierType) {
            IdentifierType checkClass = (IdentifierType) eRet;

            ClassTable rhsClass = base.getClassTable(checkClass.s);
            if (rhsClass != null) {
                // Check if it is a sub class
                ClassDecl node = (ClassDecl) rhsClass.getNode();

                // if node is not an error, then we can
                // check its declared type
                if (!node.erroneous) {
                    // asume no match
                    boolean mismatch = true;

                    // Simple equality check, if they are not equal it is
                    // a type mmismatch
                    if (node.i.s.equals(((IdentifierType) ret).s)) {
                        mismatch = false;
                    }

                    if (node instanceof ClassDeclExtends) {

                        // check parent
                        Identifier superId = ((ClassDeclExtends) node).j;
                        if (superId.s.equals(((IdentifierType) ret).s)) {
                            mismatch = false;
                        }


                    }

                    if (mismatch) {
                        Errors.typeMismatch(n.i.lineNum(), n.i.charNum());
                    }
                }
            }
            // check to see if the rhs is a superclass of the lhs
        } else if (!ret.getClass().equals(eRet.getClass())) {

            Errors.typeMismatch(n.i.lineNum(), n.i.charNum());
        }

        return ret;
    }

    // Identifier i;
    // Exp e1,e2;
    public Type visit(ArrayAssign n) {
        // Look up ID in symbol table to ensure it is an integer array

        n.i.accept(this);
        SymbolEntry temp = ((SymbolEntry) base.getCurrentScope().getEntryWalk(n.i.s, TableEntry.LEAF_ENTRY));
        Type toCheck = null;
        if (temp != null) {
            toCheck = temp.getType();
        }


        // If the type is not an instancce of int array
        if (!(toCheck instanceof IntArrayType)) {
            Errors.typeMismatch(n.lineNum(), n.charNum());
        }

        Type checkLhs = n.e1.accept(this);

        Type checkRhs = n.e2.accept(this);
        if (!(checkRhs instanceof IntegerType)) {
            Errors.typeMismatch(n.e2.lineNum(), n.e2.charNum());
        }

        if (!(checkLhs instanceof IntegerType)) {
            Errors.typeMismatch(n.e1.lineNum(), n.e1.charNum());
        }
        return new IntArrayType();
    }

    // Exp e1,e2;
    public Type visit(And n) {
        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);

        // Check if we are trying to operate on method or class
        classOpCheck(ret1, n.e1, "&&");
        classOpCheck(ret2, n.e2, "&&");

        // Check if type mismatch
        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof BooleanType))) {
            Errors.nonBooleanOperand(n.lineNum(), n.charNum(), "&&");
        }

        return new BooleanType();
    }

    // Exp e1,e2;
    public Type visit(LessThan n) {
        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);

        // Check if we are trying to operate on method or class
        classOpCheck(ret1, n.e1, "<");
        classOpCheck(ret2, n.e2, "<");

        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof IntegerType))) {
            Errors.nonIntegerOperand(n.lineNum(), n.charNum(), '<');
        }

        return new BooleanType();
    }

    // Exp e1,e2;
    public Type visit(Plus n) {
        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);


        // Check if we are trying to operate on method or class
        classOpCheck(ret1, n.e1, "+");
        classOpCheck(ret2, n.e2, "+");

        if (ret1 instanceof IntArrayType || ret1 instanceof IntegerType)

        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof IntegerType))) {
        }

        return new IntegerType();
    }

    // Exp e1,e2;
    public Type visit(Minus n) {
        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);


        // Check if we are trying to operate on method or class
        classOpCheck(ret1, n.e1, "-");
        classOpCheck(ret2, n.e2, "-");


        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof IntegerType))) {
            Errors.nonIntegerOperand(n.lineNum(), n.charNum(), '-');
        }

        return new IntegerType();
    }

    // Exp e1,e2;
    public Type visit(Times n) {


        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);

        // Check if we are trying to operate on method or class
        classOpCheck(ret1, n.e1, "*");
        classOpCheck(ret2, n.e2, "*");

        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof IntegerType))) {
            Errors.nonIntegerOperand(n.lineNum(), n.charNum(), '*');
        }

        return new IntegerType();
    }

    // Exp e1,e2;
    public Type visit(ArrayLookup n) {

        n.e1.accept(this);
        n.e2.accept(this);

        return new IntegerType();
    }

    // Exp e;
    public Type visit(ArrayLength n) {

        Type ret1 = n.e.accept(this);
        if (!(ret1 instanceof IntArrayType)) {
            Errors.nonArrayLength(n.e.lineNum(), n.e.charNum());
        }
        return new IntegerType();
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public Type visit(Call n) {
        Type toReturn = null;

        Type callObject = n.e.accept(this);


        if (callObject instanceof IdentifierType && !((IdentifierType) callObject).erroneous) {
            String callClass = ((IdentifierType) callObject).s;
            ClassTable toCheck = base.getClassTable(callClass);
            ClassDecl actualNode = (ClassDecl) toCheck.getNode();
            MethodTable toLookup;

            // first try child
            toLookup = (MethodTable) toCheck.getEntry(n.i.s, TableEntry.METHOD_ENTRY);
            // Check if toCheck has a super class
            if (toLookup == null && actualNode instanceof ClassDeclExtends) {
                TableEntry superTable = base.getEntry(((ClassDeclExtends) actualNode).j.s, TableEntry.CLASS_ENTRY);
                toLookup = (MethodTable) superTable.getEntry(n.i.s, TableEntry.METHOD_ENTRY);
            }

            // If we found a method
            if (toLookup != null) {
                // Now we get the type associated with the methid
                toReturn = ((MethodDecl) toLookup.getNode()).t;
                FormalList called = ((MethodDecl) toLookup.getNode()).fl;

                // If sizes of expression lists don't match print them
                if (n.el.size() != called.size()) {
                    Errors.argCount(n.i.lineNum(), n.i.charNum(), n.i.s);
                } else {
                    // If counts are the same then we iterate comparing types
                    for (int i = 0; i < n.el.size(); i++) {

                        Type param = n.el.elementAt(i).accept(this);
                        Type formal = called.elementAt(i).t;

                        if (!param.getClass().equals(formal.getClass())) {
                            Errors.argTypeMismatch(n.i.lineNum(), n.i.charNum(), n.i.s);
                        }
                    }
                }
            } else {
                Errors.badCall(n.i.lineNum(), n.i.charNum());
            }
        } else if (!base.hasEntryWalk(n.i.s, TableEntry.METHOD_ENTRY)) {
            Errors.badCall(n.i.lineNum(), n.i.charNum());

            /// Regardless we do type checking on the param list anyway
            for (int i = 0; i < n.el.size(); i++) {
                n.el.elementAt(i).accept(this);
            }
        }

        // Don't return null
        if (toReturn == null) {
            toReturn = new IdentifierType(n.i.lineNum(), n.i.charNum());
        }

        return toReturn;
    }

    // int i;
    public Type visit(IntegerLiteral n) {
        return new IntegerType();
    }

    public Type visit(True n) {
        return new BooleanType();
    }

    public Type visit(False n) {
        return new BooleanType();
    }

    // String s;
    public Type visit(IdentifierExp n) {
        Type returnVal = null;

        TableEntry entry = base.getCurrentScope().getEntryWalk(n.s, TableEntry.LEAF_ENTRY);


        if (entry instanceof SymbolEntry) {
            returnVal = ((SymbolEntry) entry).getType();
        }

        if (returnVal == null) {
            // We don't know the type, so we lie and create fake ID
            // We use a string that can not be used as a valid type
            // as to avoid any conflicts
            IdentifierType temp = new IdentifierType(":UNKNOWN_TYPE:", n.lineNum(), n.charNum());
            temp.erroneous = true;
            returnVal = temp;
        }

        return returnVal;
    }

    public Type visit(This n) {
        IdentifierType toReturn = null;

        // get current scope's class

        // if it is class entry it means we aren't in a method and therefore calling from main.
        if (base.getCurrentScope().isEntry(TableEntry.CLASS_ENTRY)) {

            Errors.thisInMain(n.lineNum(), n.charNum());
            toReturn = new IdentifierType("this", n.lineNum(), n.charNum());
            toReturn.erroneous = true;
            // if it is a method then we know we are in the right place
        } else if (base.getCurrentScope().isEntry(TableEntry.METHOD_ENTRY)) {
            ASTNode classNode = base.getCurrentScope().parent.getNode();
            if (classNode instanceof ClassDeclSimple) {
                toReturn = new IdentifierType(((ClassDeclSimple) classNode).i.s, n.lineNum(), n.charNum());
                toReturn.methClass = true;

            } else if (classNode instanceof ClassDeclExtends) {
                toReturn = new IdentifierType(((ClassDeclExtends) classNode).i.s, n.lineNum(), n.charNum());
                toReturn.methClass = true;
            }
        } else {
            // WE SHOULD NEVER GET HERE
            System.err.println("PROBLEM IN PROGRAM!!");
            toReturn = new IdentifierType("this", n.lineNum(), n.charNum());
        }

        return toReturn;
    }

    // Exp e;
    public Type visit(NewArray n) {
        n.e.accept(this);
        return new IntArrayType();
    }

    // Identifier i;
    public Type visit(NewObject n) {
        return new IdentifierType(n.i.s, n.lineNum(), n.charNum());
    }

    // Exp e;
    public Type visit(Not n) {
        Type toCheck = n.e.accept(this);

        classOpCheck(toCheck, n.e, "!");

        if (!(toCheck instanceof BooleanType)) {
            Errors.nonBooleanOperand(n.e.lineNum(), n.e.charNum(), "!");
        }

        return new BooleanType();
    }

    // String s;
    public Type visit(Identifier n) {
        Type retV = null;

        TableEntry getlLeaf = base.getCurrentScope().getEntryWalk(n.s, TableEntry.LEAF_ENTRY);
        TableEntry getMethod = base.getCurrentScope().getEntryWalk(n.s, TableEntry.METHOD_ENTRY);
        TableEntry getClass = base.getCurrentScope().getEntryWalk(n.s, TableEntry.CLASS_ENTRY);

        if (getlLeaf != null) {
            retV = ((SymbolEntry) getlLeaf).getType();
        }

        if (getMethod != null) {
            retV = ((MethodTable) getMethod).getType();
        }

        if (getClass != null) {
            ASTNode temp = getClass.getNode();
            if (temp instanceof MainClass) {
                retV = new IdentifierType("main", n.lineNum(), n.charNum());
            } else {
                String classId = ((ClassDecl) temp).i.s;

                retV = new IdentifierType(classId, n.lineNum(), n.charNum());
            }
        }

//        if (base.getCurrentScope().hasEntryWalk(n.s, TableEntry.METHOD_ENTRY)) {
//            MethodTable typeVar = (MethodTable) base.getCurrentScope().getEntry(n.s, TableEntry.METHOD_ENTRY);
//            retV = typeVar.getType();
//        } else if (base.getCurrentScope().hasEntryWalk(n.s,TableEntry.LEAF_ENTRY)) {
//           SymbolEntry typeVar = (SymbolEntry) base.getCurrentScope().getEntryWalk(n.s,TableEntry.LEAF_ENTRY);
//           retV = typeVar.getType();
//        } else if (base.getCurrentScope().parent.isEntry(TableEntry.METHOD_ENTRY)) {
//            ClassDecl tempDecl = (ClassDecl) base.getCurrentScope().parent.getNode();
//            if (tempDecl instanceof ClassDeclExtends) {
//                String superClass = ((ClassDeclExtends) tempDecl).j.s;
//
//                ClassTable tableToUse = base.getClassTable(superClass);
//                if (tableToUse.hasEntry(n.s, TableEntry.METHOD_ENTRY)) {
//                    MethodTable typeVar = (MethodTable) tableToUse.getEntry(n.s, TableEntry.METHOD_ENTRY);
//                    retV = typeVar.getType();
//                } else if (tableToUse.hasEntry(n.s,TableEntry.LEAF_ENTRY)) {
//                    SymbolEntry typeVar = (SymbolEntry) tableToUse.getEntry(n.s,TableEntry.LEAF_ENTRY);
//                    retV = typeVar.getType();
//                }
//            } else {
//                IdentifierType temp = new IdentifierType(":UNKNOWN_TYPE:",n.lineNum(),n.charNum() );
//                temp.erroneous = true;
//                retV = temp;
//            }
//        }
//            else
//        {
//            // We don't know the type, so we lie and create fake ID
//            // We use a string that can not be used as a valid type
//            // as to avoid any conflicts
//            IdentifierType temp = new IdentifierType(":UNKNOWN_TYPE:",n.lineNum(),n.charNum() );
//            temp.erroneous = true;
//            retV = temp;
//        }

        return retV;
    }
}
