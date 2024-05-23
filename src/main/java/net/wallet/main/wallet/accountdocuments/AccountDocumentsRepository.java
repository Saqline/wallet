package net.wallet.main.wallet.accountdocuments;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
    

@Repository
public interface AccountDocumentsRepository extends JpaRepository<AccountDocuments,String>, JpaSpecificationExecutor<AccountDocuments>{


Optional<AccountDocuments>  findById(String id);
    
}