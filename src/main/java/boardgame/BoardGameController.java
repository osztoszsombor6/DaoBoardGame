package boardgame;

import javafx.beans.binding.ObjectBinding;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

import boardgame.model.BoardGameModel;
import boardgame.model.BoardPosition;
import boardgame.model.GameResult;
import boardgame.model.GameResultDao;
import boardgame.model.Square;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * Class for controlling the match and the game scene.
 */
@Slf4j
public class BoardGameController {

    @FXML
    private GridPane board;
    
    private GameResultDao gameResultDao;
    
    private String player1Name;
    private String player2Name;
    private String winnerName;
    private String winCondition;
    
    private ZonedDateTime started;
    private ZonedDateTime finished;
    
    @FXML
    private Label player1Label;
    @FXML
    private Label player2Label;
    @FXML
    private Label winnerLabel;
    
    @FXML
    private Button continueButton;

    private BoardGameModel model = new BoardGameModel();
    int selectedX;
    int selectedY;
    private boolean gameEnded = false;

    @FXML
    private void initialize() {
        selectedX = -1;
        selectedY = -1;
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare(i, j);
                board.add(square, j, i);
            }
        }
    }
    
    private void setEndState(){
        gameEnded = true;
        finished = ZonedDateTime.now();
        if(model.getWinner() == Square.PLAYER1){
            winnerName = player1Name;
        } else {
            winnerName = player2Name;
        }
    }
    
    private void persistGame() {
        
        Duration duration = Duration.between(started, finished);
        
        GameResult result = GameResult.builder()
                .player1(player1Name)
                .player2(player2Name)
                .duration(duration)
                .winner(winnerName)
                .winCondition(winCondition)
                .started(started)
                .finished(finished)
                .build();
        
        gameResultDao.persist(result);
        
    }
    
    private void showEndState(){
        
        winnerLabel.setText("Winner: " + winnerName);
        continueButton.setVisible(true);
    }
    
    private void markActiveLabel(){
        if(model.getCurrentPlayer() == Square.PLAYER1){
            player1Label.getStyleClass().add("activePlayer1");
            player2Label.getStyleClass().remove("activePlayer2");
        } else {
            player2Label.getStyleClass().add("activePlayer2");
            player1Label.getStyleClass().remove("activePlayer1");
        }
    }
    
    public void initdata(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.winnerName = "";
        player1Label.setText("Player 1: " + this.player1Name);
        player2Label.setText("Player 2: " + this.player2Name);
        started = ZonedDateTime.now();
        markActiveLabel();
    }
    
    public Node getNodeByRowColumnIndex (final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        
        return result;
    }

    private StackPane createSquare(int i, int j) {
        var square = new StackPane();
        square.getStyleClass().add("square");
        var piece = new Circle(50);
        
        piece.fillProperty().bind(
                new ObjectBinding<Paint>() {
                    {
                        super.bind(model.squareProperty(i, j));
                    }
                    @Override
                    protected Paint computeValue() {
                        return switch (model.squareProperty(i, j).get()) {
                            case NONE -> Color.TRANSPARENT;
                            case PLAYER1 -> Color.RED;
                            case PLAYER2 -> Color.BLUE;
                        };
                    }
                }
        );
        
        square.getChildren().add(piece);
        square.setOnMouseClicked(this::handleMouseClick);
        return square;
    }
    
    private void selectSquare(StackPane square){
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        square.getStyleClass().add("selected");
            selectedX = row;
            selectedY = col;
            BoardPosition selectedPos = new BoardPosition(selectedX,selectedY);
            
            for (int i = 0; i < board.getRowCount(); i++) {
                for (int j = 0; j < board.getColumnCount(); j++) {
                    if (i != selectedX || j != selectedY) {
                        Node n = getNodeByRowColumnIndex(i, j, board);
                        
                        if (model.legalMove(selectedPos, new BoardPosition(i,j))) {
                            n.getStyleClass().add("step");
                        } else {
                            n.getStyleClass().remove("step");
                        }
                    }
                }
            }
    }
    
    private void moveSelectedSquare(StackPane square, BoardPosition selectedPos){
        
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        
        if(model.legalMove(selectedPos,new BoardPosition(row,col))){
                    log.info("{} moving from ({},{}) to ({},{})", model.getCurrentPlayer(), selectedX, selectedY, row, col);
                    model.move(selectedPos,new BoardPosition(row,col));
                    markActiveLabel();
                }
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if(gameEnded){
            return;
        }
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        log.debug("Click on square ({},{})", row, col);
        
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                Node n = getNodeByRowColumnIndex(i, j, board);
                n.getStyleClass().remove("selected");
                n.getStyleClass().remove("step");
            }
        }
        
        if((selectedX == -1 && selectedY == -1) && model.getSquare(row, col) == model.getCurrentPlayer()){
            selectSquare(square);
        } else {
            if(selectedX != -1 && selectedY !=-1){
                BoardPosition selectedPos = new BoardPosition(selectedX,selectedY);
                moveSelectedSquare(square, selectedPos);
                selectedX = -1;
                selectedY = -1;
                
                if(model.isEndState2x2()
                        || model.isEndStateCol()
                        || model.isEndStateRow()
                        || model.isEndStateCorners()
                        || model.isEndStateCornered()){
                    
                    if(model.isEndState2x2()){
                        log.info("Game ended, {} wins with 2x2 area", model.getWinner());
                        winCondition = "2 by 2 area";
                    }
                    if(model.isEndStateCol()){
                        log.info("Game ended, {} wins with column", model.getWinner());
                        winCondition = "Column";
                    }
                    if(model.isEndStateRow()){
                        log.info("Game ended, {} wins with row", model.getWinner());
                        winCondition = "Row";
                    }
                    if(model.isEndStateCorners()){
                        log.info("Game ended, {} wins with 4 corners", model.getWinner());
                        winCondition = "4 Corners";
                    }
                    if(model.isEndStateCornered()){
                        log.info("Game ended, {} wins, cornered disk", model.getWinner());
                        winCondition = "Cornered";
                    }
                    setEndState();
                    gameResultDao = GameResultDao.getInstance();
                    persistGame();
                    showEndState();
                }
            }
            
        }

    }
    
    public void continueAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/latestten.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
        log.info("Loading latest ten matches scene");
    }
}
