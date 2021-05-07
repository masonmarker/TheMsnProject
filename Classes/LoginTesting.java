import java.util.HashMap;

public class LoginTesting {

  public static void main(String[] args) {
    
    HashMap<String, String> logins = new HashMap<>();
    
    logins.put("mason", "password");
    logins.put("harris", "universe");
    logins.put("marko", "valorant");
    
    Login login = new Login(logins, "Test");
    
    while (!login.isLoggedIn()) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    System.out.println("logged in");

  }

}
