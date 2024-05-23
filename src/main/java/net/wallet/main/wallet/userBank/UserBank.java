package net.wallet.main.wallet.userBank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.wallet.main.wallet.bank.Bank;
import net.wallet.main.wallet.config.Auditable;

@Entity
@Getter
@Setter
public class UserBank extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String accountId;
    @ManyToOne
    @JoinColumn(name = "bankId")
    private Bank bank;

    private String bankAccNo;
    private String branchName;
    private String swiftCode;
    private Boolean isActive;
    private String supportingDocs;
   

    public void setBank(String bankId) {
        this.bank = new Bank();
        this.bank.setId(bankId);
    }
    
   
}