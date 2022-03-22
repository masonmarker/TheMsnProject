class Main {
  public static void main(String[] args) {

    Timer t = new Timer();
    t.start();



    long input = 343434340000000L;


    for (int j = 0; j < 30000000; j++) {
      // String stringrep = "" + input;
      // boolean jumping = true;
      // for (int i = 0; i < stringrep.length(); i++) {
      // int current = Integer.parseInt(stringrep.charAt(i) + "");
      // int next = -4;
      // try {
      // next = stringrep.charAt(i + 1);
      // if (next - current != 1) {
      // jumping = false;
      // }
      // } catch (IndexOutOfBoundsException e) {
      //
      // }
      // }
      //
      // if (jumping) {
      // System.out.println(input + ": jumping");
      // }


      boolean jumping = isJumping(input);
      if (jumping) {
        System.out.println(input + ":" + jumping);
      }
      input += 1;
    }
    t.stop();
    t.printHistory();



  }


  public static boolean isJumping(long input) {
    boolean isJumping = false;
    String inputStr = String.valueOf(input);
    if (input < 10) {
      isJumping = true;
      return isJumping;
    }
    for (int i = 0; i < inputStr.length() - 1; i++) {
      if ((Math.abs(Long.parseLong(String.valueOf(inputStr.charAt(i)))
          - Long.parseLong(String.valueOf(inputStr.charAt(i + 1))))) == 1) {
        isJumping = true;
      } else {
        isJumping = false;
        break;
      }
    }
    return isJumping;
  }



}
