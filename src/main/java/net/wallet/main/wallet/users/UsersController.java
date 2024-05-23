package net.wallet.main.wallet.users;
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
@RequestMapping(path="users")
public class UsersController {
    @Autowired
    private final UsersService usersService;
    public UsersController(UsersService usersService){
        this.usersService = usersService;
        
    }
    @GetMapping
    public ResponseEntity<Page<Users>> getQueryEntities(@RequestParam Map<String, String> params) {
        try {
            Page<Users> users = usersService.getQueryEntities(params);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
   
   
   
    @PostMapping(path="/add")
    public Users addNewUsers(@RequestBody  Users users){
           try{
            users.setWalletbalance(1000.00);
             return usersService.addNewUsers(users);
           }catch(Exception e){
             throw new CustomeException(e.getMessage(),null);
           }
          
        }
    
        @GetMapping(path="{usersId}")
        public Users getbyId(@PathVariable("usersId") String id){
              try {
              return   usersService.getbyId(id);
               } catch (Exception e) {
                 throw new CustomeException(e.getMessage(),null);
               }
        }

   @DeleteMapping(path="{usersId}")
   public void deleteUsers(@PathVariable("usersId") String id){
         try {
            usersService.deleteUsers(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{usersId}")
   public void updateUsers(@PathVariable("usersId") String id,@RequestBody Users users, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        usersService.updateUsers(id,users);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
