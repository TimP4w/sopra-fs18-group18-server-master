package ch.uzh.ifi.seal.soprafs18.entity;

import ch.uzh.ifi.seal.soprafs18.Application;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)

public class ExpeditionBoardEntityTest {

    @Test
    public void getAndSetId(){
        ExpeditionBoard expBoardTest99 = new ExpeditionBoard();
        Long generatedTestLongNr45 = new Random().nextLong();
        expBoardTest99.setId(generatedTestLongNr45);
        Assert.assertEquals(generatedTestLongNr45, expBoardTest99.getId());
    }

    @Test
    public void getAndSetGameId(){
        ExpeditionBoard expBoardTest999 = new ExpeditionBoard();
        Long generatedTestLongNr451 = new Random().nextLong();
        expBoardTest999.setGameId(generatedTestLongNr451);
        Assert.assertEquals(generatedTestLongNr451, expBoardTest999.getGameId());
    }

    @Test
    public void getAndSetUserId(){
        ExpeditionBoard expBoardTest9999 = new ExpeditionBoard();
        Long generatedTestLongNr452 = new Random().nextLong();
        expBoardTest9999.setOwnerId(generatedTestLongNr452);
        Assert.assertEquals(generatedTestLongNr452, expBoardTest9999.getOwnerId());
    }

    @Test
    public void setAndGetDrawPile() {
        ExpeditionBoard testExpeditionBoard1 = new ExpeditionBoard();
        Card scout123 = new Card();
        Card scout1234 = new Card();
        List<Card> testDrawPile = new ArrayList<>();
        testDrawPile.add(scout123);
        testDrawPile.add(scout1234);
        testExpeditionBoard1.setDrawPile(testDrawPile);
        Assert.assertEquals(testDrawPile, testExpeditionBoard1.getDrawPile());
    }

    @Test
    public void setAndGetHandPile(){
        ExpeditionBoard testExpeditionBoard2 = new ExpeditionBoard();
        Card testExplorer98 = new Card();
        Card testExplorer99 = new Card();
        List<Card> testHandPile = new ArrayList<>();
        testHandPile.add(testExplorer98);
        testHandPile.add(testExplorer99);
        testExpeditionBoard2.setHandPile(testHandPile);
        Assert.assertEquals(testHandPile, testExpeditionBoard2.getHandPile());
    }

    @Test
    public void setAndGetDiscardPile() {
        ExpeditionBoard testExpeditionBoard3 = new ExpeditionBoard();
        Card testTransmitter1 = new Card();
        Card testTransmitter2 = new Card();
        List<Card> testDiscardPile = new ArrayList<>();
        testDiscardPile.add(testTransmitter1);
        testDiscardPile.add(testTransmitter2);
        testExpeditionBoard3.setDiscardPile(testDiscardPile);
        Assert.assertEquals(testDiscardPile, testExpeditionBoard3.getDiscardPile());
    }

    @Test
    public void setAndGetPlayedCards() {
        ExpeditionBoard testExpeditionBoard4 = new ExpeditionBoard();
        Card testPlayedCard1 = new Card();
        Card testPlayedCard2 = new Card();
        List<Card> testPlayedCards = new ArrayList<>();
        testPlayedCards.add(testPlayedCard1);
        testPlayedCards.add(testPlayedCard2);
        testExpeditionBoard4.setPlayedCards(testPlayedCards);
        Assert.assertEquals(testPlayedCards, testExpeditionBoard4.getPlayedCards());
    }

    @Test
    public void setAndGetGoldBalance() {
        ExpeditionBoard testExpeditionBoard5 = new ExpeditionBoard();
        Double testGoldBalance = 0.5;
        testExpeditionBoard5.setGoldBalance(testGoldBalance);
        Assert.assertEquals(testGoldBalance, testExpeditionBoard5.getGoldBalance());
    }

    @Test
    public void setAndGetCanRemove(){
        ExpeditionBoard testExpeditionBoard6 = new ExpeditionBoard();
        Integer testCanRemove = 4;
        testExpeditionBoard6.setCanRemove(testCanRemove);
        Assert.assertEquals(testCanRemove, testExpeditionBoard6.getCanRemove());
    }

    @Test
    public void setAndGetCanMove(){
        ExpeditionBoard testExpeditionBoard7 = new ExpeditionBoard();
        Boolean testCanMove = true;
        testExpeditionBoard7.setCanMove(testCanMove);
        Assert.assertEquals(testCanMove, testExpeditionBoard7.getCanMove());
    }

    @Test
    public void setAndGetCanBuy (){
        ExpeditionBoard testExpeditionBoard8 = new ExpeditionBoard();
        Integer testCanBuy = 1;
        testExpeditionBoard8.setCanBuy(testCanBuy);
        Assert.assertEquals(testCanBuy, testExpeditionBoard8.getCanBuy());
    }

    @Test
    public void setAndGetColor(){
        ExpeditionBoard testExpeditionBoard9 = new ExpeditionBoard();
        String testColor = "pink";
        testExpeditionBoard9.setColor(testColor);
        Assert.assertEquals(testColor, testExpeditionBoard9.getColor());
    }

    @Test
    public void setAndGetPossibleMoves(){
        ExpeditionBoard testExpeditionBoard10 = new ExpeditionBoard();
        HexSpace testHexSpace = new HexSpace();
        List<HexSpace> testPossibleMoves = new ArrayList<>();
        testPossibleMoves.add(testHexSpace);
        testExpeditionBoard10.setPossibleMoves(testPossibleMoves);
        Assert.assertEquals(testPossibleMoves, testExpeditionBoard10.getPossibleMoves());
    }

    @Test
    public void setAndGetPieces() {
        ExpeditionBoard testExpeditionBoard11 = new ExpeditionBoard();
        Piece testPiece1 = new Piece();
        Piece testPiece2 = new Piece();
        List<Piece> testPieces = new ArrayList<>();
        testPieces.add(testPiece1);
        testPieces.add(testPiece2);
        testExpeditionBoard11.setPieces(testPieces);
        Assert.assertEquals(testPieces, testExpeditionBoard11.getPieces());
    }

    @Test
    public void SetAndGetPieces(){
        ExpeditionBoard testExpeditionBoard12 = new ExpeditionBoard();
        Blockade testBlockade1 = new Blockade();
        Blockade testBlockade2 = new Blockade();
        List<Blockade> testWonBlockades = new ArrayList<>();
        testWonBlockades.add(testBlockade1);
        testWonBlockades.add(testBlockade2);
        testExpeditionBoard12.setBlockadesWon(testWonBlockades);
        Assert.assertEquals(testWonBlockades, testExpeditionBoard12.getBlockadesWon());
    }

    @Test
    public void SetAndGetAlreadyBought(){
        ExpeditionBoard testExpeditionBoard13 = new ExpeditionBoard();
        Boolean testAlreadyBought = false;
        testExpeditionBoard13.setAlreadyBought(testAlreadyBought);
        Assert.assertEquals(testAlreadyBought, testExpeditionBoard13.isAlreadyBought());
    }

    @Test
    public void drawCard(){
        ExpeditionBoard testExpBoardNr78 = new ExpeditionBoard();
        List<Card> testDrawPileNr78 = new ArrayList<>();
        List<Card> testDiscardPileNr78 = new ArrayList<>();
        Card testCardNr781 = new Card();
        testDrawPileNr78.add(testCardNr781);

        testExpBoardNr78.setDrawPile(testDrawPileNr78);
        testExpBoardNr78.setDiscardPile(testDiscardPileNr78);
        Assert.assertEquals(testExpBoardNr78.drawCard(), testCardNr781);

        Assert.assertEquals(testExpBoardNr78.getDrawPile().isEmpty(), true);
        Assert.assertEquals(testExpBoardNr78.getDiscardPile().isEmpty(), true);
    }

    @Test
    public void swapDrawAndDiscardPile(){
        ExpeditionBoard testExpBoardNr79 = new ExpeditionBoard();
        List<Card> testDrawPileNr79 = new ArrayList<>();
        List<Card> testDiscardPileNr79 = new ArrayList<>();
        Card testCardNr791 = new Card();
        Card testCardNr792 = new Card();
        testDrawPileNr79.add(testCardNr791);
        testDiscardPileNr79.add(testCardNr792);

        List<Card> assembledList = new ArrayList<>();
        assembledList.add(testCardNr791);
        assembledList.add(testCardNr792);

        testExpBoardNr79.setDrawPile(testDrawPileNr79);
        testExpBoardNr79.setDiscardPile(testDiscardPileNr79);
        Assert.assertEquals(testExpBoardNr79.getDrawPile(), testDrawPileNr79);
        Assert.assertEquals(testExpBoardNr79.getDiscardPile(), testDiscardPileNr79);

        testExpBoardNr79.swapDrawAndDiscardPile();
        Assert.assertEquals(testExpBoardNr79.getDrawPile(), assembledList);
        Assert.assertEquals(testExpBoardNr79.getDiscardPile().isEmpty(), true);
    }

    @Test
    public void resetStats(){
        ExpeditionBoard testExpBoardNr990 = new ExpeditionBoard();
        List<HexSpace> emptyResetedList = new ArrayList<>();
        testExpBoardNr990.resetStats();

        Assert.assertNotNull(testExpBoardNr990.getGoldBalance());
        Assert.assertNotNull(testExpBoardNr990.getCanRemove());
        Assert.assertNotNull(testExpBoardNr990.getCanMove());
        Assert.assertNotNull(testExpBoardNr990.getCanBuy());
        Assert.assertEquals(testExpBoardNr990.getPossibleMoves(), emptyResetedList);
        Assert.assertNotNull(testExpBoardNr990.isAlreadyBought());
    }

    @Test
    public void drawHand(){
        ExpeditionBoard testExpBoardNr81 = new ExpeditionBoard();
        List<Card> testDrawPileNr81 = new ArrayList<>();
        List<Card> testHandPileNr81 = new ArrayList<>();
        Card testCardNr811 = new Card(); Card testCardNr812 = new Card();
        Card testCardNr813 = new Card(); Card testCardNr814 = new Card();
        Card testCardNr815 = new Card();

        testDrawPileNr81.add(testCardNr811);
        testDrawPileNr81.add(testCardNr812);
        testDrawPileNr81.add(testCardNr813);
        testDrawPileNr81.add(testCardNr814);
        testDrawPileNr81.add(testCardNr815);

        testExpBoardNr81.setDrawPile(testDrawPileNr81);
        testExpBoardNr81.setHandPile(testHandPileNr81);

        Assert.assertEquals(testExpBoardNr81.getDrawPile(), testDrawPileNr81);
        Assert.assertEquals(testExpBoardNr81.getHandPile(), testHandPileNr81);
        Assert.assertEquals(testExpBoardNr81.getHandPile().isEmpty(), true);

        testExpBoardNr81.drawHand();

        Assert.assertEquals(testExpBoardNr81.getHandPile().isEmpty(), false);
    }
}