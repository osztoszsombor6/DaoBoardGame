package boardgame;

import boardgame.BoardGameController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LaunchController {

    @FXML
    private TextField player1Textfield;
    @FXML
    private TextField player2Textfield;

    @FXML
    private Label errorLabel;

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (player1Textfield.getText().isEmpty() || player2Textfield.getText().isEmpty()) {
            errorLabel.setText("* Player names cannot be empty!");
        } else {
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<BoardGameController>getController().initdata(player1Textfield.getText(),player2Textfield.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            
            log.info("Player1 is set to {}, Player 2 is set to {}, loading game scene.", player1Textfield.getText(), player2Textfield.getText());
        }

    }
}