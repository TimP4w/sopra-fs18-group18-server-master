package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.entity.HexRow;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("HexRowRepository")
public interface HexRowRepository extends CrudRepository<HexRow, Long> {

}
