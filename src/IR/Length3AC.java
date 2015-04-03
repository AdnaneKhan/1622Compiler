package IR;

import SymTable.TableEntry;

import java.lang.Override;

public class Length3AC extends ThreeAddressStatement {
    private String type;
    private TableEntry leftHandVar;
    private TableEntry rightHandVar;


    /**
     *
     * @param leftSide left hand side of the x := length y (the x)
     * @param rightSide right hand side of x := length y (the y)
     */
    public Length3AC(TableEntry leftSide, TableEntry rightSide) {
        this.leftHandVar = leftSide;
        this.rightHandVar = rightSide;
    }

    @Override
    public String statementType() {
        return this.type;
    }

    /**
     *
     * @return the 3AC representation in string form to be outputted in the 3AC generation
     */
    public String toString () {
        String returnValue = this.leftHandVar.getSymbolName() + " := length " + this.rightHandVar.getSymbolName();
        return returnValue;
    }
}