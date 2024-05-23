package net.wallet.main.wallet.userBank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/userbanks")
public class UserBankController {

    @Autowired
    private UserBankService userBankService;

    @GetMapping
    public Page<UserBank> getWithQuerySearch(@RequestParam Map<String, String> params) {
        return userBankService.getQueryEntities(params);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserBank> getUserBankById(@PathVariable String id) {
        Optional<UserBank> userBank = userBankService.getUserBankById(id);
        return userBank.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserBank> createUserBank(@RequestBody UserBank userBank) {
        UserBank createdUserBank = userBankService.createUserBank(userBank);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserBank);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserBank> updateUserBank(@PathVariable String id, @RequestBody UserBank userBank) {
        UserBank updatedUserBank = userBankService.updateUserBank(id, userBank);
        return updatedUserBank != null ? ResponseEntity.ok(updatedUserBank) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserBank(@PathVariable String id) {
        userBankService.deleteUserBank(id);
        return ResponseEntity.noContent().build();
    }
}

