public class StockTesting {

  public static void main(String[] args) throws InterruptedException {
    Stock s = new Stock("AAPL");
    Investor mason = new Investor("mason", 400);
    s.addInvestor(mason);


    for (int i = 0; i < 100; i++) {
      if (i == 80)
        s.sell("mason", 10);
      s.step();
    }

    
    s.visualize();
    mason.visualize();


  }

}
