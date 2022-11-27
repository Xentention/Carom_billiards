package kpo.coursework.carom_billiards;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Main extends Application{
    static final int WIDTH = 1050;
    static final int HEIGHT = 550;

    static PoolTable table;
    static Ball[] balls = new Ball[3];

    boolean gameOver = false;
    static GraphicsContext gc ;


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(50), e ->run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);
        stage.setTitle("Carom billiard");
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        setUpGameEntities();
        tl.play();
    }

    private void run(GraphicsContext gc){
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
        // стандартные стартовые позиции для карамболя
        Point2D cueBallPos = new Point2D(table.leftTopCorner.getX() + table.TABLE_WIDTH / 4,
                                        table.leftTopCorner.getY() + 5 * table.TABLE_HEIGHT / 8);
        Point2D yellowBallPos = new Point2D(table.leftTopCorner.getX() + table.TABLE_WIDTH / 4,
                                          table.leftTopCorner.getY() + table.TABLE_HEIGHT / 2);
        Point2D redBallPos = new Point2D(table.leftTopCorner.getX() + 3 * table.TABLE_WIDTH / 4,
                                        table.leftTopCorner.getY() + table.TABLE_HEIGHT / 2);

        balls[0] = new Ball(cueBallPos, BallType.CUEBALL);
        balls[1] = new Ball(yellowBallPos, BallType.YELLOW);
        balls[2] = new Ball(redBallPos, BallType.RED);

        balls[1].setVelocity(100, -30);
    }
}