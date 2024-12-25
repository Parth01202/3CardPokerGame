import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

class ThreeCardLogicTest {

	// Helper method to create a hand of three cards with given suits and values
	private ArrayList<Card> createHand(char s1, int v1, char s2, int v2, char s3, int v3) {
		return new ArrayList<>(Arrays.asList(new Card(s1, v1), new Card(s2, v2), new Card(s3, v3)));
	}

	// Tests for evaluating different hand rankings in Three Card Poker

	@Test
	@DisplayName("Test Straight Flush")
	void testStraightFlush() {
		ArrayList<Card> hand = createHand('H', 10, 'H', 11, 'H', 12);
		assertEquals(1, ThreeCardLogic.evaluateHand(hand), "Should be Straight Flush");
	}

	@Test
	@DisplayName("Test Three of a Kind")
	void testThreeOfAKind() {
		ArrayList<Card> hand = createHand('S', 5, 'D', 5, 'H', 5);
		assertEquals(2, ThreeCardLogic.evaluateHand(hand), "Should be Three of a Kind");
	}

	@Test
	@DisplayName("Test Straight")
	void testStraight() {
		ArrayList<Card> hand = createHand('C', 4, 'D', 5, 'H', 6);
		assertEquals(3, ThreeCardLogic.evaluateHand(hand), "Should be Straight");
	}

	@Test
	@DisplayName("Test Flush")
	void testFlush() {
		ArrayList<Card> hand = createHand('S', 2, 'S', 9, 'S', 13);
		assertEquals(4, ThreeCardLogic.evaluateHand(hand), "Should be Flush");
	}

	@Test
	@DisplayName("Test Pair")
	void testPair() {
		ArrayList<Card> hand = createHand('C', 7, 'H', 7, 'S', 3);
		assertEquals(5, ThreeCardLogic.evaluateHand(hand), "Should be Pair");
	}

	@Test
	@DisplayName("Test High Card")
	void testHighCard() {
		ArrayList<Card> hand = createHand('H', 2, 'S', 5, 'D', 9);
		assertEquals(0, ThreeCardLogic.evaluateHand(hand), "Should be High Card");
	}

	// Tests for evaluating Pair Plus winnings based on hand rankings and bets

	@Test
	@DisplayName("Test Pair Plus Winnings - Straight Flush")
	void testPPWinningsStraightFlush() {
		ArrayList<Card> hand = createHand('D', 10, 'D', 11, 'D', 12);
		assertEquals(40 * 5, ThreeCardLogic.evalPPWinnings(hand, 5), "Should win 200 for Straight Flush with bet of 5");
	}

	@Test
	@DisplayName("Test Pair Plus Winnings - Three of a Kind")
	void testPPWinningsThreeOfAKind() {
		ArrayList<Card> hand = createHand('H', 8, 'D', 8, 'S', 8);
		assertEquals(30 * 5, ThreeCardLogic.evalPPWinnings(hand, 5), "Should win 150 for Three of a Kind with bet of 5");
	}

	@Test
	@DisplayName("Test Pair Plus Winnings - Straight")
	void testPPWinningsStraight() {
		ArrayList<Card> hand = createHand('C', 3, 'D', 4, 'S', 5);
		assertEquals(6 * 5, ThreeCardLogic.evalPPWinnings(hand, 5), "Should win 30 for Straight with bet of 5");
	}

	@Test
	@DisplayName("Test Pair Plus Winnings - Flush")
	void testPPWinningsFlush() {
		ArrayList<Card> hand = createHand('H', 2, 'H', 6, 'H', 9);
		assertEquals(3 * 5, ThreeCardLogic.evalPPWinnings(hand, 5), "Should win 15 for Flush with bet of 5");
	}

	@Test
	@DisplayName("Test Pair Plus Winnings - Pair")
	void testPPWinningsPair() {
		ArrayList<Card> hand = createHand('S', 4, 'H', 4, 'D', 7);
		assertEquals(5, ThreeCardLogic.evalPPWinnings(hand, 5), "Should win 5 for Pair with bet of 5");
	}

	@Test
	@DisplayName("Test Pair Plus Winnings - High Card")
	void testPPWinningsHighCard() {
		ArrayList<Card> hand = createHand('D', 3, 'H', 7, 'S', 10);
		assertEquals(0, ThreeCardLogic.evalPPWinnings(hand, 5), "Should win 0 for High Card with bet of 5");
	}

	// Tests for comparing hands between dealer and player to determine the winner

	@Test
	@DisplayName("Test Compare Hands - Dealer Wins with Higher Ranking Hand")
	void testCompareHandsDealerWins() {
		ArrayList<Card> dealerHand = createHand('S', 9, 'S', 10, 'S', 11); // Straight Flush
		ArrayList<Card> playerHand = createHand('H', 2, 'H', 6, 'H', 9);   // Flush
		assertEquals(2, ThreeCardLogic.compareHands(dealerHand, playerHand), "Dealer should win with Straight Flush over Flush");
	}

	@Test
	@DisplayName("Test Compare Hands - Player Wins with Higher Ranking Hand")
	void testCompareHandsPlayerWins() {
		ArrayList<Card> dealerHand = createHand('D', 2, 'D', 5, 'D', 9); // Flush
		ArrayList<Card> playerHand = createHand('H', 3, 'C', 4, 'S', 5); // Straight
		assertEquals(1, ThreeCardLogic.compareHands(dealerHand, playerHand), "Player should win with Straight over Flush");
	}

	@Test
	@DisplayName("Test Compare Hands - Tie")
	void testCompareHandsTie() {
		ArrayList<Card> dealerHand = createHand('H', 7, 'S', 9, 'D', 11); // High Card
		ArrayList<Card> playerHand = createHand('C', 7, 'D', 9, 'S', 11); // High Card
		assertEquals(0, ThreeCardLogic.compareHands(dealerHand, playerHand), "Should be a tie with identical High Card hands");
	}

	// Additional tests for edge cases and special hand rankings

	@Test
	@DisplayName("Test Invalid Hand Size")
	void testInvalidHandSize() {
		ArrayList<Card> hand = new ArrayList<>(Arrays.asList(new Card('H', 2), new Card('H', 3)));
		Exception exception = assertThrows(IllegalArgumentException.class, () -> ThreeCardLogic.evaluateHand(hand));
		assertEquals("Hand must contain exactly 3 cards.", exception.getMessage());
	}

	@Test
	@DisplayName("Test Special Case Straight - Ace, 2, 3")
	void testSpecialStraightAceTwoThree() {
		ArrayList<Card> hand = createHand('S', 14, 'D', 2, 'H', 3); // Ace, 2, 3
		assertEquals(3, ThreeCardLogic.evaluateHand(hand), "Should be a Straight with A, 2, 3");
	}

	@Test
	@DisplayName("Test High Card with Similar Ranks and Different Suits")
	void testHighCardSimilarRanksDifferentSuits() {
		ArrayList<Card> hand = createHand('H', 12, 'D', 9, 'S', 6);
		assertEquals(0, ThreeCardLogic.evaluateHand(hand), "Should be High Card with non-consecutive values and mixed suits");
	}

	@Test
	@DisplayName("Test Flush but Not Straight")
	void testFlushButNotStraight() {
		ArrayList<Card> hand = createHand('C', 3, 'C', 7, 'C', 10);
		assertEquals(4, ThreeCardLogic.evaluateHand(hand), "Should be a Flush but not a Straight");
	}

	@Test
	@DisplayName("Test Straight but Not Flush")
	void testStraightButNotFlush() {
		ArrayList<Card> hand = createHand('H', 4, 'S', 5, 'D', 6);
		assertEquals(3, ThreeCardLogic.evaluateHand(hand), "Should be a Straight but not a Flush");
	}
}


class DeckTest {

	// Test if a new deck contains the correct number of cards
	@Test
	@DisplayName("Test Deck Initialization - 52 Cards")
	void testDeckInitializationSize() {
		Deck deck = new Deck();
		assertEquals(52, deck.size(), "Deck should contain 52 cards after initialization");
	}

	// Test if all cards in a deck are unique
	@Test
	@DisplayName("Test Unique Cards in Deck")
	void testDeckUniqueCards() {
		Deck deck = new Deck();
		HashSet<String> uniqueCards = new HashSet<>();
		for (Card card : deck) {
			uniqueCards.add(card.getSuit() + "-" + card.getValue());
		}
		assertEquals(52, uniqueCards.size(), "Deck should contain 52 unique cards");
	}

	// Test if deck shuffling changes the card order
	@Test
	@DisplayName("Test Deck Shuffling - Order Changes")
	void testDeckShufflingOrder() {
		Deck deck1 = new Deck();
		Deck deck2 = new Deck();
		boolean isDifferentOrder = false;

		for (int i = 0; i < deck1.size(); i++) {
			if (!deck1.get(i).equals(deck2.get(i))) {
				isDifferentOrder = true;
				break;
			}
		}

		assertTrue(isDifferentOrder, "Decks should be in different orders after shuffling");
	}

	// Test if the deck resets to 52 cards after calling newDeck
	@Test
	@DisplayName("Test New Deck - Deck Reset to 52 Cards")
	void testNewDeckReset() {
		Deck deck = new Deck();
		deck.remove(0);  // Remove one card to change the deck size
		deck.newDeck();  // Reset the deck

		assertEquals(52, deck.size(), "Deck should be reset to 52 cards after calling newDeck()");
	}

	// Test if deck contains all suits and values from 2 to 14
	@Test
	@DisplayName("Test All Suits and Values Present in Deck")
	void testAllSuitsAndValues() {
		Deck deck = new Deck();
		char[] suits = {'C', 'D', 'H', 'S'};
		boolean hasAllSuitsAndValues = true;

		for (char suit : suits) {
			for (int value = 2; value <= 14; value++) {
				final char suitFinal = suit;
				final int valueFinal = value;
				boolean found = deck.stream().anyMatch(card -> card.getSuit() == suitFinal && card.getValue() == valueFinal);
				if (!found) {
					hasAllSuitsAndValues = false;
					break;
				}
			}
			if (!hasAllSuitsAndValues) break;
		}

		assertTrue(hasAllSuitsAndValues, "Deck should contain all 4 suits and values from 2 to 14");
	}
}

class DealerTest {

	private Dealer dealer;

	// Setup before each test to create a new dealer instance
	@BeforeEach
	void setup() {
		dealer = new Dealer();
	}

	// Test if the dealer deals a hand with exactly 3 cards
	@Test
	@DisplayName("Test dealHand returns a hand of 3 cards")
	void testDealHandSize() {
		ArrayList<Card> hand = dealer.dealHand();
		assertEquals(3, hand.size(), "Dealt hand should contain 3 cards");
	}

	// Test if deck size decreases by 3 after dealing a hand
	@Test
	@DisplayName("Test deck size decreases by 3 after dealing a hand")
	void testDeckSizeAfterDealingHand() {
		int initialDeckSize = dealer.getDeck().size();
		dealer.dealHand();
		int newDeckSize = dealer.getDeck().size();
		assertEquals(initialDeckSize - 3, newDeckSize, "Deck size should decrease by 3 after dealing a hand");
	}

	// Test setDealersHand and getDealersHand methods
	@Test
	@DisplayName("Test setDealersHand and getDealersHand methods")
	void testSetAndGetDealersHand() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('H', 12));
		hand.add(new Card('D', 10));
		hand.add(new Card('S', 5));

		dealer.setDealersHand(hand);
		assertEquals(hand, dealer.getDealersHand(), "Dealer's hand should match the set hand");
	}

	// Test if dealer qualifies with a hand containing a card with value 12 or higher
	@Test
	@DisplayName("Test qualifies method with a qualifying hand")
	void testQualifiesTrue() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('H', 12)); // Highest card value is 12
		hand.add(new Card('D', 10));
		hand.add(new Card('S', 5));

		dealer.setDealersHand(hand);
		assertTrue(dealer.qualifies(), "Dealer should qualify with a hand containing a card of value 12 or higher");
	}

	// Test if dealer does not qualify with a hand where the highest card value is below 12
	@Test
	@DisplayName("Test qualifies method with a non-qualifying hand")
	void testQualifiesFalse() {
		ArrayList<Card> hand = new ArrayList<>();
		hand.add(new Card('H', 11)); // Highest card value is 11
		hand.add(new Card('D', 10));
		hand.add(new Card('S', 5));

		dealer.setDealersHand(hand);
		assertFalse(dealer.qualifies(), "Dealer should not qualify with a hand containing a highest card value below 12");
	}
}
