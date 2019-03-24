package ch.uzh.ifi.seal.soprafs18.web.rest;
import ch.uzh.ifi.seal.soprafs18.entity.projections.BoardSummary;
import ch.uzh.ifi.seal.soprafs18.service.BoardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardResource extends GenericResource {
    private static final String CONTEXT = "/boards";
    Logger logger = LoggerFactory.getLogger(BoardResource.class);

    @Autowired
    private BoardService boardService;


    /*
     * Context: /boards/{board-id}
     */
    @RequestMapping(value = CONTEXT + "/{boardId}")
    @ResponseStatus(HttpStatus.OK)
    public BoardSummary getBoard(@PathVariable Long boardId) {
        logger.debug("getBoard: " + boardId);
        return this.boardService.getBoard(boardId);
    }

    /*
     * Context: /boards/{boardId}/move
     */
    @RequestMapping(value = CONTEXT + "/{boardId}/move", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public BoardSummary movePiece(@PathVariable Long boardId, @RequestParam("token") String userToken, @RequestParam("pieceId") Long pieceId, @RequestParam("spaceId") Long spaceId) {
        logger.debug("Move Piece: " + userToken);
        return this.boardService.move(boardId, pieceId, spaceId, userToken);
    }

    /*
     * Context: /boards/{boardId}/move
     */
    @RequestMapping(value = CONTEXT + "/{boardId}/winblockade", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public BoardSummary winBlockade(@PathVariable Long boardId, @RequestParam("token") String userToken, @RequestParam("pieceId") Long pieceId, @RequestParam("blockadeId") Long blockadeId) {
        logger.debug("Earn blockade: " + userToken);
        return this.boardService.winBlockade(boardId, pieceId, blockadeId, userToken);
    }

}