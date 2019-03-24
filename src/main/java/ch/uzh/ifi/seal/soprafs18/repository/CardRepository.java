package ch.uzh.ifi.seal.soprafs18.repository;
import ch.uzh.ifi.seal.soprafs18.entity.Card;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("CardRepository")
public interface CardRepository extends CrudRepository<Card, Long> {
    Card findBySlug(String slug);
}
