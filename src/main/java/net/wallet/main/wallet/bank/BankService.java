package net.wallet.main.wallet.bank;

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
public class BankService {

    @Autowired
    private BankRepository bankRepository;

    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    public Page<Bank> getQueryEntities(Map<String, String> params) {
        Specification<Bank> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);

        specification = getBankQuerySearch(params, specification);

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

        return bankRepository.findAll(specification, pageRequest);
    }

    private Specification<Bank> getBankQuerySearch(Map<String, String> params, Specification<Bank> specification) {
        // Search by any field (optional)
        String searchTerm = params.get("search");
        if (searchTerm != null) {
            specification = specification.and((root, query, builder) -> {
                Predicate[] predicates = new Predicate[3]; // Assuming there are 3 searchable fields
                predicates[0] = builder.like(root.get("name"), "%" + searchTerm + "%");
                predicates[1] = builder.like(root.get("shortName"), "%" + searchTerm + "%");
                predicates[2] = builder.like(root.get("logo"), "%" + searchTerm + "%");
                return builder.or(predicates);
            });
        }
    
        // Filter by specific fields (optional)
        for (Map.Entry<String, String> entry : params.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            specification = specification.and((root, query, builder) -> {
                switch (key) {
                    case "name":
                    case "shortName":
                    case "logo":
                        return builder.like(root.get(key), "%" + value + "%");
                    default:
                        break;
                }
                return null;
            });
        }
        return specification;
    }
    

    public Optional<Bank> getBankById(String id) {
        return bankRepository.findById(id);
    }

    public Bank createBank(Bank bank) {
        return bankRepository.save(bank);
    }

    public Bank updateBank(String id, Bank bank) {
        if (bankRepository.existsById(id)) {
            bank.setId(id);
            return bankRepository.save(bank);
        } else {
            return null; // Or throw an exception
        }
    }

    public void deleteBank(String id) {
        bankRepository.deleteById(id);
    }
}
