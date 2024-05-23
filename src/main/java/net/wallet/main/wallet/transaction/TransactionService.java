package net.wallet.main.wallet.transaction;
import java.util.Objects;

import net.wallet.main.wallet.transaction.transaction_enum.TransactionType;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
        
        
    }
    
    public Page<Transaction> getQueryEntities(Map<String, String> params) {
        Specification<Transaction> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);
    
        specification = getTransactionQuerySearch(params, specification);
    
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
    
        return transactionRepository.findAll(specification, pageRequest);
    }

    private Specification<Transaction> getTransactionQuerySearch(Map<String, String> params, Specification<Transaction> specification) {
    // Search by any field (optional)
    String searchTerm = params.get("search");
    if (searchTerm != null) {
        specification = specification.and((root, query, builder) -> {
            Predicate[] predicates = new Predicate[3]; 
            predicates[0] = builder.like(root.get("particular"), "%" + searchTerm + "%");
            predicates[1] = builder.like(root.get("accountid"), "%" + searchTerm + "%");
            predicates[2] = builder.like(root.get("transactionType").as(String.class), "%" + searchTerm + "%"); // Convert enum to string for like comparison
            return builder.or(predicates);
        });
    }

    // Filter by specific fields (optional)
    for (Map.Entry<String, String> entry : params.entrySet()) {
        final String key = entry.getKey();
        final String value = entry.getValue();
        specification = specification.and((root, query, builder) -> {
            switch (key) {
                case "particular":
                case "accountid":
                    return builder.like(root.get(key), "%" + value + "%");
                case "amount":
                    return builder.equal(root.get(key), Double.parseDouble(value));
                case "transactionType":
                    return builder.equal(root.get(key), TransactionType.valueOf(value));
                default:
                    break;
            }
            return null;
        });
    }
    return specification;
}

    public Transaction getTransactionById(String id){
      
        return transactionRepository.findById(id).orElseThrow(()->new IllegalStateException("NOT_FOUND"));
	}

  

    public Transaction addNewTransaction(Transaction transaction) {
       
        return transactionRepository.save(transaction);
    }



    public void deleteTransaction(String id) {
    

        transactionRepository.deleteById(id);
    }

     @Transactional
     public Transaction updateTransaction(String id, Transaction transaction) {
        Transaction check = transactionRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(transaction.getCreateby()!=null && transaction.getCreateby().length()>0 && !Objects.equals(check.getCreateby(),transaction.getCreateby())){
        
        check.setCreateby(transaction.getCreateby());
    }

        if(transaction.getCreateat()!=null  && !Objects.equals(check.getCreateat(),transaction.getCreateat())){
            check.setCreateat(transaction.getCreateat());
        }
                             
    if(transaction.getUpdateby()!=null && transaction.getUpdateby().length()>0 && !Objects.equals(check.getUpdateby(),transaction.getUpdateby())){
        
        check.setUpdateby(transaction.getUpdateby());
    }

        if(transaction.getUpdateat()!=null  && !Objects.equals(check.getUpdateat(),transaction.getUpdateat())){
            check.setUpdateat(transaction.getUpdateat());
        }
                             
    if(transaction.getParticular()!=null && transaction.getParticular().length()>0 && !Objects.equals(check.getParticular(),transaction.getParticular())){
        
        check.setParticular(transaction.getParticular());
    }
  

        if(transaction.getAccountid()!=null   && !Objects.equals(check.getAccountid(),transaction.getAccountid())){
            check.setAccountid(transaction.getAccountid());
        }

        if(transaction.getAmount()!=null  && !Objects.equals(check.getAmount(),transaction.getAmount())){
            check.setAmount(transaction.getAmount());
        }

        if(transaction.getBalance()!=null  && !Objects.equals(check.getBalance(),transaction.getBalance())){
            check.setBalance(transaction.getBalance());
        }
     return transactionRepository.save(check);

    }

   }
  