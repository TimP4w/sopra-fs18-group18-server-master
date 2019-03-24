package ch.uzh.ifi.seal.soprafs18.service;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs18.entity.User;
import ch.uzh.ifi.seal.soprafs18.repository.UserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;


@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserServiceTest {

    @Autowired
    @Qualifier("UserRepository")
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void beforeSetup() {
        User user = new User();
        user.setName("TestServiceName");
        user.setUsername("TestServiceUsername");
        userService.addUser(user);
    }

    @After
    public void removeUser() {
        Optional<User> user = userRepository.findByUsername("TestServiceUsername");
        userRepository.delete(user.get());
    }

    @Test
    public void addUser() {
        Assert.assertFalse(userRepository.findByUsername("TestUsernameAdd").isPresent());
        User user = new User();
        user.setName("TestNameAdd");
        user.setUsername("TestUsernameAdd");

        User createdUser = userService.addUser(user);
        Assert.assertEquals("TestUsernameAdd", createdUser.getUsername());
        Assert.assertEquals("TestNameAdd", createdUser.getName());

        Optional<User> retrievedUser = userRepository.findByUsername("TestUsernameAdd");
        Assert.assertTrue(retrievedUser.isPresent());

        userRepository.delete(createdUser);

    }

    @Test
    public void login(){
        Optional<User> user = userRepository.findByUsername("TestServiceUsername");
        Assert.assertTrue(user.isPresent());
        Assert.assertNotEquals(user.get().getStatus(), UserStatus.ONLINE);
        userService.login(user.get().getUsername());
        user = userRepository.findByUsername("TestServiceUsername");
        Assert.assertEquals(UserStatus.ONLINE, user.get().getStatus());

    }

    @Test
    public void logout(){
        Optional<User> user = userRepository.findByUsername("TestServiceUsername");
        Assert.assertTrue(user.isPresent());
        userService.login("TestServiceUsername");
        user = userRepository.findByUsername("TestServiceUsername");
        Assert.assertEquals(UserStatus.ONLINE, user.get().getStatus());
        userService.logout(user.get().getId(), user.get().getToken());
        user = userRepository.findByUsername("TestServiceUsername");
        Assert.assertNotEquals(UserStatus.ONLINE, user.get().getStatus());

    }



}
