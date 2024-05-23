package net.wallet.main.wallet.emenu;
import java.util.Objects;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Service
public class EmenuService {

    private final EmenuRepository emenuRepository;
    private EntityManager entityManager;

    
    public EmenuService(EmenuRepository emenuRepository,EntityManager entityManager){
        this.emenuRepository = emenuRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
  
    public Emenu getEmenu(String id){
        return emenuRepository.findById(id).orElseThrow(()->new IllegalStateException("NOT_FOUND"));
	}

    

    public Emenu addNewEmenu(Emenu emenu) {
       
        return emenuRepository.save(emenu);
    }



    public void deleteEmenu(String id) {
        

        emenuRepository.deleteById(id);
    }

     @Transactional
     public Emenu updateEmenu(String id, Emenu emenu) {
        Emenu check = emenuRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(emenu.getCreateby()!=null && emenu.getCreateby().length()>0 && !Objects.equals(check.getCreateby(),emenu.getCreateby())){
        
        check.setCreateby(emenu.getCreateby());
    }

        if(emenu.getCreateat()!=null  && !Objects.equals(check.getCreateat(),emenu.getCreateat())){
            check.setCreateat(emenu.getCreateat());
        }
                             
    if(emenu.getUpdateby()!=null && emenu.getUpdateby().length()>0 && !Objects.equals(check.getUpdateby(),emenu.getUpdateby())){
        
        check.setUpdateby(emenu.getUpdateby());
    }

        if(emenu.getUpdateat()!=null  && !Objects.equals(check.getUpdateat(),emenu.getUpdateat())){
            check.setUpdateat(emenu.getUpdateat());
        }
                             
    if(emenu.getProductname()!=null && emenu.getProductname().length()>0 && !Objects.equals(check.getProductname(),emenu.getProductname())){
        
        check.setProductname(emenu.getProductname());
    }
                             
    if(emenu.getProductgroupname()!=null && emenu.getProductgroupname().length()>0 && !Objects.equals(check.getProductgroupname(),emenu.getProductgroupname())){
        
        check.setProductgroupname(emenu.getProductgroupname());
    }
                             
    if(emenu.getUnit()!=null && emenu.getUnit().length()>0 && !Objects.equals(check.getUnit(),emenu.getUnit())){
        
        check.setUnit(emenu.getUnit());
    }

        if(emenu.getUnitprice()!=null  && !Objects.equals(check.getUnitprice(),emenu.getUnitprice())){
            check.setUnitprice(emenu.getUnitprice());
        }

        if(emenu.getActive()!=null  && !Objects.equals(check.getActive(),emenu.getActive())){
            check.setActive(emenu.getActive());
        }
                             
    if(emenu.getStatus()!=null && emenu.getStatus().length()>0 && !Objects.equals(check.getStatus(),emenu.getStatus())){
        
        check.setStatus(emenu.getStatus());
    }
                             
    if(emenu.getPhoto()!=null && emenu.getPhoto().length()>0 && !Objects.equals(check.getPhoto(),emenu.getPhoto())){
        
        check.setPhoto(emenu.getPhoto());
    }
  

        if(emenu.getAccountid()!=null   && !Objects.equals(check.getAccountid(),emenu.getAccountid())){
            check.setAccountid(emenu.getAccountid());
        }
     return emenuRepository.save(check);

    }

   
    
}
  