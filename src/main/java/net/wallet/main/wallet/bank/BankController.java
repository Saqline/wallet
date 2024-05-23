package net.wallet.main.wallet.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/banks")
public class BankController {

    @Autowired
    private BankService bankService;

    
    @GetMapping
    public Page<Bank> getWithQuerySearch(@RequestParam Map<String, String> params) {
        
        return bankService.getQueryEntities(params);
    }
    @GetMapping("/all")
    public List<Bank> getAllBanks() {
        return bankService.getAllBanks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBankById(@PathVariable String id) {
        Optional<Bank> bank = bankService.getBankById(id);
        return bank.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    

    @PostMapping
    public ResponseEntity<Bank> createBank(@RequestBody Bank bank) {
        Bank createdBank = bankService.createBank(bank);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBank);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bank> updateBank(@PathVariable String id, @RequestBody Bank bank) {
        Bank updatedBank = bankService.updateBank(id, bank);
        return updatedBank != null ? ResponseEntity.ok(updatedBank) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBank(@PathVariable String id) {
        bankService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }
}

