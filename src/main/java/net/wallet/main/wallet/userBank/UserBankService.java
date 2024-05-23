package net.wallet.main.wallet.userBank;

import net.wallet.main.wallet.bank.Bank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

import java.util.Map;
import java.util.Optional;

@Service
public class UserBankService {

    @Autowired
    private UserBankRepository userBankRepository;

     public Page<UserBank> getQueryEntities(Map<String, String> params) {
        Specification<UserBank> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);

        specification = getUserBankQuerySearch(params, specification);

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

        return userBankRepository.findAll(specification, pageRequest);
    }


    private Specification<UserBank> getUserBankQuerySearch(Map<String, String> params, Specification<UserBank> specification) {
    // Search by any field
    String searchTerm = params.get("search");
    if (searchTerm != null) {
        specification = specification.and((root, query, builder) -> {
            Predicate[] predicates = new Predicate[5]; 
            predicates[0] = builder.like(root.get("accountId"), "%" + searchTerm + "%");
            predicates[1] = builder.like(root.get("bankAccNo"), "%" + searchTerm + "%");
            predicates[2] = builder.like(root.get("branchName"), "%" + searchTerm + "%");
            predicates[3] = builder.like(root.get("swiftCode"), "%" + searchTerm + "%");
            predicates[4] = builder.like(root.join("bank").get("name"), "%" + searchTerm + "%"); // Assuming bank name is searchable
            return builder.or(predicates);
        });
    }

    // Filter by specific fields (optional)
    for (Map.Entry<String, String> entry : params.entrySet()) {
        final String key = entry.getKey(); 
        final String value = entry.getValue(); 
        specification = specification.and((root, query, builder) -> {
            switch (key) {
                case "accountId":
                case "bankAccNo":
                case "branchName":
                case "swiftCode":
                    return builder.like(root.get(key), "%" + value + "%");
                case "bank":
                    // Assuming value is bankId, you can create a join and filter by bankId
                    Join<UserBank, Bank> bankJoin = root.join("bank");
                    return builder.equal(bankJoin.get("id"), value);
                case "isActive":
                    return builder.equal(root.get(key), Boolean.parseBoolean(value));
                default:
                    break;
            }
            return null; 
        });
    }
    return specification;
}



    public Optional<UserBank> getUserBankById(String id) {
        return userBankRepository.findById(id);
    }

    public UserBank createUserBank(UserBank userBank) {
        return userBankRepository.save(userBank);
    }

    public UserBank updateUserBank(String id, UserBank userBank) {
        if (userBankRepository.existsById(id)) {
            userBank.setId(id);
            return userBankRepository.save(userBank);
        } else {
            return null; // Or throw an exception
        }
    }

    public void deleteUserBank(String id) {
        userBankRepository.deleteById(id);
    }
}
