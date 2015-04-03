package Visitor;

import SymTable.*;
import SyntaxTree.*;
import IR.*;

public class IRGeneratorVisitor extends DepthFirstVisitor {

	SymbolTable base;
	ArrayList<IRMethod> methods;
	IRMethod currentMethod;

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
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        base.descend(n.i1.s,SymbolTable.CLASS_ENTRY);

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

    public String visit(IntArrayType n) {
    }

    public String visit(BooleanType n) {
    }

    public String visit(IntegerType n) {
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
        Quadruple ifQuad = new Quadruple(Quadruple.CONDITIONAL_JUMP);
        ifQuad.op = "iffalse"; 
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
    public Quadruple visit(And n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public Quadruple visit(LessThan n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public Quadruple visit(Plus n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public Quadruple visit(Times n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e1,e2;
    public Quadruple visit(ArrayLookup n) {
        n.e1.accept(this);
        n.e2.accept(this);
    }

    // Exp e;
    public Quadruple visit(ArrayLength n) {
        n.e.accept(this);
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public Quadruple visit(Call n) {
        n.e.accept(this);
        n.i.accept(this);
        for (int i = 0; i < n.el.size(); i++) {
            n.el.elementAt(i).accept(this);
        }
    }

    // int i;
    public Quadruple visit(IntegerLiteral n) {
    }

    public Quadruple visit(True n) {
    }

    public Quadruple visit(False n) {
    }

    // String s;
    public Quadruple visit(IdentifierExp n) {
    }

    public Quadruple visit(This n) {
    }

    // Exp e;
    public Quadruple visit(NewArray n) {
        n.e.accept(this);
    }

    // Identifier i;
    public Quadruple visit(NewObject n) {

    }

    // Exp e;
    public Quadruple visit(Not n) {
        n.e.accept(this);
    }

    // String s;
    public Quadruple visit(Identifier n) {
    }
}
