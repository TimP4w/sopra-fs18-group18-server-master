package ch.uzh.ifi.seal.soprafs18.web.rest;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.entity.Card;
import ch.uzh.ifi.seal.soprafs18.entity.MarketPile;
import ch.uzh.ifi.seal.soprafs18.entity.Marketplace;
import ch.uzh.ifi.seal.soprafs18.repository.MarketPlaceRepository;
import ch.uzh.ifi.seal.soprafs18.service.MarketPlaceService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "security.basic.enabled=false"
        })
public class MarketPlaceResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    MarketPlaceService marketPlaceService;

    @Autowired
    MarketPlaceRepository marketPlaceRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void getMarketPlace(){

        Card testCardNr1 = new Card();
        Card testCardNr2 = new Card();
        Card testCardNr3 = new Card();

        MarketPile testMarketPileNr1 = new MarketPile();
        MarketPile testMarketPileNr2 = new MarketPile();
        MarketPile testMarketPileNr3 = new MarketPile();

        List<MarketPile> testMarketPileListNr1 = new ArrayList<>();
        List<MarketPile> testMarketPileListNr2 = new ArrayList<>();
        List<MarketPile> testMarketPileListNr3 = new ArrayList<>();

        List<Card> testBackMarketPile = new ArrayList<>(); testBackMarketPile.add(testCardNr1);
        List<Card> testMiddleMarketPile = new ArrayList<>(); testMiddleMarketPile.add(testCardNr2);
        List<Card> testFrontMarketPile = new ArrayList<>(); testFrontMarketPile.add(testCardNr3);

        testMarketPileNr1.setMarketPlacePile(testFrontMarketPile); testMarketPileListNr1.add(testMarketPileNr1);
        testMarketPileNr2.setMarketPlacePile(testMiddleMarketPile); testMarketPileListNr2.add(testMarketPileNr2);
        testMarketPileNr3.setMarketPlacePile(testBackMarketPile); testMarketPileListNr3.add(testMarketPileNr3);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        //Test for non existing marketplace
        Long falseId = -1L;
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/marketplaces/" + falseId),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals("404", response.getStatusCode().toString());


        //Test for existing marketplace that is saved in the database
        Marketplace testMarketplace = new Marketplace();
        marketPlaceRepository.save(testMarketplace);
        testMarketplace.setMarketplaceFrontRow(testMarketPileListNr1);
        testMarketplace.setMarketplaceMiddleRow(testMarketPileListNr2);
        testMarketplace.setMarketplaceBackRow(testMarketPileListNr3);
        Long testMarketPlaceId = testMarketplace.getId();

        response = restTemplate.exchange(
                createURLWithPort("/marketplaces/" + testMarketPlaceId),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals("200", response.getStatusCode().toString());
    }
}