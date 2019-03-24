package ch.uzh.ifi.seal.soprafs18.entity;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Blockade implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private int power;

    @Column
    private PowerType color;

    @ManyToMany(cascade = {CascadeType.MERGE})
    private List<HexSpace> blockedSpaces;

    @Column
    private String rotation;

    public Long getId() {
        return id;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public PowerType getColor() {
        return color;
    }

    public void setColor(PowerType color) {
        this.color = color;
    }

    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public List<HexSpace> getBlockedSpaces() {
        return blockedSpaces;
    }

    public void setBlockedSpaces(List<HexSpace> blockedSpaces) {
        this.blockedSpaces = blockedSpaces;
    }
}
