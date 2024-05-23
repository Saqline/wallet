package net.wallet.main.wallet.topup;

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
import net.wallet.main.wallet.topup.topup_enum.TopUpStatus;

@Entity
@Table(name = "topup")
@Getter
@Setter
@NoArgsConstructor
public class Topup extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
        
        
@NotBlank(message = "method required")
private String method ;

@NotNull(message = "amount required")
private Double amount ;

@NotBlank(message = "status required")
 @Enumerated(EnumType.STRING)
private TopUpStatus topUpStatus ;

@NotBlank(message = "notes required")
private String notes ;

private String transactionid ;

private String marchanttransactionid ;

@NotNull(message = "accountid required")
private String accountid ;

}

