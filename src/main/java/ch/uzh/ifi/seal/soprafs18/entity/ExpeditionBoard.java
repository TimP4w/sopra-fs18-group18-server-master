package ch.uzh.ifi.seal.soprafs18.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class ExpeditionBoard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long gameId;

    @Column
    private Long ownerId;

    @ManyToMany(cascade = {CascadeType.MERGE})
    private List<Card> drawPile = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.MERGE})
    private List<Card> discardPile = new ArrayList<>();

    @ManyToMany(targetEntity = Card.class, cascade = {CascadeType.MERGE})
    private List<Card> handPile = new ArrayList<>();

    @Column
    private Double goldBalance;

    @Column
    private Integer canRemove;

    @Column
    private Boolean canMove;

    @Column
    private Integer canBuy;

    @Column
    private String color;

    @ManyToMany(targetEntity = Card.class, cascade = {CascadeType.ALL})
    private List<Card> playedCards = new ArrayList<>();

    @ManyToMany(targetEntity = HexSpace.class, cascade = {CascadeType.ALL})
    private List<HexSpace> possibleMoves = new ArrayList<>();

    @OneToMany
    private List<Piece> pieces;

    @OneToMany
    private List<Blockade> blockadesWon = new ArrayList<>();

    @Column
    private boolean alreadyBought = false;


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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Card> getDrawPile() {
        return drawPile;
    }

    public void setDrawPile(List<Card> drawPile) {
        this.drawPile = drawPile;
    }

    public List<Card> getDiscardPile() {
        return discardPile;
    }

    public void setDiscardPile(List<Card> discardPile) {
        this.discardPile = discardPile;
    }

    public List<Card> getHandPile() {
        return handPile;
    }

    public void setHandPile(List<Card> handPile) {
        this.handPile = handPile;
    }

    public Double getGoldBalance() {
        return goldBalance;
    }

    public void setGoldBalance(Double goldBalance) {
        this.goldBalance = goldBalance;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Card> getPlayedCards() {
        return playedCards;
    }
    
    public Integer getCanRemove() {
        return canRemove;
    }

    public void setCanRemove(Integer canRemove) {
        this.canRemove = canRemove;
    }

    public Integer getCanBuy() {
        return canBuy;
    }

    public void setCanBuy(Integer canBuy) {
        this.canBuy = canBuy;
    }

    public void setPlayedCards(List<Card> playedCards) {
        this.playedCards = playedCards;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(List<Piece> pieces) {
        this.pieces = pieces;
    }

    public Boolean getCanMove() {
        return canMove;
    }

    public void setCanMove(Boolean canMove) {
        this.canMove = canMove;
    }

    public List<HexSpace> getPossibleMoves() {
        return possibleMoves;
    }

    public void setPossibleMoves(List<HexSpace> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    public List<Blockade> getBlockadesWon() {
        return blockadesWon;
    }

    public void setBlockadesWon(List<Blockade> blockadesWon) {
        this.blockadesWon = blockadesWon;
    }

    public boolean isAlreadyBought() {
        return alreadyBought;
    }

    public void setAlreadyBought(boolean alreadyBought) {
        this.alreadyBought = alreadyBought;
    }

    // Helper functions
    public Card drawCard() {
        if (this.drawPile.isEmpty()) {
            this.swapDrawAndDiscardPile();
            this.shuffleDrawpile();
        }
        Card card = this.drawPile.remove(0);
        this.handPile.add(card);
        return card;
    }

    public void swapDrawAndDiscardPile(){
        while (! discardPile.isEmpty()){
            drawPile.add(discardPile.remove(0));
        }
    }

    public void shuffleDrawpile(){
        Collections.shuffle(this.drawPile);
    }

    public void discardPlayedCards() {
        if(this.getPlayedCards().size() == 1 && this.getPlayedCards().get(0).getRemovable()) {
            this.getPlayedCards().remove(0);
            return;
        }
        while(!this.getPlayedCards().isEmpty()) {
            Card card = this.getPlayedCards().remove(0);
            if (!card.getRemovable()) {
                this.getDiscardPile().add(card);
            }
        }
    }

    public void resetStats() {
        this.goldBalance = 0.0;
        this.canRemove = 0;
        this.canMove = false;
        this.canBuy = 0;
        this.resetPossibleMoves();
        this.alreadyBought = false;
    }

    public void resetPossibleMoves() {
        this.setPossibleMoves(new ArrayList<HexSpace>());
    }

    public void drawHand() {
        while(this.getHandPile().size() < 4) {
            this.drawCard();
        }
    }
}