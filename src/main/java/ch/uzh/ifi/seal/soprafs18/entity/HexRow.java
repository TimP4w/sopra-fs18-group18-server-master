package ch.uzh.ifi.seal.soprafs18.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class HexRow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(cascade = {CascadeType.MERGE})
    private List<HexSpace> spaces;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<HexSpace> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<HexSpace> spaces) {
        this.spaces = spaces;
    }
}