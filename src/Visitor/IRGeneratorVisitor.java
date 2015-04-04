package Visitor;

import SymTable.*;
import SyntaxTree.*;
import IR.*;
import java.util.ArrayList;

public class IRGeneratorVisitor implements Visitor {

	SymbolTable base;
	ArrayList<IRMethod> methods;
	IRMethod currentMethod;
    Quadruple currentQuad;

    int tempNum = 0;


	public IRGeneratorVisitor(SymbolTable toUse) {
		base = toUse;
		methods = new ArrayList<IRMethod>();
	}

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        n.m.accept(this);
        for (int i = 0; i < n.cl.size(); i++) {
            n.cl.elementAt(i).accept(this);
        }

        System.out.println("soup");
        for (int i = 0; i<methods.size(); i++) {
            methods.get(i).toString();
        }
        System.out.println("Howdy");

    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        base.descendScope(n.i1.s, SymbolTable.CLASS_ENTRY);

        currentMethod = new IRMethod("main"); 
        n.i1.accept(this);
        n.i2.accept(this);
        n.s.accept(this);

        methods.add(currentMethod);
        base.ascendScope();
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        base.descendScope(n.i.s,SymbolTable.CLASS_ENTRY);

        n.i.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {
            currentMethod = new IRMethod(n.ml.elementAt(i).i.toString());
            n.ml.elementAt(i).accept(this);
            methods.add(currentMethod);
        }

        base.ascendScope();
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        base.descendScope(n.i.s,SymbolTable.CLASS_ENTRY);

        n.i.accept(this);
        n.j.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {
            currentMethod = new IRMethod(n.ml.elementAt(i).i.toString());
            n.ml.elementAt(i).accept(this);
            methods.add(currentMethod);
        }

        base.ascendScope();
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        n.t.accept(this);
        n.i.accept(this);
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        base.descendScope(n.i.s,SymbolTable.METHOD_ENTRY);

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
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        n.t.accept(this);
        n.i.accept(this);
    }

    public void visit(IntArrayType n) {
    }

    public void visit(BooleanType n) {
    }

    public void visit(IntegerType n) {
    }

    // String s;
    public void visit(IdentifierType n) {
    }

    // StatementList sl;
    public void visit(Block n) {
        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.elementAt(i).accept(this);
        }
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        n.e.accept(this);
        n.s1.accept(this);
        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        n.e.accept(this);
        n.s.accept(this);
    }

    // Exp e;
    public void visit(Print n) {
        n.e.accept(this);
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        if (currentQuad.op.equals("")) {
            currentQuad.type = Quadruple.COPY;
        }
        else if (currentQuad.arg2.equals("")) {
            currentQuad.type = Quadruple.UNARY_ASSIGNMENT;
        }
        else {
            currentQuad.type = Quadruple.ASSIGNMENT;
        }
        currentQuad.result = n.i.s;
        currentMethod.add(currentQuad);
        n.i.accept(this);
        n.e.accept(this);
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.i.accept(this);
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(And n) {
        currentQuad.op = "+";
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        currentQuad.op = "<";
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        currentQuad.op = "+";
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        currentQuad.op = "-";
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        if (currentQuad.type == Quadruple.NEW_3AC) {
            currentQuad.result = "_t" + tempNum;
            currentMethod.add(currentQuad);
            currentQuad = new Quadruple();
        }
        n.i.accept(this);
        for (int i = 0; i < n.el.size(); i++) {
            n.el.elementAt(i).accept(this);
        }
    }

    // int i;
    public void visit(IntegerLiteral n) {
        if (currentQuad.arg1 != "") {
            currentQuad.arg2 = Integer.toString(n.i);
            currentQuad.result = "_t" + tempNum;
            currentMethod.add(currentQuad);
            System.out.println("ADDED!");
            currentQuad = new Quadruple();
            currentQuad.arg1 = "_t" + tempNum;
            tempNum++;
        }
        else {
            currentQuad = new Quadruple();
            currentQuad.arg1 = Integer.toString(n.i);
        }
    }

    public void visit(True n) {
    }

    public void visit(False n) {
    }

    // String s;
    public void visit(IdentifierExp n) {
        if (currentQuad.arg1 != "") {
            currentQuad.arg2 = n.s;
            currentQuad.result = "_t" + tempNum;
            currentMethod.add(currentQuad);
            System.out.println("ADDED!");
            currentQuad = new Quadruple();
            currentQuad.arg1 = "_t" + tempNum;
            tempNum++;
        }
        else {
            currentQuad = new Quadruple();
            currentQuad.arg1 = n.s;
            currentQuad.op = "new";
        }
    }

    public void visit(This n) {
    }

    // Exp e;
    public void visit(NewArray n) {
        n.e.accept(this);
    }

    // Identifier i;
    public void visit(NewObject n) {
        currentQuad = new Quadruple();
        currentQuad.type = Quadruple.NEW_3AC;
        currentQuad.arg1 = n.i.s;
    }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
    }

    // String s;
    public void visit(Identifier n) {
    }

    public void visit(ErroneousStatement n) {
    }
    public void visit(ErroneousClassDecl n) {
    }
}
