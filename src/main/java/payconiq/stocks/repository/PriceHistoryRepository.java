package payconiq.stocks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import payconiq.stocks.model.PriceHistory;

/**
 * Repository for {@link PriceHistory} entities to perform base operations.
 */
@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
}
