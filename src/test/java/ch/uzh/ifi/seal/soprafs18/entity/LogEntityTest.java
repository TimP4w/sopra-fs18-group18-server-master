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

public class LogEntityTest {

    @Test
    public void getAndSetId(){
        Log testLog1 = new Log();
        Long generatedTestLong = new Random().nextLong();
        testLog1.setId(generatedTestLong);
        Assert.assertEquals(generatedTestLong, testLog1.getId());
    }

    @Test
    public void getAndSetGameId(){
        Log testLog2 = new Log();
        Long generatedTestGameLong = new Random().nextLong();
        testLog2.setGameId(generatedTestGameLong);
        Assert.assertEquals(generatedTestGameLong, testLog2.getGameId());
    }

    @Test
    public void getAndSetUserId(){
        Log testLog3 = new Log();
        Long generatedTestUserLong = new Random().nextLong();
        testLog3.setUserId(generatedTestUserLong);
        Assert.assertEquals(generatedTestUserLong, testLog3.getUserId());
    }

    @Test
    public void getAndSetMessage(){
        Log testLog4 = new Log();
        String testMessage1 = "A card was played.";
        testLog4.setMessage(testMessage1);
        Assert.assertEquals(testMessage1, testLog4.getMessage());
    }
}
