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

public class MarketPileEntityTest {

    @Test
    public void getAndSetId(){
        MarketPile marketTestPile89 = new MarketPile();
        Long marketTestPileLong = new Random().nextLong();
        marketTestPile89.setId(marketTestPileLong);
        Assert.assertEquals(marketTestPileLong, marketTestPile89.getId());
    }

    @Test
    public void setAndGetDrawPile() {
        MarketPile testMarketPile1 = new MarketPile();
        Card scout789 = new Card();
        Card scout7890 = new Card();
        List<Card> testMarketPlacePile = new ArrayList<>();
        testMarketPlacePile.add(scout789);
        testMarketPlacePile.add(scout7890);
        testMarketPile1.setMarketPlacePile(testMarketPlacePile);
        Assert.assertEquals(testMarketPlacePile, testMarketPile1.getMarketPlacePile());
    }

    @Test
    public void setAndGetSlug() {
        MarketPile testMarketPile2 = new MarketPile();
        String testSlug = "scoutnumber1";
        testMarketPile2.setCardSlug(testSlug);
        Assert.assertEquals(testSlug, testMarketPile2.getCardSlug());
    }

    @Test
    public void setAndGetMarketpileId() {
        MarketPile testMarketPileNr7891765 = new MarketPile();
        Long generatedTestLong12243243 = new Random().nextLong();
        testMarketPileNr7891765.setMarketPlaceId(generatedTestLong12243243);
        Assert.assertEquals(generatedTestLong12243243, testMarketPileNr7891765.getMarketPlaceId());
    }
}