package coursework.carom_billiards;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    private boolean gameIsOn = false;

    static final int WIDTH = 1050;
    static final int HEIGHT = 550;

    static PoolTable table;
    static Ball[] balls = new Ball[3];

    static GraphicsContext gc;
    static private final Canvas canvas = new Canvas(WIDTH, HEIGHT);
    static private final Pane pane = new StackPane(canvas);
    static private final Scene scene = new Scene(pane);



    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Carom billiard");
        scene.getStylesheets().add(this.getClass()
                                        .getResource("/coursework/carom_billiards/CSS/startFormatting.css")
                                        .toExternalForm());
        Button startGame = new Button("Start");
        startGame.getStyleClass().add("button");
        pane.getChildren().addAll(startGame);
        pane.setId("start-pane");
        stage.setScene(scene);

        startGame.setOnAction(event -> {
            startGame(stage);
            pane.getChildren().removeAll(startGame);
        });

        stage.show();
    }

    public void startGame(Stage stage){
        stage.show();
        gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(50), e ->run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);
        stage.setScene(scene);
        setUpGameEntities();

        tl.play();
    }


    private void run(GraphicsContext gc){
        gameIsOn = true;
        table.draw(gc);
        for(Ball ball
                : balls)
            ball.update(gc);
    }

    private void setUpGameEntities(){
        constructTable();
        constructBalls();

    }

    /**
     * Задает начальное состояние стола, на котором
     * проходит игра
     */
    private void constructTable(){
        gc.setFill(Color.MAROON);
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        table  = new PoolTable(25, 25);
    }

    /**
     * Задает начальное состояние всех бильярдных шаров
     */
    private void constructBalls(){
        constructCueBall();
        // стандартные стартовые позиции для карамболя
        Point2D yellowBallPos = new Point2D(table.leftTopCorner.getX() + table.TABLE_WIDTH / 4,
                                          table.leftTopCorner.getY() + table.TABLE_HEIGHT / 2);
        Point2D redBallPos = new Point2D(table.leftTopCorner.getX() + 3 * table.TABLE_WIDTH / 4,
                                        table.leftTopCorner.getY() + table.TABLE_HEIGHT / 2);

        balls[1] = new Ball(yellowBallPos, BallType.YELLOW);
        balls[2] = new Ball(redBallPos, BallType.RED);

        // delete in the future
        //balls[1].setVelocity(100, -30);
    }

    private void constructCueBall(){

        Point2D cueBallPos = new Point2D(table.leftTopCorner.getX() + table.TABLE_WIDTH / 4,
                                        table.leftTopCorner.getY() + 5 * table.TABLE_HEIGHT / 8);

        balls[0] = new Ball(cueBallPos, BallType.CUEBALL);


        scene.setOnMouseClicked(event -> {
            if (gameIsOn && !areBallsInMotion()) {
                double newStepX = (event.getX() - balls[0].getCenter().getX()) / 2;
                double newStepY = (event.getY() - balls[0].getCenter().getY()) / 2;
                balls[0].setVelocity(newStepX, newStepY);
            }
        });

    }

    private boolean areBallsInMotion(){
        for(Ball ball : balls){
            if(ball.getVelocity().magnitude() != 0)
                return true;
        }
        return false;
    }

}