
public class TreeTesting {

  public static void main(String[] args) {
    
    MsnNode<Integer> root = new MsnNode<>(5);
    
    root.addChild(new MsnNode<>(8));
    root.addChild(new MsnNode<>(1));
    
    root.removeChild(root.getChild(0));
   
    System.out.println(root.children());
    
  }
  
  
  
  
}
