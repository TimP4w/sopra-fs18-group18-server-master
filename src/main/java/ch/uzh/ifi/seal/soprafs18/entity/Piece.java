package ch.uzh.ifi.seal.soprafs18.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Piece implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(targetEntity = ExpeditionBoard.class, cascade = {CascadeType.ALL})
    private ExpeditionBoard expBoard;

    @ManyToOne(targetEntity = User.class, cascade = {CascadeType.ALL})
    private User owner;

    @OneToOne
    private HexSpace space;

    @Column
    private String color;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExpeditionBoard getExpBoard() {
        return expBoard;
    }

    public void setExpBoard(ExpeditionBoard expBoard) {
        this.expBoard = expBoard;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public HexSpace getSpace() {
        return space;
    }

    public void setSpace(HexSpace space) {
        this.space = space;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}