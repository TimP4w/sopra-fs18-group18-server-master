package ch.uzh.ifi.seal.soprafs18.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final SimpMessagingTemplate template;

    @Autowired
    WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/send/lobby/{gameId}/message")
    public void onLobbyMessage(@DestinationVariable String gameId, String message) {
        this.template.convertAndSend("/lobby/" + gameId, message);
    }

    @MessageMapping("/send/games/message")
    public void onGameListMessage(String message) {
        this.template.convertAndSend("/games", message);
    }

    @MessageMapping("/send/game/{gameId}/message")
    public void onGameMessage(@DestinationVariable String gameId, String message) {
        this.template.convertAndSend("/game/" + gameId, message);
    }
}