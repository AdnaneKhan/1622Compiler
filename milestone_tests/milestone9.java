class Factorial{
    public static void main(String[] a){
        System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {
    int k;
    int b;
    int g;
    Fac j;
    public int ComputeFac(int num){

        int num_aux ;

        j = new Fac();
        b = 2;
        g = j.returnSomeThing();


        return g ;
    }

    public int returnSomeThing() {
        return 5;
    }
}