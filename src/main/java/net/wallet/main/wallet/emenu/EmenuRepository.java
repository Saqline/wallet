package net.wallet.main.wallet.emenu;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
    

@Repository
public interface EmenuRepository extends JpaRepository<Emenu,String>, JpaSpecificationExecutor<Emenu>{


Optional<Emenu>  findById(String id);
    
}