package ch.uzh.ifi.seal.soprafs18.service;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.*;
import ch.uzh.ifi.seal.soprafs18.entity.projections.BoardSummary;
import ch.uzh.ifi.seal.soprafs18.repository.*;
import ch.uzh.ifi.seal.soprafs18.web.rest.ResourceException;
import ch.uzh.ifi.seal.soprafs18.web.rest.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class BoardService {

    private final TileService tileService;
    private final HexSpaceRepository hexSpaceRepository;
    private final HexRowRepository hexRowRepository;
    private final BoardRepository boardRepository;
    private final BlockadeService blockadeService;
    private final PieceRepository pieceRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final HexSpaceService hexSpaceService;
    private final WebSocketController webSocketController;
    private final BlockadeRepository blockadeRepository;
    private final LogService logService;

    @Autowired
    public BoardService(TileService tileService,
                        HexSpaceRepository hexSpaceRepository,
                        HexRowRepository hexRowRepository,
                        BoardRepository boardRepository,
                        BlockadeService blockadeService,
                        PieceRepository pieceRepository,
                        UserRepository userRepository,
                        GameRepository gameRepository,
                        HexSpaceService hexSpaceService,
                        WebSocketController webSocketController,
                        BlockadeRepository blockadeRepository,
                        LogService logService) {
        this.tileService = tileService;
        this.hexSpaceRepository = hexSpaceRepository;
        this.hexRowRepository = hexRowRepository;
        this.boardRepository = boardRepository;
        this.blockadeService = blockadeService;
        this.pieceRepository = pieceRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.hexSpaceService = hexSpaceService;
        this.webSocketController = webSocketController;
        this.blockadeRepository = blockadeRepository;
        this.logService = logService;
    }

    private ArrayList<HexRow> insertTileIntoMap(ArrayList<HexRow> map, Tile tile, int XOffset, int YOffset) {
        boolean unevenXOffset = XOffset % 2 == 1;

        for(int a=0; a < 7; a++) {
            for (int b = 0; b < 7; b++) {
                HexSpace hex = tile.getRows().get(a).getSpaces().get(b);
                int Xval = XOffset+a;
                int Yval = YOffset+b;
                if(unevenXOffset && a%2 == 1) {
                    Yval--;
                }
                if (hex != null) {
                    hex.setX(Xval);
                    hex.setY(Yval);
                    map.get(Xval).getSpaces().set(Yval, hex);
                }
            }
        }
        return map;
    }

    private Tile insertBlockadeIntoTile(Tile tile, Blockade blockade, int position) {
        int[][][] coordinateLists = {
            {{0,1},{0,2},{0,3},{0,4}},             // 0: top
            {{0,4},{1,5},{2,5},{3,6}},             // 1: top right
            {{3,6},{4,5},{5,5},{6,4}},             // 2: bottom right
            {{6,4},{6,3},{6,2},{6,1}},             // 3: bottom
            {{6,1},{5,1},{4,0},{3,0}},             // 4: bottom left
            {{3,0},{2,0},{1,1},{0,1}},             // 5: top left
            {{2,0},{2,1},{2,2},{2,3},{2,4},{2,5}}, // 6: rectangular (-) top
            {{0,2},{1,3},{2,3},{3,4},{4,4},{5,5}}, // 7: rectangular (\) top-right
            {{1,5},{2,4},{3,4},{4,3},{5,3},{6,2}}, // 8: rectangular (/) bottom-right
            {{4,0},{4,1},{4,2},{4,3},{4,4},{4,5}}, // 9: rectangular (-) bottom
            {{6,3},{5,3},{4,2},{3,2},{2,1},{1,1}}, //10: rectangular (\) bottom-left
            {{5,1},{4,1},{3,2},{2,2},{1,3},{0,3}}  //11: rectangular (/) top-left
        };

        int limit;
        if (position < 6) { // Set loop limit to 4 for hexagonal tiles and to 5 for rectangular ones
            limit = 4;
        }
        else {
            limit = 6;
        }

        String rotation;

        switch(position % 6) {
            case 0:
                rotation = "top";
                break;
            case 1:
                rotation = "top-right";
                break;
            case 2:
                rotation = "bottom-right";
                break;
            case 3:
                rotation = "bottom";
                break;
            case 4:
                rotation = "bottom-left";
                break;
            case 5:
                rotation = "top-left";
                break;
            default:
                rotation = "top";
        }

        List<HexSpace> blockadeSpaces = new ArrayList<>();
        blockade.setRotation(rotation);
        for (int i=0; i < limit; i++) {
            int x = coordinateLists[position][i][0];
            int y = coordinateLists[position][i][1];
            HexSpace currentSpace = tile.getSpace(x,y);
            if(currentSpace != null) {
                currentSpace.setBlockade(blockade);
                blockadeSpaces.add(currentSpace);
                tile.setSpace(x, y, currentSpace);
            }
        }
        blockade.setBlockedSpaces(blockadeSpaces);
        return tile;
    }

    private Tile setStartPositions(Tile tile, Game game, int[][] coordinates) {
        // get hex spaces at specified positions
        HexSpace startOne = tile.getSpace(coordinates[0][0], coordinates[0][1]);
        HexSpace startTwo = tile.getSpace(coordinates[1][0], coordinates[1][1]);
        HexSpace startThree = tile.getSpace(coordinates[2][0], coordinates[2][1]);
        HexSpace startFour = tile.getSpace(coordinates[3][0], coordinates[3][1]);

        Piece pieceOne = null;
        Piece pieceTwo = null;
        Piece pieceThree = null;
        Piece pieceFour = null;

        if(game.getNumberPlayers() == 2 ) {
            pieceOne = game.getExpeditionBoards().get(0).getPieces().get(0);
            pieceTwo = game.getExpeditionBoards().get(1).getPieces().get(0);
            pieceThree = game.getExpeditionBoards().get(0).getPieces().get(1);
            pieceFour = game.getExpeditionBoards().get(1).getPieces().get(1);
        } else if(game.getNumberPlayers() == 3) {
            pieceOne = game.getExpeditionBoards().get(0).getPieces().get(0);
            pieceTwo = game.getExpeditionBoards().get(1).getPieces().get(0);
            pieceThree = game.getExpeditionBoards().get(2).getPieces().get(0);
        } else {
            pieceOne = game.getExpeditionBoards().get(0).getPieces().get(0);
            pieceTwo = game.getExpeditionBoards().get(1).getPieces().get(0);
            pieceThree = game.getExpeditionBoards().get(2).getPieces().get(0);
            pieceFour = game.getExpeditionBoards().get(3).getPieces().get(0);
        }

        startOne.setOccupiedBy(pieceOne);
        startTwo.setOccupiedBy(pieceTwo);
        startThree.setOccupiedBy(pieceThree);

        pieceOne.setSpace(startOne);
        pieceTwo.setSpace(startTwo);
        pieceThree.setSpace(startThree);

        if(game.getNumberPlayers() == 4 || game.getNumberPlayers() == 2) {
            startFour.setOccupiedBy(pieceFour);
            pieceFour.setSpace(startFour);
        }

        return tile;
    }

    public Board generateHillsOfGold(Game game) {
        Tile cTile = tileService.generateTile("C");
        Tile bTile = tileService.generateTile("B");
        Tile gTile = tileService.generateTile("G");
        Tile kTile = tileService.generateTile("K");
        Tile jTile = tileService.generateTile("J");
        Tile nTile = tileService.generateTile("N");
        Tile EndTile = tileService.generateTile("End1");

        // Specify start coordinates and set start positions in start tile
        int[][] coordinates = {{3,0},{4,0},{5,1},{6,1}};
        bTile = setStartPositions(bTile,game,coordinates);

        // Rotate tiles if necessary
        cTile.rotate(2);
        gTile.rotate(4);
        kTile.rotate(1);
        jTile.rotate(1);
        nTile.rotate(1);

        //Generate empty map
        ArrayList<HexRow> map = this.generateNullMap();


        // Add Blockades!
        List<Blockade> blockades = new ArrayList<>();
        for(int i=1; i<=6; i++) {
            blockades.add(blockadeService.generateBlockade(i));
        }

        insertBlockadeIntoTile(cTile, blockades.remove((int)(Math.random() * blockades.size())), 3);
        insertBlockadeIntoTile(gTile, blockades.remove((int)(Math.random() * blockades.size())), 3);
        insertBlockadeIntoTile(kTile, blockades.remove((int)(Math.random() * blockades.size())), 5);
        insertBlockadeIntoTile(jTile, blockades.remove((int)(Math.random() * blockades.size())), 5);
        insertBlockadeIntoTile(nTile, blockades.remove((int)(Math.random() * blockades.size())), 4);

        insertTileIntoMap(map, bTile, 14, 1);
        insertTileIntoMap(map, cTile, 7, 1);
        insertTileIntoMap(map, gTile, 0, 1);
        insertTileIntoMap(map, kTile, 3, 7);
        insertTileIntoMap(map, jTile, 7, 12);
        insertTileIntoMap(map, nTile, 4, 17);
        insertTileIntoMap(map, EndTile, 4, 21);

        Board board = new Board();
        board.setGame(game);
        board.setName("Hills of Gold");
        board.setPath(map);
        Board newBoard = boardRepository.save(board);

        for(HexRow row:newBoard.getPath()) {
            for(HexSpace space:row.getSpaces()) {
                if (space != null) {
                    space.setBoard(newBoard);
                }
            }
        }

        return board;

    }

    public Board generateHomeStretch(Game game) {
        Tile bTile = tileService.generateTile("B");
        Tile jTile = tileService.generateTile("J");
        Tile qTile = tileService.generateTile("Q");
        Tile kTile = tileService.generateTile("K");
        Tile mTile =  tileService.generateTile("M");
        Tile cTile = tileService.generateTile("C");
        Tile EndTile = tileService.generateTile("End1");

        // Specify start coordinates and set start positions in start tile
        int[][] coordinates = {{3,0},{4,0},{5,1},{6,1}};
        bTile = setStartPositions(bTile,game,coordinates);

        // Rotate tiles if necessary
        jTile.rotate(5);
        qTile.rotate(4);
        mTile.rotate(4);
        cTile.rotate(4);
        EndTile.rotate(5);

        //Generate empty map
        ArrayList<HexRow> map = this.generateNullMap();

        // Add Blockades!
        List<Blockade> blockades = new ArrayList<>();
        for(int i=1; i<=6; i++) {
            blockades.add(blockadeService.generateBlockade(i));
        }

        insertBlockadeIntoTile(jTile, blockades.remove((int)(Math.random() * blockades.size())), 3);
        insertBlockadeIntoTile(qTile, blockades.remove((int)(Math.random() * blockades.size())), 10);
        insertBlockadeIntoTile(mTile, blockades.remove((int)(Math.random() * blockades.size())), 4);
        insertBlockadeIntoTile(cTile, blockades.remove((int)(Math.random() * blockades.size())), 4);

        insertTileIntoMap(map, bTile, 42, 0);
        insertTileIntoMap(map, jTile, 35, 1);
        insertTileIntoMap(map, qTile, 32, 4);
        insertTileIntoMap(map, kTile, 30, 8);
        insertTileIntoMap(map, mTile, 26, 13);
        insertTileIntoMap(map, cTile, 23, 19);
        insertTileIntoMap(map, EndTile, 19, 21);

        Board board = new Board();
        board.setGame(game);
        board.setName("Home Stretch");
        board.setPath(map);
        Board newBoard = boardRepository.save(board);

        for(HexRow row:newBoard.getPath()) {
            for(HexSpace space:row.getSpaces()) {
                if (space != null) {
                    space.setBoard(newBoard);
                }
            }
        }

        return board;

    }

    public Board generateWindingPaths(Game game) {
        Tile bTile = tileService.generateTile("B");
        Tile iTile = tileService.generateTile("I");
        Tile fTile = tileService.generateTile("F");
        Tile gTile = tileService.generateTile("G");
        Tile cTile = tileService.generateTile("C");
        Tile nTile = tileService.generateTile("N");
        Tile EndTile = tileService.generateTile("End2");

        iTile.rotate(3);
        fTile.rotate(3);
        gTile.rotate(1);
        cTile.rotate(2);
        nTile.rotate(4);
        EndTile.rotate(5);

        // Specify start coordinates and set start positions in start tile
        int[][] coordinates = {{3,0},{4,0},{5,1},{6,1}};
        bTile = setStartPositions(bTile,game,coordinates);

        //Generate empty map
        ArrayList<HexRow> map = this.generateNullMap();

        // Add Blockades!
        List<Blockade> blockades = new ArrayList<>();
        for(int i=1; i<=6; i++) {
            blockades.add(blockadeService.generateBlockade(i));
        }

        insertBlockadeIntoTile(iTile, blockades.remove((int)(Math.random() * blockades.size())), 3);
        insertBlockadeIntoTile(fTile, blockades.remove((int)(Math.random() * blockades.size())), 4);
        insertBlockadeIntoTile(gTile, blockades.remove((int)(Math.random() * blockades.size())), 5);
        insertBlockadeIntoTile(cTile, blockades.remove((int)(Math.random() * blockades.size())), 3);
        insertBlockadeIntoTile(nTile, blockades.remove((int)(Math.random() * blockades.size())), 4);

        insertTileIntoMap(map, bTile, 42, 0);
        insertTileIntoMap(map, iTile, 35, 1);
        insertTileIntoMap(map, fTile, 31, 6);
        insertTileIntoMap(map, gTile, 34, 11);
        insertTileIntoMap(map, cTile, 27, 12);
        insertTileIntoMap(map, nTile, 23, 17);
        insertTileIntoMap(map, EndTile, 19, 19);


        Board board = new Board();
        board.setGame(game);
        board.setName("Winding Paths");
        board.setPath(map);
        Board newBoard = boardRepository.save(board);

        for(HexRow row:newBoard.getPath()) {
            for(HexSpace space:row.getSpaces()) {
                if (space != null) {
                    space.setBoard(newBoard);
                }
            }
        }

        return board;
    }

    public Board generateSerpentine(Game game) {
        Tile aTile = tileService.generateTile("A");
        Tile cTile = tileService.generateTile("C");
        Tile eTile = tileService.generateTile("E");
        Tile gTile = tileService.generateTile("G");
        Tile jTile = tileService.generateTile("J");
        Tile mTile = tileService.generateTile("M");
        Tile EndTile = tileService.generateTile("End2");

        aTile.rotate(1);
        eTile.rotate(1);
        gTile.rotate(3);
        jTile.rotate(3);
        mTile.rotate(5);
        EndTile.rotate(1);

        // Specify start coordinates and set start positions in start tile
        int[][] coordinates = {{3,0},{4,0},{5,1},{6,1}};
        aTile = setStartPositions(aTile,game,coordinates);

        //Generate empty map
        ArrayList<HexRow> map = this.generateNullMap();

        // Add Blockades!
        List<Blockade> blockades = new ArrayList<>();
        for(int i=1; i<=6; i++) {
            blockades.add(blockadeService.generateBlockade(i));
        }

        insertBlockadeIntoTile(cTile, blockades.remove((int)(Math.random() * blockades.size())), 5);
        insertBlockadeIntoTile(eTile, blockades.remove((int)(Math.random() * blockades.size())), 4);
        insertBlockadeIntoTile(gTile, blockades.remove((int)(Math.random() * blockades.size())), 2);
        insertBlockadeIntoTile(jTile, blockades.remove((int)(Math.random() * blockades.size())), 4);
        insertBlockadeIntoTile(mTile, blockades.remove((int)(Math.random() * blockades.size())), 5);

        insertTileIntoMap(map, aTile, 10, 0);
        insertTileIntoMap(map, cTile, 13, 6);
        insertTileIntoMap(map, eTile, 9, 11);
        insertTileIntoMap(map, gTile, 5, 6);
        insertTileIntoMap(map, jTile, 1, 11);
        insertTileIntoMap(map, mTile, 4, 16);
        insertTileIntoMap(map, EndTile, 8, 18);

        Board board = new Board();
        board.setGame(game);
        board.setName("Serpentine");
        board.setPath(map);
        Board newBoard = boardRepository.save(board);

        for(HexRow row:newBoard.getPath()) {
            for(HexSpace space:row.getSpaces()) {
                if (space != null) {
                    space.setBoard(newBoard);
                }
            }
        }

        return board;
    }

    public Board generateSwamplands(Game game) {
        Tile aTile = tileService.generateTile("A");
        Tile rTile = tileService.generateTile("R");
        Tile dTile = tileService.generateTile("D");
        Tile hTile = tileService.generateTile("H");
        Tile eTile = tileService.generateTile("E");
        Tile oTile = tileService.generateTile("O");
        Tile kTile = tileService.generateTile("K");
        Tile EndTile = tileService.generateTile("End2");

        rTile.rotate(1);
        dTile.rotate(2);
        hTile.rotate(2);
        eTile.rotate(1);
        oTile.rotate(4);
        EndTile.rotate(2);

        // Specify start coordinates and set start positions in start tile
        int[][] coordinates = {{6,1},{6,2},{6,3},{6,4}};
        aTile = setStartPositions(aTile,game,coordinates);

        //Generate empty map
        ArrayList<HexRow> map = this.generateNullMap();

        // Add Blockades!
        List<Blockade> blockades = new ArrayList<>();
        for(int i=1; i<=6; i++) {
            blockades.add(blockadeService.generateBlockade(i));
        }

        insertBlockadeIntoTile(rTile, blockades.remove((int)(Math.random() * blockades.size())),10);
        insertBlockadeIntoTile(hTile, blockades.remove((int)(Math.random() * blockades.size())), 5);
        insertBlockadeIntoTile(eTile, blockades.remove((int)(Math.random() * blockades.size())), 5);
        insertBlockadeIntoTile(oTile, blockades.remove((int)(Math.random() * blockades.size())), 7);

        insertTileIntoMap(map, aTile, 5, 1);
        insertTileIntoMap(map, rTile, 3, 5);
        insertTileIntoMap(map, dTile, 0, 8);
        insertTileIntoMap(map, hTile, 4, 13);
        insertTileIntoMap(map, eTile, 8,18);
        insertTileIntoMap(map, oTile, 11, 15);
        insertTileIntoMap(map, kTile, 14, 11);
        insertTileIntoMap(map, EndTile, 18, 9);

        Board board = new Board();
        board.setGame(game);
        board.setName("Swamplands");
        board.setPath(map);
        Board newBoard = boardRepository.save(board);

        for(HexRow row:newBoard.getPath()) {
            for(HexSpace space:row.getSpaces()) {
                if (space != null) {
                    space.setBoard(newBoard);
                }
            }
        }

        return board;

    }

    public Board generateWitchsCauldron(Game game){
        Tile aTile = tileService.generateTile("A");
        Tile lTile = tileService.generateTile("L");
        Tile gTile = tileService.generateTile("G");
        Tile dTile = tileService.generateTile("D");
        Tile mTile = tileService.generateTile("M");
        Tile iTile = tileService.generateTile("I");
        Tile EndTile = tileService.generateTile("End2");

        lTile.rotate(5);
        gTile.rotate(4);
        dTile.rotate(4);
        iTile.rotate(5);
        EndTile.rotate(3);

        // Specify start coordinates and set start positions in start tile
        int[][] coordinates = {{6,1},{6,2},{6,3},{6,4}};
        aTile = setStartPositions(aTile,game,coordinates);

        //Generate empty map
        ArrayList<HexRow> map = this.generateNullMap();

        // Add Blockades!
        List<Blockade> blockades = new ArrayList<>();
        for(int i=1; i<=6; i++) {
            blockades.add(blockadeService.generateBlockade(i));
        }

        insertBlockadeIntoTile(lTile, blockades.remove((int)(Math.random() * blockades.size())),4);
        insertBlockadeIntoTile(gTile, blockades.remove((int)(Math.random() * blockades.size())),4);
        insertBlockadeIntoTile(dTile, blockades.remove((int)(Math.random() * blockades.size())),5);
        insertBlockadeIntoTile(mTile, blockades.remove((int)(Math.random() * blockades.size())),0);
        insertBlockadeIntoTile(iTile, blockades.remove((int)(Math.random() * blockades.size())),1);

        insertTileIntoMap(map, aTile, 7, 1);
        insertTileIntoMap(map, lTile, 3, 6);
        insertTileIntoMap(map, gTile, 0, 11);
        insertTileIntoMap(map, dTile, 3,16);
        insertTileIntoMap(map, mTile, 10, 15);
        insertTileIntoMap(map, iTile, 14, 10);
        insertTileIntoMap(map, EndTile, 14, 6);

        Board board = new Board();
        board.setGame(game);
        board.setName("Witch's Cauldron");
        board.setPath(map);
        Board newBoard = boardRepository.save(board);

        for(HexRow row:newBoard.getPath()) {
            for(HexSpace space:row.getSpaces()) {
                if (space != null) {
                    space.setBoard(newBoard);
                }
            }
        }

        return board;
    }

    public Board generateMiniMap(Game game) {
        Tile miniA = tileService.generateTile("MiniA");
        Tile miniB = tileService.generateTile("MiniB");
        Tile endTile = tileService.generateTile("End1");

        int[][] coordinates = {{4,1},{5,2},{5,3},{5,4}};
        miniA = setStartPositions(miniA,game,coordinates);

        ArrayList<HexRow> map = this.generateNullMap();

        List<Blockade> blockades = new ArrayList<>();
        for(int i=1; i<=6; i++) {
            blockades.add(blockadeService.generateBlockade(i));
        }

        Blockade blockade = blockades.remove((int)(Math.random() * blockades.size()));

        // inserting blockade without the appropriate method, due to special case of mini tile
        List<HexSpace> blockadeSpaces = new ArrayList<>();
        blockade.setRotation("bottom-left");
        int[][] blockadeCoordinates = {{3,1},{4,1},{5,2}};
        for(int[] i: blockadeCoordinates) {
            int x = i[0];
            int y = i[1];
            HexSpace currentSpace = miniB.getSpace(x,y);
            currentSpace.setBlockade(blockade);
            blockadeSpaces.add(currentSpace);
            miniB.setSpace(x,y,currentSpace);
        }
        blockade.setBlockedSpaces(blockadeSpaces);


        insertTileIntoMap(map, miniA, 2, 0);
        insertTileIntoMap(map, miniB, 0, 4);
        insertTileIntoMap(map, endTile, 0, 7);

        Board board = new Board();
        board.setGame(game);
        board.setName("Mini");
        board.setPath(map);
        Board newBoard = boardRepository.save(board);

        for(HexRow row:newBoard.getPath()) {
            for(HexSpace space:row.getSpaces()) {
                if (space != null) {
                    space.setBoard(newBoard);
                }
            }
        }

        return board;
    }

    private ArrayList<HexRow> generateNullMap() {
        // Generate map of nulls
        ArrayList<HexRow> map = new ArrayList<>();
        for (int i = 0; i < 49; i++) {
            HexRow hexRow = new HexRow();
            List<HexSpace> spaces = new ArrayList<>();
            for (int j = 0; j < 49; j++) {
                spaces.add(null);
            }
            hexRow.setSpaces(spaces);
            hexRowRepository.save(hexRow);
            map.add(hexRow);
        }
        return map;
    }

    public BoardSummary getBoard(Long id) {
        Optional<BoardSummary> board = boardRepository.findProjectedById(id);
        if (!board.isPresent()) {
            return null;
        }
        return board.get();
    }

    public BoardSummary move(Long boardId, Long pieceId, Long hexSpaceId, String userToken) {
        Optional<Piece> piece = pieceRepository.findById(pieceId);
        Optional<HexSpace> space = hexSpaceRepository.findById(hexSpaceId);
        Optional<Board> board = boardRepository.findById(boardId);
        if(!piece.isPresent() || !space.isPresent() || !board.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Some resources couldn't be found on the server.");
        }

        ExpeditionBoard expBoard = piece.get().getExpBoard();
        User requester = userRepository.findByToken(userToken);
        Optional<User> owner = userRepository.findById(expBoard.getOwnerId());
        Optional<Game> game = gameRepository.findById(expBoard.getGameId());
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found!");
        }
        if(!owner.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "User not found!");
        }
        int currentTurn = game.get().getCurrentPlayer();
        //Check if requester is owner
        if(!requester.getId().equals(owner.get().getId())) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You can only move your own pieces!");
        }
        //Check if turn is right
        if(!game.get().getExpeditionBoards().get(currentTurn).equals(expBoard)) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "It's not your turn to move.");
        }

        //If number players are 2, then check if the movement is possible for the chosen piece
        if(game.get().getNumberPlayers() == 2) {
            expBoard.setPossibleMoves(new ArrayList<HexSpace>()); // reset possible moves
            //Check if only 1 card played
            if (expBoard.getPlayedCards().size() == 1) {
                //If card is not joker, check only with one power
                if (!expBoard.getPlayedCards().get(0).getPower().equals(PowerType.JOKER)) {
                    hexSpaceService.findPaths(piece.get().getSpace(), expBoard, expBoard.getPlayedCards().get(0).getPower(), expBoard.getPlayedCards().get(0).getPowerValue(), 0);
                } else { // For jokers check all color possibilities
                    PowerType[] powers = {PowerType.GREEN, PowerType.BLUE, PowerType.YELLOW};
                    for (PowerType power : powers) {
                        hexSpaceService.findPaths(piece.get().getSpace(), expBoard, power, expBoard.getPlayedCards().get(0).getPowerValue(), 0);
                    }
                }
                hexSpaceService.findAdjacentRemoveAndDiscard(piece.get().getSpace(), expBoard);
            } else if (expBoard.getPlayedCards().size() > 1) { // If more than 1 card, check only for remove and discard spaces
                expBoard.setPossibleMoves(hexSpaceService.findAdjacentRemoveAndDiscard(piece.get().getSpace(), expBoard));
            } else if (expBoard.getCanMove()) { // Check if player can move for free (action card played)
                expBoard.setPossibleMoves(hexSpaceService.findValidNeighbours(piece.get().getSpace(), expBoard));
            } else {
                throw new ResourceException(HttpStatus.BAD_REQUEST, "You cannot make any moves.");
            }
        }
        //check if space is within possible moves (if players > 2 do directly that)
        if(!expBoard.getPossibleMoves().contains(space.get())) {
            throw new ResourceException(HttpStatus.BAD_REQUEST, "The space you're trying to move to it's not in your range.");
        }
        piece.get().getSpace().setOccupiedBy(null);
        space.get().setOccupiedBy(piece.get());
        piece.get().setSpace(space.get());
        expBoard.setPossibleMoves(new ArrayList<HexSpace>());

        //Discard Played Cards if space is not of type REMOVE.
        if(space.get().getColor().equals(PowerType.REMOVE)) {
            expBoard.setPlayedCards(new ArrayList<Card>());
        }

        expBoard.discardPlayedCards();

        //Check if eldorado Reached
        if(space.get().isElDoradoSpace()) {

            int x = space.get().getX();
            int y = board.get().getPath().get(x).getSpaces().indexOf(space.get());

            Optional<HexSpace> elDorado = hexSpaceService.getNeighbours(space.get()).stream().filter(e -> e.getColor().equals(PowerType.ELDORADO)).findFirst();

            HexSpace finalSpace = elDorado.get();
            
            piece.get().getSpace().setOccupiedBy(null);
            finalSpace.setOccupiedBy(piece.get());
            piece.get().setSpace(finalSpace);

            if(game.get().getNumberPlayers() == 2) {
                if (expBoard.getPieces().get(0).getSpace().isElDoradoSpace() && expBoard.getPieces().get(1).getSpace().isElDoradoSpace()) {
                    game.get().getHaveReachedElDorado().add(expBoard);
                }
            } else {
                game.get().getHaveReachedElDorado().add(expBoard);
            }
        }

        if (expBoard.getCanMove()) {
            expBoard.setCanMove(false);
        }

            Optional<BoardSummary> boardSummary = boardRepository.findProjectedById(space.get().getBoard().getId());
        if(!boardSummary.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Board not found!");
        }


        logService.createLog(game.get(), owner.get(), "Player " + owner.get().getName() + " moved his piece.");

        webSocketController.onGameMessage(expBoard.getGameId().toString(),"{\"action\": \"PIECE_MOVED\", \"uid\": \"" + requester.getId() + "\" }");
        return boardSummary.get();
    }

    public BoardSummary winBlockade(Long boardId, Long pieceId, Long blockadeId, String userToken) {
        Optional<Piece> piece = pieceRepository.findById(pieceId);
        Optional<Blockade> blockade = blockadeRepository.findById(blockadeId);
        Optional<Board> board = boardRepository.findById(boardId);
        if(!piece.isPresent() || !blockade.isPresent() || !board.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Some resources couldn't be found on the server.");
        }

        ExpeditionBoard expBoard = piece.get().getExpBoard();
        User requester = userRepository.findByToken(userToken);
        Optional<User> owner = userRepository.findById(expBoard.getOwnerId());
        Optional<Game> game = gameRepository.findById(expBoard.getGameId());
        if(!game.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Game not found!");
        }
        if(!owner.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "User not found!");
        }
        int currentTurn = game.get().getCurrentPlayer();
        //Check if requester is owner
        if(!requester.getId().equals(owner.get().getId())) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "You can only move your own pieces!");
        }
        //Check if turn is right
        if(!game.get().getExpeditionBoards().get(currentTurn).equals(expBoard)) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "It's not your turn to move.");
        }

        List<HexSpace> spaces = hexSpaceService.getNeighbours(piece.get().getSpace());
        HexSpace possibleBlockedNeighbour = null;
        for(HexSpace space:blockade.get().getBlockedSpaces()) {
            if(spaces.contains(space)) {
                possibleBlockedNeighbour = space;
            }
        }

        if(possibleBlockedNeighbour == null) {
            throw new ResourceException(HttpStatus.FORBIDDEN, "The blockade is unreachable from your position.");
        }


        if(blockade.get().getColor().equals(PowerType.DISCARD)) {
            if(expBoard.getPlayedCards().size() < blockade.get().getPower()) {
                throw new ResourceException(HttpStatus.FORBIDDEN, "You don't have enough cards to overcome this blockade.");
            }
        } else {
            if(expBoard.getPlayedCards().size() > 1 || expBoard.getPlayedCards().isEmpty()) {
                throw new ResourceException(HttpStatus.FORBIDDEN, "You can't overcome this blockade with your cards");
            }
            if(!expBoard.getPlayedCards().get(0).getPower().equals(blockade.get().getColor()) && !expBoard.getPlayedCards().get(0).getPower().equals(PowerType.JOKER)) {
                throw new ResourceException(HttpStatus.FORBIDDEN, "The color of your card doesn't match the color of the blockade.");
            }
            if(expBoard.getPlayedCards().get(0).getPowerValue() < blockade.get().getPower()) {
                throw new ResourceException(HttpStatus.FORBIDDEN, "The power of your card doesn't match the power of the blockade.");
            }

        }

        for(HexSpace space:blockade.get().getBlockedSpaces()) {
            space.setBlockade(null);
        }
        List<Blockade> blockades = expBoard.getBlockadesWon();
        blockades.add(blockade.get());
        expBoard.setPossibleMoves(new ArrayList<HexSpace>());
        expBoard.setBlockadesWon(blockades);
        //Discard Played Cards
        expBoard.discardPlayedCards();

        Optional<BoardSummary> boardSummary = boardRepository.findProjectedById(boardId);
        if(!boardSummary.isPresent()) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Board not found!");
        }

        logService.createLog(game.get(), owner.get(), "Player " + owner.get().getName() + " won a blockade.");


        webSocketController.onGameMessage(expBoard.getGameId().toString(),"{\"action\": \"BLOCKADE_WON\", \"uid\": \"" + requester.getId() + "\" }");
        return boardSummary.get();

    }

    public Board generateMap(Game game) {

         /*
            {id:'1', name:'Hils of Gold', difficulty:'easy'},
            {id:'2', name:'Home Stretch', difficulty:'easy'},
            {id:'3', name:'Winding Paths', difficulty:'moderate'},
            {id:'4', name:'Serpentine', difficulty:'moderate'},
            {id:'5', name:'Swamplands', difficulty:'difficult'},
            {id:'6', name:"Witch's Cauldron", difficulty:'difficult'}
            {id:'7', name:"Mini", difficulty:'extreme'}
        */

        switch(game.getMapId()) {
            case 1:
                return this.generateHillsOfGold(game);
            case 2:
                return this.generateHomeStretch(game);
            case 3:
                return this.generateWindingPaths(game);
            case 4:
                return this.generateSerpentine(game);
            case 5:
                return this.generateSwamplands(game);
            case 6:
                return this.generateWitchsCauldron(game);
            case 7:
                return this.generateMiniMap(game);
            default:
                return this.generateMiniMap(game);
        }
    }
}