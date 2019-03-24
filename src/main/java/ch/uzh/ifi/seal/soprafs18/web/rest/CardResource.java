package ch.uzh.ifi.seal.soprafs18.web.rest;
import ch.uzh.ifi.seal.soprafs18.entity.Card;
import ch.uzh.ifi.seal.soprafs18.service.CardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class CardResource extends GenericResource {

    private static final String CONTEXT = "/cards";
    Logger logger = LoggerFactory.getLogger(CardResource.class);

    @Autowired
    private CardService cardService;

    @RequestMapping(value = CONTEXT + "/{cardSlug}/getcard", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Card getCard(@PathVariable String cardSlug) {
        return this.cardService.getCard(cardSlug);
    }
}