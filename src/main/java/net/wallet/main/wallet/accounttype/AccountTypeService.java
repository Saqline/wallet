package net.wallet.main.wallet.accounttype;
import java.util.Map;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;
    private EntityManager entityManager;

    
    public AccountTypeService(AccountTypeRepository accountTypeRepository,EntityManager entityManager){
        this.accountTypeRepository = accountTypeRepository;
        this.entityManager = entityManager;
        
        
    }
    
     public Page<AccountType> getQueryEntities(Map<String, String> params) {
        Specification<AccountType> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);

        specification = getAccountTypeQuerySearch(params, specification);

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

        return accountTypeRepository.findAll(specification, pageRequest);
    }

    private Specification<AccountType> getAccountTypeQuerySearch(Map<String, String> params, Specification<AccountType> specification) {
        // Search by any field (optional)
        String searchTerm = params.get("search");
        if (searchTerm != null) {
            specification = specification.and((root, query, builder) -> {
                Predicate[] predicates = new Predicate[1]; // Assuming there is 1 searchable field
                predicates[0] = builder.like(root.get("typename"), "%" + searchTerm + "%");
                return builder.or(predicates);
            });
        }
    
        // Filter by specific fields (optional)
        for (Map.Entry<String, String> entry : params.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            specification = specification.and((root, query, builder) -> {
                switch (key) {
                    case "typename":
                        return builder.like(root.get(key), "%" + value + "%");
                    case "mintopup":
                    case "maxtopup":
                    case "minwithdraw":
                    case "maxwithdraw":
                    case "mintransfer":
                    case "maxtransfer":
                        return builder.equal(root.get(key), Double.parseDouble(value));
                    case "active":
                        return builder.equal(root.get(key), Boolean.parseBoolean(value));
                    default:
                        break;
                }
                return null;
            });
        }
        return specification;
    }
    
    
    public AccountType getAccountType(String id){
        return accountTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("AccountType with id " + id + " does not exist"));
    }
   


    public AccountType addNewAccountType(AccountType accountType) {
       
        return accountTypeRepository.save(accountType);
    }



    public void deleteAccountType(String id) {
       

        accountTypeRepository.deleteById(id);
    }

     @Transactional
     public AccountType updateAccountType(String id, AccountType accountType) {
        AccountType check = accountTypeRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(accountType.getCreateby()!=null && accountType.getCreateby().length()>0 && !Objects.equals(check.getCreateby(),accountType.getCreateby())){
        
        check.setCreateby(accountType.getCreateby());
    }

        if(accountType.getCreateat()!=null  && !Objects.equals(check.getCreateat(),accountType.getCreateat())){
            check.setCreateat(accountType.getCreateat());
        }
                             
    if(accountType.getUpdateby()!=null && accountType.getUpdateby().length()>0 && !Objects.equals(check.getUpdateby(),accountType.getUpdateby())){
        
        check.setUpdateby(accountType.getUpdateby());
    }

        if(accountType.getUpdateat()!=null  && !Objects.equals(check.getUpdateat(),accountType.getUpdateat())){
            check.setUpdateat(accountType.getUpdateat());
        }
                             
    if(accountType.getTypename()!=null && accountType.getTypename().length()>0 && !Objects.equals(check.getTypename(),accountType.getTypename())){
        
        check.setTypename(accountType.getTypename());
    }

        if(accountType.getMintopup()!=null  && !Objects.equals(check.getMintopup(),accountType.getMintopup())){
            check.setMintopup(accountType.getMintopup());
        }

        if(accountType.getMaxtopup()!=null  && !Objects.equals(check.getMaxtopup(),accountType.getMaxtopup())){
            check.setMaxtopup(accountType.getMaxtopup());
        }

        if(accountType.getMinwithdraw()!=null  && !Objects.equals(check.getMinwithdraw(),accountType.getMinwithdraw())){
            check.setMinwithdraw(accountType.getMinwithdraw());
        }

        if(accountType.getMaxwithdraw()!=null  && !Objects.equals(check.getMaxwithdraw(),accountType.getMaxwithdraw())){
            check.setMaxwithdraw(accountType.getMaxwithdraw());
        }

        if(accountType.getMintransfer()!=null  && !Objects.equals(check.getMintransfer(),accountType.getMintransfer())){
            check.setMintransfer(accountType.getMintransfer());
        }

        if(accountType.getMaxtransfer()!=null  && !Objects.equals(check.getMaxtransfer(),accountType.getMaxtransfer())){
            check.setMaxtransfer(accountType.getMaxtransfer());
        }

        if(accountType.getActive()!=null  && !Objects.equals(check.getActive(),accountType.getActive())){
            check.setActive(accountType.getActive());
        }
     return accountTypeRepository.save(check);

    }

   
   

}
  