package ch.uzh.ifi.seal.soprafs18.service;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.*;
import ch.uzh.ifi.seal.soprafs18.entity.projections.ExpeditionBoardSummary;
import ch.uzh.ifi.seal.soprafs18.repository.*;
import ch.uzh.ifi.seal.soprafs18.web.rest.ResourceException;
import ch.uzh.ifi.seal.soprafs18.web.rest.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpeditionBoardService {

    private final ExpeditionBoardRepository expeditionBoardRepository;
    private final CardService cardService;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final GameRepository gameRepository;
    private final PieceRepository pieceRepository;
    private final HexSpaceService hexSpaceService;
    private final WebSocketController webSocketController;
    private final UserService userService;
    private final MarketPlaceRepository marketPlaceRepository;
    private final MarketPileRepository marketPileRepository;
    private final LogService logService;

    @Autowired
    public ExpeditionBoardService(ExpeditionBoardRepository expeditionBoardRepository,
                                  CardService cardService,
                                  UserRepository userRepository,
                                  CardRepository cardRepository,
                                  GameRepository gameRepository,
                                  PieceRepository pieceRepository,
                                  HexSpaceService hexSpaceService,
                                  WebSocketController webSocketController,
                                  UserService userService,
                                  MarketPlaceRepository marketPlaceRepository,
                                  MarketPileRepository marketPileRepository,
                                  LogService logService) {

        this.expeditionBoardRepository = expeditionBoardRepository;
        this.cardService = cardService;
        this.userRepository = userRepository;
        this.cardRepository = cardRepository;
        this.gameRepository = gameRepository;
        this.pieceRepository = pieceRepository;
        this.hexSpaceService = hexSpaceService;
        this.webSocketController = webSocketController;
        this.userService = userService;
        this.marketPlaceRepository = marketPlaceRepository;
        this.marketPileRepository = marketPileRepository;
        this.logService = logService;
    }

    String cantUseCardsNow = "You can't use any cards now.";

    public ExpeditionBoardSummary getOwnExpBoard(String userToken, Long gameId) {
        User user = userRepository.findByToken(userToken);
        Optional<Game> currentGame = gameRepository.findById(gameId);

        if (currentGame.isPresent() && user != null) {
            Optional<ExpeditionBoard> expBoard = currentGame.get().getExpeditionBoards().stream().filter(o -> o.getOwnerId().equals(user.getId())).findFirst();
            if(expBoard.isPresent()) {
                return expeditionBoardRepository.findProjectedById(expBoard.get().getId());
            }
        }
        return null;
    }


    public boolean userCanUseCard(Card card, Optional<ExpeditionBoard> expBoard, User user) {
        //ExpBoard exists? Card exists?
        if (!expBoard.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Expedition Board not found.");
        }
        if(card == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Card not found.");
        }
        if(user == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "User not found.");
        }

        Optional<Game> game = gameRepository.findById(expBoard.get().getGameId());
        if (!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found.");
        }

        int currentPlayerBoard = game.get().getCurrentPlayer();
        if(!game.get().getExpeditionBoards().get(currentPlayerBoard).equals(expBoard.get())) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "It's not your turn.");
        }
        if (!user.getId().equals(expBoard.get().getOwnerId())) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You're not the owner of the Expedition Board");
        }

         if (!expBoard.get().getHandPile().contains(card)) {
             throw new ResourceException(HttpStatus.FORBIDDEN, "You don't have that card in your hand!");

         }
        return true;
    }

    public ExpeditionBoardSummary removeCard(String cardSlug, Long expBoardId, String userToken){
        // Retrieve data
        Optional<ExpeditionBoard> expBoard = expeditionBoardRepository.findById(expBoardId);
        User user = userRepository.findByToken(userToken);
        Card card = cardRepository.findBySlug(cardSlug);

        if(!this.userCanUseCard(card, expBoard, user)) {
            throw new ResourceException(HttpStatus.FORBIDDEN, cantUseCardsNow);
        }

        //Check if has right to remove card
         if(expBoard.get().getCanRemove() > 0) {
            //remove card
             expBoard.get().getHandPile().remove(card);
             //Decrease counter
             expBoard.get().setCanRemove(expBoard.get().getCanRemove() - 1);
             Optional<Game> game = gameRepository.findById(expBoard.get().getGameId());
             logService.createLog(game.get(), user, "Player " + user.getName() + " removed " + card.getName());
             return expeditionBoardRepository.findProjectedById(expBoardId);
        } else {
             throw new ResourceException(HttpStatus.FORBIDDEN, cantUseCardsNow);
         }



    }

    public ExpeditionBoardSummary discardCard(String cardSlug, Long expBoardId, String userToken){
        Optional<ExpeditionBoard> expBoard = expeditionBoardRepository.findById(expBoardId);
        User user = userRepository.findByToken(userToken);
        Card card = cardRepository.findBySlug(cardSlug);
        if(!this.userCanUseCard(card, expBoard, user)) {
            throw new ResourceException(HttpStatus.FORBIDDEN, cantUseCardsNow);
        }

        expBoard.get().getHandPile().remove(card);
        expBoard.get().getDiscardPile().add(card);
        Optional<Game> game = gameRepository.findById(expBoard.get().getGameId());
        logService.createLog(game.get(), user, "Player " + user.getName() + " discarded " + card.getName());
        return expeditionBoardRepository.findProjectedById(expBoardId);
    }

    public ExpeditionBoardSummary playCard(String cardSlug, Long expBoardId, String userToken) {
        Optional<ExpeditionBoard> expBoard = expeditionBoardRepository.findById(expBoardId);
        User user = userRepository.findByToken(userToken);
        Card card = cardRepository.findBySlug(cardSlug);
        //Check that user can play card
        if(!this.userCanUseCard(card, expBoard, user)) {
            throw new ResourceException(HttpStatus.FORBIDDEN, cantUseCardsNow);
        }
        //If user played action card to move, can't play a new card until moved
        if(expBoard.get().getCanMove()) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You need to make a move before playing a card!");
        }
        //Reset possible moves
        expBoard.get().setPossibleMoves(new ArrayList<>());
        //Remove card from hand
        expBoard.get().getHandPile().remove(card);
        //Add card to playedCards
        expBoard.get().getPlayedCards().add(card);
        //If only one card played, check possible paths
        for(Piece piece:expBoard.get().getPieces()) {
            if(expBoard.get().getPlayedCards().size() == 1) {
                //if the powertype of the card is not joker, find moves with card power, else run 3 times with all powers
                if(!expBoard.get().getPlayedCards().get(0).getPower().equals(PowerType.JOKER)) {
                    hexSpaceService.findPaths(piece.getSpace(), expBoard.get(), expBoard.get().getPlayedCards().get(0).getPower(), expBoard.get().getPlayedCards().get(0).getPowerValue(), 0);
                } else {
                    PowerType[] powers = {PowerType.GREEN, PowerType.BLUE, PowerType.YELLOW};
                    for(PowerType power:powers) {
                        hexSpaceService.findPaths(piece.getSpace(), expBoard.get(), power, expBoard.get().getPlayedCards().get(0).getPowerValue(), 0);
                    }
                }
            }
            //Find adjacent DISCARD or REMOVE with power <= card played count
            hexSpaceService.findAdjacentRemoveAndDiscard(piece.getSpace(), expBoard.get());
        }
        Optional<Game> game = gameRepository.findById(expBoard.get().getGameId());
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found!");
        }
        logService.createLog(game.get(), user, "Player " + user.getName() + " played " + card.getName());
        webSocketController.onGameMessage(expBoard.get().getGameId().toString(),"{\"action\": \"CARD_PLAYED\", \"cardSlug\": \"" +  cardSlug + "\", \"uid\": \"" + user.getId() + "\" }");
        return expeditionBoardRepository.findProjectedById(expBoardId);
    }

    public ExpeditionBoardSummary playActionCard(String cardSlug, Long expBoardId, String userToken) {
        // Retrieve data
        Optional<ExpeditionBoard> expBoard = expeditionBoardRepository.findById(expBoardId);
        User user = userRepository.findByToken(userToken);
        Card card = cardRepository.findBySlug(cardSlug);
        //ExpBoard exists? Card exists?
        if(!this.userCanUseCard(card, expBoard, user)) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You can't use any cards now.");
        }                //Do action
        switch (card.getPower().toString()) {
            case "DRAWREMOVE":
                expBoard.get().getHandPile().remove(card);
                for (int i = 0; i < card.getPowerValue(); i++) {
                    expBoard.get().drawCard();
                }
                expBoard.get().setCanRemove(card.getPowerValue());
                break;
            case "BUYANY":
                expBoard.get().getHandPile().remove(card);
                expBoard.get().setCanBuy(card.getPowerValue());
                break;
            case "DRAW":
                expBoard.get().getHandPile().remove(card);
                for (int i = 0; i < card.getPowerValue(); i++) {
                    expBoard.get().drawCard();
                }
                break;
            case "MOVETOANY":
                expBoard.get().getHandPile().remove(card);
                if(expBoard.get().getPlayedCards().isEmpty()) {
                    expBoard.get().setCanMove(true);
                    for(Piece piece:expBoard.get().getPieces()) {
                        hexSpaceService.findValidNeighbours(piece.getSpace(), expBoard.get());
                    }
                }
                break;
            default:
                return expeditionBoardRepository.findProjectedById(expBoardId);
        }
        //If card is not item, place it in the discard pile
        if (!card.getRemovable()) {
            expBoard.get().getDiscardPile().add(card);
        }
        Optional<Game> game = gameRepository.findById(expBoard.get().getGameId());
        logService.createLog(game.get(), user, "Player " + user.getName() + " played " + card.getName());
        webSocketController.onGameMessage(expBoard.get().getGameId().toString(),"{\"action\": \"CARD_PLAYED\", \"cardSlug\": \"" +  cardSlug + "\", \"uid\": \"" + user.getId() + "\" }");
        return expeditionBoardRepository.findProjectedById(expBoardId);
    }

    private List<Card> generateStartingDeck() {

        List<Card> startingDeck = new ArrayList<>();

        Card sailor = cardService.getCard("sailor");
        Card explorer = cardService.getCard("explorer");
        Card traveler = cardService.getCard("traveler");

        // Add cards to starting deck
        startingDeck.add(sailor);

        for (int i = 0; i < 3; i++) {
            startingDeck.add(explorer);
        }
        for (int i = 0; i < 4; i++) {
            startingDeck.add(traveler);
        }

        return startingDeck;
    }

    public List<ExpeditionBoard> generateExpeditionBoards(Game game) {

        List<ExpeditionBoard> expBoards = new ArrayList<>();

        String[] colors = {"Red", "Blue", "Green", "Yellow"};

        for (int i = 0; i<game.getNumberPlayers(); i++) {
            ExpeditionBoard expBoard = new ExpeditionBoard();
            List<Card> startingDeck = this.generateStartingDeck();
            expBoard.setGameId(game.getId());
            expBoard.setOwnerId(game.getPlayers().get(i).getId());
            expBoard.setOwnerId(game.getPlayers().get(i).getId());
            expBoard.setDrawPile(startingDeck);
            expBoard.setGoldBalance(0.0);
            expBoard.setCanMove(false);
            expBoard.setCanRemove(0);
            expBoard.setCanBuy(0);
            expBoard.setColor(colors[i]);
            expBoard.shuffleDrawpile();

            List<Piece> pieces = new ArrayList<>();

            if(game.getNumberPlayers() == 2) {
                Piece piece1 = new Piece();
                piece1.setColor(colors[i]);
                piece1.setOwner(game.getPlayers().get(i));
                Piece piece2 = new Piece();
                piece2.setColor(colors[i]);
                piece2.setOwner(game.getPlayers().get(i));

                pieceRepository.save(piece1);
                pieceRepository.save(piece2);

                pieces.add(piece1);
                pieces.add(piece2);

            } else {
                Piece piece = new Piece();
                piece.setColor(colors[i]);

                piece.setOwner(game.getPlayers().get(i));
                pieceRepository.save(piece);

                pieces.add(piece);
            }

            expBoard.setPieces(pieces);

            for (int j=0; j<4; j++) {
                expBoard.drawCard();
            }
            expeditionBoardRepository.save(expBoard);

            for(Piece piece:expBoard.getPieces()) {
                piece.setExpBoard(expBoard);
            }
            expBoards.add(expBoard);
        }
        return expBoards;
    }

    public double sellCard(String cardSlug, String userToken, Long expBoardId){
        User user = userService.getByToken(userToken);
        Card toSellCard = cardRepository.findBySlug(cardSlug);

        Optional<ExpeditionBoard> expBoard = expeditionBoardRepository.findById(expBoardId);

        if(!this.userCanUseCard(toSellCard, expBoard, user)) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "Can't use the card now."); //403
        }
        if(expBoard.get().isAlreadyBought()) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You already bought a card in this turn."); //403
        }
        Optional<Game> game = gameRepository.findById(expBoard.get().getGameId());
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found."); //403
        }
        Optional<Marketplace> marketplace = marketPlaceRepository.findById(game.get().getMarketPlace().getId());
        if(!marketplace.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Market not found."); //403
        }

        logService.createLog(game.get(), user, "Player " + user.getName() + " sold " + toSellCard.getName());

        expBoard.get().getHandPile().remove(toSellCard);

        //If card is yellow or joker, and it's removable, don't add it back to discard pile.
        ArrayList<PowerType> removableTypes = new ArrayList<>();
        removableTypes.add(PowerType.YELLOW);
        removableTypes.add(PowerType.JOKER);
        if(toSellCard.getRemovable() && removableTypes.contains(toSellCard.getPower())) {
            logService.createLog(game.get(), user, "Card " + toSellCard.getName() + " was removed from the game.");
        } else {
            expBoard.get().getDiscardPile().add(toSellCard);
        }


        Double cardGoldValue = toSellCard.getValue();
        Double currentGoldBalance = expBoard.get().getGoldBalance();
        Double newGoldBalance = currentGoldBalance + cardGoldValue;
        expBoard.get().setGoldBalance(newGoldBalance);

        webSocketController.onGameMessage(expBoard.get().getGameId().toString(),"{\"action\": \"CARD_SOLD\", \"cardSlug\": \"" +  cardSlug + "\", \"uid\": \"" + user.getId() + "\" }");


        return newGoldBalance;

    }

    public double buyCard(String cardSlug, String userToken, Long expBoardId){
        User user = userService.getByToken(userToken);
        Card toBuyCard = cardRepository.findBySlug(cardSlug);

        Optional<ExpeditionBoard> expBoard = expeditionBoardRepository.findById(expBoardId);

        //Check user didn't already bought card
        if(expBoard.get().isAlreadyBought()) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You already bought a card in this turn."); //403
        }
        //Check game exist
        Optional<Game> game = gameRepository.findById(expBoard.get().getGameId());
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found."); //403
        }

        //Check marketplace exists
        Optional<Marketplace> marketplace = marketPlaceRepository.findById(game.get().getMarketPlace().getId());
        if(!marketplace.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Market not found."); //403
        }

        //Check correct turn
        int currentPlayerBoard = game.get().getCurrentPlayer();
        if(!game.get().getExpeditionBoards().get(currentPlayerBoard).equals(expBoard.get())) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "Not your turn."); //403
        }

        //check balance is enough
        if(expBoard.get().getGoldBalance() < toBuyCard.getPrice() && expBoard.get().getCanBuy() == 0) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "Not enough gold.."); //403
        }

        //Check marketPile exist
        MarketPile marketPile = marketPileRepository.findByCardSlugAndMarketPlaceId(cardSlug, marketplace.get().getId());
        if(marketPile == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "MarketPile not found."); //403
        }

        //Check pile has still cards
        if(marketPile.getMarketPlacePile().size() == 0) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "This card is sell out"); //403
        }

        //Check if front row has a free spot
        boolean isFrontRowVacant = false;
        int vacantMarketPilePos = 0;
        int pos = 0;
        //Check frontRow full
        for(MarketPile pile:marketplace.get().getMarketplaceFrontRow()) {
            if(pile.getMarketPlacePile().size() == 0) {
                isFrontRowVacant = true;
                vacantMarketPilePos = pos;
            }
            pos++;
        }


        //Check marketPile is in frontRow
        if(!isFrontRowVacant && !marketplace.get().getMarketplaceFrontRow().contains(marketPile) && expBoard.get().getCanBuy() == 0) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You can only buy cards from the front row"); //403
        }

        //If frontRow has one vacant spot and card was bought from another row, replace rows.
        if(isFrontRowVacant && marketplace.get().getMarketplaceMiddleRow().contains(marketPile)) {
            marketplace.get().getMarketplaceFrontRow().set(vacantMarketPilePos, marketPile);
            marketplace.get().getMarketplaceMiddleRow().remove(marketPile);
        }

        //Remove 1 card from marketPile
        marketPile.getMarketPlacePile().remove(0);

        //Add to discardPile
        List<Card> newDiscardPile = expBoard.get().getDiscardPile();
        newDiscardPile.add(toBuyCard);
        expBoard.get().setDiscardPile(newDiscardPile);

        //Player can't buy anymore in this turn
        expBoard.get().setAlreadyBought(true);
        int freeBuy = expBoard.get().getCanBuy();
        if(freeBuy > 0) {
            expBoard.get().setCanBuy(freeBuy - 1);
        } else {
            //Decrease gold balance
            Double currentGoldBalance = expBoard.get().getGoldBalance();
            Double newGoldBalance = currentGoldBalance - toBuyCard.getPrice();
            expBoard.get().setGoldBalance(newGoldBalance);
        }

        logService.createLog(game.get(), user, "Player " + user.getName() + " bought " + toBuyCard.getName());
        webSocketController.onGameMessage(expBoard.get().getGameId().toString(),"{\"action\": \"CARD_BOUGHT\", \"cardSlug\": \"" +  cardSlug + "\", \"uid\": \"" + user.getId() + "\" }");
        return expBoard.get().getGoldBalance();

    }


}