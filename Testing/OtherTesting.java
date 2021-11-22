import java.io.FileNotFoundException;
import MsnLib.Msn;

public class OtherTesting {

  public static void main(String[] args) {

    String contents = Msn.contentsOf("gencode.v38.annotation.gff3");
    try {
      Msn.writeTo("humangenome.txt", contents);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
     
  }


}
