package net.wallet.main.wallet.accountdocuments;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.wallet.main.wallet.utility.exception.CustomException;
import net.wallet.main.wallet.utility.exception.CustomeException;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;
import net.wallet.main.wallet.utility.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.data.domain.Page;

@RestController
@RequestMapping(path="accountdocuments")
public class AccountDocumentsController {
    @Autowired
    FilesStorageService storageService;
    private final AccountDocumentsService accountdocumentsService;
    public AccountDocumentsController(AccountDocumentsService accountdocumentsService){
        this.accountdocumentsService = accountdocumentsService;
        
    }

     @GetMapping
    public ResponseEntity<Page<AccountDocuments>> getQueryEntities(@RequestParam Map<String, String> params) {
        try {
            Page<AccountDocuments> users = accountdocumentsService.getQueryEntities(params);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
   @GetMapping(path="/{accountdocumentsId}")
   public AccountDocuments getAccountDocuments(@PathVariable("accountdocumentsId") String id){
        try{
            return accountdocumentsService.getAccountDocuments(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   
   @PostMapping
   public AccountDocuments addNewAccountDocuments(@RequestBody @Validated(IDataAdd.class) AccountDocuments accountdocuments, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return accountdocumentsService.addNewAccountDocuments(accountdocuments);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   
    @PostMapping(path="/upload/{columnName}/{accountdocumentsId}")
    public String uploadFile(@RequestParam("uploadFile") MultipartFile file,@PathVariable("columnName") String columnName,@PathVariable("accountdocumentsId") String id ) {
        try{
            UUID uuid = UUID.randomUUID();
            String savedfileName="";
            String leading= uuid.toString();
            storageService.save(file,leading);
            AccountDocuments accountdocuments = new AccountDocuments(id,leading+file.getOriginalFilename(),columnName);
            AccountDocuments accountdocuments_upload = accountdocumentsService.updateAccountDocuments(id,accountdocuments);
            
        if(columnName.toLowerCase().equals("photo1")){
            savedfileName= accountdocuments_upload.getPhoto1();
        }
        
        else if(columnName.toLowerCase().equals("photo2")){
            savedfileName= accountdocuments_upload.getPhoto2();
        }
            
            return savedfileName;
        }catch(Exception e){
            throw new CustomeException(e.getMessage(),null);
        }
    }
        

   @DeleteMapping(path="{accountdocumentsId}")
   public void deleteAccountDocuments(@PathVariable("accountdocumentsId") String id){
         try {
            accountdocumentsService.deleteAccountDocuments(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{accountdocumentsId}")
   public void updateAccountDocuments(@PathVariable("accountdocumentsId") String id,@RequestBody AccountDocuments accountdocuments, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        accountdocumentsService.updateAccountDocuments(id,accountdocuments);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
