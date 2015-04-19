class Factorial{
    public static void main(String[] a){
    System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {
    public int ComputeFac(int num) {
    int num_aux;
    boolean x;
    x = this.testBool(num);
    num_aux = this.testInt(num);

    if (num < 1)
        num_aux = 1;
    else 
        num_aux = num * (this.ComputeFac(num-1));
    return num_aux;
    }

    public int testInt(int num) {
        int a;
        a = 1;
        System.out.println(1+1); 
        System.out.println(a+1); 
        System.out.println(1+a); 
        System.out.println(a+num); 
        System.out.println(1*1); 
        System.out.println(a*1); 
        System.out.println(1*a); 
        System.out.println(a*num); 
        System.out.println(1-1);
        System.out.println(a-1); 
        System.out.println(1-a); 
        System.out.println(num-a); 
        return a;
    }

    public boolean testBool(int x) {
        boolean y;
        boolean z;
        int q;
        q = 1;
        y = true;
        z = true;
        if (true && true) {
            System.out.println(1);
        } 
        else {
            q = 1;
        }
        if (true && y) {
            System.out.println(1);
        }
        else {
            q = 1;
        }
        if (y && true) {
            System.out.println(1);
        }
        else {
            q = 1;
        }
        if (y && z) {
            System.out.println(1);
        }
        else {
            q = 1;
        }
        if (1 < 10) {
            System.out.println(1);
        }
        else {
            q = 1;
        }
        if (q < 10) {
            System.out.println(1);
        }
        else {
            q = 1;
        }
        if (1 < x) {
            System.out.println(1);
        }
        else {
            q = 1;
        }
        if (q < x) {
            System.out.println(1);
        }
        else {
            q = 1;
        }
        return true;
    }
}