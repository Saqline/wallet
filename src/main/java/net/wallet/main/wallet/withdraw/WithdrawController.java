package net.wallet.main.wallet.withdraw;

import net.wallet.main.wallet.users.Users;
import net.wallet.main.wallet.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/withdraws")
public class WithdrawController {

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    UsersService usersService;

    @GetMapping
    public Page<Withdraw> getWithQuerySearch(@RequestParam Map<String, String> params) {
        return withdrawService.getQueryEntities(params);
    }

    @GetMapping("/all")
    public List<Withdraw> getAllWithdraws() {
        return withdrawService.getAllWithdraws();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Withdraw> getWithdrawById(@PathVariable String id) {
        Optional<Withdraw> withdraw = withdrawService.getWithdrawById(id);
        return withdraw.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Withdraw> createWithdraw(@RequestBody Withdraw withdraw) {
        Users sendFrom = usersService.getbyId(withdraw.getAccountId());
            
            // Check if  users exist
            if (sendFrom == null ) {
                throw new RuntimeException("Invalid Id");
            }

            // Check if the sender has enough balance to make the withdraw
            if (withdraw.getAmount().compareTo(sendFrom.getWalletbalance()) > 0) {
                throw new RuntimeException("Insufficient balance");
            }
        Withdraw createdWithdraw = withdrawService.createWithdraw(withdraw);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdWithdraw);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Withdraw> updateWithdraw(@PathVariable String id, @RequestBody Withdraw withdraw) {
        Withdraw updatedWithdraw = withdrawService.updateWithdraw(id, withdraw);
        return updatedWithdraw != null ? ResponseEntity.ok(updatedWithdraw) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWithdraw(@PathVariable String id) {
        withdrawService.deleteWithdraw(id);
        return ResponseEntity.noContent().build();
    }
}
