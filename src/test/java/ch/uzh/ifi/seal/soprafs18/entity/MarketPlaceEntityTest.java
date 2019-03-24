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
public class MarketPlaceEntityTest {

    @Test
    public void getAndSetId(){
        Marketplace marketPlaceTest89675 = new Marketplace();
        Long marketTestPlaceLong = new Random().nextLong();
        marketPlaceTest89675.setId(marketTestPlaceLong);
        Assert.assertEquals(marketTestPlaceLong, marketPlaceTest89675.getId());
    }

    @Test
    public void setAndGetMarketPlaceBackRow(){
        Marketplace testMarketPlace1 = new Marketplace();
        MarketPile testMarketPile1 = new MarketPile();
        MarketPile testMarketPile2 = new MarketPile();
        List<MarketPile> testMarketPlaceBackRow = new ArrayList<>();

        testMarketPlaceBackRow.add(testMarketPile1);
        testMarketPlaceBackRow.add(testMarketPile2);
        testMarketPlace1.setMarketplaceBackRow(testMarketPlaceBackRow);
        Assert.assertEquals(testMarketPlaceBackRow, testMarketPlace1.getMarketplaceBackRow());
    }

    @Test
    public void setAndGetMarketPlaceMiddleRow(){
        Marketplace testMarketPlace2 = new Marketplace();
        MarketPile testMarketPile3 = new MarketPile();
        MarketPile testMarketPile4 = new MarketPile();
        MarketPile testMarketPile5 = new MarketPile();
        List<MarketPile> testMarketPlaceMiddleRow = new ArrayList<>();

        testMarketPlaceMiddleRow.add(testMarketPile3);
        testMarketPlaceMiddleRow.add(testMarketPile4);
        testMarketPlaceMiddleRow.add(testMarketPile5);
        testMarketPlace2.setMarketplaceMiddleRow(testMarketPlaceMiddleRow);
        Assert.assertEquals(testMarketPlaceMiddleRow, testMarketPlace2.getMarketplaceMiddleRow());
    }

    @Test
    public void setAndGetMarketPlaceFrontRow() {
        Marketplace testMarketPlace3 = new Marketplace();
        MarketPile testMarketPile3 = new MarketPile();
        MarketPile testMarketPile4 = new MarketPile();
        MarketPile testMarketPile5 = new MarketPile();
        MarketPile testMarketPile6 = new MarketPile();
        List<MarketPile> testMarketFrontRow = new ArrayList<>();

        testMarketFrontRow.add(testMarketPile3);
        testMarketFrontRow.add(testMarketPile4);
        testMarketFrontRow.add(testMarketPile5);
        testMarketFrontRow.add(testMarketPile6);
        testMarketPlace3.setMarketplaceFrontRow(testMarketFrontRow);
        Assert.assertEquals(testMarketFrontRow, testMarketPlace3.getMarketplaceFrontRow());
    }
}