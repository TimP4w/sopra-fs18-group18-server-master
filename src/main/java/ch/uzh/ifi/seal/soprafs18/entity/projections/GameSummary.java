package ch.uzh.ifi.seal.soprafs18.entity.projections;

import ch.uzh.ifi.seal.soprafs18.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs18.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs18.entity.Card;
import ch.uzh.ifi.seal.soprafs18.entity.Log;

import java.util.List;

public interface GameSummary {
    Long getId();
    String getName();
    Long getOwner();
    Integer getNumberPlayers();
    GameStatus getStatus();
    Integer getCurrentPlayer();
    List<ExpeditionBoardSummary> getExpeditionBoards();
    int getMapId();

    interface ExpeditionBoardSummary {
        Long getId();
        Long getOwnerId();
        String getColor();
        List<HexSpaceSummary> getPossibleMoves();
        interface HexSpaceSummary {
            Long getId();
            int getX();
            int getY();
        }
    }
    List<UserSummary> getPlayers();
    interface UserSummary {
        Long getId();
        String getName();
        UserStatus getStatus();
    }

    BoardSummary getBoard();

    MarketplaceSummary getMarketPlace();
    interface MarketplaceSummary {
        Long getId();
        Long getGameId();
        List<MarketPileSummary> getMarketplaceBackRow();
        List<MarketPileSummary> getMarketplaceMiddleRow();
        List<MarketPileSummary> getMarketplaceFrontRow();
        interface MarketPileSummary {
            Long getId();
            List<Card> getMarketPlacePile();
            String getCardSlug();
        }
    }

    List<ExpeditionBoardSummary> getWinners();
    List<ExpeditionBoardSummary> getHaveReachedElDorado();

    int getTurnNumber();

    List<Log> getLogs();
}