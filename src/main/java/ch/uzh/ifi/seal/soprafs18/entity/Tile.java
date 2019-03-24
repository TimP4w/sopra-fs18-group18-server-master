package ch.uzh.ifi.seal.soprafs18.entity;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
public class Tile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @OneToMany(cascade = {CascadeType.MERGE})
    List<HexRow> rows = new ArrayList<>();

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

    public List<HexRow> getRows() {
        return rows;
    }

    public void setRows(List<HexRow> rows) {
        this.rows = rows;
    }

    public HexSpace getSpace(int x, int y) {
        HexRow currentRow = this.rows.get(x);
        return currentRow.getSpaces().get(y);
    }

    public void setSpace(int x, int y, HexSpace newHS) {
        HexRow currentRow = this.rows.get(x);
        List<HexSpace> currentSpaces = currentRow.getSpaces();
        currentSpaces.set(y, newHS);
        currentRow.setSpaces(currentSpaces);
        this.rows.set(x, currentRow);
    }

    public void rotate(int n) {
        int[][][] cycles = {
            {{0,1},{3,0},{6,1},{6,4},{3,6},{0,4}}, // first outer
            {{0,2},{2,0},{5,1},{6,3},{4,5},{1,5}}, // second outer
            {{0,3},{1,1},{4,0},{6,2},{5,5},{2,5}}, // third outer
            {{1,2},{3,1},{5,2},{5,4},{3,5},{1,4}}, // first middle
            {{1,3},{2,1},{4,1},{5,3},{4,4},{2,4}}, // second middle
            {{2,2},{3,2},{4,2},{4,3},{3,4},{2,3}}  // inner
           };
        for(int count = 0; count < n; count++) {
            for (int[][] cycle : cycles) {
                HexSpace t = this.getSpace(cycle[0][0], cycle[0][1]);
                for (int i = 1; i < 6; i++) {
                    this.setSpace(cycle[i - 1][0], cycle[i - 1][1], this.getSpace(cycle[i][0], cycle[i][1]));
                }
                this.setSpace(cycle[5][0], cycle[5][1], t);
            }
        }
    }
}