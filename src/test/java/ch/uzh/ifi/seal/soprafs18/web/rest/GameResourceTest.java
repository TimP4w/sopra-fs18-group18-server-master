package ch.uzh.ifi.seal.soprafs18.web.rest;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.entity.Game;
import ch.uzh.ifi.seal.soprafs18.entity.User;
import ch.uzh.ifi.seal.soprafs18.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs18.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs18.service.GameService;
import ch.uzh.ifi.seal.soprafs18.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "security.basic.enabled=false"
        })
public class GameResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    UserService userService;

    @Autowired
    GameService gameService;
    GameRepository gameRepository;

    @Autowired
    @Qualifier("UserRepository")
    private UserRepository userRepository;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();


    @Test
    public void addGame() {
        User user = new User();
        user.setName("TestAddGameName");
        user.setUsername("TestAddGameUsername");
        user = userService.addUser(user);
        user = userService.login(user.getUsername());

        HttpEntity<User> entity = new HttpEntity<User>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/games"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("201", response.getStatusCode().toString());

        User unsavedUser = new User();
        unsavedUser.setToken("not-existing");
        entity = new HttpEntity<User>(unsavedUser, headers);
        response = restTemplate.exchange(
                createURLWithPort("/games"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("404", response.getStatusCode().toString());
    }

    @Test
    public void getGame() {
        User user = new User();
        user.setName("TestGetGameName");
        user.setUsername("TestGetGameUsername");
        user = userService.addUser(user);
        user = userService.login(user.getUsername());
        Long gameId = gameService.addGame(user.getToken());


        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/games/" + gameId),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals("200", response.getStatusCode().toString());

        Long falseId = -1L;
        response = restTemplate.exchange(
                createURLWithPort("/games/" + falseId),
                HttpMethod.GET, entity, String.class);

        Assert.assertEquals("404", response.getStatusCode().toString());

    }

    @Test
    public void gameStart() {

        // Not enough players
        User user = new User();
        user.setName("TestStartGameName");
        user.setUsername("TestStartGameUsername");
        user = userService.addUser(user);
        user = userService.login(user.getUsername());
        Long gameId = gameService.addGame(user.getToken());

        HttpEntity<User> entity = new HttpEntity<User>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/games/"  + gameId + "/start"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("403", response.getStatusCode().toString());

        //Test not owner
        User secondUser = new User();
        secondUser.setName("TestStartGameName2");
        secondUser.setUsername("TestStartGameUsername2");
        secondUser = userService.addUser(secondUser);
        secondUser = userService.login(secondUser.getUsername());
        gameService.addPlayer(gameId, secondUser.getToken());

        entity = new HttpEntity<User>(secondUser, headers);
        response = restTemplate.exchange(
                createURLWithPort("/games/"  + gameId + "/start"),
                HttpMethod.POST, entity, String.class);
        Assert.assertEquals("403", response.getStatusCode().toString());

        //A null player tries to start the game
        User thirdUser = null;
        HttpEntity<User>entity5 = new HttpEntity<User>(thirdUser, headers);
        ResponseEntity<String>response5 = restTemplate.exchange(
                createURLWithPort("/games/"  + gameId + "/start"),
                HttpMethod.POST, entity5, String.class);
        Assert.assertEquals("500", response5.getStatusCode().toString());

        // OK
        entity = new HttpEntity<User>(user, headers);
        response = restTemplate.exchange(
                createURLWithPort("/games/"  + gameId + "/start"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("200", response.getStatusCode().toString());


        //Test game already started
        entity = new HttpEntity<User>(user, headers);
        response = restTemplate.exchange(
                createURLWithPort("/games/"  + gameId + "/start"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("403", response.getStatusCode().toString());

        //Next turn logic
        entity = new HttpEntity<User>(user, headers);
        response = restTemplate.exchange(
                createURLWithPort("/games/"  + gameId + "/next"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("200", response.getStatusCode().toString());

        //Test non existing game
        entity = new HttpEntity<User>(user, headers);
        response = restTemplate.exchange(
                createURLWithPort("/games/99999999999/start"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("404", response.getStatusCode().toString());

        //Test getting the game logs
        HttpEntity<String> entity2 = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/games/" + gameId +"/logs"),
                HttpMethod.GET, entity2, String.class);

        Assert.assertEquals("200", response2.getStatusCode().toString());

        //Test getting the list of pending games
        ResponseEntity<String> response3 = restTemplate.exchange(
                createURLWithPort("/games/pending"),
                HttpMethod.GET, entity2, String.class);

        Assert.assertEquals("200", response3.getStatusCode().toString());
    }

    @Test
    public void addPlayer(){
        User user = new User();
        user.setName("TestStartGameNameNr3");
        user.setUsername("TestStartGameUsernameNr3");
        user.setToken("Nr1FF");
        user = userService.addUser(user);
        user = userService.login(user.getUsername());
        Long testgameId1234321 = gameService.addGame(user.getToken());

        User user2test = new User();
        user2test.setName(("TestStartGameNameNr31"));
        user2test.setUsername("TestStartGameUsernameNr31");
        user2test.setToken("Nr2FF");
        user2test = userService.addUser(user2test);
        user2test = userService.login(user.getUsername());

        User user3false = null;

        //Try to add a player to a game that does not exist
        Long falseGameId = -1L;
        HttpEntity<User> entity = new HttpEntity<User>(user2test, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/games/"  + falseGameId + "/player"),
                HttpMethod.POST, entity, String.class);
        Assert.assertEquals("404", response.getStatusCode().toString());

        //Try to add a non existing player
        HttpEntity<User> entity2 = new HttpEntity<User>(user3false, headers);
        ResponseEntity<String> response2 = restTemplate.exchange(
                createURLWithPort("/games/"  + testgameId1234321 + "/player"),
                HttpMethod.POST, entity2, String.class);
        Assert.assertEquals("404", response.getStatusCode().toString());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}