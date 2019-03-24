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

public class CardEntityTest {

    @Autowired
    private CardService cardService;

    @Test
    public void getName (){
        Card transmitter = cardService.createCard("Transmitter", "transmitter01", true, 4, 0.5, PowerType.BUYANY, 1);
        Assert.assertEquals(transmitter.getName(), "Transmitter");
    }

    @Test
    public void setName (){
        Card transmitter = cardService.createCard("Transmitter", "transmitter02", true, 4, 0.5, PowerType.BUYANY, 1);
        transmitter.setName("Transmitter2");
        Assert.assertEquals(transmitter.getName(), "Transmitter2");
        Assert.assertNotEquals(transmitter.getName(), "Transmitter");
    }

    @Test
    public void getRemovable (){
        Card transmitter = cardService.createCard("Transmitter", "transmitter03", true, 4, 0.5, PowerType.BUYANY, 1);
        Assert.assertEquals(transmitter.getRemovable(), true);
    }

    @Test
    public void setRemovable () {
        Card transmitter = cardService.createCard("Transmitter", "transmitter04", false, 4, 0.5, PowerType.BUYANY, 1);
        transmitter.setRemovable(true);
        Assert.assertEquals(transmitter.getRemovable(), true);
        Assert.assertNotEquals(transmitter.getRemovable(), false);
    }

    @Test
    public void getPower (){
        Card explorer = cardService.createCard("Explorer", "explorer06", false, 0, 0.5, PowerType.GREEN, 1);
        Assert.assertEquals(explorer.getPower(), PowerType.GREEN);
    }

    @Test
    public void setPower () {
        Card explorer = cardService.createCard("Explorer", "explorer07", false, 0, 0.5, PowerType.GREEN, 1);
        explorer.setPower(PowerType.YELLOW);
        Assert.assertEquals(explorer.getPower(), PowerType.YELLOW);
        Assert.assertNotEquals(explorer.getRemovable(), PowerType.GREEN);
    }

    @Test
    public void getAndSetId(){
        Card testCard5423 = new Card();
        Long generatedTestLong123 = new Random().nextLong();
        testCard5423.setId(generatedTestLong123);
        Assert.assertEquals(generatedTestLong123, testCard5423.getId());
    }

    @Test
    public void getAndSetSlug(){
        Card testCard567 = new Card();
        String testSlug123 = "1234tz";
        testCard567.setSlug(testSlug123);
        Assert.assertEquals(testSlug123, testCard567.getSlug());
    }

    @Test
    public void getAndSetPrice(){
        Card testCard890 = new Card();
        Integer testPrice12 = 3;
        testCard890.setPrice(testPrice12);
        Assert.assertEquals(testPrice12, testCard890.getPrice());
    }

    @Test
    public void getAndSetValue(){
        Card testCard1290 = new Card();
        Double testValue000 = 0.5;
        testCard1290.setValue(testValue000);
        Assert.assertEquals(testValue000, testCard1290.getValue());
    }

    @Test
    public void getAndSetPowerValue(){
        Card testCard0021 = new Card();
        Integer testPowerValue78 = 4;
        testCard0021.setPowerValue(testPowerValue78);
        Assert.assertEquals(testPowerValue78, testCard0021.getPowerValue());
    }

}