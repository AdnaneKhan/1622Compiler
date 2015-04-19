class TestRegAlloc{
    public static void main(String[] a){
        System.out.println(new SixA().Test(10)); // The final printed result is 23
    }
}

class SixA {
    public int Test(int num){
        int x, y, z;
        x = 1;  // x will become alive here
        y = x + 2;   // y will become alive here
        x = y + num; // y will die after this assignment...    
        z = x + num; // so z will get y's register
        return z;  // because x doesn't die until here, nothing will overwrite z's register
    }
}