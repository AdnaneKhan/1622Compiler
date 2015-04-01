package Visitor;

import SymTable.ClassTable;
import SymTable.MethodTable;
import SymTable.SymbolEntry;
import SymTable.SymbolTable;
import SyntaxTree.*;

public class NameAnalysisVisitor implements Visitor {

  SymbolTable base;

  public NameAnalysisVisitor(SymbolTable toPopulate, Program toUse) {
    base = toPopulate;

    // Initiate visitation process
    this.visit(toUse);
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    // Add the main class to symbol
    base.putClass(n.m);
    // go to the main class scope

    n.m.accept(this);

    // Checks that classes do not have the same name while adding them to symbol table
    for ( int i = 0; i < n.cl.size(); i++ ) {
      if (!(base.keys().contains(n.cl.elementAt(i).i.toString()))) {
        base.putClass(n.cl.elementAt(i));
      }
      else {
        System.out.println("Multiply defined class name at line " + n.cl.elementAt(i).lineNum() + ", character " + n.cl.elementAt(i).charNum());
      }
    }

    // Checks that super classes exist where 'extends' called
    for (int i = 0; i < n.cl.size(); i++) {
      if (n.cl.elementAt(i) instanceof ClassDeclExtends) {
        if (!(base.keys().contains(n.cl.elementAt(i).j.toString()))) {
          System.out.println("Use of undefined class identifier at line " + n.cl.elementAt(i).lineNum() + ", character " + n.cl.elementAt(i).charNum());
        }
        else {
          n.cl.elementAt(i).accept(this);
        }
      }
      else {
        n.cl.elementAt(i).accept(this);
      }
    }
  }
  
  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    base.descendScope(n.i1.toString());
    // We are in the main class scope now
    n.i1.accept(this);

    // Now we need to descend into the main method scope
    // Add this id to the main static method variable list
    SymbolEntry stringID = (new SymbolEntry(n.i2.toString(),n.i2));
    stringID.parent = base.getCurrentScope();
    base.currentScopeMap().put(n.i2.toString(), stringID ); // note we need to set parent
    n.i2.accept(this);
    n.s.accept(this);

    base.ascendScope();
  }
  
  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    base.descendScope(n.i.toString());

    n.i.accept(this);

    // Check for duplicate variable names in class
    for ( int i = 0; i < n.vl.size(); i++ ) {
      if (base.keys().contains(n.vl.elementAt(i).i.toString())) {
        if (base.get(n.vl.elementAt(i).i.toString()).entryType() == 3) {
          System.out.println("Multiply defined variable name at line " + n.vl.elementAt(i).lineNum() + ", character " + n.vl.elementAt(i).charNum());
        }
      }
      else {
        base.putVariable(n.vl.elementAt(i));
        n.vl.elementAt(i).accept(this);      
      }
    }

    // Check for duplicate method names in class
    for ( int i = 0; i < n.ml.size(); i++ ) {
      if (base.keys().contains(n.ml.elementAt(i).toString())) {
        if (base.get(n.ml.elementAt(i).i.toString()).entryType == 1) {
          System.out.println("Multiply defined method name at line " + n.ml.elementAt(i).lineNum() + ", character " + n.ml.elementAt(i).charNum());
        }
      }
      else {
        base.putMethod(n.ml.elementAt(i));
        n.ml.elementAt(i).accept(this);
      }
    }

    base.ascendScope();
  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    base.descendScope(n.i.toString());

    n.i.accept(this);
    n.j.accept(this);

    // These are class variables. What we need to check here is that
    // there are no multiple definitions in this same list
    // it is ok if there are variables that belong to the parent class, (since it
    // would be an override)

    // we need to pull the current scope
    ClassTable cs = (ClassTable) base.getCurrentScope();

    for ( int i = 0; i < n.vl.size(); i++ ) {

        // If this variable exists in the LOCAL scope of the class and the variable binds to a var (not
        // a method) (use the entryType getter to check the static finals)

        // ok if the var is not defined (or it is a method) then we can add it to this scope
        // this process is repaeted
        base.putVariable(n.vl.elementAt(i));
        n.vl.elementAt(i).accept(this);
    }

    // Check for duplicate method names in class
    for ( int i = 0; i < n.ml.size(); i++ ) {
        base.putMethod(n.ml.elementAt(i));
        n.ml.elementAt(i).accept(this);
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
    base.descendScope(n.i.toString());
    
    n.t.accept(this);
    n.i.accept(this);

    // Checks formal list for duplicate variable names
    for ( int i = 0; i < n.fl.size(); i++ ) {
        if (base.keys().contains(n.fl.elementAt(i).toString())) {
          if (base.get(n.fl.elemnetAt(i).toString()) == 3) {
            System.out.println("Multiply defined variable name at line " + n.fl.elementAt(i).lineNum() + ", character " + n.fl.elementAt(i).charNum());
          }
        }
        else {
          base.putVariable(n.fl.elementAt(i));
          n.fl.elementAt(i).accept(this);
        }
    }

    // Checks variable decls for duplicate names
    for ( int i = 0; i < n.vl.size(); i++ ) {
        if (base.keys().contains(n.vl.elementAt(i).toString())) {
          if (base.get(n.vl.elemnetAt(i).toString()) == 3) {
            System.out.println("Multiply defined variable name at line " + n.vl.elementAt(i).lineNum() + ", character " + n.vl.elementAt(i).charNum());
          }
        }
        else {
          base.putVariable(n.vl.elementAt(i));
          n.vl.elementAt(i).accept(this);
        }
    } 
    for ( int i = 0; i < n.sl.size(); i++ ) {
        n.sl.elementAt(i).accept(this);
    }
    n.e.accept(this);
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
    SymbolTable savedState = base;
    boolean identifierFound = false;

    // While the parent isn't null and the identifier is not found
    while (base.parent != NULL && identifierFound == false) {

      // Check the keys for the identifier, if found set to true, else ascend scope
      if (base.keys().contains(n.s)) {
        if (base.get(n.s).entryType() == 2) {
          identifierFound = true;
        }
      }
      else {
        base.ascendScope();
      }
    }

    // Return base to original state
    base = savedState;

    if (identifierFound == false) {
      System.out.println("Use of undefined variable identifier at line " + n.lineNum() + ", character " + n.charNum());
    }
  }

  // StatementList sl;
  public void visit(Block n) {
    for ( int i = 0; i < n.sl.size(); i++ ) {
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
    SymbolTable savedState = base;
    boolean identifierFound = false;

    // While the parent isn't null and the identifier is not found
    while (base.parent != NULL && identifierFound == false) {

      // Check the keys for the identifier, if found set to true, else ascend scope
      if (base.keys().contains(n.i.toString())) {
        if (base.get(n.i.toString()).entryType() == 3) {
          identifierFound = true;
        }
      }
      else {
        base.ascendScope();
      }
    }

    // Return base to original state
    base = savedState;

    if (identifierFound == false) {
      System.out.println("Use of undefined variable identifier at line " + n.i.lineNum() + ", character " + n.i.charNum());
    }
    else {
      n.i.accept(this);
    }
    n.e.accept(this);
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    SymbolTable savedState = base;
    boolean identifierFound = false;

    // While the parent isn't null and the identifier is not found
    while (base.parent != NULL && identifierFound == false) {

      // Check the keys for the identifier, if found set to true, else ascend scope
      if (base.keys().contains(n.i.toString())) {
        if (base.get(n.i.toString()).entryType() == 3) {
          identifierFound = true;
        }
      }
      else {
        base.ascendScope();
      }
    }

    // Return base to original state
    base = savedState;

    if (identifierFound == false) {
      System.out.println("Use of undefined variable identifier at line " + n.i.lineNum() + ", character " + n.i.charNum());
    }
    else {
      n.i.accept(this);
    }
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(And n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Minus n) {
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
    SymbolTable savedState = base;
    boolean identifierFound = false;

    // While the parent isn't null and the identifier is not found
    while (base.parent != NULL && identifierFound == false) {

      // Check the keys for the identifier, if found set to true, else ascend scope
      if (base.keys().contains(n.i.toString())) {
        if (base.get(n.i.toString()).entryType() == 1) {
          identifierFound = true;
        }
      }
      else {
        base.ascendScope();
      }
    }

    base = savedState;

    if (identifierFound == false) {
      System.out.println("Use of undefined variable identifier at line " + n.i.lineNum() + ", character " + n.i.charNum());
    }
    else {
      n.i.accept(this);
    }

    for ( int i = 0; i < n.el.size(); i++ ) {
      n.el.elementAt(i).accept(this);
    }
  }

  // int i;
  public void visit(IntegerLiteral n) {
  }

  public void visit(True n) {
  }

  public void visit(False n) {
  }

  // String s;
  public void visit(IdentifierExp n) {
    SymbolTable savedState = base;
    boolean identifierFound = false;

    // While the parent isn't null and the identifier is not found
    while (base.parent != NULL && identifierFound == false) {

      // Check the keys for the identifier, if found set to true, else ascend scope
      if (base.keys().contains(n.s)) {
        if (base.get(n.s).entryType() == 3) {
          identiferFound = true;
        }
      }
      else {
        base.ascendScope();
      }
    }

    // Return base to original state
    base = savedState;

    if (identifierFound == false) {
      System.out.println("Use of undefined variable identifier at line " + n.lineNum() + ", character " + n.charNum());
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
    SymbolTable savedState = base;
    boolean identifierFound = false;

    // While the parent is not null (used to get to class level)
    while (base.parent != NULL) {
      base.ascendScope();
    }

    // Check if keys contain class name of new object
    if (base.keys().contains(n.i.toString())) {
      if (base.get(n.i.toString()).entryType() == 2) {
        identifierFound = true;
      }
    }

    // Return base to original state
    base = savedState;

    if (identifierFound == false) {
      System.out.println("Use of undefined variable identifier at line " + n.lineNum() + ", character " + n.charNum());
    }
  }

  // Exp e;
  public void visit(Not n) {
    n.e.accept(this);
  }

  // String s;
  public void visit(Identifier n) {
  }
}
