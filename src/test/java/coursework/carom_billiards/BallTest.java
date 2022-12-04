package coursework.carom_billiards;

import javafx.geometry.Point2D;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class BallTest {

    @Test
    public void testResolveBallsCollision() {
        final PoolTable testTable = new PoolTable(0, 0);
        // шар в центре поля
        Ball testBall1 = new Ball(new Point2D(testTable.leftTopCorner.getX() + testTable.TABLE_WIDTH / 2,
                                                testTable.leftTopCorner.getY() + testTable.TABLE_HEIGHT / 2),
                                    BallType.CUEBALL);
        //шар, немного смещенный относительно первого
        Ball testBall2 = new Ball(new Point2D(testTable.leftTopCorner.getX() + testTable.TABLE_WIDTH / 2 + testBall1.RADIUS,
                                                 testTable.leftTopCorner.getY() + testTable.TABLE_HEIGHT / 2),
                                  BallType.CUEBALL);

        assertTrue(testBall1.resolveBallsCollision(testBall2));
    }

    @Test
    public void testResolveBallsNoCollision() {
        final PoolTable testTable = new PoolTable(0, 0);
        // шар в центре поля
        Ball testBall1 = new Ball(new Point2D(testTable.leftTopCorner.getX() + testTable.TABLE_WIDTH / 2,
                                              testTable.leftTopCorner.getY() + testTable.TABLE_HEIGHT / 2),
                                  BallType.CUEBALL);
        //шар, сильно смещенный относительно первого
        Ball testBall2 = new Ball(new Point2D(testTable.leftTopCorner.getX() + testTable.TABLE_WIDTH / 2 + 10 * testBall1.RADIUS,
                                              testTable.leftTopCorner.getY() + testTable.TABLE_HEIGHT / 2 + 10 * testBall1.RADIUS),
                                   BallType.CUEBALL);

        assertFalse(testBall1.resolveBallsCollision(testBall2));
    }
}