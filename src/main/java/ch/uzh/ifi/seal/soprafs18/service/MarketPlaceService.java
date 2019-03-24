package ch.uzh.ifi.seal.soprafs18.service;

import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.*;
import ch.uzh.ifi.seal.soprafs18.entity.projections.ExpeditionBoardSummary;
import ch.uzh.ifi.seal.soprafs18.entity.projections.GameSummary;
import ch.uzh.ifi.seal.soprafs18.repository.*;
import ch.uzh.ifi.seal.soprafs18.web.rest.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.error.Mark;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("MarketPlaceService")
public class MarketPlaceService {

    //GameResource does not yet exist
    //private final Logger logger = LoggerFactory.getLogger(GameResource.class);
    private final MarketPlaceRepository marketPlaceRepository;
    private final MarketPileRepository marketPileRepository;
    private final CardService cardService;
    private final ExpeditionBoardRepository expeditionBoardRepository;
    private final UserService userService;
    private final GameRepository gameRepository;


    @Autowired
    public MarketPlaceService (MarketPlaceRepository marketPlaceRepository,
                               CardService cardService,
                               MarketPileRepository marketPileRepository,
                               ExpeditionBoardRepository expeditionBoardRepository,
                               UserService userService,
                               GameRepository gameRepository) {
        this.marketPlaceRepository = marketPlaceRepository;
        this.cardService =cardService;
        this.marketPileRepository = marketPileRepository;
        this.expeditionBoardRepository = expeditionBoardRepository;
        this.userService = userService;
        this.gameRepository = gameRepository;
    }

    public Marketplace getMarketplace(Long marketId){
        Optional<Marketplace> market = marketPlaceRepository.findById(marketId);
        if(!market.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Couldn't find the marketplace");
        }
        return market.get();
    }

    //This method should be used when starting a game to generate the MarketPlace
    //Every of the 18 piles have 3 cards of the same type in them

    public Marketplace generateInitialMarketPlace(Long gameId){

        Marketplace marketplace = new Marketplace();
        marketplace.setGameId(gameId);
        marketplace = marketPlaceRepository.save(marketplace);
        //Initialize FrontRow of Marketplace
        List<Card> scoutList = new ArrayList<>();
        List<Card> trailblazerList = new ArrayList<>();
        List<Card> jackList = new ArrayList<>();
        List<Card> treasureList = new ArrayList<>();
        List<Card> photographerList = new ArrayList<>();
        List<Card> transmitterList = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            scoutList.add(cardService.getCard("scout"));
            trailblazerList.add(cardService.getCard("trailblazer"));
            jackList.add(cardService.getCard("jackofalltrades"));
            treasureList.add(cardService.getCard("treasurechest"));
            photographerList.add(cardService.getCard("photographer"));
            transmitterList.add(cardService.getCard("transmitter"));
        }

        MarketPile scoutPile = new MarketPile();
        MarketPile trailblazerPile = new MarketPile();
        MarketPile jackPile = new MarketPile();
        MarketPile treasurePile = new MarketPile();
        MarketPile photographerPile = new MarketPile();
        MarketPile transmitterPile = new MarketPile();

        scoutPile.setMarketPlaceId(marketplace.getId());
        trailblazerPile.setMarketPlaceId(marketplace.getId());
        jackPile.setMarketPlaceId(marketplace.getId());
        treasurePile.setMarketPlaceId(marketplace.getId());
        photographerPile.setMarketPlaceId(marketplace.getId());
        transmitterPile.setMarketPlaceId(marketplace.getId());

        scoutPile.setMarketPlacePile(scoutList);
        trailblazerPile.setMarketPlacePile(trailblazerList);
        jackPile.setMarketPlacePile(jackList);
        treasurePile.setMarketPlacePile(treasureList);
        photographerPile.setMarketPlacePile(photographerList);
        transmitterPile.setMarketPlacePile(transmitterList);

        List<MarketPile> frontRow = new ArrayList<>();

        frontRow.add(marketPileRepository.save(scoutPile));
        frontRow.add( marketPileRepository.save(trailblazerPile));
        frontRow.add(marketPileRepository.save(jackPile));
        frontRow.add(marketPileRepository.save(treasurePile));
        frontRow.add(marketPileRepository.save(photographerPile));
        frontRow.add(marketPileRepository.save(transmitterPile));

        marketplace.setMarketplaceFrontRow(frontRow);

        //Initialize middle and backRow
        List<MarketPile> pileList= new ArrayList<>();

        String[] slugs = {"pioneer", "giantmachete", "captain", "journalist", "millionaire", "adventurer", "propplane", "travellog", "cartographer", "native", "compass", "scientist"};

        for(String slug: slugs) {
            List<Card> nextList = new ArrayList<>();
            for(int i = 0; i < 3; i++) {
                nextList.add(cardService.getCard(slug));
            }
            MarketPile nextPile = new MarketPile();
            nextPile.setMarketPlacePile(nextList);
            nextPile.setMarketPlaceId(marketplace.getId());
            MarketPile pile = marketPileRepository.save(nextPile);
            pileList.add(pile);
        }

        // randomly insert 6 of these piles into the middle row and then insert the remaining 6 into the back row
        List<MarketPile> middleRow = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            middleRow.add(pileList.remove((int)(Math.random() * pileList.size())));
        }
        List<MarketPile> backRow = new ArrayList<>();
        for(int i = 0; i < 6; i++) {
            backRow.add(pileList.remove((int)(Math.random() * pileList.size())));
        }

        marketplace.setMarketplaceMiddleRow(middleRow);
        marketplace.setMarketplaceBackRow(backRow);

        return marketplace;
    }
}