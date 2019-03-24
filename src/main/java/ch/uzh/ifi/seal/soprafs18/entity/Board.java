package ch.uzh.ifi.seal.soprafs18.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity(name="Board")
public class Board implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name="gameid", referencedColumnName = "id")

    private Game game;

    @Column
    private String rotation;

    @OneToMany(cascade = {CascadeType.MERGE})
    private List<HexRow> path;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<HexRow> getPath() {
        return path;
    }

    public void setPath(List<HexRow> path) {
        this.path = path;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}