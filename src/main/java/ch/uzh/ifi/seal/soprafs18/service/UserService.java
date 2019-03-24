package ch.uzh.ifi.seal.soprafs18.service;
import ch.uzh.ifi.seal.soprafs18.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs18.entity.User;
import ch.uzh.ifi.seal.soprafs18.repository.GameRepository;
import ch.uzh.ifi.seal.soprafs18.repository.UserRepository;
import ch.uzh.ifi.seal.soprafs18.web.rest.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.UUID;

/**
 * /**
 * Created by LucasPelloni on 26.01.18.
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);


    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private boolean isValidUser(User user) {
        if (user == null) {
            return false;
        }
        else if (user.getName() == null || user.getUsername() == null) {
            return false;
        }
        else {
            return true;
        }
    }

    public User addUser(User user) {
        log.debug("Creating new User: {}", user);
        if(isValidUser(user)) {
            Optional<User> userOptional = userRepository.findByUsername(user.getUsername());
            if (!userOptional.isPresent()) {
                user.setStatus(UserStatus.OFFLINE);
                user.setToken(UUID.randomUUID().toString());
                return userRepository.save(user);
            } else {
                throw new ResourceException(HttpStatus.CONFLICT, "User already exists!"); //Error 409
            }
        } else {
            throw new ResourceException(HttpStatus.BAD_REQUEST, "User data not valid."); //Error 400
        }
    }

    public User login(String username) {
        if (username == null) {
            throw new ResourceException(HttpStatus.BAD_REQUEST, "User data not valid."); //Error 400
        }
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.ONLINE);
            log.debug("User logged in: {}", user);
            return user;
        }
        throw new ResourceException(HttpStatus.NOT_FOUND, "User not found."); //404
    }

    public void logout(Long userId, String userToken) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && userOptional.get().getToken().equals(userToken)) {
            User user = userOptional.get();
            user.setStatus(UserStatus.OFFLINE);
        }
    }

    public User getByToken(String token) {
        User user = userRepository.findByToken(token);
        if(user == null){
            throw new ResourceException(HttpStatus.NOT_FOUND, "User not found."); //404
        } else {
            return user;
        }
    }
}