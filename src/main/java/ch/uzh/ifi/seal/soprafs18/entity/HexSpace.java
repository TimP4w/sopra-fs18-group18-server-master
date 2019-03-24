package ch.uzh.ifi.seal.soprafs18.entity;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class HexSpace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private PowerType color;

    @Column
    private int power;

    @Column
    private int x;

    @Column
    private int y;

    @ManyToOne
    private Blockade blockade;

    @ManyToOne
    private Board board;

    @OneToOne
    private Piece occupiedBy;

    @Column
    private boolean isElDoradoSpace;

    public PowerType getColor() {
        return color;
    }

    public void setColor(PowerType color) {
        this.color = color;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Piece getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(Piece occupiedBy) {
        this.occupiedBy = occupiedBy;
    }

    public Blockade getBlockade() {
        return blockade;
    }

    public void setBlockade(Blockade blockade) {
        this.blockade = blockade;
    }

    public boolean isElDoradoSpace() {
        return isElDoradoSpace;
    }

    public void setElDoradoSpace(boolean elDoradoSpace) {
        isElDoradoSpace = elDoradoSpace;
    }
}