import java.util.ArrayList;
import java.util.Collections;
import MsnLib.Msn;

/**
 * Contains 52 Card objects.
 * 
 * @author Mason Marker
 * @version 1.0 - 05/29/2021
 */
public class Deck {

  private ArrayList<Card> cards;

  /**
   * Constructs a Deck of Cards.
   * 
   * @param shuffled whether to shuffle this Deck upon creation.
   */
  public Deck(boolean shuffled) {
    if (shuffled) {
      setup();
      shuffle();
    } else
      setup();
  }

  /**
   * Resets this Deck to the default Deck.
   */
  public void reset() {
    setup();
  }

  /**
   * Empties this Deck.
   */
  public void empty() {
    cards = new ArrayList<>();
  }

  /**
   * Adds a Card to this Deck.
   * 
   * @param c the Card to add
   */
  public void add(Card c) {
    cards.add(c);
  }

  /**
   * Checks whether the given Card is a Jack.
   * 
   * @param c the Card to check
   * @return whether the Card is a Jack or not
   */
  public boolean isJack(Card c) {
    return c.value() == 11;
  }

  /**
   * Checks whether the given Card is a Queen.
   * 
   * @param c the Card to check
   * @return whether the Card is a Queen or not
   */
  public boolean isQueen(Card c) {
    return c.value() == 12;
  }

  /**
   * Checks whether the given Card is a King.
   * 
   * @param c the Card to check
   * @return whether the Card is a King or not
   */
  public boolean isKing(Card c) {
    return c.value() == 13;
  }

  /**
   * Checks whether the given Card is an Ace.
   * 
   * @param c the Card to check
   * @return whether the Card is an Ace or not
   */
  public boolean isAce(Card c) {
    return c.value() == 14;
  }

  /**
   * Draws a Card from this Deck.
   * 
   * Boolean remove decides whether the Card will be removed from the Deck, or placed in a random
   * location in the Deck between 1 and 50.
   * 
   * @param remove whether to remove the Card or not
   * @return the Card drawn
   */
  public Card draw(boolean remove) {
    if (remove)
      return cards.remove(0);
    Card removed = cards.remove(0);
    cards.add(Msn.randomInt(1, 50), removed);
    return removed;
  }

  /**
   * Shuffles this Deck.
   */
  public void shuffle() {
    Collections.shuffle(cards);
  }

  /**
   * Gets the size of this Deck.
   * 
   * @return the amount of cards in this Deck
   */
  public int size() {
    return cards.size();
  }

  /**
   * Gets all Cards in the current Deck.
   * 
   * @return the cards
   */
  public Card[] getCards() {
    return cards.toArray(Card[]::new);
  }

  /**
   * String representation of this Deck.
   */
  public String toString() {
    return cards.toString();
  }

  /**
   * Recreates the Deck with 52 playing Cards.
   */
  private void setup() {
    cards = new ArrayList<>();
    for (int i = 2; i < 11; i++)
      cards.add(new Card("black", "clubs", i));
    for (int i = 2; i < 11; i++)
      cards.add(new Card("black", "spades", i));
    for (int i = 2; i < 11; i++)
      cards.add(new Card("red", "hearts", i));
    for (int i = 2; i < 11; i++)
      cards.add(new Card("red", "diamonds", i));

    cards.add(new Card("black", "clubs", 11));
    cards.add(new Card("black", "spades", 11));
    cards.add(new Card("black", "hearts", 11));
    cards.add(new Card("black", "diamonds", 11));

    cards.add(new Card("black", "clubs", 12));
    cards.add(new Card("black", "spades", 12));
    cards.add(new Card("black", "hearts", 12));
    cards.add(new Card("black", "diamonds", 12));

    cards.add(new Card("black", "clubs", 13));
    cards.add(new Card("black", "spades", 13));
    cards.add(new Card("black", "hearts", 13));
    cards.add(new Card("black", "diamonds", 13));

    cards.add(new Card("black", "clubs", 14));
    cards.add(new Card("black", "spades", 14));
    cards.add(new Card("black", "hearts", 14));
    cards.add(new Card("black", "diamonds", 14));
  }

  /**
   * Card class.
   * 
   * @author Mason Marker
   * @version 1.0 - 05/29/2021
   */
  class Card implements Comparable<Card> {
    private String color;
    private String suit;
    private int value;

    /**
     * Card constructor.
     * 
     * @param color the color of the Card
     * @param suit the suit of the Card
     * @param value the value of the Card
     */
    public Card(String color, String suit, int value) {
      this.color = color;
      this.suit = suit;
      this.value = value;
    }

    /**
     * Returns the color of this Card.
     * 
     * @return the color
     */
    public String color() {
      return color;
    }

    /**
     * Returns the suit of this Card.
     * 
     * @return the suit
     */
    public String suit() {
      return suit;
    }

    /**
     * Returns the suit of this Card.
     * 
     * @return the suit
     */
    public int value() {
      return value;
    }

    /**
     * String representation of this Card.
     */
    public String toString() {
      return "{Color=" + color + ", " + "Suit=" + suit + ", " + "value=" + value + "}";
    }

    @Override
    public int compareTo(Card c) {
      Card comparingTo = (Card) c;
      if (comparingTo.value() > value)
        return -1;
      else if (comparingTo.value() < value)
        return 1;
      else
        return 0;
    }
  }
}
