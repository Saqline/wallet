package net.wallet.main.wallet.accountdocuments;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//DB SEPECFIC IMPORT
import net.wallet.main.wallet.config.Auditable;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;

@Entity
@Table(name = "accountdocuments")
@NoArgsConstructor
@Getter
@Setter
public class AccountDocuments extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
        
        
@NotNull(groups = {IDataAdd.class},message = "accountid required")
private Integer accountid ;

@NotBlank(groups = {IDataAdd.class},message = "documenttype required")
private String documenttype ;

@NotBlank(groups = {IDataAdd.class},message = "documentnumber required")
private String documentnumber ;

private String photo1 ;

private String photo2 ;

private Boolean verified ;
@Transient
private String verifiedStr ;
private Double verfricationstatus ;

        public AccountDocuments(String id,String file,String colName) {
          this.id = id;
          if(colName.toLowerCase().equals("photo1")){
              this.photo1 = file;
          }
        else if(colName.toLowerCase().equals("photo2")){
            this.photo2=file;
        }
            
      }
        
}

