package CodeGeneration;

import IR.IRClass;
import IR.IRMethod;
import IR.Quadruple;
import SymTable.SymbolEntry;
import SymTable.SymbolTable;
import SymTable.TableEntry;

import java.util.ArrayList;
import javafx.util.Pair;


public class Optimizer {

	public Optimizer() {}

	/*public ArrayList<IRClass> deadCodeElim(ArrayList<IRClass> ir, ArrayList<ControlFlowNode> cfg, ArrayList<Row> defsUses) {
		// instantiate a list of defs for each variable 
		ArrayList<Pair<SymbolEntry,Integer>> defs = new ArrayList<Pair<SymbolEntry,Integer>>();
		for (int i = 0; i < defsUses.size(); i++) {
			for (SymbolEntry s : defsUses.get(i).defs) {
				Pair<SymbolEntry,Integer> def = new Pair(s,i);
				defs.add(def);
			}
		}

		ArrayList<SymbolEntry> defsToRemove = new ArrayList<SymbolEntry>();

		// Chcek if there is a use, if not eliminate the def
		for (int i = 0; i < defs.size(); i++) {
			int def1 = defs.get(i).getValue();
			int def2 = 0;

			for (int j = i+1; j < defs.size(); j++) {
				if (defs.get(j).getKey().equals(defs.get(i).getKey())) {
					def2 = defs.get(j).getValue();
				}
				else {
					def2 = j;
				}
			}

			boolean useFound = false;
			for (int j = def1+1; j < def2; j++) {
				if (defsUses.get(j).uses.contains(defs.get(i).getValue())) {
					useFound = true;
					break;
				}
			}

			if (!useFound) {
				defsToRemove.add(defs.get(i).getKey());
			}
		}

		for (SymbolEntry def : defsToRemove) {
			boolean quadAdjusted = false;
			for (int j = 0; j < ir.size(); j++) {
				for (int k = 0; k < ir.get(j).lines.size(); k++) {
					for (int h = 0; h < ir.get(j).lines.get(k).lines.size(); h++) {
						if (def.equals((SymbolEntry) ir.get(j).lines.get(k).lines.get(h).resVar)) {
							System.out.println(ir.get(j).lines.get(k).lines.get(h));
							ir.get(j).lines.get(k).lines.remove(h);
							quadAdjusted = true;
							break;
						}	
					}
					if (quadAdjusted) {
						break;
					}
				}
				if (quadAdjusted) {
					break;
				}
			}	
		}
		return ir;
	} */

	ArrayList<String> linesRemoved = new ArrayList<String>();
	ArrayList<String> linesToSave = new ArrayList<String>();
	ArrayList<Pair<String,Integer>> linesToSaveValues = new ArrayList<Pair<String,Integer>>();

	public ArrayList<IRClass> restoreUses(ArrayList<IRClass> ir, ArrayList<ControlFlowNode> cfg, ArrayList<Row> defsUses) {

		ArrayList<Pair<SymbolEntry,Integer>> indexesOfUses = new ArrayList<Pair<SymbolEntry,Integer>>();
		ArrayList<String> listOfResultsWhereUses = new ArrayList<String>();

		// get the inexes of all uses along with the symbol associated with it
		for (int i = 0; i < linesToSaveValues.size(); i++) {
			for (int j = 0; j < defsUses.size(); j++) {
				for (SymbolEntry e : defsUses.get(j).uses) {
					if (e.getSymbolName().charAt(0) == '_') {
						if (e.getSymbolName().equals(linesToSaveValues.get(i).getKey())) {
							indexesOfUses.add(new Pair<SymbolEntry,Integer>(e,j));
						}
					}
					else {
						if (e.getHierarchyName().equals(linesToSaveValues.get(i).getKey())) {
							indexesOfUses.add(new Pair<SymbolEntry,Integer>(e,j));
						}					
					}	
				}
			}
		}

		for (int i = 0; i < indexesOfUses.size(); i++) {
			String result = cfg.get(indexesOfUses.get(i).getValue()).irLine.getResult();
			listOfResultsWhereUses.add(result);
		}

		for (int i = 0; i < listOfResultsWhereUses.size(); i++) {

			// Adjust the IR to replace the conditional branch with a goto or just remove the quad
			for (int j = 0; j < ir.size(); j++) {
				for (int k = 0; k < ir.get(j).lines.size(); k++) {
					for (int h = 0; h < ir.get(j).lines.get(k).lines.size(); h++) {
						if (listOfResultsWhereUses.get(i).equals(ir.get(j).lines.get(k).lines.get(h).getResult())) {
							for (int l = 0; l < linesToSaveValues.size(); l++) {
								if (linesToSaveValues.get(l).getKey().equals(ir.get(j).lines.get(k).lines.get(h).getArg1())) {
									ir.get(j).lines.get(k).lines.get(h).setArg1(linesToSaveValues.get(l).getValue());
								}
								if (linesToSaveValues.get(l).getKey().equals(ir.get(j).lines.get(k).lines.get(h).getArg2())) {
									ir.get(j).lines.get(k).lines.get(h).setArg2(linesToSaveValues.get(l).getValue());
								}
							}
						}
							
					}
				}
			}
		}

		return ir;

	}

	// Changes loops with evaluated immediates in preprocessing to goto getting rid of the calculation in code
	public ArrayList<IRClass> constantConditions(ArrayList<IRClass> ir, ArrayList<ControlFlowNode> cfg, ArrayList<Row> defsUses) {

		// Index of quadruple to be removed
		int toBeRemoved = 0;
		boolean exitOptimization = false;

		// for everything in the control flow graph
		for (int i = 0; i < cfg.size(); i++) {

			// if there is a conditional branch
			if (cfg.get(i).irLine.type == Quadruple.CONDITIONAL_JUMP) {

				// get row of def for value used to evaluate
				SymbolEntry branchCondition = (SymbolEntry) cfg.get(i).irLine.arg1_entry;
				for (int j = 0; j < i; j++) {
					if (defsUses.get(j).defs.contains(branchCondition)) {
						toBeRemoved = j;
						break;
					}
				}
				// if no uses of var after branch and before next def, remove the node, else escape
				int nextDef = 0;
				if (!(defsUses.size()-1 <= toBeRemoved+1)) {
					for (int j = toBeRemoved+1; j < defsUses.size(); j++) {
						if (defsUses.get(j).defs.contains(branchCondition)) {
							nextDef = j;
							break;
						}
						else {
							nextDef = j;
						}
					}
				}

				for (int j = i; j < nextDef+1; j++) {
					if (defsUses.get(j).uses.contains(branchCondition)) {
						//linesToSave.add(cfg.get(toBeRemoved).irLine.getResult());
						break;
					}
				}

				// if a use was found, exit the optmization
				if (exitOptimization) {
					break;
				}

				// check the same thing for all nodes above it til it hits ints
				if (cfg.get(toBeRemoved).irLine.arg1_entry != null) {
					//System.out.println(cfg.get(toBeRemoved).irLine.arg1_entry.getSymbolName());
					exitOptimization = determineInterference((SymbolEntry) cfg.get(toBeRemoved).irLine.arg1_entry, cfg, defsUses, i);
				}
				if (cfg.get(toBeRemoved).irLine.arg2_entry != null) {
					//System.out.println(cfg.get(toBeRemoved).irLine.arg2_entry.getSymbolName());
					exitOptimization = determineInterference((SymbolEntry) cfg.get(toBeRemoved).irLine.arg2_entry, cfg, defsUses, i);
				}

				if (exitOptimization) {
					break;
				}

				// Get the final true or false value of the branch
				int currentValue = 0;
				int arg1 = 0;
				int arg2 = 0;

				// check the same thing for all nodes above it til it hits ints
				if (cfg.get(toBeRemoved).irLine.arg1_entry != null && cfg.get(toBeRemoved).irLine.arg1_entry.entryType() == 3 && !cfg.get(toBeRemoved).irLine.arg1Literal()) {
					//System.out.println(cfg.get(toBeRemoved).irLine.arg1_entry.getHierarchyName());
					arg1 = evaluateExpression((SymbolEntry) cfg.get(toBeRemoved).irLine.arg1_entry, cfg, defsUses, i);
				}
				if (cfg.get(toBeRemoved).irLine.arg2_entry != null && cfg.get(toBeRemoved).irLine.arg1_entry.entryType() == 3 && !cfg.get(toBeRemoved).irLine.arg2Literal()) {
					//System.out.println(cfg.get(toBeRemoved).irLine.arg2_entry.getHierarchyName());
					arg2 = evaluateExpression((SymbolEntry) cfg.get(toBeRemoved).irLine.arg2_entry, cfg, defsUses, i);
				}

				if (cfg.get(toBeRemoved).irLine.arg1Literal()) {
					arg1 = Integer.parseInt(cfg.get(toBeRemoved).irLine.getArg1());
				}

				if (cfg.get(toBeRemoved).irLine.arg2Literal()) {
					arg1 = Integer.parseInt(cfg.get(toBeRemoved).irLine.getArg1());
				}

				if (cfg.get(toBeRemoved).irLine.type == Quadruple.COPY) {
					currentValue = arg1;
					linesRemoved.add(cfg.get(toBeRemoved).irLine.getResult());
				}
				else if (cfg.get(toBeRemoved).irLine.type == Quadruple.ASSIGNMENT) {
					linesRemoved.add(cfg.get(toBeRemoved).irLine.getResult());
					if (cfg.get(toBeRemoved).irLine.op.equals("+")) {
						currentValue = arg1 + arg2;
					}
					else if (cfg.get(toBeRemoved).irLine.op.equals("-")) {
						currentValue = arg1 - arg2;
					}
					else if (cfg.get(toBeRemoved).irLine.op.equals("*")) {
						currentValue = arg1 * arg2;
					}
					else if (cfg.get(toBeRemoved).irLine.op.equals("<")) {
						if (arg1<arg2) {
							currentValue = 1;
						}
						else {
							currentValue = 0;
						}
					}
					else if (cfg.get(toBeRemoved).irLine.op.equals("&&")) {
						boolean temp1 = false;
						boolean temp2 = false;
						if (arg1 == 0) {
							temp1 = false;
						}
						else {
							temp1 = true;
						}
						if (arg2 == 0) {
							temp2 = false;
						}
						else {
							temp2 = true;
						}
						boolean temp3 = temp1 && temp2;
						if (temp3) {
							currentValue = 1;
						}
						else {
							currentValue = 0;
						}
					}
				}

				if (linesToSave.contains(cfg.get(toBeRemoved).irLine.getResult())) {
					Pair<String,Integer> def = new Pair<String,Integer>(cfg.get(toBeRemoved).irLine.getResult(), currentValue);
					linesToSaveValues.add(def);
				}

				if (branchCondition.getSymbolName().charAt(0) == '_') {
					linesRemoved.add(branchCondition.getSymbolName());
				}
				else {
					linesRemoved.add(branchCondition.getHierarchyName());
				}

				System.out.println(currentValue);
				
				// Adjust the IR to replace the conditional branch with a goto or just remove the quad
				boolean quadAdjusted = false;
				for (int j = 0; j < ir.size(); j++) {
					for (int k = 0; k < ir.get(j).lines.size(); k++) {
						for (int h = 0; h < ir.get(j).lines.get(k).lines.size(); h++) {
							if (ir.get(j).lines.get(k).lines.get(h).type == Quadruple.CONDITIONAL_JUMP) {
								if (branchCondition.equals((SymbolEntry) ir.get(j).lines.get(k).lines.get(h).arg1_entry)) {
									if (currentValue == 0) {
										ir.get(j).lines.get(k).lines.get(h).result = ir.get(j).lines.get(k).lines.get(h).getArg2();
										ir.get(j).lines.get(k).lines.get(h).type = Quadruple.UNCONDITIONAL_JUMP;
									}
									else if (currentValue == 1) {
										ir.get(j).lines.get(k).lines.remove(h);
									}
									quadAdjusted = true;
									break;
								}
							}
						}
						if (quadAdjusted) {
							break;
						}
					}
					if (quadAdjusted) {
						break;
					}
				}

				

				// Remove linesToSave from linesRemoved (set subtraction)	
				for (int j = 0; j < linesToSave.size(); j++) {
					if (linesRemoved.contains(linesToSave.get(j))) {
						linesRemoved.remove(linesToSave.get(j));
					}
				}

				boolean remove = true;
				while (remove) {
					remove = false;
					for (int j = 0; j < linesToSaveValues.size(); j++) {
						if (linesToSave.contains(linesToSaveValues.get(j).getKey())) {
							linesToSaveValues.remove(j);
							remove = true;
							break;
						}
					}
				}

				System.out.println("LINES TO BE REMOVED");
				for (int j = 0; j < linesRemoved.size(); j++) {
					System.out.println(linesRemoved.get(j));
				}
				System.out.println("\nLINES TO BE SAVED");
				for (int j = 0; j < linesToSave.size(); j++) {
					System.out.println(linesToSave.get(j));
				}
				System.out.println();

				System.out.println("Trying to get rid of useless instructions...");
				// Remove IR instructions now
				remove = true;
				while (remove) {
					remove = false;

					for (int j = 0; j < ir.size(); j++) {
						for (int k = 0; k < ir.get(j).lines.size(); k++) {
							for (int h = 0; h < ir.get(j).lines.get(k).lines.size(); h++) {
								if (linesRemoved.contains(ir.get(j).lines.get(k).lines.get(h).getResult())) {
									System.out.println(ir.get(j).lines.get(k).lines.get(h).getResult());
									ir.get(j).lines.get(k).lines.remove(h);
									remove = true;
									break;
								}
								if (linesToSave.contains(ir.get(j).lines.get(k).lines.get(h).getResult())) {
									ir.get(j).lines.get(k).lines.get(h).type = Quadruple.COPY;
									for (Pair p : linesToSaveValues) {
										if (p.getKey().equals(ir.get(j).lines.get(k).lines.get(h).getResult())) {
											ir.get(j).lines.get(k).lines.get(h).type = Quadruple.COPY;
											ir.get(j).lines.get(k).lines.get(h).setArg1((Integer) p.getValue());
										}
									}
								}
							}
							if (remove) {
								break;
							}
						}
						if (remove) {
							break;
						}
					}

				}


				// Clear out the array lists for the next iteration
				linesRemoved.clear();
				linesToSave.clear();						
			}
		}


		System.out.println("\nVARIABLES THAT NEED RESTORED WHERE USED");
		for (int i = 0; i < linesToSaveValues.size(); i++) {
			System.out.print(linesToSaveValues.get(i).getKey() + ": " + linesToSaveValues.get(i).getValue() + "\n");
		}

		return ir;
	}

	// Recursive function that determines uses and defs for linesRemoved passed in and the others that go along with it
	// i is the index of the branch
	private boolean determineInterference(SymbolEntry varToCheck, ArrayList<ControlFlowNode> cfg, ArrayList<Row> defsUses, int i) {
		boolean exitOptimization = false;

		int toBeRemoved = 0;
		// Find index of node in defs
		for (int j = 0; j < i; j++) {
			if (defsUses.get(j).defs.contains(varToCheck)) {
				toBeRemoved = j;
				break;
			}
		}
		// if no uses of var after branch and before next def, remove the node, else escape
		int nextDef = 0;
		if (!(defsUses.size()-1 <= toBeRemoved+1)) {
			for (int j = toBeRemoved+1; j < defsUses.size(); j++) {
				if (defsUses.get(j).defs.contains(varToCheck)) {
					nextDef = j;
					break;
				}
				else {
					nextDef = j;
				}
			}
		}

		for (int j = i+1; j < nextDef+1; j++) {
			if (defsUses.get(j).uses.contains(varToCheck)) {
				//linesToSave.add(cfg.get(toBeRemoved).irLine.getResult());
				break;
			}
		}

		// if a use was found, exit the optmization
		if (exitOptimization) {
			return exitOptimization;
		}

		// check the same thing for all nodes above it til it hits ints
		if (cfg.get(toBeRemoved).irLine.arg1_entry != null && cfg.get(toBeRemoved).irLine.arg1_entry.entryType() == 3) {
			//System.out.println(cfg.get(toBeRemoved).irLine.arg1_entry.getHierarchyName());
			exitOptimization = determineInterference((SymbolEntry) cfg.get(toBeRemoved).irLine.arg1_entry, cfg, defsUses, i);
		}
		if (cfg.get(toBeRemoved).irLine.arg2_entry != null && cfg.get(toBeRemoved).irLine.arg1_entry.entryType() == 3) {
			//System.out.println(cfg.get(toBeRemoved).irLine.arg2_entry.getHierarchyName());
			exitOptimization = determineInterference((SymbolEntry) cfg.get(toBeRemoved).irLine.arg2_entry, cfg, defsUses, i);
		}		

		return exitOptimization;

	}


	public int evaluateExpression(SymbolEntry var, ArrayList<ControlFlowNode> cfg, ArrayList<Row> defsUses, int i) {
		int currentValue = 0;
		int arg1 = 0;
		int arg2 = 0;

		int toBeRemoved = 0;
		// Find index of node in defs
		for (int j = 0; j < i; j++) {
			if (defsUses.get(j).defs.contains(var)) {
				toBeRemoved = j;
				break;
			}
		}

		// if no uses of var after branch and before next def, remove the node, else escape
		int nextDef = 0;
		if (!(defsUses.size()-1 <= toBeRemoved+1)) {
			for (int j = toBeRemoved+1; j < defsUses.size(); j++) {
				if (defsUses.get(j).defs.contains(var)) {
					nextDef = j;
					break;
				}
				else {
					nextDef = j;
				}
			}
		}

		for (int j = i+1; j < nextDef+1; j++) {
			if (defsUses.get(j).uses.contains(var)) {
				linesToSave.add(cfg.get(toBeRemoved).irLine.getResult());
				break;
			}
		}

		// check the same thing for all nodes above it til it hits ints
		if (cfg.get(toBeRemoved).irLine.arg1_entry != null && cfg.get(toBeRemoved).irLine.arg1_entry.entryType() == 3 && !cfg.get(toBeRemoved).irLine.arg1Literal()) {
			arg1 = evaluateExpression((SymbolEntry) cfg.get(toBeRemoved).irLine.arg1_entry, cfg, defsUses, i);
		}
		if (cfg.get(toBeRemoved).irLine.arg2_entry != null && cfg.get(toBeRemoved).irLine.arg1_entry.entryType() == 3 && !cfg.get(toBeRemoved).irLine.arg2Literal()) {
			arg2 = evaluateExpression((SymbolEntry) cfg.get(toBeRemoved).irLine.arg2_entry, cfg, defsUses, i);
		}

		if (cfg.get(toBeRemoved).irLine.arg1Literal()) {
			arg1 = Integer.parseInt(cfg.get(toBeRemoved).irLine.getArg1());
		}

		if (cfg.get(toBeRemoved).irLine.arg2Literal()) {
			arg1 = Integer.parseInt(cfg.get(toBeRemoved).irLine.getArg1());
		}

		if (cfg.get(toBeRemoved).irLine.type == Quadruple.COPY) {
			currentValue = arg1;
			linesRemoved.add(cfg.get(toBeRemoved).irLine.getResult());
		}
		else if (cfg.get(toBeRemoved).irLine.type == Quadruple.ASSIGNMENT) {
			linesRemoved.add(cfg.get(toBeRemoved).irLine.getResult());
			if (cfg.get(toBeRemoved).irLine.op.equals("+")) {
				currentValue = arg1 + arg2;
			}
			else if (cfg.get(toBeRemoved).irLine.op.equals("-")) {
				currentValue = arg1 - arg2;
			}
			else if (cfg.get(toBeRemoved).irLine.op.equals("*")) {
				currentValue = arg1 * arg2;
			}
			else if (cfg.get(toBeRemoved).irLine.op.equals("<")) {
				if (arg1<arg2) {
					currentValue = 1;
				}
				else {
					currentValue = 0;
				}
			}
			else if (cfg.get(toBeRemoved).irLine.op.equals("&&")) {
				boolean temp1 = false;
				boolean temp2 = false;
				if (arg1 == 0) {
					temp1 = false;
				}
				else {
					temp1 = true;
				}
				if (arg2 == 0) {
					temp2 = false;
				}
				else {
					temp2 = true;
				}
				boolean temp3 = temp1 && temp2;
				if (temp3) {
					currentValue = 1;
				}
				else {
					currentValue = 0;
				}
			}
		}

		if (linesRemoved.contains(cfg.get(toBeRemoved).irLine.getResult())) {
			Pair<String,Integer> def = new Pair<String,Integer>(cfg.get(toBeRemoved).irLine.getResult(), currentValue);
			linesToSaveValues.add(def);
		}

		return currentValue;
	}


}