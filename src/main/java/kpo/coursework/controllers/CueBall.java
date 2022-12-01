package kpo.coursework.controllers;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import kpo.coursework.carom_billiards.Ball;
import kpo.coursework.carom_billiards.BallType;
import kpo.coursework.carom_billiards.Main;

public class CueBall extends Ball{

    public CueBall(Point2D pos){
        super(pos, BallType.CUEBALL);
    }

    public void mouseClicked(){
        /*scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("mouse click detected! "+event.getSource());
            }
        });
        Main.getScene().setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(this.getVelocity().getX() == 0 &&
                        this.getVelocity().getY() == 0)
                    return;
                Point2D newPos = new Point2D(event.getX(), event.getY());
                double newStepX = (event.getX() - this.getVelocity().getX()) / 30;
                double newStepY = (event.getY() - this.getVelocity().getY()) / 30;
                this.setVelocity(newStepX, newStepY);

            }
        })*/

    }



}
