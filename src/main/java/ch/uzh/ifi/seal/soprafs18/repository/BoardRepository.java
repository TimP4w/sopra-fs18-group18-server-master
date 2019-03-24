package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.entity.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ch.uzh.ifi.seal.soprafs18.entity.projections.BoardSummary;
import java.util.Optional;

@Repository("BoardRepository")
public interface BoardRepository extends CrudRepository<Board, Long> {


    Optional<BoardSummary> findProjectedById(Long id);

}