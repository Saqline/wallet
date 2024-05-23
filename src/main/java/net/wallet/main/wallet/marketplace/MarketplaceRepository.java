package net.wallet.main.wallet.marketplace;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
    

@Repository
public interface MarketplaceRepository extends JpaRepository<Marketplace,String>, JpaSpecificationExecutor<Marketplace>{


Optional<Marketplace>  findById(String id);
    
}