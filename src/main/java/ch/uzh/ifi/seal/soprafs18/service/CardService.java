package ch.uzh.ifi.seal.soprafs18.service;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.Card;
import ch.uzh.ifi.seal.soprafs18.repository.CardRepository;
import ch.uzh.ifi.seal.soprafs18.web.rest.CardResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cardService")
public class CardService {

    private final Logger logger = LoggerFactory.getLogger(CardResource.class);

    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    public Card getCard(String cardSlug) {
        Card card = cardRepository.findBySlug(cardSlug);
        if(card != null) {
            return card;
        }
        return null;
    }


    public Card createCard (String name, String slug, Boolean removable, Integer price, Double value, PowerType power, Integer powerValue){
        Card card = new Card();
        card.setName(name);
        card.setSlug(slug);
        card.setRemovable(removable);
        card.setPrice(price);
        card.setValue(value);
        card.setPower(power);
        card.setPowerValue(powerValue);
        cardRepository.save(card);
        return card;
    }

    // Generate green cards
    public Card generateExplorer() {
        return createCard("Explorer", "explorer", false, 0, 0.5, PowerType.GREEN, 1);
    }

    public Card generateScout() {
        return createCard("Scout", "scout", false, 1, 0.5, PowerType.GREEN, 2);
    }

    public Card generateTrailblazer() {
        return createCard("Traiblazer", "trailblazer", false, 3, 0.5, PowerType.GREEN, 3);
    }

    public Card generatePioneer() {
        return createCard("Pioneer", "pioneer", false, 5, 0.5, PowerType.GREEN, 5);
    }

    public Card generateGiantMachete() {
        return createCard("Giant Machete", "giantmachete", true, 3, 0.5, PowerType.GREEN, 6);
    }

    // generate blue cards

    public Card generateSailor() {
        return createCard("Sailor", "sailor", false, 0, 0.5, PowerType.BLUE, 1);
    }
    public Card generateCaptain() {
        return createCard("Captain", "captain", false, 2, 0.5, PowerType.BLUE, 3);
    }

    // generate yellow cards

    public Card generateTraveler() {
        return createCard("Traveler", "traveler", false, 0, 1.0, PowerType.YELLOW, 1);
    }

    public Card generatePhotographer() {
        return createCard("Photographer", "photographer", false, 2, 2.0, PowerType.YELLOW, 2);
    }

    public Card generateJournalist() {
        return createCard("Journalist", "journalist", false, 3, 3.0, PowerType.YELLOW, 3);
    }

    public Card generateTreasureChest() {
        return createCard("Treasure Chest", "treasurechest", true, 3, 4.0, PowerType.YELLOW, 4);
    }

    public Card generateMillionaire() {
        return createCard("Millionaire", "millionaire", false, 5, 4.0, PowerType.YELLOW, 4);
    }

    //Jokers

    public Card generateJackOfAllTrades() {
        return createCard("Jack of all Trades", "jackofalltrades", false, 2, 1.0, PowerType.JOKER, 1);
    }

    public Card generateAdventurer() {
        return createCard("Adventurer", "adventurer", false, 4, 2.0, PowerType.JOKER, 2);
    }

    public Card generatePropPlane() {
        return createCard("Prop Plane", "propplane", true, 4, 4.0, PowerType.JOKER, 4);
    }



    // Action cards
    public Card generateScientist() {
        return createCard("Scientist", "scientist", false, 4, 0.5, PowerType.DRAWREMOVE, 1);
    }

    public Card generateTransmitter() {
        return createCard("Transmitter", "transmitter", true, 4, 0.5, PowerType.BUYANY, 1);
    }

    public Card generateTravelLog() {
        return createCard("Travel Log", "travellog", true, 3, 0.5, PowerType.DRAWREMOVE, 2);
    }

    public Card generateCartograpgher() {
        return createCard("Cartographer", "cartographer", false, 4, 0.5, PowerType.DRAW, 2);
    }

    public Card generateNative() {
        return createCard("Native", "native", false, 5, 0.5, PowerType.MOVETOANY, 1);
    }

    public Card generateCompass() {
        return createCard("Compass", "compass", true, 2, 0.5, PowerType.DRAW, 3);
    }

}