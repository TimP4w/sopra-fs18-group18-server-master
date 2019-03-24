package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.entity.Blockade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("BlockadeRepository")
public interface BlockadeRepository extends CrudRepository<Blockade,Long>{
}
