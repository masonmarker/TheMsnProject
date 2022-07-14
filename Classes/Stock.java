import java.util.ArrayList;
import Drawing.GraphPanel;
import MsnLib.Msn;

/**
 * Simulates a stock.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/26/2022
 */
public class Stock {

  private String ticker;

  private double currentDaySway;
  private double currentValue;

  private double startValueToday = 300;
  private double percentGain;

  ArrayList<Double> valuesOverTime;

  ArrayList<Investor> investors;

  public Stock(String ticker) {
    currentDaySway = 0;
    currentValue = startValueToday;
    this.ticker = ticker;
    investors = new ArrayList<>();
    valuesOverTime = new ArrayList<>();
    valuesOverTime.add(currentValue);
  }

  /**
   * Simulates a single step for this Stock, the Stock could either go up or down.
   */
  public void step() {
    currentDaySway = Msn.decFormat(Msn.random(-1, 1), 2);
    currentValue += currentDaySway;
    if (currentValue < 0)
      currentValue = 0;
    percentGain = (currentValue - startValueToday) / Math.abs(currentValue) * 100;
    for (Investor i : investors) {
      i.setMoney(i.getStartedWith() + i.getStartedWith() * (percentGain / 100));
      if (i.getMoney() < 0)
        i.setMoney(0);
      i.setPercentGain((i.getMoney() - i.getStartedWith()) / Math.abs(i.getMoney()) * 100);
    }
    valuesOverTime.add(currentValue);
  }

  public void addInvestor(Investor investor) {
    investors.add(investor);
  }

  public void buy(String investorName, double amount) {
    Investor i = getInvestorByName(investorName);
    i.setMoney(i.getMoney() + amount);
    i.setStartedWith(i.getMoney());
  }

  public void sell(String investorName, double amount) {
    Investor i = getInvestorByName(investorName);
    i.setMoney(i.getMoney() - amount);
    i.setStartedWith(i.getMoney());
  }

  public Investor getInvestorByName(String name) {
    for (Investor i : investors)
      if (i.name.equals(name))
        return i;
    return null;
  }

  public String ticker() {
    return ticker;
  }

  public double currentValue() {
    return currentValue;
  }

  public double startValueToday() {
    return startValueToday;
  }

  public double percentGain() {
    return percentGain;
  }

  public void visualize() {
    new GraphPanel(valuesOverTime).createAndShowGui();
  }

  public String toString() {
    String s = "---- stock ----\n";
    s += "ticker: " + ticker + "\n";
    s += "start value today: " + Msn.moneyFormat(startValueToday) + "\n";
    s += "current value: " + Msn.moneyFormat(currentValue) + "\n";
    s += "percent gain: %" + percentGain;
    return s;
  }

}


