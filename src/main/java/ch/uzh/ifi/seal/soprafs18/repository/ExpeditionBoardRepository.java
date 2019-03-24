package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.entity.ExpeditionBoard;
import ch.uzh.ifi.seal.soprafs18.entity.projections.ExpeditionBoardSummary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository(" Expeditionboard Repository")
public interface ExpeditionBoardRepository extends CrudRepository<ExpeditionBoard, Long> {
    List<ExpeditionBoard> findByOwnerId(Long ownerId);
    ExpeditionBoardSummary findProjectedById(Long id);
}