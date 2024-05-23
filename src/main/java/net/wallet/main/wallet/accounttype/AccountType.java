package net.wallet.main.wallet.accounttype;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT
import lombok.Setter;
import jakarta.persistence.Id;
 import jakarta.persistence.Table;
 import jakarta.persistence.Entity;
 import jakarta.persistence.GeneratedValue;
 import jakarta.persistence.GenerationType;
     
import net.wallet.main.wallet.config.Auditable;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;

@Entity
@Getter
@Setter
@Table(name = "accounttype")
@NoArgsConstructor
public class AccountType extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
        
        
@NotBlank(groups = {IDataAdd.class},message = "typename required")
private String typename ;

@NotNull(groups = {IDataAdd.class},message = "mintopup required")
private Double mintopup ;

@NotNull(groups = {IDataAdd.class},message = "maxtopup required")
private Double maxtopup ;

@NotNull(groups = {IDataAdd.class},message = "minwithdraw required")
private Double minwithdraw ;

@NotNull(groups = {IDataAdd.class},message = "maxwithdraw required")
private Double maxwithdraw ;

@NotNull(groups = {IDataAdd.class},message = "mintransfer required")
private Double mintransfer ;

@NotNull(groups = {IDataAdd.class},message = "maxtransfer required")
private Double maxtransfer ;

@NotNull(groups = {IDataAdd.class},message = "active required")
private Boolean active ;
@Transient
private String activeStr ;


 
    
            


}

