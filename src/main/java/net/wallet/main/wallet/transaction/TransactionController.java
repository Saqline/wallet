package net.wallet.main.wallet.transaction;
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
@RequestMapping(path="transaction")
public class TransactionController {
    @Autowired

    private final TransactionService transactionService;
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
        
    }

    @GetMapping
    public ResponseEntity<Page<Transaction>> getQueryEntities(@RequestParam Map<String, String> params) {
        try {
            Page<Transaction> users = transactionService.getQueryEntities(params);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
   @GetMapping(path="/{transactionId}")
   public Transaction getTransaction(@PathVariable("transactionId") String id){
        try{
            return transactionService.getTransactionById(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   

  
   @PostMapping
   public Transaction addNewTransaction(@RequestBody @Validated(IDataAdd.class) Transaction transaction, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return transactionService.addNewTransaction(transaction);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   

   @DeleteMapping(path="{transactionId}")
   public void deleteTransaction(@PathVariable("transactionId") String id){
         try {
            transactionService.deleteTransaction(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{transactionId}")
   public void updateTransaction(@PathVariable("transactionId") String id,@RequestBody Transaction transaction, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        transactionService.updateTransaction(id,transaction);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
