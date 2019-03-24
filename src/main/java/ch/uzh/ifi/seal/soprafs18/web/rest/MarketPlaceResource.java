package ch.uzh.ifi.seal.soprafs18.web.rest;

import ch.uzh.ifi.seal.soprafs18.entity.Marketplace;
import ch.uzh.ifi.seal.soprafs18.service.ExpeditionBoardService;
import ch.uzh.ifi.seal.soprafs18.service.MarketPlaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class MarketPlaceResource extends GenericResource {

    private static final String CONTEXT = "/marketplaces";
    Logger logger = LoggerFactory.getLogger(CardResource.class);

    @Autowired
    private MarketPlaceService marketPlaceService;

    @Autowired
    private ExpeditionBoardService expBoardService;

    @RequestMapping(value = CONTEXT + "/{marketplaceId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Marketplace getMarket(@PathVariable Long marketplaceId) {
        return this.marketPlaceService.getMarketplace(marketplaceId);
    }


}