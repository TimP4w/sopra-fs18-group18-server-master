package ch.uzh.ifi.seal.soprafs18.entity;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.constant.GameStatus;
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

public class GameEntityTest {

    @Test
    public void getAndSetId(){
        Game testGameNr12 = new Game();
        Long generatedTestLong123456321 = new Random().nextLong();
        testGameNr12.setId(generatedTestLong123456321);
        Assert.assertEquals(generatedTestLong123456321, testGameNr12.getId());
    }

    @Test
    public void setAndGetName() {
        Game game = new Game();
        game.setName("TestName");
        Assert.assertEquals("TestName", game.getName());
    }

    @Test
    public void setAndGetOwner() {
        Game game = new Game();
        game.setOwner(1L);
        Assert.assertEquals(1L, game.getOwner().longValue());
    }

    @Test
    public void setAndGetStatus() {
        Game game = new Game();
        game.setStatus(GameStatus.PENDING);
        Assert.assertEquals(GameStatus.PENDING, game.getStatus());
    }

    @Test
    public void setAndGetCurrentPlayer() {
        Game game = new Game();
        game.setCurrentPlayer(0);
        Assert.assertEquals(0, game.getCurrentPlayer().intValue());
    }

    @Test
    public void setAndGetNumberPlayer() {
        Game game = new Game();
        game.setNumberPlayers(2);
        Assert.assertEquals(2, game.getNumberPlayers().intValue());
    }

    @Test
    public void setAndGetExpeditionBoards() {
        Game game = new Game();
        List<ExpeditionBoard> expBoards = new ArrayList<ExpeditionBoard>();
        game.setExpeditionBoards(expBoards);
        Assert.assertEquals(expBoards, game.getExpeditionBoards());
    }

    @Test
    public void setAndGetPlayers() {
        Game game = new Game();
        List<User> users = new ArrayList<User>();
        game.setPlayers(users);
        Assert.assertEquals(users, game.getPlayers());
    }

    @Test
    public void setAndGetBoard() {
        Game game = new Game();
        Board board = new Board();
        game.setBoard(board);
        Assert.assertEquals(board, game.getBoard());
    }

    @Test
    public void setAndGetMapId() {
        Game game = new Game();
        game.setMapId(1);
        Assert.assertEquals(1, game.getMapId());
    }

    @Test
    public void setAndGetTurnNumber() {
        Game game = new Game();
        game.setTurnNumber(1);
        Assert.assertEquals(1, game.getTurnNumber());
    }

    @Test
    public void getAndSetHaveReachedElDorado(){
        Game testGame78 = new Game();
        ExpeditionBoard testExpBoardNr78 = new ExpeditionBoard();
        List<ExpeditionBoard> testHaveReachedElDorado = new ArrayList<>();

        testHaveReachedElDorado.add(testExpBoardNr78);
        testGame78.setHaveReachedElDorado(testHaveReachedElDorado);
        Assert.assertEquals(testGame78.getHaveReachedElDorado(), testHaveReachedElDorado);
    }

    @Test
    public void getAndSetWinners(){
        Game testGameNr124 = new Game();
        ExpeditionBoard testExpBoard7812 = new ExpeditionBoard();
        List<ExpeditionBoard> testWinners = new ArrayList<>();

        testWinners.add(testExpBoard7812);
        testGameNr124.setWinners(testWinners);

        Assert.assertEquals(testGameNr124.getWinners(), testWinners);
    }

    @Test
    public void getAndSetLogs(){
        Game testGameNr89378264 = new Game();
        Log testLogNr67892347 = new Log();
        List<Log> testLogList = new ArrayList<>();

        testLogList.add(testLogNr67892347);
        testGameNr89378264.setLogs(testLogList);

        Assert.assertEquals(testGameNr89378264.getLogs(), testLogList);
    }
}