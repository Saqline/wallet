package net.wallet.main.wallet.marketplace;
import java.util.Objects;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class MarketplaceService {

    private final MarketplaceRepository marketplaceRepository;
    public MarketplaceService(MarketplaceRepository marketplaceRepository){
        this.marketplaceRepository = marketplaceRepository;
        
        
    }
    

    
   
    public Marketplace getMarketplace(String id){
        return marketplaceRepository.findById(id).orElseThrow(()->new IllegalStateException("NOT_FOUND"));
       
	}

   

    public Marketplace addNewMarketplace(Marketplace marketplace) {
       
        return marketplaceRepository.save(marketplace);
    }



    public void deleteMarketplace(String id) {
       

        marketplaceRepository.deleteById(id);
    }

     @Transactional
     public Marketplace updateMarketplace(String id, Marketplace marketplace) {
        Marketplace check = marketplaceRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(marketplace.getCreateby()!=null && marketplace.getCreateby().length()>0 && !Objects.equals(check.getCreateby(),marketplace.getCreateby())){
        
        check.setCreateby(marketplace.getCreateby());
    }

        if(marketplace.getCreateat()!=null  && !Objects.equals(check.getCreateat(),marketplace.getCreateat())){
            check.setCreateat(marketplace.getCreateat());
        }
                             
    if(marketplace.getUpdateby()!=null && marketplace.getUpdateby().length()>0 && !Objects.equals(check.getUpdateby(),marketplace.getUpdateby())){
        
        check.setUpdateby(marketplace.getUpdateby());
    }

        if(marketplace.getUpdateat()!=null  && !Objects.equals(check.getUpdateat(),marketplace.getUpdateat())){
            check.setUpdateat(marketplace.getUpdateat());
        }
                             
    if(marketplace.getBusinessname()!=null && marketplace.getBusinessname().length()>0 && !Objects.equals(check.getBusinessname(),marketplace.getBusinessname())){
        
        check.setBusinessname(marketplace.getBusinessname());
    }
  

        if(marketplace.getType()!=null   && !Objects.equals(check.getType(),marketplace.getType())){
            check.setType(marketplace.getType());
        }
                             
    if(marketplace.getAddress()!=null && marketplace.getAddress().length()>0 && !Objects.equals(check.getAddress(),marketplace.getAddress())){
        
        check.setAddress(marketplace.getAddress());
    }
                             
    if(marketplace.getCity()!=null && marketplace.getCity().length()>0 && !Objects.equals(check.getCity(),marketplace.getCity())){
        
        check.setCity(marketplace.getCity());
    }
                             
    if(marketplace.getState()!=null && marketplace.getState().length()>0 && !Objects.equals(check.getState(),marketplace.getState())){
        
        check.setState(marketplace.getState());
    }
                             
    if(marketplace.getPostcode()!=null && marketplace.getPostcode().length()>0 && !Objects.equals(check.getPostcode(),marketplace.getPostcode())){
        
        check.setPostcode(marketplace.getPostcode());
    }
                             
    if(marketplace.getContactnumber()!=null && marketplace.getContactnumber().length()>0 && !Objects.equals(check.getContactnumber(),marketplace.getContactnumber())){
        
        check.setContactnumber(marketplace.getContactnumber());
    }
                             
    if(marketplace.getWebsite()!=null && marketplace.getWebsite().length()>0 && !Objects.equals(check.getWebsite(),marketplace.getWebsite())){
        
        check.setWebsite(marketplace.getWebsite());
    }
                             
    if(marketplace.getMapurl()!=null && marketplace.getMapurl().length()>0 && !Objects.equals(check.getMapurl(),marketplace.getMapurl())){
        
        check.setMapurl(marketplace.getMapurl());
    }
                             
    if(marketplace.getPhoto()!=null && marketplace.getPhoto().length()>0 && !Objects.equals(check.getPhoto(),marketplace.getPhoto())){
        
        check.setPhoto(marketplace.getPhoto());
    }
                             
    if(marketplace.getStatus()!=null && marketplace.getStatus().length()>0 && !Objects.equals(check.getStatus(),marketplace.getStatus())){
        
        check.setStatus(marketplace.getStatus());
    }

        if(marketplace.getActive()!=null  && !Objects.equals(check.getActive(),marketplace.getActive())){
            check.setActive(marketplace.getActive());
        }
  

        if(marketplace.getAccountid()!=null   && !Objects.equals(check.getAccountid(),marketplace.getAccountid())){
            check.setAccountid(marketplace.getAccountid());
        }
     return marketplaceRepository.save(check);

    }

   
    
}
  