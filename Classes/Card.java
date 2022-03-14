
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