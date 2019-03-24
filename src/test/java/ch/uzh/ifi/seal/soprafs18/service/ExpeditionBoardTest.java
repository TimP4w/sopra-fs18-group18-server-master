package ch.uzh.ifi.seal.soprafs18.service;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs18.entity.*;
import ch.uzh.ifi.seal.soprafs18.entity.projections.ExpeditionBoardSummary;
import ch.uzh.ifi.seal.soprafs18.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs18.repository.HexSpaceRepository;
import ch.uzh.ifi.seal.soprafs18.repository.MarketPlaceRepository;
import ch.uzh.ifi.seal.soprafs18.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs18.web.rest.ResourceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)

public class ExpeditionBoardTest {

    @Autowired
    private ExpeditionBoardService expeditionBoardService;

    @Autowired
    private CardService cardService;

    @Autowired
    @Qualifier("UserRepository")
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private MarketPlaceService marketPlaceService;

    @Autowired
    private MarketPlaceRepository marketPlaceRepository;

    @Autowired
    private HexSpaceRepository hexSpaceRepository;

    @Test
    public void createExpeditionBoard (){
        ExpeditionBoard expBoard = new ExpeditionBoard();

        List<Card> testDeck = new Stack<>();
        List<Card> controlDeck = new Stack<>();

        Card sailor = cardService.getCard("sailor");
        Card explorer = cardService.getCard("explorer");
        Card traveler = cardService.getCard("traveler");
        Card scout = cardService.getCard("scout");

        testDeck.add(sailor);
        testDeck.add(explorer);
        testDeck.add(traveler);
        testDeck.add(scout);

        controlDeck.add(sailor);
        controlDeck.add(explorer);
        controlDeck.add(traveler);
        controlDeck.add(scout);

        Long testId = -1L;

        expBoard.setGameId(testId);
        expBoard.setOwnerId(testId);
        expBoard.setDrawPile(testDeck);
        expBoard.setGoldBalance(0.0);
        expBoard.setCanRemove(0);
        expBoard.setCanBuy(0);
        expBoard.setColor("Red");
        expBoard.setCanMove(false);

        Assert.assertEquals(expBoard.getDrawPile(), controlDeck);

        expBoard.shuffleDrawpile();

        Assert.assertEquals(expBoard.getGameId(), testId);
        Assert.assertEquals(expBoard.getOwnerId(), testId);
        Assert.assertTrue(expBoard.getGoldBalance().equals(0.0));
        Assert.assertEquals((int) expBoard.getCanRemove(), 0);
        Assert.assertEquals((int) expBoard.getCanBuy(), 0);
        Assert.assertEquals(expBoard.getColor(), "Red");
        Assert.assertEquals(expBoard.getCanMove(), false);

        // TODO check this again since it MAY fail (random)
        // Assert.assertNotEquals(expBoard.getDrawPile(), controlDeck);

    }

    @Test
    public void drawCard() {
        ExpeditionBoard expBoard = new ExpeditionBoard();

        List<Card> testDeck = new Stack<>();
        Card sailor = cardService.getCard("sailor");
        Card explorer = cardService.getCard("explorer");
        Card traveler = cardService.getCard("traveler");
        Card scout = cardService.getCard("scout");

        testDeck.add(sailor);
        testDeck.add(explorer);
        testDeck.add(traveler);
        testDeck.add(scout);

        Long testId = -2L;

        expBoard.setGameId(testId);
        expBoard.setOwnerId(testId);
        expBoard.setDrawPile(testDeck);
        expBoard.setGoldBalance(0.0);
        expBoard.setCanRemove(0);
        expBoard.setCanBuy(0);
        expBoard.setColor("Red");
        expBoard.setCanMove(false);

        Assert.assertTrue(expBoard.getDrawPile().size() == 4);

        //Check hand pile empty
        Assert.assertTrue(expBoard.getHandPile().size() == 0);
        //Check first element in drawPile is sailor
        Assert.assertEquals(expBoard.getDrawPile().get(0), sailor);

        expBoard.drawCard();

        //Now hand pile shouldn't be empty
        Assert.assertTrue(expBoard.getHandPile().size() == 1);
        //First element should be former first element of draw pile
        Assert.assertEquals(expBoard.getHandPile().get(0), sailor);
        //First element of draw pile no longer sailor
        Assert.assertEquals(expBoard.getDrawPile().get(0), explorer);
        //Card removed from drawPile
        Assert.assertTrue(expBoard.getDrawPile().size() == 3);
    }

    @Transactional
    @Test
    public void playCardTest() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.

        User user1 = new User();
        user1.setToken("t123-test-token");
        user1.setName("TestUser");
        user1.setUsername("Test");
        user1.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);

        User user2 = new User();
        user2.setToken("t456-test-token");
        user2.setName("TestUser2");
        user2.setUsername("User2");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user2);

        User user3 = null;

        //Test a map setting of a non existing game
        Long randomTestGameIdNr1 = -1L;
        int TestMapId = 6;
        Assert.assertNull(gameService.setMap(randomTestGameIdNr1, user1.getToken(), TestMapId));

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(user1.getToken());
        Optional<Game> game = gameRepository.findById(gameId);

        //Test number players method before starting the game
        gameService.setNumberPlayer(gameId, user1.getToken(), 2);
        Assert.assertNotNull(game.get().getNumberPlayers());

        //Test list of pending games; must be one and not zero
        Assert.assertNotEquals(gameService.listPendingGames().size(), 0);

        gameService.addPlayer(gameId, user2.getToken());
        gameService.startGame(gameId, user1.getToken());

        List<ExpeditionBoard> expBoards = game.get().getExpeditionBoards();

        ExpeditionBoard expBoard = expBoards.get(0);
        ExpeditionBoard expBoardPl2 = expBoards.get(1);

        // replace hand cards with a deck of suitable cards for testing
        List<Card> testDeck = new Stack<>();

        Card sailor = cardService.getCard("sailor");
        Card explorer = cardService.getCard("explorer");
        Card traveler = cardService.getCard("pioneer");
        Card jack = cardService.getCard("jackofalltrades");
        Card transmitter = cardService.getCard("transmitter");

        testDeck.add(sailor);
        testDeck.add(explorer);
        testDeck.add(traveler);
        testDeck.add(jack);

        expBoard.setHandPile(testDeck);
        expBoardPl2.setHandPile(testDeck);

        // put a piece on an arbitrary position (10,15) on the map
        Board board = game.get().getBoard();
        Piece piece = expBoard.getPieces().get(0);
        HexSpace newPos = hexSpaceRepository.findByXAndYAndBoardId(10, 15, board.getId());
        HexSpace oldPos = piece.getSpace();
        oldPos.setOccupiedBy(null);
        piece.setSpace(newPos);
        newPos.setOccupiedBy(piece);

        //Check that possible moves are the right ones
        Assert.assertTrue(expBoard.getPossibleMoves().size() == 0);

        ExpeditionBoardSummary expSummary = expeditionBoardService.playCard("explorer", expBoard.getId(), user1.getToken());
        Assert.assertTrue(expSummary.getPossibleMoves().size() > 0);
        Assert.assertTrue(expSummary.getPossibleMoves().size() == 5);
        HexSpace green1Piece1 = hexSpaceRepository.findByXAndYAndBoardId(9, 15, board.getId());
        HexSpace green2Piece1 = hexSpaceRepository.findByXAndYAndBoardId(11, 15, board.getId());
        HexSpace redPiece1 = hexSpaceRepository.findByXAndYAndBoardId(10, 14, board.getId());
        HexSpace green1Piece2 = hexSpaceRepository.findByXAndYAndBoardId(18, 2, board.getId());
        HexSpace green2Piece2 = hexSpaceRepository.findByXAndYAndBoardId(19, 3, board.getId());


        List<HexSpace> possibleHardMoves = new ArrayList<>();
        possibleHardMoves.add(green1Piece1 );
        possibleHardMoves.add(green2Piece1 );
        possibleHardMoves.add(redPiece1);
        possibleHardMoves.add(green1Piece2);
        possibleHardMoves.add(green2Piece2);


        Assert.assertTrue(expBoard.getPossibleMoves().containsAll(possibleHardMoves));

        //Check that the card was removed from the hand of the player
        Assert.assertFalse(expSummary.getHandPile().contains(explorer));

        //Check that card is present in played cards
        Assert.assertTrue(expSummary.getPlayedCards().contains(explorer));

        //Check that a ResourceException is thrown if the user is not allowed to move
        try {
            expeditionBoardService.userCanUseCard(sailor, Optional.ofNullable(expBoardPl2), user2);
            assert false;
        } catch (ResourceException e) {
            Assert.assertEquals("It's not your turn.", e.getMessage());
        }

        //Check that a user cannot play a card that is not in his/her hand -> must throw an exception
        try {
            expeditionBoardService.userCanUseCard(transmitter, Optional.ofNullable(expBoard), user1);
            assert false;
        } catch (ResourceException e1) {
            Assert.assertEquals("You don't have that card in your hand!", e1.getMessage());
        }

        //Check that one can't play a card if the user is in a move sequence
        expBoard.setCanMove(true);
        try {
            expeditionBoardService.playCard("sailor", expBoard.getId(), user1.getToken());
        } catch (ResourceException e2) {
            Assert.assertEquals("You need to make a move before playing a card!", e2.getMessage());
        }


        //Ensure that if a player (user1) plays more than one card, the only moves left are DISCARD and REMOVE
        expBoard.setCanMove(false);

        expeditionBoardService.playCard("sailor", expBoard.getId(), user1.getToken());
        HexSpace grey = hexSpaceRepository.findByXAndYAndBoardId(11, 16, board.getId());

        List<HexSpace> possibleHardMoves2 = new ArrayList<>();
        possibleHardMoves2.add(redPiece1);
        possibleHardMoves2.add(grey);

        Assert.assertTrue(expBoard.getPossibleMoves().containsAll(possibleHardMoves2));


        //Ensure that when a joker is played, the right moves are displayed
        expBoard.setPlayedCards(new ArrayList<Card>());
        expeditionBoardService.playCard("jackofalltrades", expBoard.getId(), user1.getToken());

        List<HexSpace> possibleHardMoves3 = new ArrayList<>();
        possibleHardMoves3.add(hexSpaceRepository.findByXAndYAndBoardId(9, 15, board.getId()));
        possibleHardMoves3.add(hexSpaceRepository.findByXAndYAndBoardId(10, 16, board.getId()));
        possibleHardMoves3.add(hexSpaceRepository.findByXAndYAndBoardId(11, 15, board.getId()));
        possibleHardMoves3.add(hexSpaceRepository.findByXAndYAndBoardId(10, 14, board.getId()));

        Assert.assertTrue(expBoard.getPossibleMoves().containsAll(possibleHardMoves3));

        //Check that a user cannot play a card in someone else's expBoard
        try {
            expeditionBoardService.userCanUseCard(transmitter,Optional.ofNullable(expBoard), user2);
            assert false;
        } catch (ResourceException e3) {
            Assert.assertEquals("You're not the owner of the Expedition Board", e3.getMessage());
        }

        //Check that a null user cannot play a card
        try {
            expeditionBoardService.userCanUseCard(transmitter, Optional.ofNullable(expBoard), user3);
            assert false;
        } catch (ResourceException e4){
            Assert.assertEquals("User not found.", e4.getMessage());
        }

        expeditionBoardService.discardCard("pioneer", expBoard.getId(), user1.getToken());
        Assert.assertEquals(expBoard.getHandPile().size(), 0);
        Assert.assertEquals(expBoard.getDiscardPile().size(), 1);

        try {
            expeditionBoardService.discardCard("explorer", expBoard.getId(), user1.getToken());
            assert false;
        } catch (ResourceException e5){
            Assert.assertEquals("You don't have that card in your hand!", e5.getMessage());
        }

        //Set canremove to 0 to get an exception and catch it
        expBoard.setCanRemove(0);
        try {
            expeditionBoardService.removeCard("explorer", expBoard.getId(), user1.getToken());
            assert false;
        } catch (ResourceException e6){
            Assert.assertEquals("You don't have that card in your hand!", e6.getMessage());
        }
        List<Card> mustUpdatedList = new ArrayList<>();
        mustUpdatedList = expBoard.getHandPile();
        mustUpdatedList.add(explorer);
        expBoard.setHandPile(mustUpdatedList);

        try {
            expeditionBoardService.removeCard("explorer", expBoard.getId(), user1.getToken());
            assert false;
        } catch (ResourceException e7){
            Assert.assertEquals("You can't use any cards now.", e7.getMessage());
        }

        expBoard.setCanRemove(1);
        expeditionBoardService.removeCard("explorer", expBoard.getId(), user1.getToken());
        Assert.assertNotNull(expBoard.getCanRemove());
        Assert.assertEquals(expBoard.getHandPile().size(), 0);


      //Testing of selling a card
        //We insert 4 new cards into a new handpile, put the hand to the expeditionboard and test the sell cards mechanisms
        Card photographer = cardService.getCard("photographer"); //Powertype yellow
        photographer.setPower(PowerType.YELLOW);
        photographer.setRemovable(true);
        Card giantmachete = cardService.getCard("giantmachete");
        Card millionaire = cardService.getCard("millionaire");
        Card adventurer = cardService.getCard("adventurer");
        adventurer.setPower(PowerType.JOKER);
        adventurer.setRemovable(true);
        adventurer.setValue(2.5);

        List<Card> handPileTestSellCard = new ArrayList<>();
        handPileTestSellCard.add(photographer); handPileTestSellCard.add(giantmachete);
        handPileTestSellCard.add(millionaire); handPileTestSellCard.add(adventurer);

        List<Card> discardPileSellCard = new ArrayList<>();

        expBoard.setHandPile(handPileTestSellCard);
        expBoard.setDiscardPile(discardPileSellCard);

        //Try to sell a card even if the user already sold one in his/her turn
        expBoard.setAlreadyBought(true);

        try {
            expeditionBoardService.sellCard("giantmachete", user1.getToken(), expBoard.getId());
            assert false;
        } catch (ResourceException e8){
            Assert.assertEquals("You already bought a card in this turn.", e8.getMessage());
        }
        expBoard.setAlreadyBought(false);

        //Sell a card. The hand pile size must not be 4 anymore, but three
        expeditionBoardService.sellCard("giantmachete", user1.getToken(), expBoard.getId());
        Assert.assertEquals(expBoard.getHandPile().size(), 3);
        Assert.assertEquals(expBoard.getDiscardPile().size(), 1);

        //Sell a card of type yellow. Now the hand pile should decrease and nothing happens with the size of the discard pile
        expeditionBoardService.sellCard("photographer", user1.getToken(), expBoard.getId());
        Assert.assertEquals(expBoard.getHandPile().size(), 2);
        Assert.assertEquals(expBoard.getDiscardPile().size(), 1);

        Double goldBalanceUser1Exp1 = 0.0;
        expBoard.setGoldBalance(goldBalanceUser1Exp1);

        //Sell a card of type joker. Now the hand pile should decrease and nothing happens with the size of the discard pile
        expeditionBoardService.sellCard("adventurer", user1.getToken(), expBoard.getId());
        Assert.assertEquals(expBoard.getHandPile().size(), 1);
        Assert.assertEquals(expBoard.getDiscardPile().size(), 1);
        Assert.assertNotEquals(expBoard.getGoldBalance(), 0.0);
    }

    @Transactional
    @Test
    public void playActionCardTest() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.

        User userNr1 = new User();
        userNr1.setToken("t123-test-token");
        userNr1.setName("TestUser");
        userNr1.setUsername("Test");
        userNr1.setStatus(UserStatus.ONLINE);
        userRepository.save(userNr1);

        User userNr2 = new User();
        userNr2.setToken("t456-test-token");
        userNr2.setName("TestUser2");
        userNr2.setUsername("User2");
        userNr2.setStatus(UserStatus.ONLINE);
        userRepository.save(userNr2);

        User userNr3 = null;

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(userNr1.getToken());
        Optional<Game> game = gameRepository.findById(gameId);

        gameService.addPlayer(gameId, userNr2.getToken());
        gameService.startGame(gameId, userNr1.getToken());

        List<ExpeditionBoard> expBoards = game.get().getExpeditionBoards();
        ExpeditionBoard expBoard = expBoards.get(0);
        ExpeditionBoard expBoardPl2 = expBoards.get(1);

        // replace hand cards with a deck of suitable cards for testing
        List<Card> testDeck = new Stack<>();

        Card scientist = cardService.getCard("scientist"); //Of type DRAWREMOVE
        Card transmitter = cardService.getCard("transmitter");// Of type BUYANY
        Card cartographer = cardService.getCard("cartographer");// Of type DRAW
        Card nativeCard = cardService.getCard("native");// Of type MOVETOANY

        testDeck.add(scientist);
        testDeck.add(cartographer);
        testDeck.add(transmitter);
        testDeck.add(nativeCard);

        expBoard.setHandPile(testDeck);
        expBoardPl2.setHandPile(testDeck);

        //User 2 tries to play a card
        try {
            expeditionBoardService.playActionCard("scientist", expBoardPl2.getId(), userNr2.getToken());
            assert false;
        } catch (ResourceException e8){
            Assert.assertEquals("It's not your turn.", e8.getMessage());
        }

        expeditionBoardService.playActionCard("scientist", expBoard.getId(), userNr1.getToken());
        Assert.assertEquals(expBoard.getHandPile().size(), 4);

        expeditionBoardService.playActionCard("transmitter", expBoard.getId(), userNr1.getToken());
        Assert.assertEquals(expBoard.getHandPile().size(), 3);

        expeditionBoardService.playActionCard("cartographer", expBoard.getId(), userNr1.getToken());
        Assert.assertEquals(expBoard.getHandPile().size(), 4);

        expeditionBoardService.playActionCard("native", expBoard.getId(), userNr1.getToken());
        Assert.assertEquals(expBoard.getHandPile().size(), 3);
        Assert.assertEquals(expBoard.getCanMove(), true);
    }

    @Transactional
    @Test
    public void buyCardTestMiniMap() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.
        User user1 = new User();
        user1.setToken("t123123-test-token");
        user1.setName("TestUser123");
        user1.setUsername("Test123");
        user1.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);

        User user2 = new User();
        user2.setToken("t456123-test-token");
        user2.setName("TestUser2123");
        user2.setUsername("User2123");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user2);

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(user1.getToken());
        gameService.addPlayer(gameId, user2.getToken());
        //Test setting the map

        int TestMapId2 = 7;
        Assert.assertNotNull(gameService.setMap(gameId, user1.getToken(), TestMapId2));
        gameService.startGame(gameId, user1.getToken());


        Optional<Game> game = gameRepository.findById(gameId);
        List<ExpeditionBoard> expBoards = game.get().getExpeditionBoards();

        ExpeditionBoard expBoard = expBoards.get(0);
        ExpeditionBoard expBoard2 = expBoards.get(1);

        // replace hand cards with a deck of suitable cards for testing
        List<Card> testDeck = new Stack<>();

        Card sailor = cardService.getCard("sailor");
        Card explorer = cardService.getCard("explorer");
        Card traveler = cardService.getCard("pioneer");
        Card jack = cardService.getCard("jackofalltrades");

        testDeck.add(sailor);
        testDeck.add(explorer);
        testDeck.add(traveler);
        testDeck.add(jack);

        expBoard.setHandPile(testDeck);
        expBoard.setGoldBalance(10.0);
        expBoard2.setHandPile(testDeck);

        //The user now tries to buy a card that is in another row than in the front row
        //There must be an exception
        try {
            expeditionBoardService.buyCard("propplane", user1.getToken(), expBoard.getId());
            assert false;
        } catch (ResourceException e){
            Assert.assertEquals("You can only buy cards from the front row", e.getMessage());
        }

        //Now the user tries to buy a card with not enough gold balance
        expBoard.setGoldBalance(0.5);
        try {
            expeditionBoardService.buyCard("treasurechest", user1.getToken(), expBoard.getId());
            assert false;
        } catch (ResourceException e2){
            Assert.assertEquals("Not enough gold..", e2.getMessage());
        }

        expBoard.setCanBuy(1);
        //Now the user buys a card and then tries to buy another one
        //There must be an exception
        expeditionBoardService.buyCard("trailblazer", user1.getToken(), expBoard.getId());

        try {
            expeditionBoardService.buyCard("scout", user1.getToken(), expBoard.getId());
            assert false;
        } catch (ResourceException e1){
            Assert.assertEquals("You already bought a card in this turn.", e1.getMessage());
        }

        //Now we test the nextturn method
        //We try to nextturn a game that does not exist, we expect a null value to be returned
        Long notExistingGameId = -2L;
        Assert.assertNull(gameService.nextTurn(notExistingGameId, user2.getToken()));

        //We try to nextturn even if it is not the users turn that requests it
        try {
            gameService.nextTurn(gameId, user2.getToken());
            assert false;
        } catch (ResourceException e2){
            Assert.assertEquals("It's not your turn.", e2.getMessage());
        }

     //Now we nextturn the current player
        //Check that the played cards got discarded, the possible moves got reseted and the drawHand pile size is 4
        Assert.assertNotNull(gameService.nextTurn(gameId, user1.getToken()));
        Assert.assertEquals(expBoard.getPlayedCards().isEmpty(), true);
        Assert.assertEquals(expBoard.getDiscardPile().isEmpty(), false);

        Assert.assertNotNull(expBoard.getGoldBalance());
        Assert.assertNotNull(expBoard.getCanRemove());
        Assert.assertNotNull(expBoard.getCanMove());
        Assert.assertNotNull(expBoard.getCanBuy());
        Assert.assertNotNull(expBoard.isAlreadyBought());

        Assert.assertEquals(expBoard.getHandPile().size(), 4);

        //Furthermore, the turnnumber of the game shouldve been increased by one
        //and the next player (method: setcurrentplayer) must be user2

        Assert.assertEquals(game.get().getTurnNumber(), 1);
        Assert.assertNotNull(game.get().getCurrentPlayer());

     //Check for getting Game Logs
        Long falseGameId = -3L;
        //We expect an exception when searching for a game with a GameId that does not exist
        try {
            gameService.getGameLogs(falseGameId);
            assert false;
        } catch (ResourceException e3){
            Assert.assertEquals("Game not found.", e3.getMessage());
        }

        //Now we expect to get returned the list of logs that is saved in the database
        List<Log> gameLogListTest = new ArrayList<>();
        gameLogListTest = game.get().getLogs();
        Assert.assertEquals(gameService.getGameLogs(gameId), gameLogListTest);
    }

    @Transactional
    @Test
    public void testHomeStretchGenerateMap() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.
        User user1 = new User();
        user1.setToken("t123123-test-token1");
        user1.setName("TestUser123");
        user1.setUsername("Test123");
        user1.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);

        User user2 = new User();
        user2.setToken("t456123-test-token2");
        user2.setName("TestUser2123");
        user2.setUsername("User2123");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user2);

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(user1.getToken());
        gameService.addPlayer(gameId, user2.getToken());
        //Test setting the map

        int TestMapId2 = 2;
        Assert.assertNotNull(gameService.setMap(gameId, user1.getToken(), TestMapId2));
        gameService.startGame(gameId, user1.getToken());
    }

    @Transactional
    @Test
    public void testWindingPathsGenerateMap() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.
        User user1 = new User();
        user1.setToken("t123123-test-token134");
        user1.setName("TestUser12334");
        user1.setUsername("Test12334");
        user1.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);

        User user2 = new User();
        user2.setToken("t456123-test-token234");
        user2.setName("TestUser212334");
        user2.setUsername("User212334");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user2);

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(user1.getToken());
        gameService.addPlayer(gameId, user2.getToken());
        //Test setting the map

        int TestMapId2 = 3;
        Assert.assertNotNull(gameService.setMap(gameId, user1.getToken(), TestMapId2));
        gameService.startGame(gameId, user1.getToken());
    }

    @Transactional
    @Test
    public void testSerpentineGenerateMap() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.
        User user1 = new User();
        user1.setToken("t123123-test-token1678");
        user1.setName("TestUser123678");
        user1.setUsername("Test123678");
        user1.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);

        User user2 = new User();
        user2.setToken("t456123-test-token2678");
        user2.setName("TestUser2123678");
        user2.setUsername("User2123678");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user2);

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(user1.getToken());
        gameService.addPlayer(gameId, user2.getToken());
        //Test setting the map

        int TestMapId2 = 4;
        Assert.assertNotNull(gameService.setMap(gameId, user1.getToken(), TestMapId2));
        gameService.startGame(gameId, user1.getToken());
    }

    @Transactional
    @Test
    public void testSwampLandsGenerateMap() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.
        User user1 = new User();
        user1.setToken("t123123-test-token1678456");
        user1.setName("TestUser123678456");
        user1.setUsername("Test123678456");
        user1.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);

        User user2 = new User();
        user2.setToken("t456123-test-token2678456");
        user2.setName("TestUser2123678456");
        user2.setUsername("User2123678456");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user2);

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(user1.getToken());
        gameService.addPlayer(gameId, user2.getToken());
        //Test setting the map

        int TestMapId2 = 5;
        Assert.assertNotNull(gameService.setMap(gameId, user1.getToken(), TestMapId2));
        gameService.startGame(gameId, user1.getToken());
    }

    @Transactional
    @Test
    public void testWitchsCauldronGenerateMap() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.
        User user1 = new User();
        user1.setToken("t123123-test-token167845");
        user1.setName("TestUser12367845");
        user1.setUsername("Test12367845");
        user1.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);

        User user2 = new User();
        user2.setToken("t456123-test-token267845");
        user2.setName("TestUser212367845");
        user2.setUsername("User212367845");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user2);

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(user1.getToken());
        gameService.addPlayer(gameId, user2.getToken());
        //Test setting the map

        int TestMapId2 = 6;
        Assert.assertNotNull(gameService.setMap(gameId, user1.getToken(), TestMapId2));
        gameService.startGame(gameId, user1.getToken());
    }



    @Transactional
    @Test
    public void getOwnExpBoard() {
        // Setting up prerequisites for test
        // Instantiate two users and store them in the db.
        User user1 = new User();
        user1.setToken("t123123-test-token1567");
        user1.setName("TestUser1567");
        user1.setUsername("Test1567");
        user1.setStatus(UserStatus.ONLINE);
        userRepository.save(user1);

        User user2 = new User();
        user2.setToken("t456123-test-token1567");
        user2.setName("TestUser21567");
        user2.setUsername("User21567");
        user2.setStatus(UserStatus.ONLINE);
        userRepository.save(user2);

        User user3 = null;

        // Start a game with both of the above players, user1 being the owner
        Long gameId = gameService.addGame(user1.getToken());
        gameService.addPlayer(gameId, user2.getToken());
        gameService.startGame(gameId, user1.getToken());

        //Check if expBoards are not the same
        Optional<Game> game = gameRepository.findById(gameId);
        List<ExpeditionBoard> expBoards = game.get().getExpeditionBoards();

        ExpeditionBoard expBoardPlayer1 = expBoards.get(0);
        Assert.assertNotEquals(expBoardPlayer1, expeditionBoardService.getOwnExpBoard(user1.getToken(), gameId));
    }
}