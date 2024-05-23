package net.wallet.main.wallet.topup;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.wallet.main.wallet.utility.exception.CustomException;
import net.wallet.main.wallet.utility.exception.CustomeException;

import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping(path="topup")
public class TopupController {
    @Autowired

    private final TopupService topupService;
    public TopupController(TopupService topupService){
        this.topupService = topupService;
        
    }

   
 
    @GetMapping
    public ResponseEntity<Page<Topup>> getQueryEntities(@RequestParam Map<String, String> params) {
        try {
            Page<Topup> users = topupService.getQueryEntities(params);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
  
    
   @PostMapping
   public Topup addNewTopup(@RequestBody Topup topup, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return topupService.addNewTopup(topup);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   
    @GetMapping(path="{topupId}")
    public Topup getbyId(@PathVariable("topupId") String id){
        try {
         return  topupService.getbyId(id);
         } catch (Exception e) {
           throw new CustomeException(e.getMessage(),null);
         }
  }
   @DeleteMapping(path="{topupId}")
   public void deleteTopup(@PathVariable("topupId") String id){
         try {
            topupService.deleteTopup(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{topupId}")
   public void updateTopup(@PathVariable("topupId") String id,@RequestBody Topup topup, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        topupService.updateTopup(id,topup);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
