public class Card {
    // Suit of the card: 'C' = Clubs, 'D' = Diamonds, 'H' = Hearts, 'S' = Spades
    private char suit;

    // Value of the card: 2 to 14, where 14 represents Ace
    private int value;

    // Constructor to initialize card suit and value
    public Card(char suit, int value) {
        this.suit = suit;
        this.value = value;
    }

    // Getter for the card's suit
    public char getSuit() {
        return suit;
    }

    // Getter for the card's value
    public int getValue() {
        return value;
    }

    // Provides a string representation of the card
    @Override
    public String toString() {
        String valueStr;

        // Convert face card values and Ace to their respective symbols
        switch (value) {
            case 14: valueStr = "A"; break; // Ace
            case 13: valueStr = "K"; break; // King
            case 12: valueStr = "Q"; break; // Queen
            case 11: valueStr = "J"; break; // Jack
            default: valueStr = String.valueOf(value); // Numeric values (2-10)
        }

        // Combine suit and value for card display
        return suit + valueStr;
    }
}
