package net.wallet.main.wallet.userBank;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBankRepository extends JpaRepository<UserBank, String>, JpaSpecificationExecutor<UserBank> {
}
