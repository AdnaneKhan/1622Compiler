package CodeGeneration;

import SymTable.SymbolEntry;

import java.util.HashSet;

/**
 * Created by adnankhan on 4/14/15.
 */
public class Row {
    boolean moveRelated = false;

    public HashSet<SymbolEntry> uses = new HashSet<SymbolEntry>();
    public HashSet<SymbolEntry> defs = new HashSet<SymbolEntry>();
}
