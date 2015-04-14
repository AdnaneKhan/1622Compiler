package Visitor;

import SymTable.*;
import SyntaxTree.*;
import IR.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class IRGeneratorVisitor implements Visitor {

    SymbolTable base;
    ArrayList<IRClass> classes;
    IRClass currentClass;
    IRMethod currentMethod;
    Quadruple currentQuad;

    ArrayList<Quadruple> quadstack;

    int tempNum = 0;


    private TableEntry addTemp(MethodTable methodScope, String tempVar, Type varType) {
        VarDecl tempID = new VarDecl(varType, new Identifier(tempVar,0,0),0,0);

        methodScope.putVariable(tempID);

        return methodScope.getEntry(tempVar,TableEntry.LEAF_ENTRY);
    }

    private TableEntry addTemp(ClassTable classScope, String tempVar, Type varType) {
        VarDecl tempID = new VarDecl(varType, new Identifier(tempVar,0,0),0,0);

        classScope.putVariable(tempID);

        return classScope.getEntry(tempVar,TableEntry.LEAF_ENTRY);
    }



    public List getQuadList() {
        LinkedList<Quadruple> retArray = new LinkedList<Quadruple>();

        for (IRClass iterClass : classes) {
            // Create class label

            for (IRMethod irMethod : iterClass.lines) {
               Quadruple labelQuad = new Quadruple(Quadruple.LABEL);
                labelQuad.result = irMethod.getName();
                retArray.add(labelQuad);
                for (Quadruple quad : irMethod.lines) {
                    retArray.add(quad);
                }
            }
        }

        return retArray;
    }

    public ArrayList<IRClass> getClasses() {
        return this.classes;
    }

    private TableEntry getSymbol(String theVar) {
        TableEntry toExtract = base.getCurrentScope().getEntryWalk(theVar,SymbolTable.LEAF_ENTRY);

        if (toExtract == null) {
            System.err.println("WAARNING, RETURNING NULL FROM GET SYMBOL IN IR!!");
        }

        return toExtract;
    }



    public IRGeneratorVisitor(SymbolTable toUse) {
        base = toUse;
        classes = new ArrayList<IRClass>();
        quadstack = new ArrayList<Quadruple>();
    }

    public void pushStack(Quadruple q) {
        quadstack.add(q);
    }

    public Quadruple popStack() {
        Quadruple q = quadstack.get(quadstack.size()-1);
        quadstack.remove(quadstack.size()-1);
        return q;
    }

    public int top() {
        return quadstack.size()-1;
    }

    public int getType(Statement s) {
        int x = 0;
        if (s instanceof Block) {
            x = -1;
        }
        else if (s instanceof If || s instanceof While) {
            x = Quadruple.CONDITIONAL_JUMP;
        }
        else if (s instanceof Print) {
            x = Quadruple.PRINT;
        }
        else if (s instanceof Assign) {
            x = Quadruple.COPY;
        }
        else if (s instanceof ArrayAssign) {
            x = Quadruple.INDEXED_ASSIGNMENT;
        }
        return x;
    }

    public int getType(Exp e) {
        int x = 0;
        if (e instanceof And || e instanceof LessThan || e instanceof Plus || e instanceof Minus || e instanceof Times) {
            x = Quadruple.ASSIGNMENT;
        }
        else if (e instanceof ArrayLookup) {
            x = Quadruple.INDEXED_LOOKUP;
        }
        else if (e instanceof ArrayLength) {
            x = Quadruple.LENGTH_3AC;
        }
        else if (e instanceof Call) {
            x = Quadruple.CALL;
        }
        else if (e instanceof NewObject) {
            x = Quadruple.NEW_3AC;
        }
        else if (e instanceof NewArray) {
            x = Quadruple.NEW_ARRAY;
        }
        else if (e instanceof Not) {
            x = Quadruple.UNARY_ASSIGNMENT;
        }
        else if (e instanceof IntegerLiteral || e instanceof IdentifierExp || e instanceof This) {
            x = -2;
        }
        return x;
    }


    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        currentClass = new IRClass(n.m.i1.s);
        n.m.accept(this);
        classes.add(currentClass);
        for (int i = 0; i < n.cl.size(); i++) {
            n.cl.elementAt(i).accept(this);
        }
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        base.descendScope(n.i1.s, SymbolTable.CLASS_ENTRY);

        currentMethod = new IRMethod("main");
        n.i1.accept(this);
        n.i2.accept(this);
        currentQuad = new Quadruple();
        currentQuad.type = getType(n.s);
        pushStack(currentQuad);
        n.s.accept(this);
        Quadruple tempQuad = popStack();
        if (tempQuad.type != Quadruple.CONDITIONAL_JUMP) {
            currentMethod.add(tempQuad);
        }

        currentClass.add(currentMethod);
        base.ascendScope();
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        currentClass = new IRClass(n.i.s);

        base.descendScope(n.i.s, SymbolTable.CLASS_ENTRY);

        n.i.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {
            String methodLabel = n.i.s + "_" + n.ml.elementAt(i).i.toString();


            currentMethod = new IRMethod(methodLabel,
                    (MethodTable)base.getCurrentScope().getEntry(n.ml.elementAt(i).i.s,TableEntry.METHOD_ENTRY));
            n.ml.elementAt(i).accept(this);
            currentClass.add(currentMethod);
        }

        base.ascendScope();

        classes.add(currentClass);
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        currentClass = new IRClass(n.i.s, n.j.s);

        base.descendScope(n.i.s, SymbolTable.CLASS_ENTRY);

        n.i.accept(this);
        n.j.accept(this);
        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.elementAt(i).accept(this);
        }
        for (int i = 0; i < n.ml.size(); i++) {

            String methodLabel = n.i.s + "_" + n.ml.elementAt(i).i.toString();
            currentMethod =new IRMethod(methodLabel,
                    (MethodTable)base.getCurrentScope().getEntry(n.ml.elementAt(i).i.s,TableEntry.METHOD_ENTRY));
            n.ml.elementAt(i).accept(this);
            currentClass.add(currentMethod);
        }

        base.ascendScope();

        classes.add(currentClass);
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
            currentQuad = new Quadruple();
            currentQuad.type = getType(n.sl.elementAt(i));
            pushStack(currentQuad);
            n.sl.elementAt(i).accept(this);
            Quadruple tempQuad = popStack();
            if (tempQuad.type != Quadruple.CONDITIONAL_JUMP) {
                currentMethod.add(tempQuad);
            }
        }


        currentQuad = new Quadruple();

        // First check if the expression
        int tempType = getType(n.e);
        if (tempType != -2) {
            currentQuad.type = tempType;
            pushStack(currentQuad);
            n.e.accept(this);
            Quadruple tempQuad = popStack();

            // now mmake a return quad
            Quadruple retQuad = new Quadruple();
            retQuad.transferResult(tempQuad);
            retQuad.type = Quadruple.RETURN_3AC;
            currentMethod.add(retQuad);
        } else {
            pushStack(currentQuad);
            n.e.accept(this);
            currentMethod.add(popStack());
            currentQuad.type = Quadruple.RETURN_3AC;

        }





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
            currentQuad = new Quadruple();
            currentQuad.type = getType(n.sl.elementAt(i));
            pushStack(currentQuad);
            n.sl.elementAt(i).accept(this);
            Quadruple tempQuad = popStack();
            if (tempQuad.type != Quadruple.CONDITIONAL_JUMP) {
                currentMethod.add(tempQuad);
            }
        }
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        String tempLabel = "_t" + tempNum;
        tempNum++;
        String tempLabel2 = "_t" + tempNum;
        tempNum++;

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e);
        pushStack(currentQuad);
        n.e.accept(this);
        Quadruple tempQuad = popStack();

        // Set up if, add if
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);



        currentQuad.setArg2(tempLabel);

        currentQuad.result = "iffalse";
        currentQuad = quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);

        // Add statement 1
        currentQuad = new Quadruple();
        currentQuad.type = getType(n.s1);
        pushStack(currentQuad);
        n.s1.accept(this);
        tempQuad = popStack();
        if (tempQuad.type != Quadruple.CONDITIONAL_JUMP) {
            currentMethod.add(tempQuad);
        }

        // Add unconditional jump
        currentQuad = new Quadruple();
        currentQuad.type = Quadruple.UNCONDITIONAL_JUMP;
        currentQuad.result = tempLabel2;
        currentMethod.add(currentQuad);

        // Add label for first jump
        currentQuad = new Quadruple();
        currentQuad.type = Quadruple.LABEL;
        currentQuad.result = tempLabel;
        currentMethod.add(currentQuad);

        //Add statement 2
        currentQuad = new Quadruple();
        currentQuad.type = getType(n.s2);
        pushStack(currentQuad);
        n.s2.accept(this);
        tempQuad = popStack();
        if (tempQuad.type != Quadruple.CONDITIONAL_JUMP) {
            currentMethod.add(tempQuad);
        }

        //Add in last label for unconditional jump
        currentQuad = new Quadruple();
        currentQuad.type = Quadruple.LABEL;
        currentQuad.result = tempLabel2;
        currentMethod.add(currentQuad);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        String tempLabel = "_t" + tempNum;
        tempNum++;
        String tempLabel2 = "_t" + tempNum;
        tempNum++;

        Quadruple tempQuad2 = new Quadruple();
        tempQuad2.type = Quadruple.LABEL;
        tempQuad2.result = tempLabel;
        currentMethod.add(tempQuad2);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e);
        pushStack(currentQuad);
        n.e.accept(this);
        Quadruple tempQuad = popStack();


        // Set up while, add while

        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);
        currentQuad.setArg2(tempLabel2);
        currentQuad.result = "iffalse";
        currentQuad = quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);

        // Add statement
        currentQuad = new Quadruple();
        currentQuad.type = getType(n.s);
        pushStack(currentQuad);
        n.s.accept(this);
        tempQuad = popStack();
        if (tempQuad.type != Quadruple.CONDITIONAL_JUMP) {
            currentMethod.add(tempQuad);
        }

        // Add unconditional jump
        currentQuad = new Quadruple();
        currentQuad.type = Quadruple.UNCONDITIONAL_JUMP;
        currentQuad.result = tempLabel;
        currentMethod.add(currentQuad);

        // Add label for first jump
        currentQuad = new Quadruple();
        currentQuad.type = Quadruple.LABEL;
        currentQuad.result = tempLabel2;
        currentMethod.add(currentQuad);
    }

    // Exp e;
    public void visit(Print n) {
        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e);

        currentQuad.setArg1("_system_out_print");
      //  currentQuad.arg1 = "_system_out_print";
        pushStack(currentQuad);
        n.e.accept(this);
        currentQuad = popStack();

        Quadruple tempCurr = new Quadruple(Quadruple.PARAMETER);
        tempCurr.transferResult(currentQuad);

        currentQuad = tempCurr;
        currentMethod.add(currentQuad);
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        currentQuad = quadstack.get(top());


        currentQuad.setResEntry(getSymbol(n.i.s));

        quadstack.set(top(), currentQuad);

        n.i.accept(this);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e);
        pushStack(currentQuad);
        n.e.accept(this);

        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);
        quadstack.set(top(), currentQuad);
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        currentQuad = quadstack.get(top());

        quadstack.set(top(), currentQuad);

        n.i.accept(this);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e1);
        pushStack(currentQuad);
        n.e1.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg2(tempQuad);
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e2);
        pushStack(currentQuad);
        n.e2.accept(this);
        tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);
        quadstack.set(top(), currentQuad);
    }

    // Exp e1,e2;
    public void visit(And n) {
        currentQuad = quadstack.get(top());
        currentQuad.op = "&&";
        currentQuad.result = "_t" + tempNum;
        this.addTemp((MethodTable) base.getCurrentScope(),currentQuad.result,new BooleanType());

        tempNum++;
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e1);
        pushStack(currentQuad);
        n.e1.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);
    //    currentQuad.arg1 = tempQuad.getResult();
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e2);
        pushStack(currentQuad);
        n.e2.accept(this);
        tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg2(tempQuad);
       // currentQuad.arg2 = tempQuad.getResult();
        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        currentQuad = quadstack.get(top());
        currentQuad.op = "<";
        currentQuad.result = "_t" + tempNum;

        currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(), currentQuad.result, new BooleanType()));

        tempNum++;
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e1);
        pushStack(currentQuad);
        n.e1.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);


        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e2);
        pushStack(currentQuad);
        n.e2.accept(this);
        tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg2(tempQuad);

        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        currentQuad = quadstack.get(top());
        currentQuad.op = "+";
        currentQuad.result = "_t" + tempNum;

        currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(), currentQuad.result, new IntegerType()));

        tempNum++;
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e1);
        pushStack(currentQuad);
        n.e1.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);

        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e2);
        pushStack(currentQuad);
        n.e2.accept(this);
        tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg2(tempQuad);

        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        currentQuad = quadstack.get(top());
        currentQuad.op = "-";
        currentQuad.result = "_t" + tempNum;

        currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(), currentQuad.result,new IntegerType()));
        tempNum++;
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e1);
        pushStack(currentQuad);
        n.e1.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);

        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e2);
        pushStack(currentQuad);
        n.e2.accept(this);
        tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg2(tempQuad);

        quadstack.set(top(), currentQuad);

        currentMethod.add(currentQuad);
    }

    // Exp e1,e2;
    public void visit(Times n) {
        currentQuad = quadstack.get(top());
        currentQuad.op = "*";
        currentQuad.result = "_t" + tempNum;

        currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(), currentQuad.result,new IntegerType()));

        tempNum++;
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e1);
        pushStack(currentQuad);
        n.e1.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);

        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e2);
        pushStack(currentQuad);
        n.e2.accept(this);
        tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg2(tempQuad);

        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        currentQuad = quadstack.get(top());
        currentQuad.result = "_t" + tempNum;

        currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(), currentQuad.result,new IntegerType()));

        tempNum++;
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e1);
        pushStack(currentQuad);
        n.e1.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());


        // Put res of temmQuad into arg2, preserving type
        currentQuad.resToArg1(tempQuad);
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e2);
        pushStack(currentQuad);
        n.e2.accept(this);
        tempQuad = popStack();
        currentQuad = quadstack.get(top());


        // Put the result of tempQuad into arg2, preserving type
        currentQuad.resToArg2(tempQuad);

        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // Exp e;
    public void visit(ArrayLength n) {
        currentQuad = quadstack.get(top());
        currentQuad.result = "_t" + tempNum;

        currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(), currentQuad.result,new IntegerType()));

        tempNum++;
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e);
        pushStack(currentQuad);
        n.e.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());


        currentQuad.resToArg1(tempQuad);

        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        ArrayList<Quadruple> params = new ArrayList<Quadruple>();

        currentQuad = quadstack.get(top());
        currentQuad.result = "_t" + tempNum;
        tempNum++;


        String toAdd = "";
        if (n.e instanceof NewObject) {


            String lookup = ((NewObject) n.e).i.s;
            toAdd = lookup;

        } else if (n.e instanceof IdentifierExp) {
            String lookup = ((IdentifierExp)n.e).s;
            SymbolEntry temp = (SymbolEntry) base.getCurrentScope().getEntryWalk(lookup,TableEntry.LEAF_ENTRY);
            String classParam;


            /// Get the name of the current class to act as the this paramemter
            if (temp.parent.isEntry(TableEntry.CLASS_ENTRY)) {
                classParam = temp.parent.getSymbolName();
            } else if(temp.parent.isEntry(TableEntry.METHOD_ENTRY)) {
                classParam = temp.parent.parent.getSymbolName();
            }

        } else if (n.e instanceof This) {
                if(base.getCurrentScope().isEntry(TableEntry.METHOD_ENTRY)) {
                    toAdd = base.getCurrentScope().parent.getSymbolName();
                }
            }

        currentQuad.setArg1(base.getClassTable(toAdd).getMethod(n.i.s));
        if (base.getCurrentScope().isEntry(TableEntry.METHOD_ENTRY)) {
            currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(), currentQuad.result, base.getClassTable(toAdd).getMethod(n.i.s).getType()));
        } else {
            currentQuad.setResEntry(this.addTemp((ClassTable) base.getCurrentScope(), currentQuad.result, base.getClassTable(toAdd).getMethod(n.i.s).getType()));
        }

        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e);
        pushStack(currentQuad);
        n.e.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = new Quadruple();
        currentQuad.type = Quadruple.PARAMETER;
        currentQuad.transferResult(tempQuad);
        params.add(currentQuad);

        n.i.accept(this);

        for (int i = 0; i < n.el.size(); i++) {
            currentQuad = new Quadruple();
            currentQuad.type = getType(n.el.elementAt(i));
            pushStack(currentQuad);
            n.el.elementAt(i).accept(this);
            tempQuad = popStack();
            currentQuad = new Quadruple();
            currentQuad.type = Quadruple.PARAMETER;
            currentQuad.transferResult(tempQuad);
            params.add(currentQuad);
        }

        for (int i = 0; i < params.size(); i++) {
            currentMethod.add(params.get(i));
        }

        currentQuad = quadstack.get(top());


        currentQuad.setArg2(n.el.size() +1);

        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }


    // int i;
    public void visit(IntegerLiteral n) {
        currentQuad = quadstack.get(top());
        currentQuad.setIntResult(n.i);
        quadstack.set(top(), currentQuad);
    }

    public void visit(True n) {
        currentQuad = quadstack.get(top());
        currentQuad.result = "$TRUE";
        currentQuad.setIntResult(1);
        currentQuad.setIsBoolean();
        quadstack.set(top(), currentQuad);
    }

    public void visit(False n) {
        currentQuad = quadstack.get(top());
        currentQuad.result = "$FALSE";
        currentQuad.setIntResult(0);
        currentQuad.setIsBoolean();
        quadstack.set(top(), currentQuad);
    }

    // String s;
    public void visit(IdentifierExp n) {
        currentQuad = quadstack.get(top());

        TableEntry lookupRes = base.getCurrentScope().getEntryWalk(n.s,TableEntry.LEAF_ENTRY);
        if (lookupRes == null) {

        lookupRes = base.getCurrentScope().getEntryWalk(n.s,TableEntry.CLASS_ENTRY);
        }
        if (lookupRes == null) {
        lookupRes = base.getCurrentScope().getEntryWalk(n.s,TableEntry.METHOD_ENTRY);
        }

        currentQuad.setResEntry(lookupRes);

        quadstack.set(top(), currentQuad);
    }

    public void visit(This n) {
        currentQuad = quadstack.get(top());
        currentQuad.setResEntry(base.getCurrentScope().getEntry("this",TableEntry.LEAF_ENTRY));
        quadstack.set(top(), currentQuad);
    }

    // Exp e;
    public void visit(NewArray n) {
        currentQuad = quadstack.get(top());
        currentQuad.result = "_t" + tempNum;
        currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(),currentQuad.result,new IntArrayType()));

        tempNum++;
        currentQuad.op = "new int";
        quadstack.set(top(), currentQuad);



        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e);
        pushStack(currentQuad);
        n.e.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());

        currentQuad.resToArg1(tempQuad);
       // currentQuad.arg1 = tempQuad.getResult();
        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // Identifier i;
    public void visit(NewObject n) {
        currentQuad = quadstack.get(top());
        currentQuad.result = "_t" + tempNum;

        tempNum++;
        currentQuad.op = "new";

        // Get the actual object
        ClassTable temp = (ClassTable) base.getCurrentScope().getEntryWalk(n.i.s, TableEntry.CLASS_ENTRY);
        currentQuad.setArg1(n.i.s);
        currentQuad.setArg1(temp);

        if (base.getCurrentScope().isEntry(TableEntry.METHOD_ENTRY)) {
            currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(),currentQuad.result,temp.getClassType()));
        } else {
            currentQuad.setResEntry(this.addTemp((ClassTable) base.getCurrentScope(), currentQuad.result,temp.getClassType()));
        }

        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // Exp e;
    public void visit(Not n) {
        currentQuad = quadstack.get(top());


        currentQuad.result = "_t" + tempNum;
        currentQuad.setResEntry(this.addTemp((MethodTable) base.getCurrentScope(), currentQuad.result,new BooleanType()));

        tempNum++;
        currentQuad.op = "!";
        quadstack.set(top(), currentQuad);

        currentQuad = new Quadruple();
        currentQuad.type = getType(n.e);
        pushStack(currentQuad);
        n.e.accept(this);
        Quadruple tempQuad = popStack();
        currentQuad = quadstack.get(top());
        currentQuad.resToArg1(tempQuad);
        quadstack.set(top(), currentQuad);
        currentMethod.add(currentQuad);
    }

    // String s;
    public void visit(Identifier n) {
    }

    public void visit(ErroneousStatement n) {
    }
    public void visit(ErroneousClassDecl n) {
    }
    public void  visit(ErroneousMethodDecl n) {

    }
}
