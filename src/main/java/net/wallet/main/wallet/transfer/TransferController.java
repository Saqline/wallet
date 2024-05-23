package net.wallet.main.wallet.transfer;

import java.util.List;
import java.util.Map;

import net.wallet.main.wallet.users.Users;
import net.wallet.main.wallet.users.UsersService;
import net.wallet.main.wallet.utility.exception.CustomException;
import net.wallet.main.wallet.utility.exception.CustomeException;
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
import java.util.stream.Collectors;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping(path = "transfer")
public class TransferController {
    @Autowired

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;

    }

    @Autowired
    UsersService usersService;

    @GetMapping
    public ResponseEntity<Page<Transfer>> getQueryEntities(@RequestParam Map<String, String> params) {
        try {
            Page<Transfer> users = transferService.getQueryEntities(params);
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public Object sendTransfer(@RequestBody Transfer transfer,Authentication authentication) {
        try {
            
           Authentication auth = SecurityContextHolder.getContext().getAuthentication();
           Users user = (Users) auth.getPrincipal();
           String userId = user.getId();
           System.out.println("userId: "+userId);
            Users sendFrom = usersService.getbyId(transfer.getAccountfrom());
            Users receiveTo = usersService.getbyId(transfer.getAccountto());
            // Check if both users exist
            if (sendFrom == null || receiveTo == null) {
                throw new RuntimeException("Invalid sender or receiver");
            }

            // Check if the sender has enough balance to make the transfer
            if (transfer.getAmount().compareTo(sendFrom.getWalletbalance()) > 0) {
                throw new RuntimeException("Insufficient balance");
            }
            return transferService.newTransfer(transfer);
        } catch (Exception e) {
            throw new CustomeException(e.getMessage(), null);
        }

    }

    // @PostMapping("/topup")
    // public Transfer topUpTransfer(@RequestBody Map<String, Object> payload,Authentication authentication) {
    //     try {
            
    //        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //        Users user = (Users) authentication.getPrincipal();
    //        String userId = user.getId();
    //        System.out.println("userId: "+userId);
    //         Users sendFrom = usersService.getbyId(userId);
    //         String accountto = (String)payload.get("accountto");
    //         Users receiveTo = usersService.getbyId(accountto);
    //         // Check if both users exist
    //         if (sendFrom == null || receiveTo == null) {
    //             throw new RuntimeException("Invalid sender or receiver");
    //         }
    //         Double amount =(Double) payload.get("amount");
    //         // Check if the sender has enough balance to make the transfer
    //         if (amount.compareTo(sendFrom.getWalletbalance()) > 0) {
    //             throw new RuntimeException("Insufficient balance");
    //         }
    //         return transferService.popUpTransfer(payload);
    //     } catch (Exception e) {
    //         throw new CustomeException(e.getMessage(), null);
    //     }

    // }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        try {
            String otp = payload.get("otp");
            String id = payload.get("id");
            String topUpId = payload.get("topUpId");

            // Now you can use otp and id for further processing
            // For example, call a service method to verify the OTP
            boolean isVerified = transferService.verifySendOtp(id, otp,topUpId);

            if (isVerified) {
                return ResponseEntity.ok("OTP verified successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP");
            }
        } catch (Exception e) {
            throw new CustomeException(e.getMessage(), null);
        }
    }

    @GetMapping(path = "{transferId}")
    public Transfer getTransferById(@PathVariable("transferId") String id) {
        try {
            return transferService.getbyId(id);
        } catch (Exception e) {
            throw new CustomeException(e.getMessage(), null);
        }
    }

    @DeleteMapping(path = "{transferId}")
    public void deleteTransfer(@PathVariable("transferId") String id) {
        try {
            transferService.deleteTransfer(id);
        } catch (Exception e) {
            throw new CustomeException(e.getMessage(), null);
        }
    }

    @PatchMapping(path = "{transferId}")
    public void updateTransfer(@PathVariable("transferId") String id, @RequestBody Transfer transfer,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                throw new CustomeException(errorMessages.toString(), null);
            }
            transferService.updateTransfer(id, transfer);
        } catch (Exception e) {
            throw new CustomeException(e.getMessage(), null);
        }
    }
}
