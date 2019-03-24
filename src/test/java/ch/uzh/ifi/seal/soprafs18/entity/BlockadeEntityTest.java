package ch.uzh.ifi.seal.soprafs18.entity;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.service.BlockadeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.List;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class BlockadeEntityTest {

    @Autowired
    BlockadeService blockadeService;

    @Test
    public void getPower(){
        Blockade blockade = blockadeService.generateBlockade(1);
        Assert.assertEquals(blockade.getPower(), 1);
    }

    @Test
    public void setPower(){
        Blockade blockade1 = blockadeService.generateBlockade(1);
        blockade1.setPower(3);
        Assert.assertNotEquals(blockade1.getPower(),1);
        Assert.assertEquals(blockade1.getPower(),3);
    }

    @Test
    public void getColor(){
        Blockade blockade2 = blockadeService.generateBlockade(1);
        Assert.assertEquals(blockade2.getColor(), PowerType.GREEN);
    }

    @Test
    public void setColor(){
        Blockade blockade3 = blockadeService.generateBlockade(1);
        blockade3.setColor(PowerType.YELLOW);
        Assert.assertNotEquals(blockade3.getColor(), PowerType.GREEN);
        Assert.assertEquals(blockade3.getColor(),PowerType.YELLOW);
    }

    @Test
    public void getId(){
        Blockade testblockade = blockadeService.generateBlockade(1);
        Blockade testblockade2 = blockadeService.generateBlockade(2);
        Assert.assertNotEquals(testblockade.getId(), testblockade2.getId());
    }

    @Test
    public void getAndSetRotation(){
        Blockade testBlockade12345 = new Blockade();
        String testRotation = "rightRot";
        testBlockade12345.setRotation(testRotation);
        Assert.assertEquals(testRotation, testBlockade12345.getRotation());
    }

    @Test
    public void getAndSetBlockedSpaces(){
        Blockade testBlockade143425 = new Blockade();
        HexSpace testHexSpaceNr1 = new HexSpace();
        List<HexSpace> testBlockadeHexSpaceList = new ArrayList<>();
        testBlockadeHexSpaceList.add(testHexSpaceNr1);
        testBlockade143425.setBlockedSpaces(testBlockadeHexSpaceList);
        Assert.assertEquals(testBlockadeHexSpaceList, testBlockade143425.getBlockedSpaces());
    }
}