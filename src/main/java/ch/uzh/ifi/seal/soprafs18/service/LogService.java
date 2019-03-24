package ch.uzh.ifi.seal.soprafs18.service;

import ch.uzh.ifi.seal.soprafs18.entity.Game;
import ch.uzh.ifi.seal.soprafs18.entity.Log;
import ch.uzh.ifi.seal.soprafs18.entity.User;
import ch.uzh.ifi.seal.soprafs18.repository.LogRepository;
import ch.uzh.ifi.seal.soprafs18.web.rest.ResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;


    public Log createLog(Game game, User user, String message) {
        Log log = new Log();
        log.setGameId(game.getId());
        log.setUserId(user.getId());
        log.setMessage(message);
        log = logRepository.save(log);
        game.getLogs().add(log);
        return log;
    }


}
