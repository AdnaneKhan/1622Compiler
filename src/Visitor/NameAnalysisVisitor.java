package Visitor;

import SymTable.*;
import SyntaxTree.*;

public class NameAnalysisVisitor implements Visitor {

  SymbolTable base;

    TableEntry swapper = null;


  public NameAnalysisVisitor(SymbolTable toPopulate) {
        base = toPopulate;
  }

    /**
     * Simple utility memthods checks the actual object and gets teh idenifier ffrom it
     * @param toExtract abstract class declaration
     * @return name of class
     */
  private String extractClassName (ClassDecl toExtract) {

      String className;
      if (toExtract instanceof  ClassDeclSimple) {
          className = ((ClassDeclSimple) toExtract).i.toString();
      } else {
          className =  ((ClassDeclExtends) toExtract).i.toString();
      }
    return className;
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {




    // Checks that classes do not have the same name while adding them to symbol table
    for ( int i = 0; i < n.cl.size(); i++ ) {
        ClassDecl cursor = n.cl.elementAt(i);
        String className = extractClassName(cursor);

      if (!(base.hasEntry( className , SymbolTable.CLASS_ENTRY))) {
        base.putClass(cursor);
      }
      else {
        System.out.println("Multiply defined class name at line " + cursor.lineNum() + ", character " + cursor.charNum());
      }
    }

    // Checks that super classes exist where 'extends' called
    for (int i = 0; i < n.cl.size(); i++) {

        ClassDecl cursor = n.cl.elementAt(i);

      if (cursor instanceof ClassDeclExtends) {
          String extendsName = ((ClassDeclExtends) cursor).j.toString();
            if (!(base.hasEntry(extendsName,SymbolTable.CLASS_ENTRY))) {
          System.out.println("Use of undefined class identifier at line " + cursor.lineNum() + ", character " + cursor.charNum());
            }

        else {
                cursor.accept(this);
        }
      }
      else {
          cursor.accept(this);
      }
    }


      // go to the main class scope
      // Add the main class to symbol
      base.putClass(n.m);

      n.m.accept(this);


  }
  
  // Identifier i1,i2;
  // Statement s;
  public void visit(MainClass n) {
    base.descendScope(n.i1.toString());
    // We are in the main class scope now
      if (base.getCurrentScope().isEntry(SymbolTable.CLASS_ENTRY)) {
          ClassTable current = (ClassTable) base.getCurrentScope();

          n.i1.accept(this);

          // Now we need to descend into the main method scope
          // Add this id to the main static method variable list
          SymbolEntry stringID = new SymbolEntry(n.i2.toString(), n.i2);
          stringID.parent = base.getCurrentScope();
          current.put(n.i2.toString(), stringID); // note we need to set parent
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
    base.descendScope(n.i.toString());

      ClassTable current;
      if(base.getCurrentScope().isEntry(SymbolEntry.CLASS_ENTRY) ) {
          current = (ClassTable) base.getCurrentScope();

          n.i.accept(this);

          // Check for duplicate variable names in class
          for (int i = 0; i < n.vl.size(); i++) {
              if (current.hasEntry(n.vl.elementAt(i).i.toString(), SymbolTable.LEAF_ENTRY)) {

                  System.out.println("Multiply defined variable name at line " + n.vl.elementAt(i).lineNum() + ", character " + n.vl.elementAt(i).charNum());

              } else {
                  if (base.getCurrentScope().isEntry(SymbolEntry.CLASS_ENTRY)) {
                      current.putVariable(n.vl.elementAt(i));
                  }
                  n.vl.elementAt(i).accept(this);
              }
          }

          // Check for duplicate method names in class
          for (int i = 0; i < n.ml.size(); i++) {
                  if (current.hasEntry(n.ml.elementAt(i).i.toString(),SymbolTable.METHOD_ENTRY)) {
                      System.out.println("Multiply defined method name at line " + n.ml.elementAt(i).lineNum() + ", character " + n.ml.elementAt(i).charNum());
                  } else {
                      current.putMethod(n.ml.elementAt(i));
                  }
          }

          // Now actually accept them
          for (int i = 0; i < n.ml.size(); i++) {
              if (!current.hasEntry(n.ml.elementAt(i).i.toString(),SymbolTable.METHOD_ENTRY)) {
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
    base.descendScope(n.i.toString());

      ClassTable current;
      if(base.getCurrentScope().isEntry(SymbolEntry.CLASS_ENTRY) ) {
          current = (ClassTable) base.getCurrentScope();

          // Check the parent class

          n.i.accept(this);
          n.j.accept(this);

          // Check for duplicate variable names in class
          for (int i = 0; i < n.vl.size(); i++) {
              if (current.hasEntry(n.vl.elementAt(i).i.toString(), SymbolTable.LEAF_ENTRY)) {

                  System.out.println("Multiply defined variable name at line " + n.vl.elementAt(i).lineNum() + ", character " + n.vl.elementAt(i).charNum());

              } else {
                  if (base.getCurrentScope().isEntry(SymbolEntry.CLASS_ENTRY)) {
                      current.putVariable(n.vl.elementAt(i));
                  }
                  n.vl.elementAt(i).accept(this);
              }
          }

          // Check for duplicate method names in class
          for (int i = 0; i < n.ml.size(); i++) {
              // Checks if there is a duplicate method in iether this class OR the suuper class because there is no overriding.
              if (current.hasEntry(n.ml.elementAt(i).i.toString(),SymbolTable.METHOD_ENTRY) ||
                      base.getClassTable(n.j.toString()).hasEntry(n.ml.elementAt(i).i.toString(),SymbolEntry.METHOD_ENTRY)) {


                  System.out.println("Multiply defined method name at line " + n.ml.elementAt(i).lineNum() + ", character " + n.ml.elementAt(i).charNum());
              } else {
                  current.putMethod(n.ml.elementAt(i));
              }
          }

          // Now actually accept them
          for (int i = 0; i < n.ml.size(); i++) {
              if (!current.hasEntry(n.ml.elementAt(i).i.toString(),SymbolTable.METHOD_ENTRY)) {
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

          if (base.getCurrentScope().isEntry(SymbolTable.METHOD_ENTRY)) {
              MethodTable current = (MethodTable) base.getCurrentScope();

              n.t.accept(this);
              n.i.accept(this);

              // Checks formal list for duplicate variable names
              for (int i = 0; i < n.fl.size(); i++) {
                  if (current.hasEntry(n.fl.elementAt(i).i.toString(),SymbolTable.LEAF_ENTRY)) {
                      System.out.println("Multiply defined variable name at line " + n.fl.elementAt(i).lineNum() + ", character " + n.fl.elementAt(i).charNum());
                  } else{
                      current.putVariable( n.fl.elementAt(i) );
                      n.fl.elementAt(i).accept(this);
                  }
              }

              // Checks variable decls for duplicate names
              for (int i = 0; i < n.vl.size(); i++) {
                  if ( current.hasEntry(n.vl.elementAt(i).i.toString(),SymbolTable.LEAF_ENTRY)) {
                          System.out.println("Multiply defined variable name at line " + n.vl.elementAt(i).lineNum() + ", character " + n.vl.elementAt(i).charNum());

                  } else {
                      current.putVariable(n.vl.elementAt(i));
                      n.vl.elementAt(i).accept(this);
                  }
              }

              for (int i = 0; i < n.sl.size(); i++) {
                  n.sl.elementAt(i).accept(this);
              }
              n.e.accept(this);
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
    boolean identifierFound = false;

    // While the parent isn't null and the identifier is not found

    // Check the keys for the identifier, if found set to true, else ascend scope
    if (base.hasEntry(n.s, SymbolTable.CLASS_ENTRY)) {
       identifierFound = true;
    }


    if (identifierFound == false) {
      System.out.println("FROM CLASS Use of undefined variable identifier at line " + n.lineNum() + ", character " + n.charNum());
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

          TableEntry scopeCursor = base.getCurrentScope();

          boolean identifierFound = false;

          // While the current scope isn't root ( since we are checking id assignment)
          while ( ! scopeCursor.isEntry(SymbolTable.ROOT_ENTRY) && identifierFound == false) {

              // Check the keys for the identifier, if found set to true, else ascend scope
              if (scopeCursor.hasEntry(n.i.toString(),SymbolTable.LEAF_ENTRY)) {
                    identifierFound = true;
              } else {
                    scopeCursor = scopeCursor.parent;
              }
          }

          if (!identifierFound) {
              System.out.println("FFROM MMETHOD Use of undefined variable identifier at line " + n.i.lineNum() + ", character " + n.i.charNum());
          } else {
              n.i.accept(this);
          }
          n.e.accept(this);

  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {

    boolean identifierFound = false;
    TableEntry cursorScope = base.getCurrentScope();
    // While the parent isn't null and the identifier is not found
    while ( ! cursorScope.isEntry(SymbolTable.ROOT_ENTRY) && !identifierFound) {

      // Check the keys for the identifier, if found set to true, else ascend scope
      if ( cursorScope.hasEntry( n.i.toString() , SymbolTable.LEAF_ENTRY)) {
          identifierFound = true;
      }
      else {
       cursorScope = cursorScope.parent;
      }
    }

    if (identifierFound == false) {
      System.out.println("FROM METHOD Use of undefined variable identifier at line " + n.i.lineNum() + ", character " + n.i.charNum());
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
    boolean identifierFound = false;

    TableEntry scopeCursor = base.getCurrentScope();

    // While the parent isn't null and the identifier is not found
    while (scopeCursor != null&& identifierFound == false) {
        // f


        if (swapper != null && swapper.hasEntry(n.i.toString(),SymbolTable.METHOD_ENTRY)) {
            identifierFound = true;
            break;
        }

        swapper = null;


      // Check the keys for the identifier, if found set to true, else ascend scope
      if (scopeCursor.hasEntry(n.i.toString(),SymbolEntry.METHOD_ENTRY) ) {
          identifierFound = true;
      } else {
        scopeCursor = scopeCursor.parent;
      }
    }

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

    TableEntry scopeCursor = base.getCurrentScope();
    boolean identifierFound = false;

    // While the parent isn't null and the identifier is not found
    while (!scopeCursor.isEntry(SymbolTable.ROOT_ENTRY) && identifierFound == false) {

      // Check the keys for the identifier, if found set to true, else ascend scope
      if ( scopeCursor.hasEntry(n.s,SymbolTable.LEAF_ENTRY) ) {
            identifierFound = true;
      }
      else {
        scopeCursor =  scopeCursor.parent;
      }
    }

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
    boolean identifierFound = false;

    // Check if keys contain class name of new object
    if ( base.hasEntry(n.i.toString(),SymbolTable.CLASS_ENTRY)) {
        identifierFound = true;

        // Set flag
         swapper = base.getClassTable(n.i.toString());
    }

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
