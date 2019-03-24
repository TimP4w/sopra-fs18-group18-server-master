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

public class TileEntityTest {

    @Test
    public void getAndSetId(){
        Tile testTile1 = new Tile();
        Long testTileIdLong1 = new Random().nextLong();
        testTile1.setId(testTileIdLong1);
        Assert.assertEquals(testTileIdLong1, testTile1.getId());
    }

    @Test
    public void getAndSetName(){
        Tile testTile2 = new Tile();
        String testNameNr2 = "CoolName";
        testTile2.setName(testNameNr2);
        Assert.assertEquals(testNameNr2, testTile2.getName());
    }
}