package net.wallet.main.wallet.marketplace;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//DB SEPECFIC IMPORT
import net.wallet.main.wallet.config.Auditable;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;
import net.wallet.main.wallet.utility.interfaces.IDataSearch;
import net.wallet.main.wallet.utility.interfaces.IDataUpdate;

@Entity
@Table(name = "marketplace")
@Getter
@Setter
@NoArgsConstructor
public class Marketplace extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
        
        
@NotBlank(groups = {IDataAdd.class},message = "businessname required")
private String businessname ;

@NotNull(groups = {IDataAdd.class},message = "type required")
private Integer type ;

@NotBlank(groups = {IDataAdd.class},message = "address required")
private String address ;

@NotBlank(groups = {IDataAdd.class},message = "city required")
private String city ;

@NotBlank(groups = {IDataAdd.class},message = "state required")
private String state ;

@NotBlank(groups = {IDataAdd.class},message = "postcode required")
private String postcode ;

@NotBlank(groups = {IDataAdd.class},message = "contactnumber required")
@Pattern(regexp = "^\\d{8,16}$",groups = {IDataAdd.class, IDataUpdate.class, IDataSearch.class},message = "contactnumber should be valid phone/mobile")
private String contactnumber ;

@Pattern(regexp = "^((http|https)://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$",groups = {IDataAdd.class,IDataUpdate.class},message = "website is invalid URL")
private String website ;

@Pattern(regexp = "^((http|https)://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$",groups = {IDataAdd.class,IDataUpdate.class},message = "mapurl is invalid URL")
private String mapurl ;

private String photo ;

private String status ;

private Boolean active ;
@Transient
private String activeStr ;
@NotNull(groups = {IDataAdd.class},message = "accountid required")
private Integer accountid ;


        public Marketplace(String id,String file,String colName) {
          this.id = id;
          if(colName.toLowerCase().equals("photo")){
              this.photo = file;
          }
      }
        
}

