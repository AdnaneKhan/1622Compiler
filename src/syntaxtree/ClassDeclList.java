package syntaxtree;

import java.util.Vector;

public class ClassDeclList  implements ASTNode{
   private Vector list;

   public ClassDeclList(int col,int line) {
      list = new Vector();
       setValues(col,line);
   }

   public void addElement(ClassDecl n) {
      list.addElement(n);
   }

   public ClassDecl elementAt(int i)  { 
      return (ClassDecl)list.elementAt(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
