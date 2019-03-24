package ch.uzh.ifi.seal.soprafs18.service;

import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import ch.uzh.ifi.seal.soprafs18.entity.HexSpace;
import ch.uzh.ifi.seal.soprafs18.entity.HexRow;
import ch.uzh.ifi.seal.soprafs18.entity.Tile;
import ch.uzh.ifi.seal.soprafs18.repository.HexRowRepository;
import ch.uzh.ifi.seal.soprafs18.repository.HexSpaceRepository;
import ch.uzh.ifi.seal.soprafs18.repository.TileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TileService {

    @Autowired
    private TileRepository TileRepository;

    @Autowired
    private HexRowRepository hexRowRepository;

    @Autowired
    private HexSpaceRepository hexSpaceRepository;

    private PowerType[] colorsNull = {
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null
    };

    private int[] powersNull = {
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0
    };

    private PowerType[] colorsMiniA = {
            null, null, null, null, null, null, null,
            null, null, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, null, null,
            null, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD, null, null,
            null, PowerType.BLUE, PowerType.BLUE, PowerType.BLACK, PowerType.GREEN, PowerType.GREEN, null,
            null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null,
            null, null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null,
            null, null, null, null, null, null, null
    };

    private int[] powersMiniA = {
            0, 0, 0, 0, 0, 0, 0,
            0, 0, 2, 2, 1, 0, 0,
            0, 1, 1, 2, 2, 0, 0,
            0, 2, 2, 1, 2, 2, 0,
            0, 1, 1, 1, 1, 0, 0,
            0, 0, 1, 1, 1, 0, 0,
            0, 0, 0, 0, 0, 0, 0
    };

    private PowerType[] colorsMiniB = {
            null, null, null, null, null, null, null,
            null, null, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, null, null,
            null, PowerType.YELLOW, PowerType.YELLOW, PowerType.REMOVE, PowerType.DISCARD, null, null,
            null, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, null,
            null, PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, null, null,
            null, null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null,
            null, null, null, null, null, null, null
    };

    private int[] powersMiniB = {
            0, 0, 0, 0, 0, 0, 0,
            0, 0, 2, 2, 1, 0, 0,
            0, 1, 1, 1, 2, 0, 0,
            0, 2, 2, 1, 2, 2, 0,
            0, 1, 1, 1, 1, 0, 0,
            0, 0, 1, 1, 1, 0, 0,
            0, 0, 0, 0, 0, 0, 0
    };

    private PowerType[] colorsA = {
        null, PowerType.GREEN, PowerType.REMOVE, PowerType.GREEN, PowerType.GREEN, null, null,
        null, PowerType.BLUE, PowerType.BLACK, PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, null,
        PowerType.GREEN, PowerType.BLACK, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        PowerType.GREEN, PowerType.YELLOW, PowerType.GREEN, PowerType.BLUE, PowerType.GREEN, PowerType.YELLOW, PowerType.GREEN,
        PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, PowerType.GREEN, PowerType.BLUE, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null
    };

    private int[] powersA = {
        0, 1, 1, 1, 1, 0, 0,
        0, 1, 1, 1, 1, 1, 0,
        1, 1, 1, 1, 1, 1, 0,
        1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 0,
        0, 1, 1, 1, 1, 1, 0,
        0, 1, 1, 1, 1, 0, 0
    };

    private PowerType[] colorsB = {
        null, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, PowerType.GREEN, PowerType.BLUE, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, PowerType.GREEN, PowerType.YELLOW, PowerType.REMOVE, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, PowerType.BLUE,
        PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null
    };

    private int[] powersB = {
        0, 1, 1, 1, 1, 0, 0,
        0, 1, 1, 1, 1, 1, 0,
        1, 1, 1, 1, 1, 1, 0,
        1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 0,
        0, 1, 1, 1, 1, 1, 0,
        0, 1, 1, 1, 1, 0, 0
    };

    private PowerType[] colorsC = {
        null, PowerType.DISCARD, PowerType.YELLOW, PowerType.BLUE, PowerType.BLUE, null, null,
        null, PowerType.BLUE, PowerType.DISCARD, PowerType.YELLOW, PowerType.YELLOW, PowerType.BLUE, null,
        PowerType.BLUE, PowerType.DISCARD, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, null,
        PowerType.DISCARD, PowerType.BLUE, PowerType.YELLOW, PowerType.BLACK, PowerType.BLUE, PowerType.DISCARD, PowerType.GREEN,
        PowerType.DISCARD, PowerType.DISCARD, PowerType.YELLOW, PowerType.DISCARD, PowerType.DISCARD, PowerType.YELLOW, null,
        null, PowerType.GREEN, PowerType.YELLOW, PowerType.BLUE, PowerType.YELLOW, PowerType.YELLOW, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.BLUE, PowerType.BLUE, null, null
    };

    private int[] powersC = {
        0, 1, 1, 1, 1, 0, 0,
        0, 1, 1, 1, 1, 1, 0,
        1, 1, 1, 1, 1, 1, 1,
        1, 1, 1, 0, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1,
        0, 1, 1, 1, 1, 1, 0,
        0, 1, 1, 1, 1, 0, 0
    };

    private PowerType[] colorsD = {
        null, PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, PowerType.BLUE, null, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, PowerType.YELLOW, PowerType.BLACK, null,
        PowerType.GREEN, PowerType.BLUE, PowerType.GREEN, PowerType.BLACK, PowerType.YELLOW, PowerType.GREEN, null,
        PowerType.GREEN, PowerType.BLUE, PowerType.BLUE, PowerType.BLACK, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN,
        PowerType.GREEN, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null
    };

    private int[] powersD = {
        0, 2, 1, 1, 3, 0, 0,
        0, 1, 1, 3, 1, 1, 0,
        1, 1, 1, 1, 3, 1, 0,
        2, 1, 2, 1, 1, 1, 2,
        1, 1, 1, 2, 1, 1, 0,
        0, 1, 1, 1, 1, 1, 0,
        0, 1, 1, 1, 2, 0, 0
    };

    private PowerType[] colorsE = {
        null, PowerType.REMOVE, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, null, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, PowerType.YELLOW, null,
        PowerType.DISCARD, PowerType.GREEN, PowerType.BLACK, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD,
        PowerType.GREEN, PowerType.DISCARD, PowerType.DISCARD, PowerType.DISCARD, PowerType.BLACK, PowerType.GREEN, null,
        null, PowerType.DISCARD, PowerType.BLACK, PowerType.GREEN, PowerType.BLUE, PowerType.GREEN, null,
        null, PowerType.BLACK, PowerType.DISCARD, PowerType.DISCARD, PowerType.GREEN, null, null

    };

    private int[] powersE = {
        0, 1, 1, 1, 1, 0, 0,
        0, 1, 2, 1, 1, 1, 0,
        1, 1, 1, 1, 1, 1, 0,
        1, 2, 3, 1, 1, 2, 1,
        1, 1, 3, 1, 1, 1, 0,
        0, 1 ,1 ,2 ,2, 1, 0,
        0, 1, 1, 1, 1, 0, 0
    };

    private PowerType[] colorsF = {
        null, PowerType.GREEN, PowerType.BLACK, PowerType.BLACK, PowerType.BLUE, null, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, PowerType.BLUE, PowerType.BLUE, null,
        PowerType.DISCARD, PowerType.GREEN, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.DISCARD, null,
        PowerType.DISCARD, PowerType.YELLOW, PowerType.YELLOW, PowerType.BLACK, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD,
        PowerType.DISCARD, PowerType.YELLOW, PowerType.DISCARD, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.BLUE, PowerType.BLUE, null,
        null, PowerType.REMOVE, PowerType.GREEN, PowerType.DISCARD, PowerType.REMOVE, null, null
    };

    private int[] powersF = {
        0, 1, 1, 1, 1, 0, 0,
        0, 2, 1, 1, 1, 1, 0,
        1, 1, 2, 3, 1, 1, 0,
        1, 1, 2, 1, 2, 1, 1,
        1, 1, 2, 1, 1, 1, 0,
        0, 1, 3, 1, 2, 2, 0,
        0, 1, 2, 1, 2, 0, 0
    };

    private PowerType[] colorsG = {
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, PowerType.BLACK, PowerType.GREEN, null,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.DISCARD, PowerType.YELLOW, PowerType.GREEN, null,
        PowerType.BLACK, PowerType.BLACK, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.REMOVE,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.DISCARD, PowerType.YELLOW, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, PowerType.BLACK, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null
    };

    private int[] powersG = {
        0, 1, 1, 1, 1, 0, 0,
        0, 1, 2, 1, 1, 1, 0,
        1, 2, 2, 1, 1, 1, 0,
        1, 1, 4, 3, 2, 2, 1,
        1, 2, 2, 1, 1, 1, 0,
        0, 1, 2, 1, 1, 1, 0,
        0, 1, 1, 1, 1, 0, 0
    };

    private PowerType[] colorsH = {
        null, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, null, null,
        null, PowerType.YELLOW, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, null,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.BLUE, PowerType.BLUE, null,
        PowerType.YELLOW, PowerType.BLACK, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null
    };

    private int[] powersH = {
        0, 1, 2, 2, 2, 0, 0,
        0, 1, 1, 1, 1, 2, 0,
        2, 2, 1, 1, 1, 2, 0,
        3, 1, 2, 2, 1, 1, 1,
        2, 2, 1, 1, 1, 2, 0,
        0, 1, 1, 1, 1, 2, 0,
        0, 1, 2, 2, 2, 0, 0
    };

    private PowerType[] colorsI = {
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null,
        null, PowerType.YELLOW, PowerType.GREEN, PowerType.BLACK, PowerType.GREEN, PowerType.GREEN, null,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.BLACK, PowerType.GREEN, PowerType.GREEN, null,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, PowerType.REMOVE, PowerType.BLACK, PowerType.BLACK,
        PowerType.YELLOW, PowerType.DISCARD, PowerType.BLACK, PowerType.BLACK, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, null, null
    };

    private int[] powersI = {
        0, 1, 1, 1, 1, 0, 0,
        0, 1, 1, 1, 1, 1, 0,
        1, 2, 1, 1, 2, 1, 0,
        1, 2, 1, 2, 3, 1, 1,
        2, 3, 1, 1, 2, 1, 0,
        0, 2, 1, 1, 1, 1, 0,
        0, 2, 2, 1, 1, 0, 0
    };

    private PowerType[] colorsJ = {
        null, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, null, null,
        null, PowerType.BLUE, PowerType.BLACK, PowerType.BLUE, PowerType.BLUE, PowerType.DISCARD, null,
        PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD, PowerType.DISCARD, null,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.REMOVE, PowerType.GREEN, PowerType.DISCARD, PowerType.DISCARD,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD, PowerType.DISCARD, null,
        null, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.BLACK, PowerType.DISCARD, null,
        null, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.DISCARD, null, null
    };

    private int[] powersJ = {
        0, 1, 1, 1, 1, 0, 0,
        0, 1, 1, 2, 1, 2, 0,
        1, 2, 1, 2, 2, 1, 0,
        1, 1, 2, 1, 1, 2, 1,
        1, 2, 1, 1, 2, 1, 0,
        0, 1, 2, 2, 1, 1, 0,
        0, 1, 1, 1, 2, 0, 0
    };

    private PowerType[] colorsK = {
        null, PowerType.REMOVE, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN,
        PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.REMOVE, null, null
    };

    private int[] powersK = {
        0, 1, 2, 2, 1, 0, 0,
        0, 1, 1, 3, 1, 2, 0,
        1, 2, 1, 3, 1, 2, 0,
        2, 1, 3, 1, 3, 1, 2,
        2, 1, 3, 1, 2, 1, 0,
        0, 2, 1, 4, 1, 1, 0,
        0, 1, 2, 2, 1, 0, 0
    };

    private PowerType[] colorsL = {
        null, PowerType.BLUE, PowerType.REMOVE, PowerType.REMOVE, PowerType.GREEN, null, null,
        null, PowerType.GREEN, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, PowerType.GREEN, PowerType.BLACK, PowerType.GREEN, PowerType.GREEN,
        PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.YELLOW, PowerType.REMOVE, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, null, null
    };

    private int[] powersL = {
        0, 1, 1, 1, 3, 0, 0,
        0, 1, 1, 1, 3, 1, 0,
        2, 1, 1, 3, 1, 2, 0,
        2, 1, 1, 1, 1, 1, 2,
        1, 2, 2, 1, 2, 1, 0,
        0, 2, 2, 2, 1, 1, 0,
        0, 2, 2, 1, 1, 0, 0
    };

    private PowerType[] colorsM = {
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.REMOVE, null, null,
        null, PowerType.GREEN, PowerType.YELLOW, PowerType.BLACK, PowerType.BLACK, PowerType.BLUE, null,
        PowerType.GREEN, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, PowerType.BLUE, PowerType.BLACK, null,
        PowerType.BLACK, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD, PowerType.GREEN, PowerType.DISCARD, PowerType.BLACK,
        PowerType.BLACK, PowerType.BLACK, PowerType.BLACK, PowerType.BLACK, PowerType.DISCARD, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD, PowerType.GREEN, null,
        null, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, null, null
    };

    private int[] powersM = {
            0, 1, 1, 1, 1, 0, 0,
            0, 1, 4, 1, 1, 4, 0,
            1, 2, 1, 1, 1, 1, 0,
            1, 1, 1, 2, 1, 1, 1,
            1, 1, 1, 1, 2, 1, 0,
            0, 1, 1, 1, 2, 1, 0,
            0, 1, 1, 1, 1, 0, 0

    };

    private PowerType[] colorsN = {
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null,
        null, PowerType.YELLOW, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.BLUE, null,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.GREEN, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, null,
        PowerType.BLUE, PowerType.BLUE, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW, PowerType.YELLOW,
        PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, PowerType.YELLOW, PowerType.YELLOW, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null,
        null, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, PowerType.GREEN, null, null
    };

    private int[] powersN = {
        0, 1, 1, 1, 1, 0, 0,
        0, 1, 1, 2, 1, 1, 0,
        1, 2, 1, 1, 1, 1, 0,
        1, 1, 3, 4, 3, 2, 1,
        1, 1, 1, 1, 2, 1, 0,
        0, 1, 1, 2, 1, 1, 0,
        0, 1, 1, 1, 1, 0, 0
    };

    private PowerType[] colorsO = {
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        PowerType.YELLOW, PowerType.YELLOW, PowerType.DISCARD, PowerType.GREEN, PowerType.DISCARD, null, null,
        PowerType.YELLOW, PowerType.BLACK, PowerType.BLUE, PowerType.BLACK, PowerType.BLACK, PowerType.DISCARD, null,
        PowerType.YELLOW, PowerType.BLUE, PowerType.GREEN, PowerType.GREEN, PowerType.DISCARD, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null
    };

    private int[] powersO = {
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        2, 1, 1, 2, 2, 0, 0,
        1, 1, 4, 1, 1, 1, 0,
        1, 1, 2, 1, 1, 0 ,0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0
    };

    private PowerType[] colorsP = {
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, null, null,
        PowerType.DISCARD, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.BLUE, PowerType.GREEN, null,
        PowerType.BLUE, PowerType.GREEN, PowerType.BLUE, PowerType.DISCARD, PowerType.BLUE, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null
    };

    private int[] powersP = {
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        3, 2, 1, 2, 3, 0, 0,
        1, 1, 1, 1, 1, 1, 0,
        1, 2, 3, 2, 1, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0
    };

    private PowerType[] colorsQ = {
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        PowerType.DISCARD, PowerType.YELLOW, PowerType.YELLOW, PowerType.BLUE, PowerType.GREEN, null, null,
        PowerType.GREEN, PowerType.DISCARD, PowerType.GREEN, PowerType.YELLOW, PowerType.BLUE, PowerType.GREEN, null,
        PowerType.GREEN, PowerType.DISCARD, PowerType.GREEN, PowerType.GREEN, PowerType.BLUE, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null
    };

    private int[] powersQ = {
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        1, 1, 1, 1, 3, 0, 0,
        2, 1, 2, 3, 1, 2, 0,
        1, 3, 1, 1, 2, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0
    };

    private PowerType[] colorsR = {
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, PowerType.YELLOW, PowerType.YELLOW, null, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, PowerType.YELLOW, PowerType.REMOVE,PowerType.YELLOW, null,
        PowerType.GREEN, PowerType.GREEN, PowerType.BLACK, PowerType.YELLOW, PowerType.YELLOW, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null
    };

    private int[] powersR = {
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        1, 1, 1, 1, 1, 0, 0,
        1, 3, 1, 1, 1, 1, 0,
        1, 1, 1, 1, 1, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0
    };

    private PowerType[] colorsEnd1 = {
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        null, null, PowerType.GREEN, PowerType.ELDORADO, null, null, null,
        null, null, null, PowerType.GREEN, PowerType.ELDORADO, null, null,
        null, null, PowerType.GREEN, PowerType.ELDORADO, null, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null
    };

    private int[] powersEnd1 = {
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 1, 1, 0, 0, 0,
        0, 0, 0, 1, 1, 0, 0,
        0, 0, 1, 1, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0
    };

    private PowerType[] colorsEnd2 = {
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null,
        null, null, PowerType.BLUE, PowerType.ELDORADO, null, null, null,
        null, null, null, PowerType.BLUE, PowerType.ELDORADO, null, null,
        null, null, PowerType.BLUE, PowerType.ELDORADO, null, null, null,
        null, null, null, null, null, null, null,
        null, null, null, null, null, null, null
    };

    private int[] powersEnd2 = {
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 1, 1, 0, 0, 0,
        0, 0, 0, 1, 1, 0, 0,
        0, 0, 1, 1, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0
    };

    public Tile generateTile(String name) {
        PowerType[] colors;
        int[] powers;
        boolean elDoradoTile = false;

        switch (name) {
            case "A":
                colors = this.colorsA;
                powers = this.powersA;
                break;
            case "B":
                colors = this.colorsB;
                powers = this.powersB;
                break;
            case "C":
                colors = this.colorsC;
                powers = this.powersC;
                break;
            case "D":
                colors = this.colorsD;
                powers = this.powersD;
                break;
            case "E":
                colors = this.colorsE;
                powers = this.powersE;
                break;
            case "F":
                colors = this.colorsF;
                powers = this.powersF;
                break;
            case "G":
                colors = this.colorsG;
                powers = this.powersG;
                break;
            case "H":
                colors = this.colorsH;
                powers = this.powersH;
                break;
            case "I":
                colors = this.colorsI;
                powers = this.powersI;
                break;
            case "J":
                colors = this.colorsJ;
                powers = this.powersJ;
                break;
            case "K":
                colors = this.colorsK;
                powers = this.powersK;
                break;
            case "L":
                colors = this.colorsL;
                powers = this.powersL;
                break;
            case "M":
                colors = this.colorsM;
                powers = this.powersM;
                break;
            case "N":
                colors = this.colorsN;
                powers = this.powersN;
                break;
            case "O":
                colors = this.colorsO;
                powers = this.powersO;
                break;
            case "P":
                colors = this.colorsP;
                powers = this.powersP;
                break;
            case "Q":
                colors = this.colorsQ;
                powers = this.powersQ;
                break;
            case "R":
                colors = this.colorsR;
                powers = this.powersR;
                break;
            case "End1":
                colors = this.colorsEnd1;
                powers = this.powersEnd1;
                elDoradoTile = true;
                break;
            case "End2":
                colors = this.colorsEnd2;
                powers = this.powersEnd2;
                elDoradoTile = true;
                break;
            case "MiniA":
                colors = this.colorsMiniA;
                powers = this.powersMiniA;
                break;
            case "MiniB":
                colors = this.colorsMiniB;
                powers = this.powersMiniB;
                break;
            default:
                colors = this.colorsNull;
                powers = this.powersNull;
                break;
        }

        int i = 0;

        Tile tile = new Tile();

        ArrayList<HexRow> rows = new ArrayList<>();

        for (int q = 0; q < 7; q++) {
            HexRow hexRow = new HexRow();
            List<HexSpace> spaces = new ArrayList<>();
            for (int r = 0; r < 7; r++) {
                HexSpace hex = new HexSpace();
                if(colors[i] == null) {
                    hex = null;
                } else {
                    hex.setColor(colors[i]);
                    hex.setPower(powers[i]);
                    hex.setElDoradoSpace(elDoradoTile);
                    hexSpaceRepository.save(hex);
                }
                spaces.add(hex);
                i++;
            }
            hexRow.setSpaces(spaces);
            hexRowRepository.save(hexRow);
            rows.add(hexRow);
        }

        tile.setName(name);
        tile.setRows(rows);

        TileRepository.save(tile);

        return tile;
    }
}
