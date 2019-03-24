package ch.uzh.ifi.seal.soprafs18.entity;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.service.CardService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.Random;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)

public class BoardEntityTest {

    @Test
    public void getAndSetId(){
        Board testBoard9735 = new Board();
        Long generatedTestLong123789 = new Random().nextLong();
        Assert.assertNotEquals(generatedTestLong123789, testBoard9735.getId());
        testBoard9735.setId(generatedTestLong123789);
        Assert.assertEquals(generatedTestLong123789, testBoard9735.getId());
    }

    @Test
    public void getAndSetName(){
        Board testBoard6789321562375test = new Board();
        String testStringGetAndSetNameBoard = "ourString";
        Assert.assertNotEquals(testBoard6789321562375test.getName(), testStringGetAndSetNameBoard);
        testBoard6789321562375test.setName(testStringGetAndSetNameBoard);
        Assert.assertEquals(testBoard6789321562375test.getName(), testStringGetAndSetNameBoard);
    }

    @Test
    public void getAndSetGame(){
        Board testBoard678935test = new Board();
        Game testGameBoardEntity = new Game();
        Assert.assertNotEquals(testBoard678935test.getGame(), testGameBoardEntity);
        testBoard678935test.setGame(testGameBoardEntity);
        Assert.assertEquals(testBoard678935test.getGame(), testGameBoardEntity);
    }
}
