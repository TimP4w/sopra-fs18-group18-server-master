package ch.uzh.ifi.seal.soprafs18.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import ch.uzh.ifi.seal.soprafs18.constant.GameStatus;

@Entity(name="Game")
public class Game implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false) 
	private Long owner;
	
	@Column(nullable = false)
    private GameStatus status;
	
	@Column 
	private Integer currentPlayer;

	@Column(nullable = false)
    private Integer numberPlayers;

    @OneToMany
	private List<ExpeditionBoard> expeditionBoards;

    @ManyToMany(cascade = {CascadeType.MERGE})
    private List<User> players = new ArrayList<>();

    @OneToOne(mappedBy="game",cascade=CascadeType.MERGE)
	private Board board;

    @Column
	private int mapId;

    @Column
	private int turnNumber;

    @ManyToMany
	private List<ExpeditionBoard> haveReachedElDorado = new ArrayList<>();

    @ManyToMany
	private List<ExpeditionBoard> winners;

    @OneToOne
	private Marketplace marketPlace;

    @OneToMany
	private List<Log> logs = new ArrayList<>();


    public Integer getNumberPlayers() {return numberPlayers;}

    public void setNumberPlayers(Integer numberPlayers) {this.numberPlayers = numberPlayers;}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() { return name;}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}

	public List<User> getPlayers() {
		return players;
	}

	public void setPlayers(List<User> players) {
		this.players = players;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public Integer getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(Integer currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public List<ExpeditionBoard> getExpeditionBoards() {
		return expeditionBoards;
	}

	public void setExpeditionBoards(List<ExpeditionBoard> expeditionBoards) {
		this.expeditionBoards = expeditionBoards;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

    public int getMapId() {
        return mapId;
    }

    public void setMapId(int mapId) {
        this.mapId = mapId;
    }

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	public Marketplace getMarketPlace() {
		return marketPlace;
	}

	public void setMarketPlace(Marketplace marketPlace) {
		this.marketPlace = marketPlace;
	}

    public List<ExpeditionBoard> getHaveReachedElDorado() {
        return haveReachedElDorado;
    }

    public void setHaveReachedElDorado(List<ExpeditionBoard> haveReachedElDorado) {
        this.haveReachedElDorado = haveReachedElDorado;
    }

    public List<ExpeditionBoard> getWinners() {
		return winners;
	}

	public void setWinners(List<ExpeditionBoard> winners) {
		this.winners = winners;
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}
}