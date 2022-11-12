package kpo.coursework.carom_billiards;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Circle;

import static java.lang.Math.abs;

public class Ball {
    final int MASS = 1;
    final int RADIUS = 12;

    private Point2D position;
    private BallType ballType;
    private Point2D velocity;
    private Circle ballCircle;

    public Ball(int x, int y, BallType ballType){
        position = new Point2D(x, y);
        velocity = new Point2D(0, 0);
        this.ballType = ballType;
    }

    public void update(GraphicsContext gc){
        moveX();
        moveY();
        draw(gc);
    }

    public void draw(GraphicsContext gc){
        gc.setFill(ballType.getColor());
        gc.fillOval(position.getX(), position.getY(), 2*RADIUS, 2*RADIUS);
    }

    public void moveX(){
        if(velocity.getX() != 0) {
            position = position.add(5*velocity.getX()/abs(velocity.getX()), 0);
            velocity = velocity.getX() > 0 ?
                            velocity.subtract(PoolTable.TABLE_FRICTION, 0) :
                            velocity.subtract(-PoolTable.TABLE_FRICTION, 0);
        }
    }

    public void moveY(){
        if(velocity.getY() != 0) {
            position = position.add(0, 5*velocity.getY()/abs(velocity.getY()));
            velocity = velocity.getY() > 0 ?
                    velocity.subtract(0, PoolTable.TABLE_FRICTION) :
                    velocity.subtract(0, -PoolTable.TABLE_FRICTION);
        }
    }

    public Point2D getPosition() {
        return position;
    }
    public void setVelocity(double velX, double velY){
        velocity = velocity.subtract(velocity);
        velocity = velocity.add(velX, velY);
    }

    public Point2D getVelocity(){
        return velocity;
    }
}

