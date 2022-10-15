import java.util.ArrayList;
import Drawing.GraphPanel;
import MsnLib.Msn;

/**
 * Acts as an Investor for a Stock.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/27/2022
 */
public class Investor {

  public String name;
  
  private double money;
  private double startedWith;
  private double percentGain;
  
  private ArrayList<Double> moneyOverTime;
  private ArrayList<Trade> tradesOverTime;
  
  
  public Investor(String name, double money) {
    this.name = name;
    this.money = money;
    this.startedWith = money;
    this.percentGain = 0;
    moneyOverTime = new ArrayList<>();
    moneyOverTime.add(money);
  }

  public double getMoney() {
    return money;
  }

  public void setMoney(double money) {
    this.money = money;
    moneyOverTime.add(money);
  }

  public double getStartedWith() {
    return startedWith;
  }

  public void setStartedWith(double startedWith) {
    this.startedWith = startedWith;
  }

  public double getPercentGain() {
    return percentGain;
  }

  public void setPercentGain(double percentGain) {
    this.percentGain = percentGain;
  }
  
  public void visualize() {
    new GraphPanel(moneyOverTime).createAndShowGui();
  }

  public String toString() {
    String s = "---- investor ----\n";
    s += "money: " + Msn.moneyFormat(money) + "\n";
    s += "started with: " + Msn.moneyFormat(startedWith) + "\n";
    s += "percent gain: %" + (percentGain * 100) + "\n";
    s += "----------------\n";
    return s;
  }


}
