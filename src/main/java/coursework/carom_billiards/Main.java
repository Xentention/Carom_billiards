package coursework.carom_billiards;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class Main extends Application {
    private boolean gameIsOn = false;           // идет ли игра

    private boolean moveIsFinishing = false;    // заканчивается ли ход

    public static final int WIDTH = 1050;
    public static final int HEIGHT = 550;

    public static PoolTable table;
    public static Ball[] balls = new Ball[3];

    static GraphicsContext gc;
    private static final Canvas canvas = new Canvas(WIDTH, HEIGHT);
    private static final Pane pane = new StackPane(canvas);
    private static final Scene scene = new Scene(pane);



    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Carom billiard");
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass()
                                            .getResourceAsStream("/coursework/carom_billiards/images/icon.png"))));

        scene.getStylesheets().add(Objects.requireNonNull(this.getClass()
                              .getResource("/coursework/carom_billiards/CSS/startFormatting.css"))
                              .toExternalForm());
        Button startGameBtn = new Button("Start");
        startGameBtn.getStyleClass().add("button");
        pane.getChildren().addAll(startGameBtn);
        pane.setId("start-pane");
        stage.setScene(scene);

        startGameBtn.setOnAction(event -> {
            startGame();
            pane.getChildren().removeAll(startGameBtn);
        });

        stage.show();
    }

    public void startGame(){
        gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(50), e ->run()));
        tl.setCycleCount(Timeline.INDEFINITE);
        setUpGameEntities();

        tl.play();
    }


    private void run(){
        gameIsOn = true;        // игра началась
        table.draw(gc);
        for(Ball ball
                : balls)
            ball.update(gc);
        if(areBallsInMotion())
            moveIsFinishing = true;
        else
            if(moveIsFinishing) {
                moveIsFinishing = false;
                OneCushionScore.playResultSound();
            }
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

    /**
     * Задает параметры битка
     */
    private void constructCueBall(){

        Point2D cueBallPos = new Point2D(table.leftTopCorner.getX() + table.TABLE_WIDTH / 4,
                                        table.leftTopCorner.getY() + 5 * table.TABLE_HEIGHT / 8);

        balls[0] = new Ball(cueBallPos, BallType.CUEBALL);


        scene.setOnMouseClicked(event -> {
            if (gameIsOn && !areBallsInMotion()) {
                moveIsFinishing = false;
                double newStepX = (event.getX() - balls[0].getCenter().getX()) / 2;
                double newStepY = (event.getY() - balls[0].getCenter().getY()) / 2;
                balls[0].setVelocity(newStepX, newStepY);
            }
        });

    }

    /**
     * Проверяет, в движении ли шары
     * @return да/нет
     */
    private boolean areBallsInMotion(){
        for(Ball ball : balls){
            if(ball.getVelocity().magnitude() != 0)
                return true;
        }
        return false;
    }

}