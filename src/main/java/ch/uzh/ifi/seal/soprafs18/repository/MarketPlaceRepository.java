package ch.uzh.ifi.seal.soprafs18.repository;

import ch.uzh.ifi.seal.soprafs18.entity.Marketplace;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository ("MarketplaceRepository")
public interface MarketPlaceRepository extends CrudRepository<Marketplace, Long> {
    Marketplace findByGameId( Long gameId);
}