package Visitor;

import SymTable.KeyWrapper;
import SymTable.SymbolEntry;
import SymTable.SymbolTable;
import SymTable.TableEntry;
import SyntaxTree.*;

public class TypeCheckingVisitor extends TypeDepthFirstVisitor {

    SymbolTable base;
    public TypeCheckingVisitor(SymbolTable toUse) {

        // This symbol table has already been populated with appropriate values
        base = toUse;
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
        } else {
            className = ((ClassDeclExtends) toExtract).i.toString();
        }
        return className;
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
        base.descendScope(new KeyWrapper(n.i1.s,SymbolTable.CLASS_ENTRY));

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
        n.i.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {
            n.ml.elementAt(i).accept(this);
        }
        return null;
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public Type visit(ClassDeclExtends n) {
        n.i.accept(this);
        n.j.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {
            n.ml.elementAt(i).accept(this);
        }
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
        return null;
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

        if (!base.hasEntry(idCheck,SymbolTable.CLASS_ENTRY)){
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

        if (! (ret1 instanceof BooleanType)) {
            Errors.nonBoolIf(n.e.lineNum(),n.e.charNum(),"if");
        }

        n.s1.accept(this);
        n.s2.accept(this);
        return null;
    }

    // Exp e;
    // Statement s;
    public Type visit(While n) {
        Type ret1 = n.e.accept(this);

        if (! (ret1 instanceof BooleanType)) {
            Errors.nonBoolIf(n.e.lineNum(),n.e.charNum(),"while");
        }

        n.s.accept(this);
        return null;
    }

    // Exp e;
    public Type visit(Print n) {
        n.e.accept(this);
        return null;
    }

    // Identifier i;
    // Exp e;
    public Type visit(Assign n) {


        Type ret = n.i.accept(this);

        // Look up variable in scope

        // Find its type (by accessing the symmbol entry and reading type)



        Type eRet = n.e.accept(this);

        if ( !ret.getClass().equals(eRet.getClass())){
              Errors.typeMismatch(n.i.lineNum(),n.i.charNum());
        }


        return null;
    }

    // Identifier i;
    // Exp e1,e2;
    public Type visit(ArrayAssign n) {


        // Look up ID in symbol table to ensure it is an integer array

        n.i.accept(this);


        Type checkLhs = n.e1.accept(this);

        Type checkRhs = n.e2.accept(this);
        if (!(checkRhs instanceof IntegerType)) {
            Errors.typeMismatch(n.e2.lineNum(),n.e2.charNum());
        }
        return null;
    }

    // Exp e1,e2;
    public Type visit(And n) {
        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);

        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof BooleanType))) {
            Errors.nonBooleanOperand(n.lineNum(), n.charNum(), "&&");
        } else {
            return new BooleanType();
        }

        return null;
    }

    // Exp e1,e2;
    public Type visit(LessThan n) {
        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);

        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof IntegerType))) {
            Errors.nonIntegerOperand(n.lineNum(),n.charNum(),'<');
        } else {
            return new BooleanType();
        }

        return null;
    }

    // Exp e1,e2;
    public Type visit(Plus n) {
        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);

        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof IntegerType))) {
            Errors.nonIntegerOperand(n.lineNum(),n.charNum(),'+');
        } else {
            return new IntegerType();
        }

        return null;
    }

    // Exp e1,e2;
    public Type visit(Minus n) {
        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);

        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof IntegerType))) {
            Errors.nonIntegerOperand(n.lineNum(),n.charNum(),'-');
        } else {
            return new IntegerType();
        }

        return null;
    }

    // Exp e1,e2;
    public Type visit(Times n) {


        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);

        if (!(ret1.getClass().equals(ret2.getClass()) && (ret1 instanceof IntegerType))) {
            Errors.nonIntegerOperand(n.lineNum(),n.charNum(),'*');
        }

        return new IntegerType();
    }

    // Exp e1,e2;
    public Type visit(ArrayLookup n) {


        Type ret1 = n.e1.accept(this);
        Type ret2 = n.e2.accept(this);


        return new IntegerType();
    }

    // Exp e;
    public Type visit(ArrayLength n) {

        Type ret1 = n.e.accept(this);
        if (!(ret1 instanceof IntArrayType)) {
            Errors.nonArrayLength(n.e.lineNum(),n.e.charNum());
        }
        return new IntegerType();
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public Type visit(Call n) {
        n.e.accept(this);
        n.i.accept(this);
        for (int i = 0; i < n.el.size(); i++) {
            n.el.elementAt(i).accept(this);
        }
        return null;
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


        if (base.getCurrentScope().hasEntry(n.s, TableEntry.LEAF_ENTRY)) {
            TableEntry entry = base.getEntry(n.s, TableEntry.LEAF_ENTRY);
            if (entry instanceof SymbolEntry) {
                return ((SymbolEntry)entry).getType();
            }
        }
        return null;
    }

    public Type visit(This n) {
        return null;
    }

    // Exp e;
    public Type visit(NewArray n) {
        n.e.accept(this);
        return new IntArrayType();
    }

    // Identifier i;
    public Type visit(NewObject n) {
        return null;
    }

    // Exp e;
    public Type visit(Not n) {
        n.e.accept(this);
        return null;
    }

    // String s;
    public Type visit(Identifier n) {
        return null;
    }


}
