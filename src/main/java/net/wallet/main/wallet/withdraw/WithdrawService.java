package net.wallet.main.wallet.withdraw;

import net.wallet.main.wallet.transaction.Transaction;
import net.wallet.main.wallet.transaction.TransactionService;
import net.wallet.main.wallet.transaction.transaction_enum.TransactionType;
import net.wallet.main.wallet.users.Users;
import net.wallet.main.wallet.users.UsersService;
import net.wallet.main.wallet.withdraw.withdraw_enum.WithdrawStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class WithdrawService {

    @Autowired
    private WithdrawRepository withdrawRepository;
    @Autowired
    TransactionService transactionService;
    @Autowired
    UsersService usersService;

    public List<Withdraw> getAllWithdraws() {
        return withdrawRepository.findAll();
    }

    public Page<Withdraw> getQueryEntities(Map<String, String> params) {
        Specification<Withdraw> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);

        specification = getWithdrawQuerySearch(params, specification);

        // Sort by field (optional)
        if (params.containsKey("sort")) {
            String sortField = params.get("sort");
            String direction = params.getOrDefault("direction", "asc");
            sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        }

        // Pagination (optional)
        if (params.containsKey("page")) {
            int page = Integer.parseInt(params.get("page"));
            int size = Integer.parseInt(params.getOrDefault("size", "10"));
            pageRequest = PageRequest.of(page, size, sort);
        }

        return withdrawRepository.findAll(specification, pageRequest);
    }

    private Specification<Withdraw> getWithdrawQuerySearch(Map<String, String> params, Specification<Withdraw> specification) {
        // Search by any field
        String searchTerm = params.get("search");
        if (searchTerm != null) {
            Specification<Withdraw> searchSpec = (root, query, builder) -> {
                Predicate[] predicates = new Predicate[4]; // Assuming there are 4 fields to search
                predicates[0] = builder.like(root.get("accountId"), "%" + searchTerm + "%");
                predicates[1] = builder.like(root.get("amount").as(String.class), "%" + searchTerm + "%"); // Convert amount to string for like comparison
                predicates[2] = builder.like(root.get("status").as(String.class), "%" + searchTerm + "%"); // Convert enum to string for like comparison
                predicates[3] = builder.like(root.get("userBank").get("bankAccNo"), "%" + searchTerm + "%");
                return builder.or(predicates);
            };
            specification = specification.and(searchSpec);
        }
    
        // Filter by specific fields (optional)
        for (Map.Entry<String, String> entry : params.entrySet()) {
            final String key = entry.getKey(); 
            final String value = entry.getValue(); 
            specification = specification.and((root, query, builder) -> {
                switch (key) {
                    case "accountId":
                    case "amount":
                    case "status":
                    case "userBank":
                        return builder.like(root.get(key).as(String.class), "%" + value + "%"); // Convert to string for like comparison
                    default:
                        break;
                }
                return null; 
            });
        }
        return specification;
    }
    
    public Optional<Withdraw> getWithdrawById(String id) {
        return withdrawRepository.findById(id);
    }

    public Withdraw createWithdraw(Withdraw withdraw) {
        withdraw.setStatus(WithdrawStatus.DONE);
        createTrans(withdraw);
        return withdrawRepository.save(withdraw);
    }

    private void createTrans(Withdraw withdraw) {
         Users sendFrom= usersService.getbyId(withdraw.getAccountId());
       sendFrom.setWalletbalance(sendFrom.getWalletbalance()+withdraw.getAmount());
       usersService.updateUsers(sendFrom.getId(), sendFrom);

        Transaction transaction = new Transaction();
        transaction.setAmount(withdraw.getAmount());
        transaction.setAccountid(withdraw.getAccountId());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setParticular(withdraw.getAmount()+" Unit Withdraw Done");
        transaction.setBalance(sendFrom.getWalletbalance());
        transactionService.addNewTransaction(transaction);
       
    }

    public Withdraw updateWithdraw(String id, Withdraw withdraw) {
        if (withdrawRepository.existsById(id)) {
            withdraw.setId(id);
            return withdrawRepository.save(withdraw);
        } else {
            return null; // Or throw an exception
        }
    }

    public void deleteWithdraw(String id) {
        withdrawRepository.deleteById(id);
    }
}
