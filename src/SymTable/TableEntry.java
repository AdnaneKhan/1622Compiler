package SymTable;

/**
 * Created by adnankhan on 3/29/15.
 */
public abstract class TableEntry {

    /**
     *
     * @return type of this entry which can be:
     *
     *      Method
     *      Class
     *      Leaf Entry
     *
     */
    public int entryType() {
        return 0;

    }
}
