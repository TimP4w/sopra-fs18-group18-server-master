package ch.uzh.ifi.seal.soprafs18.entity;
import ch.uzh.ifi.seal.soprafs18.constant.PowerType;
import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Card implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String slug;

    @Column
    private String name;

    @Column
    private Boolean removable;

    @Column
    private Integer price;

    @Column
    private Double value;

    @Column
    private PowerType power;

    @Column
    private Integer powerValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRemovable() {
        return removable;
    }

    public void setRemovable(Boolean removable) {
        this.removable = removable;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public PowerType getPower() {
        return power;
    }

    public void setPower(PowerType power) {
        this.power = power;
    }

    public Integer getPowerValue() {
        return powerValue;
    }

    public void setPowerValue(Integer powerValue) {
        this.powerValue = powerValue;
    }
}