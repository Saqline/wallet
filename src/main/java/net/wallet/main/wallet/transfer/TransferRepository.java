package net.wallet.main.wallet.transfer;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, String>, JpaSpecificationExecutor<Transfer> {

    Optional<Transfer> findById(String id);

}