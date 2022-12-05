package coursework.carom_billiards;

import java.io.File;
import java.util.ArrayDeque;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class OneCushionScore {
    private static final ArrayDeque<String> collisions = new ArrayDeque<>(); // дек очередности ударенных элементов
    public static final String borderHit = "border";
    public static final String redBallHit = "red";
    public static final String yellowBallHit = "yellow";


    public static void addCollision(String type){
        collisions.addLast(type);
    }

    /**
     * Играет звук победы или поражения в зависимости
     * от успешности хода
     */
    public static void playResultSound() {
        File musicFile;
        if(gotAScore())
            musicFile = new File("src/main/resources/coursework/carom_billiards/sounds/winSound.mp3");
        else
            musicFile = new File("src/main/resources/coursework/carom_billiards/sounds/failSound.mp3");
        Media sound = new Media(musicFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    /**
     * Считает успешен ли был ход
     * @return да/нет
     */
    private static boolean gotAScore(){
        boolean hitAWall = false;

        int numberSuccessBall = 0;
        boolean hitYellowBall = false;
        boolean hitRedBall = false;
        while(!collisions.isEmpty()){
            String collision = collisions.removeFirst();
            if(collision.equals(borderHit))
                    hitAWall = true;
            if(collision.equals(redBallHit)
                    && (numberSuccessBall == 0 || (hitAWall && hitYellowBall))) {
                hitRedBall = true;
                ++numberSuccessBall;
            }
            if(collision.equals(yellowBallHit)
                    && (numberSuccessBall == 0 || (hitAWall && hitRedBall))) {
                hitYellowBall = true;
                ++numberSuccessBall;
            }
        }
        return hitAWall && (numberSuccessBall >=2 );
    }
}
