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
import boardgame.model.GameResult;
import boardgame.model.GameResultDao;
import boardgame.model.Square;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
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

@Slf4j
public class BoardGameController {

    @FXML
    private GridPane board;
    
    private GameResultDao gameResultDao;
    
    private String player1Name;
    private String player2Name;
    private String winnerName;
    
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
    int x;
    int y;
    private boolean gameEnded = false;

    @FXML
    private void initialize() {
        x = -1;
        y = -1;
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
                .started(started)
                .finished(finished)
                .build();
        
        gameResultDao.persist(result);
//        List<GameResult> list = gameResultDao.findLatest(10);
//        for(GameResult r : list){
//            System.out.println(r.getWinner() + " " + r.getFinished());
//        }
        
//        System.out.println("Persistgame");
//        System.out.println(player1Name);
//        System.out.println(player2Name);
//        System.out.println(winnerName);
//        System.out.println(started);
//        System.out.println(finished);
        
    }
    
    private void showEndState(){
        
        winnerLabel.setText("Winner: " + winnerName);
        continueButton.setVisible(true);
    }
    
    private void markActiveLabel(){
        if(model.getCurrentPlayer() == Square.PLAYER1){
            //List<String> temp = player1Label.getStyleClass();
            player1Label.getStyleClass().add("activePlayer1");
            player2Label.getStyleClass().remove("activePlayer2");
            //temp = player1Label.getStyleClass();
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
        gameResultDao = GameResultDao.getInstance();
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
/*
        piece.fillProperty().bind(Bindings.when(model.squareProperty(i, j).isEqualTo(Square.NONE))
                .then(Color.TRANSPARENT)
                .otherwise(Bindings.when(model.squareProperty(i, j).isEqualTo(Square.HEAD))
                        .then(Color.RED)
                        .otherwise(Color.BLUE))
        );
*/

//        piece.strokeProperty().bind(
//                new ObjectBinding<Paint>() {
//                    {
//                        super.bind(model.squareProperty(i, j));
//                    }
//                    @Override
//                    protected Paint computeValue() {
//                        
//                        if (i == x || j == y) {
//                            return Color.BLACK;
//                        }
//                        return Color.WHITESMOKE;
//                    }
//                }
//        );
        
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
    

    @FXML
    private void handleMouseClick(MouseEvent event) {
        if(gameEnded){
            return;
        }
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        //System.out.printf("Click on square (%d,%d)\n", row, col);
        log.debug("Click on square ({},{})\n", row, col);
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                Node n = getNodeByRowColumnIndex(i, j, board);
                n.getStyleClass().remove("kivalasztott");
                n.getStyleClass().remove("lephet");
            }
        }
        
        if((x == -1 && y == -1) && model.getSquare(row, col) == model.getCurrentPlayer()){
            log.info(model.getSquare(row, col).toString());
            log.info(model.getCurrentPlayer().toString());
            square.getStyleClass().add("kivalasztott");
            x = row;
            y = col;
            
            for (int i = 0; i < board.getRowCount(); i++) {
                for (int j = 0; j < board.getColumnCount(); j++) {
                    if (i != x || j != y) {
                        Node n = getNodeByRowColumnIndex(i, j, board);
                        boolean legalmove = model.legalMove(x, y, i, j);
                        if (legalmove) {
                            n.getStyleClass().add("lephet");
                        } else {
                            n.getStyleClass().remove("lephet");
                        }
                    }
                }
            }
        } else {
            if(x != -1 && y !=-1){
                System.out.println("lephet(" + x + "," + y + "," + row + "," + col +")");
                if(model.legalMove(x,y,row,col)){
                model.move(x, y, row, col);
                markActiveLabel();
                
                }
                x = -1;
                y = -1;
                if(model.isEndState2x2()
                        || model.isEndStateCol()
                        || model.isEndStateRow()
                        || model.isEndStateCorners()
                        || model.isEndStateCornered()){
                    setEndState();
                    persistGame();
                    showEndState();
                    
                    System.out.println("Game ended");
                    if(model.isEndState2x2()){
                        System.out.println("2x2");
                    }
                    if(model.isEndStateCol()){
                        System.out.println("Col");
                    }
                    if(model.isEndStateRow()){
                        System.out.println("Row");
                    }
                    if(model.isEndStateCorners()){
                        System.out.println("Corners");
                    }
                    if(model.isEndStateCornered()){
                        System.out.println("Cornered");
                    }
                    
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
    }
}
