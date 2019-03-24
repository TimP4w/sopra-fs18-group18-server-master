package ch.uzh.ifi.seal.soprafs18.web.rest;
import ch.uzh.ifi.seal.soprafs18.entity.Game;
import ch.uzh.ifi.seal.soprafs18.entity.Log;
import ch.uzh.ifi.seal.soprafs18.entity.User;
import ch.uzh.ifi.seal.soprafs18.entity.projections.GameSummary;
import ch.uzh.ifi.seal.soprafs18.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is in charge of managing the communication between the server and the client (RESTful Interface)
 */
@RestController
public class GameResource
        extends GenericResource {

    private static final String CONTEXT = "/games";
    Logger logger = LoggerFactory.getLogger(GameResource.class);


    @Autowired
    private GameService gameService;

    /*
     * Context : /games/
     * Returns 201 || 404 is user not found
     */
    @RequestMapping(value = CONTEXT, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    //
    public Long addGame(@RequestBody User owner) {
        logger.debug("Adding game for: " + owner.getToken());
        return this.gameService.addGame(owner.getToken());
    }

    /*
     * Context: /games/{game-id}
     * returns 200 || 404 Game not found
     */
    @RequestMapping(value = CONTEXT + "/{gameId}")
    @ResponseStatus(HttpStatus.OK)
    public synchronized GameSummary getGame(@PathVariable Long gameId) {
        logger.debug("getGame: " + gameId);
        return this.gameService.getGame(gameId);
    }

    /*
     * Context: /games/{game-id}/start
     * 200 || 400 || 403
     */
    @RequestMapping(value = CONTEXT + "/{gameId}/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Long startGame(@PathVariable Long gameId, @RequestBody User owner) {
        logger.debug("startGame: " + gameId);
        if(owner == null) {
            throw new ResourceException(HttpStatus.BAD_REQUEST, "User not provided.");
        }
        return this.gameService.startGame(gameId, owner.getToken());
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/player", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Long addPlayer(@PathVariable Long gameId, @RequestBody User user) {
        logger.debug("addPlayer: " + user.getToken());
        return this.gameService.addPlayer(gameId, user.getToken());
    }

    @RequestMapping(value = CONTEXT + "/pending")
    @ResponseStatus (HttpStatus.OK)
    public List<Game> listPendingGames(){
        logger.debug("Get the PendingGames");
        return this.gameService.listPendingGames();
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/setPlayers", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public GameSummary setNumberPlayer(@PathVariable Long gameId, @RequestParam("token") String userToken, @RequestParam("players") int players) {
        logger.debug("Setting number of players");
        return this.gameService.setNumberPlayer(gameId, userToken, players);
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/setMap", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public GameSummary setMap(@PathVariable Long gameId, @RequestParam("token") String userToken, @RequestParam("mapId") int mapId) {
        logger.debug("Setting map for game " + gameId);
        return this.gameService.setMap(gameId, userToken, mapId);
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/next", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public synchronized GameSummary nextTurn(@PathVariable Long gameId, @RequestBody User user) {
        logger.debug("Setting new turn for game " + gameId);
        return this.gameService.nextTurn(gameId, user.getToken());
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/logs", method = RequestMethod.GET)
    @ResponseStatus (HttpStatus.OK)
    public List<Log> getGameLogs(@PathVariable Long gameId) {
        logger.debug("Getting logs for " + gameId);
        return this.gameService.getGameLogs(gameId);
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/exit", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public void leaveGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        gameService.leaveGame(gameId, userToken);
        logger.debug("Leaving game for " + userToken);
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/leave", method = RequestMethod.POST)
    @ResponseStatus (HttpStatus.OK)
    public void leaveLobby(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        gameService.leaveLobby(gameId, userToken);
        logger.debug("Leaving lobby for " + userToken);
    }


}