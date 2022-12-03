package coursework.carom_billiards;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import static java.lang.Math.abs;

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
        move();
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
    public void move() {
        Point2D destination = position.add(velocity);
        double distance = position.distance(destination);

        while (distance > 0) {
            double stepX = velocity.getX() == 0 ?
                    0:
                    velocity.getX()/abs(velocity.getX());
            double stepY = velocity.getY() == 0 ?
                    0:
                    velocity.getY()/abs(velocity.getY());

            // опирается на уравнение прямой на плоскости
            if(stepX != 0 && stepY != 0) {
                stepY = (stepX * velocity.getY()) / velocity.getX();
            }

            distance -= position.distance(position.add(stepX, stepY));
            position = position.add(stepX, stepY);


            // на каждом шаге необходимо проверять не столкнулись ли
            //мы с препятствиями
            if(resolveTableCollisions()){
                // для подсчета очков
                if(this.ballType == BallType.CUEBALL)
                    OneCushionScore.onCollision("border");

                destination = position.add(velocity);
                distance = position.distance(destination);

            }

            for (Ball otherBall:
                    Main.balls) {
                if(otherBall == this) continue;
                if(resolveBallsCollision(otherBall)) {
                    // для подсчета очков
                    if(this.ballType == BallType.CUEBALL)
                        if(otherBall.ballType == BallType.RED)
                            OneCushionScore.onCollision("red");
                        else
                            OneCushionScore.onCollision("yellow");

                    destination = position.add(velocity);
                    distance = position.distance(destination);
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

        if (Main.table.checkVerticalBordersCollision(this)) {
            this.setVelocity(-this.getVelocity().getX(), this.getVelocity().getY());
            //Если шар немного зашел за границы поля, его надо оттуда вывести
            if(position.getX() > Main.table.TABLE_WIDTH / 2)
                setPosition((Main.table.leftTopCorner.getX() + Main.table.TABLE_WIDTH) - 2 * RADIUS - 1,
                        position.getY());
            else
                setPosition(Main.table.leftTopCorner.getX() + 1, position.getY());
            collision = true;
        }

        if (Main.table.checkHorizontalBordersCollision(this)) {
            this.setVelocity(this.getVelocity().getX(), -this.getVelocity().getY());
            //Если шар немного зашел за границы поля, его надо оттуда вывести
            if(position.getY() > Main.table.TABLE_HEIGHT / 2)
                setPosition(position.getX(),
                        ((Main.table.leftTopCorner.getY() + Main.table.TABLE_HEIGHT) - 2 * RADIUS - 1));
            else
                setPosition(position.getX(), Main.table.leftTopCorner.getY() + 1);
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

    public void setPosition(double posX,
                            double posY) {
        position = position.subtract(position);
        position = position.add(posX, posY);
    }

    public void setVelocity(double velX,
                            double velY){
        velocity = velocity.subtract(velocity);
        velocity = velocity.add(velX, velY);
    }


    public Point2D getVelocity(){
        return velocity;
    }
}

