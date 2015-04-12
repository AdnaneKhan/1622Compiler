class Test {
    public static void main(String[] args) {
        System.out.println(new Test2().Start(false));
    }
}
class Test2 {
    public int Start(boolean y) {
        int x;
        x = 0;
        if (y) {
        	x = 1;
        }
        else {
        	x = 2;
        }
        return x;
    }
}
