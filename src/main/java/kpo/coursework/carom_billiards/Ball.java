package kpo.coursework.carom_billiards;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import static java.lang.Math.abs;
import static kpo.coursework.carom_billiards.Main.balls;
import static kpo.coursework.carom_billiards.Main.table;

public class Ball {
    final int RADIUS = 12;

    private Point2D position;
    private final BallType ballType;
    private Point2D velocity;

    public Ball(Point2D pos, BallType ballType){
        position = new Point2D(pos.getX() - RADIUS, pos.getY() - RADIUS);
        velocity = new Point2D(0, 0);
        this.ballType = ballType;
    }

    public void update(GraphicsContext gc){
        move(gc);
        draw(gc);
    }

    public void draw(GraphicsContext gc){
        gc.setFill(ballType.getColor());
        gc.fillOval(position.getX(), position.getY(), 2*RADIUS, 2*RADIUS);
    }

    /**
     * Логика движения бильярдного шара
     * (не перерисовывает сам шар)
     */
    public void move(GraphicsContext gc) {
        Point2D destination = position.add(velocity);
        int distance = (int) position.distance(destination);

        while (distance > 0) {
            double stepX = velocity.getX() == 0 ?
                    0:
                    velocity.getX()/abs(velocity.getX());
            double stepY = velocity.getY() == 0 ?
                    0:
                    velocity.getY()/abs(velocity.getY());

            position = position.add(stepX, stepY);
            --distance;


            // на каждом шаге необходимо проверять не столкнулись ли
            // мы с препятствиями
            if(resolveTableCollisions()){
                destination = position.add(velocity);
                distance = (int) position.distance(destination);

            }

            for (Ball otherBall:
                    balls) {
                if(resolveBallsCollision(otherBall)) {
                    destination = position.add(velocity);
                    distance = (int) position.distance(destination);
                }

            }

        }
        // трение
        velocity = velocity.multiply(PoolTable.TABLE_FRICTION);
        if(abs(velocity.getX()) < 0.1) setVelocity(0, velocity.getY());
        if(abs(velocity.getY()) < 0.1) setVelocity(velocity.getX(), 0);
    }

    /**
     * Обрабатывает потенциальные столкновения шара
     * с границами стола
     * @return  true, если столкновение произошло
     */
    public boolean resolveTableCollisions(){
        boolean collision = false;
        if (table.checkVerticalBordersCollision(this)) {
            this.setVelocity(-this.getVelocity().getX(), this.getVelocity().getY());
            collision = true;
        }
        if (table.checkHorizontalBordersCollision(this)) {
            this.setVelocity(this.getVelocity().getX(), -this.getVelocity().getY());
            collision = true;
        }
        return  collision;
    }

    /**
     * Обрабатывает потенциальные столкновения шара
     * с другими шарами
     * @return  true, если столкновение произошло
     */
    public boolean resolveBallsCollision(Ball ball){
        if(this == ball) return false;
        Point2D thisCenter = this.getCenter();
        Point2D ballCenter = ball.getCenter();
        if(thisCenter.distance(ballCenter) <= 2 * RADIUS) {
            this.collideBalls(ball);
            return true;
        }
        return false;
    }

    /**
     * Логика обработки произошедшего столкновения
     * между шарами
     */
    private void collideBalls(Ball ball) {
        double dx = this.getCenter().getX() - ball.getCenter().getX();
        double dy = this.getCenter().getY() - ball.getCenter().getY();

        double d2 =  this.getCenter().distance(ball.getCenter());

        // коэффициенты
        double kB1toB1, kB2toB1, kB1toB2, kB2toB2;
        kB1toB1 = (dx * this.getVelocity().getY() - dy * this.getVelocity().getX()) / (d2 * d2);
        kB2toB1 = (dx * this.getVelocity().getX() + dy * this.getVelocity().getY()) / (d2 * d2);
        kB1toB2 = (dx * ball.getVelocity().getX() + dy * ball.getVelocity().getY()) / (d2 * d2);
        kB2toB2 = (dx * ball.getVelocity().getY() - dy * ball.getVelocity().getX()) / (d2 * d2);

        // set velocity of this ball
        this.setVelocity(kB1toB2 * dx - kB1toB1 * dy, kB1toB2 * dy + kB1toB1 * dx);

        // set velocity of another ball
        ball.setVelocity(kB2toB1 * dx - kB2toB2 * dy, kB2toB1 * dy + kB2toB2 * dx);


    }

    public Point2D getCenter(){
        return new Point2D(position.getX()+RADIUS, position.getY()+RADIUS);
    }

    public void setVelocity(double velX, double velY){
        velocity = velocity.subtract(velocity);
        velocity = velocity.add(velX, velY);
    }

    public Point2D getVelocity(){
        return velocity;
    }
}

