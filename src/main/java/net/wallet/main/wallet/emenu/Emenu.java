package net.wallet.main.wallet.emenu;

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
@Table(name = "emenu")
@NoArgsConstructor
@Getter
@Setter
public class Emenu extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
        
        
@NotBlank(groups = {IDataAdd.class},message = "productname required")
private String productname ;

@NotBlank(groups = {IDataAdd.class},message = "productgroupname required")
private String productgroupname ;

@NotBlank(groups = {IDataAdd.class},message = "unit required")
private String unit ;

@NotNull(groups = {IDataAdd.class},message = "unitprice required")
private Double unitprice ;

@NotNull(groups = {IDataAdd.class},message = "active required")

private Boolean active ;
@Transient
private String activeStr ;
@NotBlank(groups = {IDataAdd.class},message = "status required")
private String status ;

private String photo ;

@NotNull(groups = {IDataAdd.class},message = "accountid required")
private String accountid ;



   
            


        public Emenu(String id,String file,String colName) {
          this.id = id;
          if(colName.toLowerCase().equals("photo")){
              this.photo = file;
          }
      }
        
}

