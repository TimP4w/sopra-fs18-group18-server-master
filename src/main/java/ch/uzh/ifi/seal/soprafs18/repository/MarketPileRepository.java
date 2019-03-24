package ch.uzh.ifi.seal.soprafs18.repository;

import ch.uzh.ifi.seal.soprafs18.entity.MarketPile;
import ch.uzh.ifi.seal.soprafs18.entity.Marketplace;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("MarketPileRepository")
public interface MarketPileRepository extends CrudRepository<MarketPile, Long>  {
    MarketPile findByCardSlugAndMarketPlaceId(String cardSlug, Long marketId);

}
