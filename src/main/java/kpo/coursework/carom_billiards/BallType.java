package kpo.coursework.carom_billiards;

import javafx.scene.paint.Color;

public enum BallType{
    CUEBALL,
    YELLOW,
    RED;

    public Color getColor(){
        return switch (this){
            case CUEBALL -> Color.ANTIQUEWHITE;
            case RED -> Color.DARKRED;
            case YELLOW -> Color.YELLOW;
        };
    }

}