package ch.uzh.ifi.seal.soprafs18.entity;

import ch.uzh.ifi.seal.soprafs18.Application;
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

public class PieceEntityTest {

    @Test
    public void getAndSetId(){
        Piece testPieceNr1 = new Piece();
        Long testPieceIdLong = new Random().nextLong();
        testPieceNr1.setId(testPieceIdLong);
        Assert.assertEquals(testPieceIdLong, testPieceNr1.getId());
    }

    @Test
    public void getAnsSetExpBoard(){
        Piece testPieceNr1 = new Piece();
        ExpeditionBoard testExpBoardForPiece = new ExpeditionBoard();
        testPieceNr1.setExpBoard(testExpBoardForPiece);
        Assert.assertEquals(testExpBoardForPiece, testPieceNr1.getExpBoard());
    }

    @Test
    public void getAndSetOwner(){
        Piece testPieceNr2 = new Piece();
        User testOwnerNr2 = new User();
        testPieceNr2.setOwner(testOwnerNr2);
        Assert.assertEquals(testOwnerNr2, testPieceNr2.getOwner());
    }

    @Test
    public void getAndSetColor(){
        Piece testPieceNr3 = new Piece();
        String testColorNr3 = "CoolColor";
        testPieceNr3.setColor(testColorNr3);
        Assert.assertEquals(testColorNr3, testPieceNr3.getColor());
    }
}