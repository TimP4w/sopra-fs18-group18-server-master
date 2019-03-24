package ch.uzh.ifi.seal.soprafs18.web.rest;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.entity.User;
import ch.uzh.ifi.seal.soprafs18.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    UserService userService;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void addUser() {

        User user = new User();
        user.setName("TestAddUser");
        user.setUsername("TestAddUsername");

        HttpEntity<User> entity = new HttpEntity<User>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("201", response.getStatusCode().toString());
    }

    @Test
    public void login() {

        User user = new User();
        user.setName("TestLoginName");
        user.setUsername("TestLoginUsername");

        HttpEntity<User> entity = new HttpEntity<User>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/users/login"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("404", response.getStatusCode().toString());

        userService.addUser(user);

        response = restTemplate.exchange(
                createURLWithPort("/users/login"),
                HttpMethod.POST, entity, String.class);

        Assert.assertEquals("200", response.getStatusCode().toString());

    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}