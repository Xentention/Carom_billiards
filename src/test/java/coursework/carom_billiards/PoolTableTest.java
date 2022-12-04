package coursework.carom_billiards;

import javafx.geometry.Point2D;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class PoolTableTest {
    final PoolTable testTable = new PoolTable(0, 0);

    @Test
    public void testCheckVerticalBordersCollision() {
        Point2D leftBorder = new Point2D(testTable.leftTopCorner.getX() - 10,
                                            testTable.leftTopCorner.getY() + testTable.TABLE_HEIGHT / 2);
        Point2D rightBorder = new Point2D(testTable.leftTopCorner.getX() + testTable.TABLE_WIDTH + 10,
                                            testTable.leftTopCorner.getY() + testTable.TABLE_HEIGHT / 2);

        boolean leftCollision = testTable.checkVerticalBordersCollision(new Ball(leftBorder, BallType.CUEBALL));
        boolean rightCollision = testTable.checkVerticalBordersCollision(new Ball(rightBorder, BallType.CUEBALL));

        assertTrue(leftCollision && rightCollision);
    }


    @Test
    public void testCheckHorizontalBordersCollision() {
        Point2D upBorder = new Point2D(testTable.leftTopCorner.getX() + testTable.TABLE_WIDTH / 2,
                                            testTable.leftTopCorner.getY() - 10);
        Point2D downBorder = new Point2D(testTable.leftTopCorner.getX() + testTable.TABLE_WIDTH / 2,
                                            testTable.leftTopCorner.getY() + testTable.TABLE_HEIGHT + 10);

        boolean upCollision = testTable.checkHorizontalBordersCollision(new Ball(upBorder, BallType.CUEBALL));
        boolean downCollision = testTable.checkHorizontalBordersCollision(new Ball(downBorder, BallType.CUEBALL));

        assertTrue(upCollision && downCollision);
    }

    @Test
    void testCheckAllBordersNoCollision() {
        Point2D center = new Point2D(testTable.leftTopCorner.getX() + testTable.TABLE_WIDTH / 2,
                testTable.leftTopCorner.getY() + testTable.TABLE_HEIGHT / 2);

        boolean verticalCollision = testTable.checkVerticalBordersCollision(new Ball(center, BallType.CUEBALL));
        boolean horizontalCollision = testTable.checkHorizontalBordersCollision(new Ball(center, BallType.CUEBALL));

        assertFalse(verticalCollision && horizontalCollision);
    }
}