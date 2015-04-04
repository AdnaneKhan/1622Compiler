package IR;

import java.util.ArrayList;

import java.lang.Override;

public class IRMethod {
    private ArrayList<Quadruple> lines;
    private String name;

    public IRMethod(String s) {
        lines = new ArrayList<Quadruple>();
        name = s;
    }

    private int getLength() {
        return lines.size();
    }

    public void add(Quadruple x) {
        lines.add(x);
    }

    /**
     *
     * @return the 3AC representation in string form to be outputted in the 3AC generation
     */
    @Override
    public String toString() {
        String ret = name + ": ";
        for (int i = 0; i < this.getLength(); i++) {
            ret += lines.get(i).toString() + "\n";
        }
        return ret;
    }
}