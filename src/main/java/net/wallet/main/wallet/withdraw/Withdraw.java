package net.wallet.main.wallet.withdraw;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.wallet.main.wallet.config.Auditable;
import net.wallet.main.wallet.userBank.UserBank;
import net.wallet.main.wallet.withdraw.withdraw_enum.WithdrawStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
@Getter
@Setter
public class Withdraw extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String accountId;
    private Double amount;
    @ManyToOne
    @JoinColumn(name = "userBankId")
    private UserBank userBank;
    @Enumerated(EnumType.STRING)
    private WithdrawStatus status;

    public void setUserBank(String userBankId) {
        this.userBank = new UserBank();
        this.userBank.setId(userBankId);
    }
    
}
