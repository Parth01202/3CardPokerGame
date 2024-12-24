import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

public class JavaFXTemplate extends Application {

	// Text fields for showing cards and taking bets
	@FXML private Text playerOneCards, playerTwoCards, dealerCards;
	@FXML private TextField anteBetField1, pairPlusBetField1, playBetField1;
	@FXML private TextField anteBetField2, pairPlusBetField2, playBetField2;
	@FXML private Label totalWinnings1, totalWinnings2, gameStatus;
	@FXML private Button playButton1, foldButton1, playButton2, foldButton2;

	private Scene gameScene;
	private Player playerOne = new Player();
	private Player playerTwo = new Player();
	private Dealer theDealer = new Dealer();
	private boolean playerOnePlay = false;
	private boolean playerTwoPlay = false;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("3 Card Poker");

		// Load Game Screen and Welcome Screen
		FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/Fxml/GameUI.fxml"));
		Parent gameRoot = gameLoader.load();
		gameScene = new Scene(gameRoot, 800, 600);
		System.out.println("Game Screen Loaded");

		FXMLLoader welcomeLoader = new FXMLLoader(getClass().getResource("/Fxml/WelcomeScreen.fxml"));
		welcomeLoader.setController(this);  // Set this class as the controller manually
		Parent welcomeRoot = welcomeLoader.load();
		System.out.println("Welcome Screen Loaded");

		Scene welcomeScene = new Scene(welcomeRoot, 800, 600);
		primaryStage.setScene(welcomeScene);  // Start with the Welcome Screen
		primaryStage.show();
	}

	// Start the game by switching to the Game Screen
	@FXML
	private void startGame(ActionEvent event) {
		System.out.println("Play Game button clicked");
		Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
		stage.setScene(gameScene);
		System.out.println("Game Scene set successfully");
	}

	// Exit the game application
	@FXML
	private void exitGame() {
		Stage stage = (Stage) gameScene.getWindow();
		stage.close();
	}

	// Handle Options menu actions like "Exit," "Fresh Start," and "New Look"
	@FXML
	private void handleOptions(ActionEvent event) {
		MenuItem source = (MenuItem) event.getSource();
		switch (source.getText()) {
			case "Exit":
				showExitConfirmation();
				break;
			case "Fresh Start":
				resetGame();
				break;
			case "New Look":
				applyNewLook();
				break;
		}
	}

	// Show a confirmation dialog before exiting
	public void showExitConfirmation() {
		Alert exitAlert = new Alert(AlertType.CONFIRMATION);
		exitAlert.setTitle("Exit Confirmation");
		exitAlert.setContentText("Are you sure you want to quit?");
		exitAlert.initModality(Modality.APPLICATION_MODAL);

		exitAlert.showAndWait().ifPresent(response -> {
			if (response == ButtonType.OK) {
				System.exit(0);
			}
		});
	}

	// Deal cards and check if the bets are valid
	@FXML
	private void dealCards() {
		try {
			int anteBet1 = Integer.parseInt(anteBetField1.getText());
			int pairPlusBet1 = Integer.parseInt(pairPlusBetField1.getText());
			int anteBet2 = Integer.parseInt(anteBetField2.getText());
			int pairPlusBet2 = Integer.parseInt(pairPlusBetField2.getText());

			if (!isValidBet(anteBet1) || !isValidBet(pairPlusBet1) || !isValidBet(anteBet2) || !isValidBet(pairPlusBet2)) {
				gameStatus.setText("All bets must be between $5 and $25.");
				return;
			}

			// Distribute cards to players and dealer
			ArrayList<Card> playerHand1 = new ArrayList<>();
			ArrayList<Card> playerHand2 = new ArrayList<>();
			ArrayList<Card> dealerHand = new ArrayList<>();
			for (int i = 0; i < 3; i++) {
				playerHand1.add(theDealer.getDeck().remove(0));
				playerHand2.add(theDealer.getDeck().remove(0));
				dealerHand.add(theDealer.getDeck().remove(0));
			}

			// Display the cards and enable play/fold buttons
			playerOne.receiveHand(playerHand1);
			playerTwo.receiveHand(playerHand2);
			theDealer.setDealersHand(dealerHand);
			playerOneCards.setText(formatHand(playerHand1));
			playerTwoCards.setText(formatHand(playerHand2));
			dealerCards.setText("Hidden");

			playerOnePlay = false;
			playerTwoPlay = false;
			playButton1.setDisable(false);
			foldButton1.setDisable(false);
			playButton2.setDisable(false);
			foldButton2.setDisable(false);

			gameStatus.setText("Players, choose to Play or Fold.");
		} catch (NumberFormatException e) {
			gameStatus.setText("Please enter valid numeric values for all bets.");
		}
	}

	// Handle players' decisions to play or fold
	@FXML
	private void handlePlayerDecision(ActionEvent event) {
		Button clickedButton = (Button) event.getSource();
		boolean isPlayerOne = clickedButton == playButton1 || clickedButton == foldButton1;
		boolean isPlaying = clickedButton.getText().equals("Play");

		// Disable play/fold buttons after a choice
		if (isPlayerOne) {
			playerOnePlay = isPlaying;
			playButton1.setDisable(true);
			foldButton1.setDisable(true);
		} else {
			playerTwoPlay = isPlaying;
			playButton2.setDisable(true);
			foldButton2.setDisable(true);
		}

		// Handle both players' choices
		if (playButton1.isDisabled() && playButton2.isDisabled()) {
			if (!playerOnePlay && !playerTwoPlay) {
				gameStatus.setText("Both players folded. No further actions.");
			} else {
				revealDealerCards();
				if (playerOnePlay) updateWinningsForFold(playerOne, anteBetField2, totalWinnings1);
				if (playerTwoPlay) updateWinningsForFold(playerTwo, anteBetField1, totalWinnings2);
			}
		}
	}

	// Reset game to start fresh
	@FXML
	private void resetGame() {
		playerOne = new Player();
		playerTwo = new Player();
		theDealer = new Dealer();
		gameStatus.setText("Game reset! Place your bets.");
		playerOneCards.setText("");
		playerTwoCards.setText("");
		dealerCards.setText("Hidden");
		anteBetField1.clear();
		pairPlusBetField1.clear();
		playBetField1.clear();
		anteBetField2.clear();
		pairPlusBetField2.clear();
		playBetField2.clear();
		totalWinnings1.setText("0");
		totalWinnings2.setText("0");
	}

	// Apply new styling to the game interface
	@FXML
	private void applyNewLook() {
		gameStatus.getScene().getStylesheets().clear();
		gameStatus.getScene().getStylesheets().add(getClass().getResource("/CSS/newlook.css").toExternalForm());
	}

	// Reveal the dealer's hand and evaluate each player's outcome
	private void revealDealerCards() {
		dealerCards.setText(formatHand(theDealer.getDealersHand()));
		gameStatus.setText("Dealer reveals their hand.");
		evaluateHand(playerOne, playerOnePlay, anteBetField1, playBetField1, pairPlusBetField1, totalWinnings1);
		evaluateHand(playerTwo, playerTwoPlay, anteBetField2, playBetField2, pairPlusBetField2, totalWinnings2);
	}

	// Check if a bet amount is valid
	private boolean isValidBet(int bet) {
		return bet >= 5 && bet <= 25;
	}

	// Format a hand for display
	private String formatHand(ArrayList<Card> hand) {
		StringBuilder handText = new StringBuilder();
		for (Card card : hand) {
			handText.append(card.toString()).append(" ");
		}
		return handText.toString().trim();
	}

	// Update winnings if one player folds
	private void updateWinningsForFold(Player winner, TextField opponentAnteField, Label winnerWinningsLabel) {
		try {
			int opponentAnteBet = Integer.parseInt(opponentAnteField.getText());
			winner.updateWinnings(opponentAnteBet);
			winnerWinningsLabel.setText(String.valueOf(winner.getTotalWinnings()));
		} catch (NumberFormatException e) {
			gameStatus.setText("Error updating winnings. Ensure all bets are valid numbers.");
		}
	}

	// Evaluate and update winnings for each player's hand
	private void evaluateHand(Player player, boolean play, TextField anteField, TextField playField, TextField pairPlusField, Label winningsLabel) {
		if (!play) return;

		ArrayList<Card> playerHand = player.getHand();
		ArrayList<Card> dealerHand = theDealer.getDealersHand();
		int anteBet = Integer.parseInt(anteField.getText());
		int playBet = Integer.parseInt(playField.getText());
		int pairPlusBet = Integer.parseInt(pairPlusField.getText());

		int winnings = 0;

		// Calculate winnings based on dealer's hand qualification
		if (theDealer.qualifies()) {
			if (ThreeCardLogic.compareHands(dealerHand, playerHand) == 2) {
				winnings += anteBet * 2 + playBet * 2;
			} else {
				winnings -= anteBet + playBet;
			}
		} else {
			winnings += playBet;
		}

		// Add pair-plus bet winnings
		winnings += ThreeCardLogic.evalPPWinnings(playerHand, pairPlusBet);
		player.updateWinnings(winnings);
		winningsLabel.setText(String.valueOf(player.getTotalWinnings()));
	}
}
