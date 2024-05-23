package net.wallet.main.wallet.accounttype;
import java.util.List;
import java.util.Map;

import net.wallet.main.wallet.utility.exception.CustomException;
import net.wallet.main.wallet.utility.exception.CustomeException;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;
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
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping(path="accounttype")
public class AccountTypeController {
    @Autowired

    private final AccountTypeService accounttypeService;
    public AccountTypeController(AccountTypeService accounttypeService){
        this.accounttypeService = accounttypeService;
        
    }

    @GetMapping
    public ResponseEntity<Page<AccountType>> getQueryEntities(@RequestParam Map<String, String> params) {
        try {
            Page<AccountType> users = accounttypeService.getQueryEntities(params);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
   @GetMapping(path="/{accounttypeId}")
   public AccountType getAccountType(@PathVariable("accounttypeId") String id){
        try{
            return accounttypeService.getAccountType(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   
   @PostMapping
   public AccountType addNewAccountType(@RequestBody @Validated(IDataAdd.class) AccountType accounttype, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return accounttypeService.addNewAccountType(accounttype);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   

   @DeleteMapping(path="{accounttypeId}")
   public void deleteAccountType(@PathVariable("accounttypeId") String id){
         try {
            accounttypeService.deleteAccountType(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{accounttypeId}")
   public void updateAccountType(@PathVariable("accounttypeId") String id,@RequestBody AccountType accounttype, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        accounttypeService.updateAccountType(id,accounttype);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
