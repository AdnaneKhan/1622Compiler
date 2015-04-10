package IR;

import java.util.ArrayList;

import java.lang.Override;

public class IRMethod {
    public ArrayList<Quadruple> lines;
    private String name;

    public IRMethod(String s) {
        lines = new ArrayList<Quadruple>();
        name = s;
    }

    public int getLength() {
        return lines.size();
    }

    public String getName() {
        return name;
    }

    public void add(Quadruple x) {
        lines.add(x);
    }

    public Quadruple get(int i) {
        return lines.get(i);

    }


    public void replace(Quadruple quad, int i) {
        lines.set(i, quad);
    }

    /**
     *
     * @return the 3AC representation in string form to be outputted in the 3AC generation
     */
    @Override
    public String toString() {
        String ret = name + ": \n";
        for (int i = 0; i < this.getLength(); i++) {
            ret += lines.get(i).toString() + "\n";
        }
        return ret;
    }
}