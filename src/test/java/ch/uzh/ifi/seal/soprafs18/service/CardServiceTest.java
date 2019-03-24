package ch.uzh.ifi.seal.soprafs18.service;

import ch.uzh.ifi.seal.soprafs18.Application;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.Card;
import ch.uzh.ifi.seal.soprafs18.repository.CardRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest(classes= Application.class)
public class CardServiceTest {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @Test
    public void createActionCard() {
        assertNull(cardRepository.findBySlug("transmitter-test"));
        Card transmitter = cardService.createCard("Transmitter", "transmitter-test", true, 4, 0.5, PowerType.BUYANY, 1);
        assertNotNull(cardRepository.findBySlug("transmitter-test"));
    }

    @Test
    public void createExpeditionCard() {
        assertNull(cardRepository.findBySlug("explorer-test"));
        Card explorer = cardService.createCard("Explorer", "explorer-test", false, 0, 0.5, PowerType.GREEN, 1);
        assertNotNull(cardRepository.findBySlug("explorer-test"));
    }
}