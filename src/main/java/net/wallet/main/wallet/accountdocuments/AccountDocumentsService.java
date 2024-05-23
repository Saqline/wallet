package net.wallet.main.wallet.accountdocuments;

import java.util.Map;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class AccountDocumentsService {

    private final AccountDocumentsRepository accountDocumentsRepository;

    public AccountDocumentsService(AccountDocumentsRepository accountDocumentsRepository) {
        this.accountDocumentsRepository = accountDocumentsRepository;

    }

    public Page<AccountDocuments> getQueryEntities(Map<String, String> params) {
        Specification<AccountDocuments> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);

        specification = getAccountDocumentsQuerySearch(params, specification);

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

        return accountDocumentsRepository.findAll(specification, pageRequest);
    }


    private Specification<AccountDocuments> getAccountDocumentsQuerySearch(Map<String, String> params, Specification<AccountDocuments> specification) {
    // Search by any field (optional)
    String searchTerm = params.get("search");
    if (searchTerm != null) {
        specification = specification.and((root, query, builder) -> {
            Predicate[] predicates = new Predicate[3]; 
            predicates[0] = builder.like(root.get("documenttype"), "%" + searchTerm + "%");
            predicates[1] = builder.like(root.get("documentnumber"), "%" + searchTerm + "%");
            predicates[2] = builder.equal(root.get("verified"), Boolean.parseBoolean(searchTerm)); // Assuming "verified" is a boolean field
            return builder.or(predicates);
        });
    }

    // Filter by specific fields (optional)
    for (Map.Entry<String, String> entry : params.entrySet()) {
        final String key = entry.getKey();
        final String value = entry.getValue();
        specification = specification.and((root, query, builder) -> {
            switch (key) {
                case "accountid":
                    return builder.equal(root.get(key), Integer.parseInt(value));
                case "documenttype":
                case "documentnumber":
                    return builder.like(root.get(key), "%" + value + "%");
                case "verified":
                    return builder.equal(root.get(key), Boolean.parseBoolean(value));
                default:
                    break;
            }
            return null;
        });
    }
    return specification;
}


    public AccountDocuments getAccountDocuments(String id) {
        return accountDocumentsRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("AccountDocuments with id " + id + " does not exist"));
    }

    public AccountDocuments addNewAccountDocuments(AccountDocuments accountDocuments) {

        return accountDocumentsRepository.save(accountDocuments);
    }

    public void deleteAccountDocuments(String id) {

        accountDocumentsRepository.deleteById(id);
    }

    @Transactional
    public AccountDocuments updateAccountDocuments(String id, AccountDocuments accountDocuments) {
        AccountDocuments check = accountDocumentsRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("NOT_FOUND"));

        if (accountDocuments.getCreateby() != null && accountDocuments.getCreateby().length() > 0
                && !Objects.equals(check.getCreateby(), accountDocuments.getCreateby())) {

            check.setCreateby(accountDocuments.getCreateby());
        }

        if (accountDocuments.getCreateat() != null
                && !Objects.equals(check.getCreateat(), accountDocuments.getCreateat())) {
            check.setCreateat(accountDocuments.getCreateat());
        }

        if (accountDocuments.getUpdateby() != null && accountDocuments.getUpdateby().length() > 0
                && !Objects.equals(check.getUpdateby(), accountDocuments.getUpdateby())) {

            check.setUpdateby(accountDocuments.getUpdateby());
        }

        if (accountDocuments.getUpdateat() != null
                && !Objects.equals(check.getUpdateat(), accountDocuments.getUpdateat())) {
            check.setUpdateat(accountDocuments.getUpdateat());
        }

        if (accountDocuments.getAccountid() != null
                && !Objects.equals(check.getAccountid(), accountDocuments.getAccountid())) {
            check.setAccountid(accountDocuments.getAccountid());
        }

        if (accountDocuments.getDocumenttype() != null && accountDocuments.getDocumenttype().length() > 0
                && !Objects.equals(check.getDocumenttype(), accountDocuments.getDocumenttype())) {

            check.setDocumenttype(accountDocuments.getDocumenttype());
        }

        if (accountDocuments.getDocumentnumber() != null && accountDocuments.getDocumentnumber().length() > 0
                && !Objects.equals(check.getDocumentnumber(), accountDocuments.getDocumentnumber())) {

            check.setDocumentnumber(accountDocuments.getDocumentnumber());
        }

        if (accountDocuments.getPhoto1() != null && accountDocuments.getPhoto1().length() > 0
                && !Objects.equals(check.getPhoto1(), accountDocuments.getPhoto1())) {

            check.setPhoto1(accountDocuments.getPhoto1());
        }

        if (accountDocuments.getPhoto2() != null && accountDocuments.getPhoto2().length() > 0
                && !Objects.equals(check.getPhoto2(), accountDocuments.getPhoto2())) {

            check.setPhoto2(accountDocuments.getPhoto2());
        }

        if (accountDocuments.getVerified() != null
                && !Objects.equals(check.getVerified(), accountDocuments.getVerified())) {
            check.setVerified(accountDocuments.getVerified());
        }

        if (accountDocuments.getVerfricationstatus() != null
                && !Objects.equals(check.getVerfricationstatus(), accountDocuments.getVerfricationstatus())) {
            check.setVerfricationstatus(accountDocuments.getVerfricationstatus());
        }
        return accountDocumentsRepository.save(check);

    }

}
