package ch.uzh.ifi.seal.soprafs18.service;
import ch.uzh.ifi.seal.soprafs18.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs18.entity.*;
import ch.uzh.ifi.seal.soprafs18.entity.projections.GameSummary;
import ch.uzh.ifi.seal.soprafs18.repository.*;
import ch.uzh.ifi.seal.soprafs18.web.rest.GameResource;
import ch.uzh.ifi.seal.soprafs18.web.rest.ResourceException;
import ch.uzh.ifi.seal.soprafs18.web.rest.WebSocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by Lucas Pelloni on 26.01.18.
 * Service class for managing games, which contains the logic and all DB calls.
 */

@Service
@Transactional
public class GameService {

    private final GameRepository gameRepository;
    private final UserRepository userRepository;
    private final ExpeditionBoardService expeditionBoardService;
    private final BoardService boardService;
    private final MarketPlaceService marketPlaceService;
    private final WebSocketController webSocketController;
    private final LogService logService;

    private final Logger logger = LoggerFactory.getLogger(GameResource.class);


    @Autowired
    public GameService(GameRepository gameRepository,
                       UserRepository userRepository,
                       BoardService boardService,
                       ExpeditionBoardService expeditionBoardService,
                       MarketPlaceService marketPlaceService,
                       WebSocketController webSocketController,
                       LogService logService) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.boardService = boardService;
        this.expeditionBoardService = expeditionBoardService;
        this.marketPlaceService = marketPlaceService;
        this.webSocketController = webSocketController;
        this.logService = logService;
    }

    String gameNotFoundMessage = "Game not found.";
    public Long addGame(String token) {
        User owner = userRepository.findByToken(token);
        if (owner == null){
            throw new ResourceException(HttpStatus.NOT_FOUND, "User not found."); //404
        }
        Game game = new Game();
        game.setName("Player " + owner.getName() +"'s game");
        game.setOwner(owner.getId());
        List<User> players = new ArrayList<>();
        game.setPlayers(players);
        game.setCurrentPlayer(-1); //Game not started yet, no current player
        game.setStatus(GameStatus.PENDING);
        game.setNumberPlayers(2); // default 2, can be changed by owner
        game.setMapId(1);
        Game newGame = gameRepository.save(game);
        addPlayer(newGame.getId(), owner.getToken());
        this.logger.debug("Added new game: " + newGame.getId());
        return newGame.getId();
    }

    public GameSummary getGame(Long gameId) {
        Optional<GameSummary> game = gameRepository.findProjectedById(gameId);
        if (!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, gameNotFoundMessage); //404
        }
        return game.get();
    }

    public Long startGame(Long gameId, String userToken) {
        Optional<Game> game = gameRepository.findById(gameId);
        User user = userRepository.findByToken(userToken);
        // check game exists
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, gameNotFoundMessage); //404
        }
        // check user is owner
        if (!user.getId().equals(game.get().getOwner())) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "Only the game owner can start the game."); //403
        }
        //Check that players number match and game is not already running!
        if (game.get().getNumberPlayers() != game.get().getPlayers().size()) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You can start a game only if it's full."); // 403
        }
        //Check game not already running
        if(game.get().getStatus() == GameStatus.RUNNING) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "This game has already been started."); //403
        }

        // Generate ExpeditionBoards
        game.get().setExpeditionBoards(expeditionBoardService.generateExpeditionBoards(game.get()));

        // Generate Marketplace
        game.get().setMarketPlace(marketPlaceService.generateInitialMarketPlace(gameId));

        //Generate Map
        game.get().setBoard(boardService.generateMap(game.get()));

        //Set current Player
        game.get().setCurrentPlayer(0);

        //Set Status to running
        game.get().setStatus(GameStatus.RUNNING);

        //Send message via WebSocket
        webSocketController.onLobbyMessage(gameId.toString(),"GAME_STARTED");


        return game.get().getId();
    }
    

    public Long addPlayer(Long gameId, String userToken) {
        Optional<Game> game = gameRepository.findById(gameId);
        User player = userRepository.findByToken(userToken);
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, gameNotFoundMessage);
        }
        if(player == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Player not found.");
        }
        if(game.get().getPlayers().size() == game.get().getNumberPlayers()){
            throw new ResourceException(HttpStatus.FORBIDDEN, "Game is full.");
        }
        if(game.get().getPlayers().contains(player)){
            throw new ResourceException(HttpStatus.FORBIDDEN, "You already joined the game.");
        }

        game.get().getPlayers().add(player);
        player.setCurrentGameId(game.get().getId());
        this.logger.debug("Game: " + game.get().getName() + " - player added: " + player.getUsername());
        return gameId;
    }

    public GameSummary setNumberPlayer(Long gameId, String token, int players) {
        Optional<Game> game = gameRepository.findById(gameId);
        User user = userRepository.findByToken(token);
        if (game.isPresent()
                && user != null
                && game.get().getOwner().equals( user.getId())
                && game.get().getPlayers().size() <= players) {
            game.get().setNumberPlayers(players);
            Optional<GameSummary> gameSummary = gameRepository.findProjectedById(game.get().getId());
            return gameSummary.get();
        }
        return null;
    }

    public List<Game> listPendingGames (){
        return gameRepository.findByStatus(GameStatus.PENDING);
    }


    public GameSummary setMap( Long gameId, String userToken, int mapId) {
        Optional<Game> game = gameRepository.findById(gameId);
        if(game.isPresent()) {
            User requester = userRepository.findByToken(userToken);
            if(requester != null && game.get().getStatus().equals(GameStatus.PENDING)) {
                if (requester.getId().equals(game.get().getOwner())) {
                    game.get().setMapId(mapId);
                    Optional<GameSummary> gameSummary = gameRepository.findProjectedById(game.get().getId());
                    return gameSummary.get();
                }
            }
        }
        return null;
    }

    private int mostValuableBlockade(List<Blockade> blockades) {
        int currentMax = 0;
        for(Blockade blockade:blockades) {
            if(blockade.getPower() > currentMax) {
                currentMax = blockade.getPower();
            }
        }
        return currentMax;
    }

    private List<ExpeditionBoard> calculateWinners(Game game) {
        List<ExpeditionBoard> winners = new ArrayList<>();
        ExpeditionBoard currentBest = game.getHaveReachedElDorado().get(0);
        for(ExpeditionBoard expBoard:game.getHaveReachedElDorado()) {
            if (expBoard.getBlockadesWon().size() > currentBest.getBlockadesWon().size()) {
                winners = new ArrayList<>();
                currentBest = expBoard;
                winners.add(expBoard);
            } else if (expBoard.getBlockadesWon().size() == currentBest.getBlockadesWon().size()) {
                if(this.mostValuableBlockade(expBoard.getBlockadesWon()) == this.mostValuableBlockade(currentBest.getBlockadesWon())) {
                    winners.add(expBoard);
                } else if(this.mostValuableBlockade(expBoard.getBlockadesWon()) > this.mostValuableBlockade(currentBest.getBlockadesWon())) {
                    winners = new ArrayList<>();
                    currentBest = expBoard;
                    winners.add(expBoard);
                }
            }
        }
        game.setStatus(GameStatus.FINISHED);
        return winners;
    }

    public GameSummary nextTurn(Long gameId, String userToken) {
        Optional<Game> game = gameRepository.findById(gameId);
        User user = userRepository.findByToken(userToken);
        if(!game.isPresent()) {
            return null;
        }
        int expBoardId = game.get().getCurrentPlayer();
        ExpeditionBoard expBoard = game.get().getExpeditionBoards().get(expBoardId);

        if(!user.getId().equals(expBoard.getOwnerId())) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "It's not your turn.");
        }

        return this.changeTurn(game.get());
    }

    private GameSummary changeTurn(Game game) {
        int expBoardId = game.getCurrentPlayer();
        ExpeditionBoard expBoard = game.getExpeditionBoards().get(expBoardId);
        //Remove unplayed cards
        expBoard.discardPlayedCards();
        //reset possible moves
        expBoard.resetStats();
        //Draw cards until hand.size() == 4
        expBoard.drawHand();

        //Increase turn number
        int currentTurnNum = game.getTurnNumber();
        game.setTurnNumber(currentTurnNum + 1);
        //Set nextPlayer
        int nextPlayer = game.getTurnNumber() % game.getNumberPlayers();
        game.setCurrentPlayer(nextPlayer);

        //Check if game finished
        if(game.getHaveReachedElDorado().size() > 0) {
            if(game.getTurnNumber() % game.getNumberPlayers() == 0) {
                List<ExpeditionBoard> winners = this.calculateWinners(game);
                game.setWinners(winners);
                webSocketController.onGameMessage(game.getId().toString(),"{\"action\": \"GAME_FINISHED\"}");
                return gameRepository.findProjectedById(game.getId()).get();
            }
        }

        Long nextUid = game.getPlayers().get(nextPlayer).getId();

        logService.createLog(game, game.getPlayers().get(currentTurnNum % game.getNumberPlayers()), "Player " + game.getPlayers().get(currentTurnNum % game.getNumberPlayers()).getName() + " ended his turn.");
        webSocketController.onGameMessage(game.getId().toString(),"{\"action\": \"NEXT_TURN\", \"nextPlayer\": \"" + nextUid + "\"}");


        //Check that current player didn't leave the game.
        if(game.getPlayers().get(nextPlayer).getCurrentGameId() == null || !game.getPlayers().get(nextPlayer).getCurrentGameId().equals(game.getId())) {
            logService.createLog(game, game.getPlayers().get(nextPlayer), "Turn of " + game.getPlayers().get(nextPlayer).getName() + " skipped because player left the game.");
            return this.changeTurn(game);
        }

        return gameRepository.findProjectedById(game.getId()).get();
    }

    public void leaveGame(Long gameId, String userToken) {
        Optional<Game> game = gameRepository.findById(gameId);
        User user = userRepository.findByToken(userToken);
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if (user.getCurrentGameId() != null) {
            user.setCurrentGameId(null);
        }

        logService.createLog(game.get(), user, "Player " + user.getName() + " left the game.");

        //Skip turn if it was the leaving player's turn.
        if(game.get().getPlayers().get(game.get().getCurrentPlayer()).getCurrentGameId() == null || game.get().getPlayers().get(game.get().getCurrentPlayer()).equals(user)) {
            this.changeTurn(game.get());
        }

        //Stop game if players left <= 1
        int playersLeft = 0;

        List<ExpeditionBoard> winners = new ArrayList<>();

        for(int i = 0; i < game.get().getNumberPlayers(); i++) {
            if(game.get().getPlayers().get(i).getCurrentGameId() != null && game.get().getPlayers().get(i).getCurrentGameId().equals(gameId)) {
                playersLeft = playersLeft + 1;
                winners.add(game.get().getExpeditionBoards().get(i));
            }
        }

        if(playersLeft == 1) {
            game.get().setWinners(winners);
            game.get().setStatus(GameStatus.FINISHED);
        }

        webSocketController.onGameMessage(gameId.toString(),"{\"action\": \"PLAYER_LEFT\"}");

    }

    public void leaveLobby(Long gameId, String userToken) {
        Optional<Game> game = gameRepository.findById(gameId);
        User user = userRepository.findByToken(userToken);
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found");
        }

        if(game.get().getOwner().equals(user.getId())) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "The owner cannot leave the lobby!");
        }

        game.get().getPlayers().remove(user);
        user.setCurrentGameId(null);

        webSocketController.onLobbyMessage(gameId.toString(),"GAME_EDITED");

    }


    public List<Log> getGameLogs(Long gameId) {
        Optional<Game> game = gameRepository.findById(gameId);

        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found.");
        }
        return game.get().getLogs();
    }
}