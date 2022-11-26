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

    public Ball(int x, int y, BallType ballType){
        position = new Point2D(x, y);
        velocity = new Point2D(0, 0);
        this.ballType = ballType;
    }

    public void update(GraphicsContext gc){
        move();
        draw(gc);
    }

    public void draw(GraphicsContext gc){
        gc.setFill(ballType.getColor());
        gc.fillOval(position.getX(), position.getY(), 2*RADIUS, 2*RADIUS);
    }

    public void move() {
        Point2D destination = position.add(velocity);
        int distance = (int) position.distance(destination);

        while (distance > 0){
            // идем по 2 пикселя, чтобы сэкономить время
            double stepX = 2 * velocity.getX()/abs(velocity.getX());
            double stepY = 2 * velocity.getY()/abs(velocity.getY());

            position = position.add(stepX, stepY);
            distance -= 2;

            resolveTableCollisions();

            for (Ball otherBall:
                 balls) {
                if(this == otherBall) continue;
                if(this.checkBallCollisions(otherBall)) {
                    setUpCollision(this, otherBall);
                    destination = position.add(velocity);
                    distance = (int) position.distance(destination);
                }

            }
        }
        velocity.multiply(PoolTable.TABLE_FRICTION);
    }

    public void resolveTableCollisions(){
            if (table.checkVerticalBordersCollision(this))
                this.setVelocity(-this.getVelocity().getX(), this.getVelocity().getY());
            if (table.checkHorizontalBordersCollision(this))
                this.setVelocity(this.getVelocity().getX(), -this.getVelocity().getY());
    }

    public boolean checkBallCollisions(Ball ball){
        Point2D thisCenter = new Point2D(this.position.getX() + RADIUS, this.position.getY() + RADIUS);
        Point2D ballCenter = new Point2D(ball.position.getX() + ball.RADIUS, ball.position.getY() + ball.RADIUS);
        return abs(thisCenter.distance(ballCenter)) <= 2 * RADIUS;
    }

    public void setUpCollision(Ball ball, Ball otherBall) {

        // Collide the two objects and return new velocities
        Point2D[] newVels = resolveBallsCollision(ball, otherBall);

        ball.setVelocity(newVels[0].getX(), newVels[0].getY());
        otherBall.setVelocity(newVels[1].getX(), newVels[1].getY());

    }

    private Point2D[] resolveBallsCollision(Ball ball1, Ball ball2) {
        double dx = ball1.getCenter().getX() - ball2.getCenter().getX();
        double dy = ball1.getCenter().getY() - ball2.getCenter().getY();

        double d2 =  dx * dx + dy * dy;
        //double d2 = i.center.distanceSq(j.center);

        if ( d2 < 900 )  {
            double kii, kji, kij, kjj;
            kji = (dx * ball1.getVelocity().getX() + dy * ball1.getVelocity().getY()) / d2; // k of j due to i
            kii = (dx * ball1.getVelocity().getY() - dy * ball1.getVelocity().getX()) / d2; // k of i due to i
            kij = (dx * ball2.getVelocity().getX() + dy * ball2.getVelocity().getY()) / d2; // k of i due to j
            kjj = (dx * ball2.getVelocity().getY() - dy * ball2.getVelocity().getX()) / d2; // k of j due to j

            // set velocity of i
            ball1.setVelocity(kij * dy + kii * dx, kij * dx - kii * dy);

            // set velocity of j
            ball2.setVelocity(kji * dy + kjj * dx, kji * dx - kjj * dy);
        }
        return new Point2D[]{ball1.getVelocity(), ball2.getVelocity()};

    }


    public void setPosition(double posX, double posY){
        position = position.subtract(position);
        position = position.add(posX, posY);
    }

    public Point2D getCenter(){
        return new Point2D(position.getX()+RADIUS, position.getY()+RADIUS);
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

