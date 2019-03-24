package ch.uzh.ifi.seal.soprafs18.entity;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.Random;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class HexSpaceEntityTest {

    @Test
    public void getAndSetId(){
        HexSpace hexSpace531 = new HexSpace();
        Long generatedHexPlaceTestLong = new Random().nextLong();
        hexSpace531.setId(generatedHexPlaceTestLong);
        Assert.assertEquals(generatedHexPlaceTestLong, hexSpace531.getId());
    }

    @Test
    public void getAndSetColor(){
        HexSpace testHexSpace1 = new HexSpace();
        PowerType testColor = PowerType.GREY;
        testHexSpace1.setColor(testColor);
        Assert.assertEquals(testColor, testHexSpace1.getColor());
    }

    @Test
    public void getAndSetPower(){
        HexSpace testHexSpace2 = new HexSpace();
        int testPower = 2;
        testHexSpace2.setPower(testPower);
        Assert.assertEquals(testPower, testHexSpace2.getPower());
    }

    @Test
    public void getAndSetX(){
        HexSpace testHexSpace3 = new HexSpace();
        int testX = 4;
        testHexSpace3.setX(testX);
        Assert.assertEquals(testX, testHexSpace3.getX());
    }

    @Test
    public void getAndSetY(){
        HexSpace testHexSpace4 = new HexSpace();
        int testY = 3;
        testHexSpace4.setY(testY);
        Assert.assertEquals(testY, testHexSpace4.getY());
    }

    @Test
    public void getAndSetBlockade(){
        HexSpace testHexSpace5 = new HexSpace();
        Blockade testBlockadeHexSpace = new Blockade();
        testHexSpace5.setBlockade(testBlockadeHexSpace);
        Assert.assertEquals(testBlockadeHexSpace, testHexSpace5.getBlockade());
    }

    @Test
    public void getAndSetBoard(){
        HexSpace testHexSpace6 = new HexSpace();
        Board testBoardHexSpace = new Board();
        testHexSpace6.setBoard(testBoardHexSpace);
        Assert.assertEquals(testBoardHexSpace, testHexSpace6.getBoard());
    }

    @Test
    public void getAndSetOccupiedBy() {
        HexSpace testHexSpace7 = new HexSpace();
        Piece testPieceHexSpace = new Piece();
        testHexSpace7.setOccupiedBy(testPieceHexSpace);
        Assert.assertEquals(testPieceHexSpace, testHexSpace7.getOccupiedBy());
    }

    @Test
    public void getAndSetIsElDoradoSpace(){
        HexSpace testHexSpace8 = new HexSpace();
        Boolean isEldoradoSpacetest = false;
        testHexSpace8.setElDoradoSpace(isEldoradoSpacetest);
        Assert.assertEquals(isEldoradoSpacetest, testHexSpace8.isElDoradoSpace());
    }
}