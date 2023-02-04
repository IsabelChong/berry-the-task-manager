package berry;

import berry.ui.Ui;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for MainWindow. Provides the layout for the other controls.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Berry berry;

    private Stage stage;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/userDefault.png"));
    private Image berryImage = new Image(this.getClass().getResourceAsStream("/images/berryDefault.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        initialiseWelcomeMessage();
    }

    public void setBerry(Berry berry) {
        this.berry = berry;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Berry's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    public void handleUserInput() {
        String input = userInput.getText();
        String response = berry.getResponse(input);

        if (input.equals("bye")) {
            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> stage.close());
            pause.play();
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBerryDialog(response, berryImage)
        );
        userInput.clear();
    }

    private void initialiseWelcomeMessage() {
        String dukeText = Ui.showWelcome();

        dialogContainer.getChildren().addAll(
                DialogBox.getBerryDialog(dukeText, berryImage)
        );
    }

}