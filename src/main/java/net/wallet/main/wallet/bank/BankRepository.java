package net.wallet.main.wallet.bank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface BankRepository extends JpaRepository<Bank, String> , JpaSpecificationExecutor<Bank>{
}

