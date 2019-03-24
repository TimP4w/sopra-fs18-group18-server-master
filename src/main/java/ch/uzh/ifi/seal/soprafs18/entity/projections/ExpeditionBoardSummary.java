package ch.uzh.ifi.seal.soprafs18.entity.projections;

import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.Card;
import ch.uzh.ifi.seal.soprafs18.entity.User;

import java.util.List;

public interface ExpeditionBoardSummary {
    Long getId();
    Long getGameId();
    Long getOwnerId();
    List<Card> getDrawPile();
    List<Card> getHandPile();
    List<Card> getDiscardPile();
    Double getGoldBalance();
    Integer getCanRemove();
    Boolean getCanMove();
    Integer getCanBuy();
    String getColor();
    List<Card> getPlayedCards();
    List<HexSpaceSummary> getPossibleMoves();

    interface HexSpaceSummary {
        Long getId();
        PowerType getColor();
        int getPower();
        int getX();
        int getY();
        PieceSummary getOccupiedBy();
        interface PieceSummary {
            Long getId();
            String getColor();
            User getOwner();
            @FunctionalInterface
            interface UserSummary {
                Long getId();
            }
        }
    }
    List<PieceSummary> getPieces();
    interface PieceSummary {
        Long getId();
        String getColor();
    }

    List<BlockadeSummary> getBlockadesWon();
    interface BlockadeSummary {
        Long getId();
        String getColor();
        int getPower();

    }

    boolean getAlreadyBought();
}
