package net.wallet.main.wallet.transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import net.wallet.main.wallet.config.Auditable;
import net.wallet.main.wallet.transaction.transaction_enum.TransactionType;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@Getter
@Setter
public class Transaction extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(groups = { IDataAdd.class }, message = "particular required")
    private String particular;

    @NotNull(groups = { IDataAdd.class }, message = "accountid required")
    private String accountid;

    @NotNull(groups = { IDataAdd.class }, message = "amount required")
    private Double amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private Double balance;

}
