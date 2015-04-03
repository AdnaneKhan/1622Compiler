package IR;


public class UncondJump3AC extends ThreeAddressStatement {
    String destination;

    public UncondJump3AC(String gotoDest) {
        this.destination = gotoDest;
    }

    @Override
    public String statementType() {
        return null;
    }

    @Override
    public String toString() {
        return "goto " + destination;
    }
}