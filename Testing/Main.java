


class Main {
  static int a = 1;

  public static void main(String[] args) {

    V();
    System.out.println(a);
  }

  public static int t(int x) {
    if (x > 0) {
      t(0);
    }
    a += 1;
    x += 2;
    return x;
    // POINT B
  }

  public static void V() {
    int a = 4;
    int b = 5;
    // POINT A
    t(b);
  }



}
