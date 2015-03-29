package IR;

abstract class ThreeAddressStatement {

    /**
     *
     * @return type of the statement (human readable, for debugging, etc)
     */
    public String statementType() {
        return "temp";
    }


}