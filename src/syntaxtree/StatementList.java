package syntaxtree;

import java.util.Vector;

public class StatementList extends ASTNode{
   private Vector list;

   public StatementList(int col,int line) {
      list = new Vector();
       setValues(col,line);
   }

   public void addElement(Statement n) {

      list.add(0,n);
   }

   public Statement elementAt(int i)  { 
      return (Statement)list.elementAt(i); 
   }

   public int size() { 
      return list.size(); 
   }
}
