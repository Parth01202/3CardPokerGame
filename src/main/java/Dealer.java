import java.util.ArrayList;

public class Dealer {
    // The deck of cards used by the dealer
    private Deck theDeck;
    // The dealer's current hand
    private ArrayList<Card> dealersHand;

    // Constructor that initializes a new deck and an empty hand for the dealer
    public Dealer() {
        theDeck = new Deck();
        dealersHand = new ArrayList<>();
    }

    // Deals a hand of 3 cards to the player, ensuring the deck has enough cards
    public ArrayList<Card> dealHand() {
        // If there are 34 or fewer cards left in the deck, reset it
        if (theDeck.size() <= 34) {
            theDeck.newDeck();
        }

        // Draws 3 cards from the deck for the hand
        ArrayList<Card> hand = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            hand.add(theDeck.remove(0)); // Removes the top card from the deck
        }
        return hand; // Returns the dealt hand
    }

    // Returns the dealer's current hand
    public ArrayList<Card> getDealersHand() {
        return dealersHand;
    }

    // Sets the dealer's hand to a specified list of cards
    public void setDealersHand(ArrayList<Card> hand) {
        dealersHand = new ArrayList<>(hand); // Copies the given hand into dealersHand
    }

    // Returns the deck currently used by the dealer
    public Deck getDeck() {
        return theDeck;
    }

    // Determines if the dealer qualifies for play, based on having a high enough card
    public boolean qualifies() {
        int highestValue = 0; // Tracks the highest card value in the dealer's hand
        for (Card card : dealersHand) {
            highestValue = Math.max(highestValue, card.getValue()); // Updates highest value
        }
        return highestValue >= 12; // Dealer qualifies if highest card is a Queen or higher
    }
}
