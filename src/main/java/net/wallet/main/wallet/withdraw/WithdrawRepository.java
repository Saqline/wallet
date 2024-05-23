package net.wallet.main.wallet.withdraw;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, String>, JpaSpecificationExecutor<Withdraw> {
}

