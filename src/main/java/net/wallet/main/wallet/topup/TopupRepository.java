package net.wallet.main.wallet.topup;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
    

@Repository
public interface TopupRepository extends JpaRepository<Topup,String>, JpaSpecificationExecutor<Topup>{


Optional<Topup>  findById(String id);
    
}