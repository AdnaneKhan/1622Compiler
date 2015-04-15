package CodeGeneration;

import SymTable.SymbolEntry;

import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by adnankhan on 4/14/15.
 */
public class Row {
    public HashSet<SymbolEntry> uses = new HashSet<SymbolEntry>();
    public HashSet<SymbolEntry> defs = new HashSet<SymbolEntry>();
}
