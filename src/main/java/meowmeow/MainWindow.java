package meowmeow;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
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

    private MeowMeow meowMeow;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/Meow2.JPG"));
    private Image meowMeowImage = new Image(this.getClass().getResourceAsStream("/images/Meow1.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the MeowMeow instance.
     * Injects the MeowMeow instance and shows its greeting.
     *
     * @param m Chatbot instance to route user input to.
     */
    public void setMeowMeow(MeowMeow m) {
        meowMeow = m;
        dialogContainer.getChildren().add(
                DialogBox.getMeowMeowDialog(meowMeow.getWelcome(), meowMeowImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing MeowMeow's reply and then appends
     * them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = meowMeow.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getMeowMeowDialog(response, meowMeowImage)
        );
        userInput.clear();
    }
}