package ch.uzh.ifi.seal.soprafs18.repository;

import ch.uzh.ifi.seal.soprafs18.entity.Log;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("LogRepository")
public interface LogRepository extends CrudRepository<Log, Long>  {
    Log findByGameId(Long gameId);
}
