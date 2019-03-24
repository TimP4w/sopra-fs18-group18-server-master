package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository("UserRepository")
public interface UserRepository extends CrudRepository<User, Long> {
	User findByName(String name);
	Optional<User> findByUsername(String username);
	User findByToken(String token);
}