package ch.uzh.ifi.seal.soprafs18.service;

import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.Board;
import ch.uzh.ifi.seal.soprafs18.entity.ExpeditionBoard;
import ch.uzh.ifi.seal.soprafs18.entity.HexRow;
import ch.uzh.ifi.seal.soprafs18.entity.HexSpace;
import ch.uzh.ifi.seal.soprafs18.repository.HexSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service("hexSpaceService")
public class HexSpaceService {


    private final HexSpaceRepository hexSpaceRepository;

    @Autowired
    public HexSpaceService(HexSpaceRepository hexSpaceRepository) {
        this.hexSpaceRepository = hexSpaceRepository;
    }

    public List<HexSpace> getNeighbours(HexSpace hex) {
        //Hardcoded neighbours by given position (odd & even have different offsets)
        int[][] oddNeighboursOffset = {{-1, -1}, {0,-1}, {1, 0}, {0, 1}, {-1, 1}, {-1, 0}};
        int[][] evenNeighboursOffset = {{0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 0}};

        //Choose offset based on space row position in board
        List<HexSpace> neighbours = new ArrayList<>();
        int[][] offsets;
        if(hex.getX() % 2 == 0) {
            offsets = evenNeighboursOffset;
        } else {
            offsets = oddNeighboursOffset;
        }

        for(int[] offset:offsets) {
            HexSpace neighbour = hexSpaceRepository.findByXAndYAndBoardId(hex.getX() + offset[1], hex.getY() + offset[0], hex.getBoard().getId());
            if (neighbour != null) {
                neighbours.add(neighbour);
            }
        }
        return neighbours;
    }

    public List<HexSpace> findPaths(HexSpace hex, ExpeditionBoard expBoard, PowerType power, int powerValue, int cost) {

        if(cost > powerValue) {
            return expBoard.getPossibleMoves();
        }
        for(HexSpace neighbour:this.getNeighbours(hex)) {
            if(neighbour != null) {
                //Check neighbour is not behind blockade
                if(neighbour.getBlockade() == null) {
                    //Check if power match the card && the cost does not exceed the powervalue && space not occupied
                    if (cost + neighbour.getPower() <= powerValue && neighbour.getColor().equals(power) && neighbour.getOccupiedBy() == null) {
                        if (!expBoard.getPossibleMoves().contains(neighbour)) {
                            expBoard.getPossibleMoves().add(neighbour);
                        }
                        findPaths(neighbour, expBoard, power, powerValue, cost + neighbour.getPower());
                    }
                }
            }
        }
        return expBoard.getPossibleMoves();
    }

    public List<HexSpace> findAdjacentRemoveAndDiscard(HexSpace hex, ExpeditionBoard expBoard) {
        for(HexSpace neighbour:this.getNeighbours(hex)) {
            if (neighbour != null) {
                //Check neighbour is not behind blockade
                if(neighbour.getBlockade() == null) {
                    //Check neighbour color id DISCARD or REMOVE
                    if(neighbour.getColor().equals(PowerType.DISCARD) || neighbour.getColor().equals(PowerType.REMOVE)) {
                        //Check played cards count match space power
                        if(expBoard.getPlayedCards().size() >= neighbour.getPower()) {
                            expBoard.getPossibleMoves().add(neighbour);
                        }
                    }
                }
            }
        }
        return expBoard.getPossibleMoves();
    }

    public List<HexSpace> findValidNeighbours(HexSpace hex, ExpeditionBoard expBoard) {
        for(HexSpace neighbour:this.getNeighbours(hex)) {
            if (neighbour != null) {
                //Check neighbour is not behind blockade
                if(neighbour.getBlockade() == null) {
                    //Check neighbour is not Black or Occupied
                    if(!(neighbour.getColor().equals(PowerType.BLACK) || neighbour.getOccupiedBy() != null)) {
                        expBoard.getPossibleMoves().add(neighbour);
                    }
                }
            }
        }
        return expBoard.getPossibleMoves();
    }


}
