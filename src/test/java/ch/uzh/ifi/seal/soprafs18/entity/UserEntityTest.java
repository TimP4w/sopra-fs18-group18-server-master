package ch.uzh.ifi.seal.soprafs18.entity;
import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs18.repository.UserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class UserEntityTest  {

    @Autowired
    @Qualifier("UserRepository")
    private UserRepository userRepository;


    @Before
    public void beforeSetup() {
        System.out.println("SETTING UP USER");
        User newUser = new User();
        newUser.setName("TestName");
        newUser.setUsername("TestUsername");
        newUser.setToken("t123-test");
        newUser.setStatus(UserStatus.ONLINE);
        userRepository.save(newUser);
    }

    @After
    public void removeUser() {
        System.out.println("REMOVING USER");
        User user = userRepository.findByToken("t123-test");
        userRepository.delete(user);
    }


    @Test
    public void getId (){
        User user = userRepository.findByToken("t123-test");
        Assert.assertNotNull(user.getId());
    }

    @Test
    public void setId(){
        User user = userRepository.findByToken("t123-test");
        user.setId(-2L);
        Assert.assertEquals(-2L, user.getId().longValue());

    }

    @Test
    public void getName (){
        User user = userRepository.findByToken("t123-test");
        Assert.assertEquals("TestName", user.getName());
    }

    @Test
    public void setName(){
        User user = userRepository.findByToken("t123-test");
        user.setName("NewTestName");
        Assert.assertEquals("NewTestName", user.getName());
    }

    @Test
    public void getUsername(){
        User user = userRepository.findByToken("t123-test");
        Assert.assertEquals("TestUsername", user.getUsername());
    }

    @Test
    public void setUsername(){
        User user = userRepository.findByToken("t123-test");
        user.setUsername("NewTestUsername");
        Assert.assertEquals("NewTestUsername", user.getUsername());
    }

    @Test
    public void getToken() {
        User user = userRepository.findByToken("t123-test");
        Assert.assertEquals("t123-test", user.getToken());
    }

    @Test
    public void setToken() {
        User user = userRepository.findByToken("t123-test");
        user.setToken("t123-new-token");
        Assert.assertEquals("t123-new-token", user.getToken());
    }

    @Test
    public void getStatus(){
        User user = userRepository.findByToken("t123-test");
        Assert.assertEquals(UserStatus.ONLINE, user.getStatus());
    }

    @Test
    public void setStatus(){
        User user = userRepository.findByToken("t123-test");
        user.setStatus(UserStatus.OFFLINE);
        Assert.assertEquals(UserStatus.OFFLINE, user.getStatus());
    }
}