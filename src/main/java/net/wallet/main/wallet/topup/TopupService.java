package net.wallet.main.wallet.topup;
import java.util.Objects;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import net.wallet.main.wallet.topup.topup_enum.TopUpStatus;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Service
public class TopupService {

    private final TopupRepository topupRepository;
    public TopupService(TopupRepository topupRepository){
        this.topupRepository = topupRepository;
        
        
    }
    
    public Page<Topup> getQueryEntities(Map<String, String> params) {
        Specification<Topup> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);
    
        specification = getTopupQuerySearch(params, specification);
    
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
    
        return topupRepository.findAll(specification, pageRequest);
    }

    private Specification<Topup> getTopupQuerySearch(Map<String, String> params, Specification<Topup> specification) {
    // Search by any field (optional)
    String searchTerm = params.get("search");
    if (searchTerm != null) {
        specification = specification.and((root, query, builder) -> {
            Predicate[] predicates = new Predicate[6]; 
            predicates[0] = builder.like(root.get("method"), "%" + searchTerm + "%");
            predicates[1] = builder.like(root.get("topUpStatus").as(String.class), "%" + searchTerm + "%"); // Convert enum to string for like comparison
            predicates[2] = builder.like(root.get("notes"), "%" + searchTerm + "%");
            predicates[3] = builder.like(root.get("transactionid"), "%" + searchTerm + "%");
            predicates[4] = builder.like(root.get("marchanttransactionid"), "%" + searchTerm + "%");
            predicates[5] = builder.like(root.get("accountid"), "%" + searchTerm + "%");
            return builder.or(predicates);
        });
    }

    // Filter by specific fields (optional)
    for (Map.Entry<String, String> entry : params.entrySet()) {
        final String key = entry.getKey();
        final String value = entry.getValue();
        specification = specification.and((root, query, builder) -> {
            switch (key) {
                case "method":
                case "notes":
                case "transactionid":
                case "marchanttransactionid":
                case "accountid":
                    return builder.like(root.get(key), "%" + value + "%");
                case "amount":
                    return builder.equal(root.get(key), Double.parseDouble(value));
                case "topUpStatus":
                    return builder.equal(root.get(key), Enum.valueOf(TopUpStatus.class, value)); // Assuming TopUpStatus is an enum
                default:
                    break;
            }
            return null;
        });
    }
    return specification;
}


    public Topup addNewTopup(Topup topup) {
       
        return topupRepository.save(topup);
    }



    public void deleteTopup(String id) {
       

        topupRepository.deleteById(id);
    }

     @Transactional
     public Topup updateTopup(String id, Topup topup) {
        Topup check = topupRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(topup.getCreateby()!=null && topup.getCreateby().length()>0 && !Objects.equals(check.getCreateby(),topup.getCreateby())){
        
        check.setCreateby(topup.getCreateby());
    }

        if(topup.getCreateat()!=null  && !Objects.equals(check.getCreateat(),topup.getCreateat())){
            check.setCreateat(topup.getCreateat());
        }
                             
    if(topup.getUpdateby()!=null && topup.getUpdateby().length()>0 && !Objects.equals(check.getUpdateby(),topup.getUpdateby())){
        
        check.setUpdateby(topup.getUpdateby());
    }

        if(topup.getUpdateat()!=null  && !Objects.equals(check.getUpdateat(),topup.getUpdateat())){
            check.setUpdateat(topup.getUpdateat());
        }
                             
    if(topup.getMethod()!=null && topup.getMethod().length()>0 && !Objects.equals(check.getMethod(),topup.getMethod())){
        
        check.setMethod(topup.getMethod());
    }

        if(topup.getAmount()!=null  && !Objects.equals(check.getAmount(),topup.getAmount())){
            check.setAmount(topup.getAmount());
        }
                             
    if(topup.getTopUpStatus()!=null ){
        
        check.setTopUpStatus(topup.getTopUpStatus());
    }
                             
    if(topup.getNotes()!=null && topup.getNotes().length()>0 && !Objects.equals(check.getNotes(),topup.getNotes())){
        
        check.setNotes(topup.getNotes());
    }
                             
    if(topup.getTransactionid()!=null && topup.getTransactionid().length()>0 && !Objects.equals(check.getTransactionid(),topup.getTransactionid())){
        
        check.setTransactionid(topup.getTransactionid());
    }
                             
    if(topup.getMarchanttransactionid()!=null && topup.getMarchanttransactionid().length()>0 && !Objects.equals(check.getMarchanttransactionid(),topup.getMarchanttransactionid())){
        
        check.setMarchanttransactionid(topup.getMarchanttransactionid());
    }
  

        if(topup.getAccountid()!=null   && !Objects.equals(check.getAccountid(),topup.getAccountid())){
            check.setAccountid(topup.getAccountid());
        }
     return topupRepository.save(check);

    }






    public Topup getbyId(String id) {
        return topupRepository.findById(id).orElseThrow(()->new IllegalStateException("NOT_FOUND"));
    }

   
   }
  