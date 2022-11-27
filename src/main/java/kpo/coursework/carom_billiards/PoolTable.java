package kpo.coursework.carom_billiards;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class PoolTable{
    final Color TABLE_COLOR = Color.SEAGREEN;

    final Point2D leftTopCorner;
    final int TABLE_WIDTH = 1000;
    final int TABLE_HEIGHT = 500;
    static final double TABLE_FRICTION = 0.9;
    Rectangle tableRect;

    public PoolTable(int leftTopX,
                     int leftTopY) {
        leftTopCorner = new Point2D(leftTopX, leftTopY);
        tableRect = new Rectangle(leftTopCorner.getX(), leftTopCorner.getY(), TABLE_WIDTH, TABLE_HEIGHT);
    }

    public void draw(GraphicsContext gc){
        gc.setFill(TABLE_COLOR);
        gc.fillRect(leftTopCorner.getX(), leftTopCorner.getY(),
                TABLE_WIDTH, TABLE_HEIGHT);
    }

    /**
     * Проверяет, столкнулся ли шар с "вертикальными"
     * границами стола
     * @param ball  проверяемый шар
     * @return  true, если столкнулся
     */
    public boolean checkVerticalBordersCollision(Ball ball){
        return (!(ball.getCenter().getX() + ball.RADIUS < leftTopCorner.getX() + TABLE_WIDTH)) ||
                (!(ball.getCenter().getX() - ball.RADIUS > leftTopCorner.getX()));
    }

    /**
     * Проверяет, столкнулся ли шар с "горизонтальными"
     * границами стола
     * @param ball  проверяемый шар
     * @return  true, если столкнулся
     */
    public boolean checkHorizontalBordersCollision(Ball ball){
        return (!(ball.getCenter().getY() + ball.RADIUS < leftTopCorner.getY() + TABLE_HEIGHT)) ||
                (!(ball.getCenter().getY() - ball.RADIUS > leftTopCorner.getY()));
    }

}
