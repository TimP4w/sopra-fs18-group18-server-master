package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs18.entity.Game;
import ch.uzh.ifi.seal.soprafs18.entity.projections.GameSummary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository("GameRepository")
public interface GameRepository extends CrudRepository<Game, Long> {
	 Game findByName(String name);
	 List<Game> findByStatus(GameStatus status);
	 Optional<GameSummary> findProjectedById(Long id);
}