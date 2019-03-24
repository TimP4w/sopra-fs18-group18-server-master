package ch.uzh.ifi.seal.soprafs18.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "MarketPile")
public class MarketPile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany
    private List<Card> marketPlacePile = new ArrayList<>();

    @Column
    private Long marketPlaceId;

    @Column
    private String cardSlug;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Card> getMarketPlacePile() {
        return marketPlacePile;
    }

    public void setMarketPlacePile(List<Card> marketPlacePile) {
        this.cardSlug = marketPlacePile.get(0).getSlug();
        this.marketPlacePile = marketPlacePile;
    }

    public String getCardSlug() {
        return cardSlug;
    }

    public void setCardSlug(String cardSlug) {
        this.cardSlug = cardSlug;
    }

    public Long getMarketPlaceId() {
        return marketPlaceId;
    }

    public void setMarketPlaceId(Long marketPlaceId) {
        this.marketPlaceId = marketPlaceId;
    }
}