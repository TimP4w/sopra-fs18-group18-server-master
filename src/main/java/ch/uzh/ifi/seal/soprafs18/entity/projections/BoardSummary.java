package ch.uzh.ifi.seal.soprafs18.entity.projections;

import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.*;
import jdk.nashorn.internal.objects.annotations.Function;

import java.util.List;

public interface BoardSummary {
    Long getId();
    String getName();
    List<HexRowSummary> getPath();
    interface HexRowSummary {
        Long getId();
        List<HexSpaceSummary> getSpaces();
        interface HexSpaceSummary {
            Long getId();
            PowerType getColor();
            int getPower();
            int getX();
            int getY();
            boolean isElDoradoSpace();
            BlockadeSummary getBlockade();
            interface BlockadeSummary {
                Long getId();
                String getRotation();
                int getPower();
                PowerType getColor();
            }
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


    }
    GameBoardSummary getGame();
    @FunctionalInterface
    interface GameBoardSummary {
        Long getId();
    }
}
