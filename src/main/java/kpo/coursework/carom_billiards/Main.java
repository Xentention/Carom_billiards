package kpo.coursework.carom_billiards;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 500;
    private static Pane root = new Pane();
    //private static Scene scene = new Scene(root, WIDTH, HEIGHT);
    private static boolean game = true;

    private static PoolTable table = new PoolTable(0, 0);
    private static Ball[] balls = new Ball[2];

    boolean gameOver = false;
    public static GraphicsContext gc ;

    @Override
    public void start(Stage stage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), width, height);
        stage.setTitle("Carom billiards");
        stage.setScene(scene);
        stage.show();
        PoolTable p = new PoolTable();
        p.drawTable();*/
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Timeline tl = new Timeline(new KeyFrame(Duration.millis(50), e ->run(gc)));
        tl.setCycleCount(Timeline.INDEFINITE);
        balls[0] = new Ball(30, 30, BallType.RED);
        balls[1] = new Ball(100, 100, BallType.YELLOW);
        stage.setTitle("Carom billiard");
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.show();
        table.draw(gc);
        balls[0].draw(gc);
        balls[1].draw(gc);
        balls[0].setVelocity(100, 100);
        tl.play();
    }

    private void run(GraphicsContext gc){
        table.draw(gc);
        balls[0].update(gc);
        balls[1].update(gc);
        for (Ball ball:
             balls) {
            if (table.checkVerticalBordersCollision(ball))
                ball.setVelocity(-ball.getVelocity().getX(), ball.getVelocity().getY());
            if (table.checkHorizontalBordersCollision(ball))
                ball.setVelocity(ball.getVelocity().getX(), -ball.getVelocity().getY());
        }
    }


    public static void main(String[] args) {
        launch();
    }
}