package ch.uzh.ifi.seal.soprafs18.init;

import ch.uzh.ifi.seal.soprafs18.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

@Component
public class StartUpInit {

    private final CardService cardService;

    @Autowired
    public StartUpInit(CardService cardService) {
        this.cardService = cardService;
    }

    @PostConstruct
    public void init(){
        //Generate all cards
        //Green Cards
        cardService.generateExplorer();
        cardService.generateScout();
        cardService.generateTrailblazer();
        cardService.generatePioneer();
        cardService.generateGiantMachete();

        //Blue Cards
        cardService.generateSailor();
        cardService.generateCaptain();

        //Yellow Cards
        cardService.generateTraveler();
        cardService.generatePhotographer();
        cardService.generateJournalist();
        cardService.generateTreasureChest();
        cardService.generateMillionaire();

        //Jokers
        cardService.generateJackOfAllTrades();
        cardService.generateAdventurer();
        cardService.generatePropPlane();

        //Action Cards
        cardService.generateScientist();
        cardService.generateTransmitter();
        cardService.generateTravelLog();
        cardService.generateCartograpgher();
        cardService.generateNative();
        cardService.generateCompass();
    }
}