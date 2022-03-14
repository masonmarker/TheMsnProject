import java.util.Arrays;

public class Hand {
  Card[] cards;
  
  public Hand() {
    cards = new Card[5];
  }
  
  public Hand(Card[] cards) {
    this.cards = cards;
  }
  
  public Card[] getCards() {
    return cards;
  }
  
  public void setCards(Card[] cards) {
    this.cards = cards;
  }
  
  public String toString() {
    return Arrays.toString(cards);
  }
}
