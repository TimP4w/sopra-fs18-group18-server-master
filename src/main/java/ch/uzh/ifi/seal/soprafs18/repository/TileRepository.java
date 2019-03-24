package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.entity.Tile;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Repository;

@Repository("TileRepository")
public interface TileRepository extends CrudRepository<Tile, Long> {

}