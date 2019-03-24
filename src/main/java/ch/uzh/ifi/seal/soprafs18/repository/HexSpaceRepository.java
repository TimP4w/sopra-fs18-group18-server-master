package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.entity.HexSpace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("HexSpaceRepository")
public interface HexSpaceRepository extends CrudRepository<HexSpace, Long> {
    HexSpace findByXAndYAndBoardId(int x, int y, Long boardId);
}