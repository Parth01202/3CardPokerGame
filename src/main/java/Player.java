import java.util.ArrayList;

public class Player {
    // Attributes for the player's hand, bets, and total winnings
    private ArrayList<Card> hand;
    private int anteBet;
    private int playBet;
    private int pairPlusBet;
    private int totalWinnings;

    // Constructor initializes a new player with an empty hand and zero total winnings
    public Player() {
        hand = new ArrayList<>();
        totalWinnings = 0;
    }

    // Setter for the Ante Bet
    public void setAnteBet(int anteBet) {
        this.anteBet = anteBet;
    }

    // Setter for the Play Bet
    public void setPlayBet(int playBet) {
        this.playBet = playBet;
    }

    // Setter for the Pair Plus Bet
    public void setPairPlusBet(int pairPlusBet) {
        this.pairPlusBet = pairPlusBet;
    }

    // Getter for the Ante Bet
    public int getAnteBet() {
        return anteBet;
    }

    // Getter for the Play Bet
    public int getPlayBet() {
        return playBet;
    }

    // Getter for the Pair Plus Bet
    public int getPairPlusBet() {
        return pairPlusBet;
    }

    // Returns the player's current hand of cards
    public ArrayList<Card> getHand() {
        return hand;
    }

    // Replaces the player's current hand with a new hand
    public void receiveHand(ArrayList<Card> newHand) {
        hand.clear();
        hand.addAll(newHand);
    }

    // Getter for the player's total winnings
    public int getTotalWinnings() {
        return totalWinnings;
    }

    // Updates the player's total winnings by adding a specified amount
    public void updateWinnings(int amount) {
        totalWinnings += amount;
    }

    // Resets all bets to zero at the end of a round
    public void clearBets() {
        anteBet = 0;
        playBet = 0;
        pairPlusBet = 0;
    }
}
