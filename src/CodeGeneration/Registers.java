package CodeGeneration;

import java.util.*;

/**
 * Created by adnankhan on 4/14/15.
 */
public class Registers {
    public static final int AT = 1;
    public static final int V0 = 2;
    public static final int V1 = 3;
    public static final int ARG0 = 4;
    public static final int ARG1 = 5;
    public static final int ARG2 = 6;
    public static final int ARG3 = 7;
    public static final int TEMP0 = 8;
    public static final int TEMP1 = 9;
    public static final int TEMP2 = 10;
    public static final int TEMP3 = 11;
    public static final int TEMP4 = 12;
    public static final int TEMP5 = 13;
    public static final int TEMP6 = 14;
    public static final int TEMP7 = 15;
    public static final int TEMP8 = 24;
    public static final int TEMP9 = 25;
    public static final int SAVE0 = 16;
    public static final int SAVE1 = 17;
    public static final int SAVE2 = 18;
    public static final int SAVE3 = 19;
    public static final int SAVE4 = 20;
    public static final int SAVE5 = 21;
    public static final int SAVE6 = 22;
    public static final int SAVE7 = 23;
    public static final int K0 = 26;
    public static final int K1 = 27;
    public static final int GP = 28;
    public static final int RA = 31;

    private List<Integer> regCache;

    public Registers() {
        regCache = new ArrayList<Integer>();

        regCache.add(AT);
        regCache.add(V1);
        regCache.add(TEMP0);
        regCache.add(TEMP1);
        regCache.add(TEMP2);
        regCache.add(TEMP3);
        regCache.add(TEMP4);
        regCache.add(TEMP5);
        regCache.add(TEMP6);
        regCache.add(TEMP7);
        regCache.add(TEMP8);
        regCache.add(TEMP9);
        regCache.add(SAVE0);
        regCache.add(SAVE0);
        regCache.add(SAVE1);
        regCache.add(SAVE2);
        regCache.add(SAVE3);
        regCache.add(SAVE4);
        regCache.add(SAVE5);
        regCache.add(SAVE6);
        regCache.add(SAVE7);
        regCache.add(K0);
        regCache.add(K1);
        regCache.add(GP);

    }
    
    public int getRegister() {
        return regCache.get(regCache.size() -1 );
    }

    public void putRegister(int reg) {
        regCache.add(reg);
    }
}
