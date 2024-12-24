import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThreeCardLogic {
    // Constants representing hand rankings
    private static final int HIGH_CARD = 0;
    private static final int STRAIGHT_FLUSH = 1;
    private static final int THREE_OF_A_KIND = 2;
    private static final int STRAIGHT = 3;
    private static final int FLUSH = 4;
    private static final int PAIR = 5;

    // Evaluates a hand and returns its ranking
    public static int evaluateHand(ArrayList<Card> hand) {
        // Hand must contain exactly 3 cards
        if (hand == null || hand.size() != 3) {
            throw new IllegalArgumentException("Hand must contain exactly 3 cards.");
        }

        // Determine if the hand is a flush or a straight
        boolean isFlush = isFlush(hand);
        boolean isStraight = isStraight(hand);
        Map<Integer, Integer> valueCount = getValueCount(hand);

        // Determine hand ranking based on conditions
        if (isFlush && isStraight) return STRAIGHT_FLUSH;
        if (valueCount.containsValue(3)) return THREE_OF_A_KIND;
        if (isStraight) return STRAIGHT;
        if (isFlush) return FLUSH;
        if (valueCount.containsValue(2)) return PAIR;

        // Default to high card if no other ranking applies
        return HIGH_CARD;
    }

    // Calculates Pair Plus winnings based on the hand type and bet amount
    public static int evalPPWinnings(ArrayList<Card> hand, int bet) {
        if (bet <= 0) return 0; // No winnings for invalid bets

        int handValue = evaluateHand(hand);
        // Multipliers for each hand type
        switch (handValue) {
            case STRAIGHT_FLUSH: return bet * 40;
            case THREE_OF_A_KIND: return bet * 30;
            case STRAIGHT: return bet * 6;
            case FLUSH: return bet * 3;
            case PAIR: return bet;
            default: return 0; // No winnings for high card
        }
    }

    // Compares dealer and player hands, returning the winner
    public static int compareHands(ArrayList<Card> dealer, ArrayList<Card> player) {
        int dealerValue = evaluateHand(dealer);
        int playerValue = evaluateHand(player);

        // Compare hand rankings first
        if (dealerValue != playerValue) {
            return dealerValue > playerValue ? 1 : 2;
        }

        // If rankings are equal, compare card values within each hand
        ArrayList<Integer> dealerValues = new ArrayList<>();
        ArrayList<Integer> playerValues = new ArrayList<>();

        for (Card card : dealer) dealerValues.add(card.getValue());
        for (Card card : player) playerValues.add(card.getValue());

        Collections.sort(dealerValues, Collections.reverseOrder());
        Collections.sort(playerValues, Collections.reverseOrder());

        // Compare each card in descending order
        for (int i = 0; i < 3; i++) {
            if (dealerValues.get(i) > playerValues.get(i)) return 1;
            if (playerValues.get(i) > dealerValues.get(i)) return 2;
        }

        // Return 0 if hands are identical (a tie)
        return 0;
    }

    // Helper method to check if all cards in a hand are the same suit
    private static boolean isFlush(ArrayList<Card> hand) {
        char suit = hand.get(0).getSuit();
        return hand.stream().allMatch(card -> card.getSuit() == suit);
    }

    // Helper method to check if the hand is a straight
    private static boolean isStraight(ArrayList<Card> hand) {
        ArrayList<Integer> values = new ArrayList<>();
        for (Card card : hand) {
            values.add(card.getValue());
        }
        Collections.sort(values);

        // Special case: A-2-3 is considered a straight
        if (values.get(0) == 2 && values.get(1) == 3 && values.get(2) == 14) {
            return true;
        }

        // Normal straight: consecutive values
        return values.get(2) - values.get(0) == 2 && values.get(1) - values.get(0) == 1;
    }

    // Helper method to count occurrences of each card value in a hand
    private static Map<Integer, Integer> getValueCount(ArrayList<Card> hand) {
        Map<Integer, Integer> valueCount = new HashMap<>();
        for (Card card : hand) {
            valueCount.merge(card.getValue(), 1, Integer::sum);
        }
        return valueCount;
    }
}