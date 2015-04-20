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
        int[] theArray;
        int x;
        int y;
        int z;
        ArrayCopy ac;

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

        y = 0;
        while (y < 60) {
            i[y] = y;
            System.out.println(i[y]);
            y = y + 1;
        }

        ac = new ArrayCopy();


        z = ac.setK(99898989);

        System.out.println(ac.getK());

        theArray = ac.makeArray();

        theArray = ac.populateArray(theArray);

        return z + q;
    }

}

class ArrayCopy {
    int b;
    int c;
    int k;

    public int setK(int var) {
        k = var;

        return 0;
    }

    public int getK() {
        return k;
    }

    public int[] makeArray() {
        int[] a;
        a = new int[10];
        return a;
    }

    public int[] populateArray(int[] toPopulate) {
        int iter;

        iter = 0;

        while (iter < 10) {
            toPopulate[iter] = iter;
            System.out.println(iter);
            iter = iter + 1;
        }

        return toPopulate;
    }





}