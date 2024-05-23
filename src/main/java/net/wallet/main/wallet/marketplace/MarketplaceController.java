package net.wallet.main.wallet.marketplace;
import java.util.List;
import java.util.UUID;

import net.wallet.main.wallet.utility.exception.CustomeException;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;
import net.wallet.main.wallet.utility.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;

@RestController
@RequestMapping(path="marketplace")
public class MarketplaceController {
    @Autowired
    FilesStorageService storageService;
    private final MarketplaceService marketplaceService;
    public MarketplaceController(MarketplaceService marketplaceService){
        this.marketplaceService = marketplaceService;
        
    }

    @GetMapping(path="/{marketplaceId}")
   public Marketplace getMarketplace(@PathVariable("marketplaceId") String id){
        try{
            return marketplaceService.getMarketplace(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

 
   @PostMapping
   public Marketplace addNewMarketplace(@RequestBody @Validated(IDataAdd.class) Marketplace marketplace, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return marketplaceService.addNewMarketplace(marketplace);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   
    @PostMapping(path="/upload/{columnName}/{marketplaceId}")
    public String uploadFile(@RequestParam("uploadFile") MultipartFile file,@PathVariable("columnName") String columnName,@PathVariable("marketplaceId") String id ) {
        try{
            UUID uuid = UUID.randomUUID();
            String savedfileName="";
            String leading= uuid.toString();
            storageService.save(file,leading);
            Marketplace marketplace = new Marketplace(id,leading+file.getOriginalFilename(),columnName);
            Marketplace marketplace_upload = marketplaceService.updateMarketplace(id,marketplace);
            
       savedfileName  = marketplace_upload.getPhoto();
        
            return savedfileName;
        }catch(Exception e){
            throw new CustomeException(e.getMessage(),null);
        }
    }
        

   @DeleteMapping(path="{marketplaceId}")
   public void deleteMarketplace(@PathVariable("marketplaceId") String id){
         try {
            marketplaceService.deleteMarketplace(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{marketplaceId}")
   public void updateMarketplace(@PathVariable("marketplaceId") String id,@RequestBody Marketplace marketplace, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        marketplaceService.updateMarketplace(id,marketplace);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
