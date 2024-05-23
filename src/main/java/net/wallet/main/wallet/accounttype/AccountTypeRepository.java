package net.wallet.main.wallet.accounttype;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
    

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType,String>, JpaSpecificationExecutor<AccountType>{


Optional<AccountType>  findById(String id);
    
}