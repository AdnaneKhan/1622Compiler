package SymTable;

import java.util.HashMap;

/**
 * Created by adnankhan on 3/29/15.
 */
public class MethodTable extends TableEntry {
    // Reference to the parent scope which this symbol entry resides in, the parent scope can be
    // accessed to check for duplicates, etc.
    private TableEntry parent;

    protected HashMap<SymbolEntry, TableEntry> hash;
}
