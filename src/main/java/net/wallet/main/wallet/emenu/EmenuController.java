package net.wallet.main.wallet.emenu;
import java.util.List;
import java.util.UUID;
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

import net.wallet.main.wallet.utility.exception.CustomeException;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;
import net.wallet.main.wallet.utility.service.FilesStorageService;

@RestController
@RequestMapping(path="emenu")
public class EmenuController {
    @Autowired
   FilesStorageService storageService;
    private final EmenuService emenuService;
    public EmenuController(EmenuService emenuService){
        this.emenuService = emenuService;
        
    }

     @GetMapping(path="/{emenuId}")
   public Emenu getEmenu(@PathVariable("emenuId") String id){
        try{
            return emenuService.getEmenu(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   
   @PostMapping
   public Emenu addNewEmenu(@RequestBody @Validated(IDataAdd.class) Emenu emenu, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return emenuService.addNewEmenu(emenu);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   
    @PostMapping(path="/upload/{columnName}/{emenuId}")
    public String uploadFile(@RequestParam("uploadFile") MultipartFile file,@PathVariable("columnName") String columnName,@PathVariable("emenuId") String id ) {
        try{
            UUID uuid = UUID.randomUUID();
            String savedfileName="";
            String leading= uuid.toString();
            storageService.save(file,leading);
            Emenu emenu = new Emenu(id,leading+file.getOriginalFilename(),columnName);
            Emenu emenu_upload = emenuService.updateEmenu(id,emenu);
            
       savedfileName  = emenu_upload.getPhoto();
        
            return savedfileName;
        }catch(Exception e){
            throw new CustomeException(e.getMessage(),null);
        }
    }
        

   @DeleteMapping(path="{emenuId}")
   public void deleteEmenu(@PathVariable("emenuId") String id){
         try {
            emenuService.deleteEmenu(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{emenuId}")
   public void updateEmenu(@PathVariable("emenuId") String id,@RequestBody Emenu emenu, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        emenuService.updateEmenu(id,emenu);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
