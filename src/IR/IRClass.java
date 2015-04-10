package IR;

import java.util.ArrayList;

import java.lang.Override;

public class IRClass {
    public ArrayList<IRMethod> lines;
    private String name;
    private String extend;

    public IRClass(String s) {
        lines = new ArrayList<IRMethod>();
        name = s;
        extend = "";
    }

    public IRClass(String s1, String s2) {
        lines = new ArrayList<IRMethod>();
        name = s1;
        extend = s2;
    }

    public int getLength() {
        return lines.size();
    }

    public IRMethod get(int i) {
        return lines.get(i);
    }

    public void add(IRMethod x) {
        lines.add(x);
    }

    public String getName() {
        return name;
    }

    public String getExtends() {
        return extend;
    }

    public void replace(IRMethod method, int i) {
        lines.set(i, method);
    }



    /**
     *
     * @return the 3AC representation in string form to be outputted in the 3AC generation
     */
    @Override
    public String toString() {
        String ret = "";
        for (int i = 0; i < this.getLength(); i++) {
            ret += lines.get(i).toString();
        }
        return ret;
    }
}