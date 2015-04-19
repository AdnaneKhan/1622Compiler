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
        int[] i;
        int x;
        int y;
        int z;

        i = new int [60];
        z = 4;
        x = 1;
        y = 2;
        i[2] = 1;
        q = x + y;
        if (!((x<y) && (x<q))) {
            z = 1;
        }
        else {
            i[23] = z + 5;
        }
        if (y<x) {
            z = 4;
        }
        else {
            z = 5;
        }
        System.out.println(z);
        System.out.println(q);
        System.out.println(i[2]);

        return z + q + i [2];
    }
}