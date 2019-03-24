package ch.uzh.ifi.seal.soprafs18.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name="Marketplace")
public class Marketplace implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long gameId;

    @OneToMany(cascade = {CascadeType.MERGE})
    private List<MarketPile> marketplaceBackRow = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE})
    private List<MarketPile> marketplaceMiddleRow = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE})
    private List<MarketPile> marketplaceFrontRow = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public List<MarketPile> getMarketplaceBackRow() {
        return marketplaceBackRow;
    }

    public void setMarketplaceBackRow(List<MarketPile> marketplaceBackRow) {
        this.marketplaceBackRow = marketplaceBackRow;
    }

    public List<MarketPile> getMarketplaceMiddleRow() {
        return marketplaceMiddleRow;
    }

    public void setMarketplaceMiddleRow(List<MarketPile> marketplaceMiddleRow) {
        this.marketplaceMiddleRow = marketplaceMiddleRow;
    }

    public List<MarketPile> getMarketplaceFrontRow() {
        return marketplaceFrontRow;
    }

    public void setMarketplaceFrontRow(List<MarketPile> marketplaceFrontRow) {
        this.marketplaceFrontRow = marketplaceFrontRow;
    }
}