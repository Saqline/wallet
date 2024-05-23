package net.wallet.main.wallet.users;

import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import net.wallet.main.wallet.roleEnum.RoleEnum;
import net.wallet.main.wallet.utility.exception.CustomeException;

import java.util.Date;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class UsersService {

    private final UsersRepository usersRepository;
    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;

    }

    public Page<Users> getQueryEntities(Map<String, String> params) {
        Specification<Users> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);

        specification = getQuerySearch(params, specification);

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

        return usersRepository.findAll(specification, pageRequest);
    }

    private Specification<Users> getQuerySearch(Map<String, String> params, Specification<Users> specification) {
        // Search by any field
        String searchTerm = params.get("search");
        if (searchTerm != null) {
            Specification<Users> searchSpec = (root, query, builder) -> {
                Predicate[] predicates = new Predicate[4]; 
                predicates[0] = builder.like(root.get("firstname"), "%" + searchTerm + "%");
                predicates[1] = builder.like(root.get("lastname"), "%" + searchTerm + "%");
                predicates[2] = builder.like(root.get("email"), "%" + searchTerm + "%");
                predicates[3] = builder.like(root.get("mobile"), "%" + searchTerm + "%");
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
                    case "usertype":
                        return builder.equal(root.get("usertype"), Integer.parseInt(value));
                    case "createby":
                    case "updateby":
                    case "firstname":
                    case "lastname":
                    case "email":
                    case "mobile":
                        return builder.like(root.get(key), "%" + value + "%");
                    case "role":
                        return builder.equal(root.get("role"), RoleEnum.valueOf(value));
                    case "createat":
                    case "updateat":
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                        try {
                            Date date = dateFormat.parse(value);
                            return builder.equal(root.get(key), date);
                        } catch (ParseException e) {
                            System.out.println("Error parsing date: " + e.getMessage());
                        }
                        break;
                    default:
                        break;
                }
                return null; 
            });
        }
        return specification;
    }

    
    public Users addNewUsers(Users users) {

        Optional<Users> usersByEmail = usersRepository.findUsersByEmail(users.getEmail());
        if (usersByEmail.isPresent()) {
            throw new CustomeException("email_NO_AVAILABLE", null);
        }

        Optional<Users> usersByMobile = usersRepository.findUsersByMobile(users.getMobile());
        if (usersByMobile.isPresent()) {
            throw new CustomeException("mobile_NO_AVAILABLE", null);
        }
        
        return usersRepository.save(users);
    }

    public void deleteUsers(String id) {

        usersRepository.deleteById(id);
    }

    @Transactional
    public Users updateUsers(String id, Users users) {
        Users check = usersRepository.findById(id).orElseThrow(() -> new IllegalStateException("NOT_FOUND"));

        if (users.getUsertype() != null && !Objects.equals(check.getUsertype(), users.getUsertype())) {
            check.setUsertype(users.getUsertype());
        }

        if (users.getLastname() != null && users.getLastname().length() > 0
                && !Objects.equals(check.getLastname(), users.getLastname())) {

            check.setLastname(users.getLastname());
        }

        
        if (users.getFirstname() != null && users.getFirstname().length() > 0
                && !Objects.equals(check.getFirstname(), users.getFirstname())) {

            check.setFirstname(users.getFirstname());
        }

       

        if (users.getMobile() != null && users.getMobile().length() > 0
                && !Objects.equals(check.getMobile(), users.getMobile())) {

            Optional<Users> checkUsers = usersRepository.findUsersByMobile(users.getMobile());
            if (checkUsers.isPresent()) {
                throw new IllegalStateException("Mobile_NOT_AVAILABLE");
            }

            check.setMobile(users.getMobile());
        }

      

        if (users.getCreateby() != null && users.getCreateby().length() > 0
                && !Objects.equals(check.getCreateby(), users.getCreateby())) {

            check.setCreateby(users.getCreateby());
        }

        if (users.getCreateat() != null && !Objects.equals(check.getCreateat(), users.getCreateat())) {
            check.setCreateat(users.getCreateat());
        }

        if (users.getUpdateby() != null && users.getUpdateby().length() > 0
                && !Objects.equals(check.getUpdateby(), users.getUpdateby())) {

            check.setUpdateby(users.getUpdateby());
        }

        if (users.getUpdateat() != null && !Objects.equals(check.getUpdateat(), users.getUpdateat())) {
            check.setUpdateat(users.getUpdateat());
        }
        return usersRepository.save(check);

    }

    public Users getbyId(String id) {
       
        return usersRepository.findById(id).orElseThrow(() -> new IllegalStateException("NOT_FOUND"));
    }

    
}
