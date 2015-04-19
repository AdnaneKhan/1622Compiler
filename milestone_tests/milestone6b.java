class TestRegAlloc{
    public static void main(String[] a){
        System.out.println(new SixB().Test(10)); // The final printed result is 23
    }
}

class SixB {
    public int Test(int num){
        int x, y, z, a, b;
        x = 1;  // x will become alive here
        y = x + 2;   // y will become alive here
        a = y;  // adding in moves to show coalescing
        x = y + num; // y will die after this assignment...    
        z = x + num; // so z will get y's register
        b = z;  // the registers of b,z and y,a shoul be the same
        return b;  // because x doesn't die until here, nothing will overwrite z's register
    }
}