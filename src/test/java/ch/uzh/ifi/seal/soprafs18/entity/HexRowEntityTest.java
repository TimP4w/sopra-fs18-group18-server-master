package ch.uzh.ifi.seal.soprafs18.entity;

import ch.uzh.ifi.seal.soprafs18.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)

public class HexRowEntityTest {

    @Test
    public void getAndSetId(){
        HexRow hexRowTest123456 = new HexRow();
        Long generatedHexRowTestLong = new Random().nextLong();
        hexRowTest123456.setId(generatedHexRowTestLong);
        Assert.assertEquals(generatedHexRowTestLong, hexRowTest123456.getId());
    }

    @Test
    public void getAndSetSpaces(){
        HexRow testHexRow1 = new HexRow();
        HexSpace spaceNr1 = new HexSpace();
        HexSpace spaceNr2 = new HexSpace();
        List<HexSpace> testHexRowList1 = new ArrayList<>();
        testHexRowList1.add(spaceNr1);
        testHexRowList1.add(spaceNr2);
        testHexRow1.setSpaces(testHexRowList1);
        Assert.assertEquals(testHexRowList1, testHexRow1.getSpaces());
    }
}