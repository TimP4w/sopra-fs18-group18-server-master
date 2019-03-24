package ch.uzh.ifi.seal.soprafs18.web.rest;
import ch.uzh.ifi.seal.soprafs18.entity.ExpeditionBoard;
import ch.uzh.ifi.seal.soprafs18.entity.projections.ExpeditionBoardSummary;
import ch.uzh.ifi.seal.soprafs18.repository.ExpeditionBoardRepository;
import ch.uzh.ifi.seal.soprafs18.service.ExpeditionBoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ExpeditionBoardResource extends GenericResource{

    private static final String CONTEXT = "/expeditionboards";
    Logger logger = LoggerFactory.getLogger(ExpeditionBoardResource.class);


    @Autowired
    private ExpeditionBoardService expeditionBoardService;

    @RequestMapping(value = CONTEXT +"/{expId}/discardcard/{cardSlug}", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public synchronized ExpeditionBoardSummary discardCard(@RequestParam("token") String userToken, @PathVariable Long expId, @PathVariable String cardSlug){
        logger.debug("discarding a Card: " + cardSlug );
        return expeditionBoardService.discardCard(cardSlug, expId, userToken);
    }

    @RequestMapping(value = CONTEXT +"/{expId}/removecard/{cardSlug}", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public synchronized ExpeditionBoardSummary removeCard(@RequestParam("token") String userToken, @PathVariable Long expId, @PathVariable String cardSlug){
        logger.debug("removing a Card: " + cardSlug );
        return expeditionBoardService.removeCard(cardSlug, expId, userToken);
    }

    @RequestMapping(value = CONTEXT +"/{expId}/playcard/{cardSlug}", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public synchronized ExpeditionBoardSummary playCard(@RequestParam("token") String userToken, @PathVariable Long expId, @PathVariable String cardSlug){
        logger.debug("Playing a Card: " + cardSlug );
        return expeditionBoardService.playCard(cardSlug, expId, userToken);
    }

    @RequestMapping(value = CONTEXT +"/{expId}/playactioncard/{cardSlug}", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public synchronized ExpeditionBoardSummary playActionCard(@RequestParam("token") String userToken, @PathVariable Long expId, @PathVariable String cardSlug){
        logger.debug("Playing a Card: " + cardSlug );
        return expeditionBoardService.playActionCard(cardSlug, expId, userToken);
    }


    @RequestMapping(value = CONTEXT +"/{gameId}/getOwn", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public ExpeditionBoardSummary getOwnExpBoard(@RequestParam("token") String userToken, @PathVariable Long gameId){
        logger.debug("Retrieving Expedition Board for user: " + userToken );
        return expeditionBoardService.getOwnExpBoard(userToken, gameId);
    }

    @RequestMapping(value = CONTEXT + "/{expBoardId}/sell/{cardslug}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public synchronized double sellCard(@PathVariable Long expBoardId, @RequestParam("token") String userToken, @PathVariable String cardslug) {
        return this.expeditionBoardService.sellCard(cardslug, userToken, expBoardId);
    }

    @RequestMapping(value = CONTEXT + "/{expBoardId}/buy/{cardslug}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public synchronized double buyCard(@PathVariable Long expBoardId, @RequestParam("token") String userToken, @PathVariable String cardslug) {
        return this.expeditionBoardService.buyCard(cardslug, userToken, expBoardId);
    }
}