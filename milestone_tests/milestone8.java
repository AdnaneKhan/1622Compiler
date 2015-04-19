class Factorial{
    public static void main(String[] a){
    System.out.println(new Fac().ComputeFac(10));
    }
}

class Foo {
    int q;
}

class Fac extends Foo {
    public int ComputeFac(int num){
        int x;
        int y;
        int z;
        x = 1;
        y = 2;
        q = x + y;
        if (!((x<y) && (x<q))) {
            z = 1;
        }
        else {
            z = 0;
        }
        if (x<y) {
            z = 4;
        }
        else {
            z = 5;
        }
        return z;
    }
}