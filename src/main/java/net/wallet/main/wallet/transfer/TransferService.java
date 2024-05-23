package net.wallet.main.wallet.transfer;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

import net.wallet.main.wallet.topup.Topup;
import net.wallet.main.wallet.topup.TopupService;
import net.wallet.main.wallet.topup.topup_enum.TopUpStatus;
import net.wallet.main.wallet.transaction.Transaction;
import net.wallet.main.wallet.transaction.TransactionService;
import net.wallet.main.wallet.transaction.transaction_enum.TransactionType;
import net.wallet.main.wallet.transfer.transfer_enum.TransferStatus;
import net.wallet.main.wallet.transfer.transfer_enum.TransferType;
import net.wallet.main.wallet.users.Users;
import net.wallet.main.wallet.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;

    public TransferService(TransferRepository transferRepository) {
        this.transferRepository = transferRepository;

    }

    @Autowired
    TransactionService transactionService;
    @Autowired
    UsersService usersService;
    @Autowired
    TopupService topupService;

    public Page<Transfer> getQueryEntities(Map<String, String> params) {
        Specification<Transfer> specification = Specification.where(null);
        Sort sort = Sort.unsorted();
        PageRequest pageRequest = PageRequest.of(0, 10);

        specification = getTransferQuerySearch(params, specification);

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

        return transferRepository.findAll(specification, pageRequest);
    }

    private Specification<Transfer> getTransferQuerySearch(Map<String, String> params, Specification<Transfer> specification) {
        // Search by any field (optional)
        String searchTerm = params.get("search");
        if (searchTerm != null) {
            specification = specification.and((root, query, builder) -> {
                Predicate[] predicates = new Predicate[7]; 
                predicates[0] = builder.like(root.get("accountfrom"), "%" + searchTerm + "%");
                predicates[1] = builder.like(root.get("accountto"), "%" + searchTerm + "%");
                predicates[2] = builder.like(root.get("transferStatus").as(String.class), "%" + searchTerm + "%"); 
                predicates[3] = builder.like(root.get("transfertype").as(String.class), "%" + searchTerm + "%"); 
                predicates[4] = builder.like(root.get("notes"), "%" + searchTerm + "%");
                predicates[5] = builder.like(root.get("otp"), "%" + searchTerm + "%");
                predicates[6] = builder.like(root.get("otpexpires").as(String.class), "%" + searchTerm + "%"); // Convert LocalDateTime to string for like comparison
                return builder.or(predicates);
            });
        }
    
        // Filter by specific fields (optional)
        for (Map.Entry<String, String> entry : params.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();
            specification = specification.and((root, query, builder) -> {
                switch (key) {
                    case "accountfrom":
                    case "accountto":
                    case "notes":
                    case "otp":
                        return builder.like(root.get(key), "%" + value + "%");
                    case "amount":
                        return builder.equal(root.get(key), Double.parseDouble(value));
                    case "transferStatus":
                    case "transfertype":
                        return builder.equal(root.get(key), Enum.valueOf(TransferStatus.class, value)); // Assuming TransferStatus and TransferType are enums
                    case "otpexpires":
                        LocalDateTime date = LocalDateTime.parse(value); // Assuming the date string is in ISO-8601 format
                        return builder.equal(root.get(key), date);
                    default:
                        break;
                }
                return null;
            });
        }
        return specification;
    }
    
    public Object newTransfer(Transfer transfer) {
        transfer.setTransfertype(transfer.getTransfertype());
        // Generate random six-digit string for OTP
        String otp = generateRandomOTP();
        transfer.setOtp(otp);
        // Set OTP expires 5 minutes from now
        LocalDateTime otpExpires = LocalDateTime.now().plusMinutes(10);
        transfer.setOtpexpires(otpExpires);
        transfer.setTransferStatus(TransferStatus.PENDING);
        
        //TOP UP Transfer
        if(transfer.getTransfertype()== TransferType.TOP_UP){
            String topUpCreate = topUpCreate(transfer);
             Transfer save = transferRepository.save(transfer);
            return Map.of(
                "topUpId", topUpCreate,
                "id", save.getId(),
                "otp", save.getOtp()
            );
        }
        //General Transfer
        else{
           return transferRepository.save(transfer);
        }
        
    }

    private String  topUpCreate(Transfer transfer) {
       Topup topup = new Topup();
       topup.setMethod("Transfer");
       topup.setAmount(transfer.getAmount());
       topup.setAccountid(transfer.getAccountfrom());
       topup.setNotes(transfer.getNotes());
       topup.setTopUpStatus(TopUpStatus.PENDING);
       topupService.addNewTopup(topup);
       return topup.getId();
    }

    private String generateRandomOTP() {
        Random random = new Random();
        int otp = random.nextInt(900000) + 100000;
        return String.valueOf(otp);
    }

    public boolean verifySendOtp(String id, String otp,String topUpId) {

        Transfer transfer = transferRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transfer not found"));

        if (!transfer.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (LocalDateTime.now().isAfter(transfer.getOtpexpires())) {
            throw new RuntimeException("OTP has expired");
        }
      
        if(transfer.getTransfertype()==TransferType.TOP_UP){
             topUpVerify(transfer,topUpId);
        }else{
            setSenderTransaction(transfer);
            setReceiverTransaction(transfer);
        }
        

       

        transfer.setTransferStatus(TransferStatus.DONE);
        updateTransfer(id, transfer);

        return true;
    }

   
    private void topUpVerify(Transfer transfer, String topUpId) {
       
        Users sendFrom= usersService.getbyId(transfer.getAccountfrom());
       sendFrom.setWalletbalance(sendFrom.getWalletbalance()+transfer.getAmount());
       usersService.updateUsers(sendFrom.getId(), sendFrom);
      

       //Trans
        Transaction senderTransaction = new Transaction();
        senderTransaction.setParticular(transfer.getAmount()+" amount has been TOP UP");
        senderTransaction.setTransactionType(TransactionType.TOP_UP);
        senderTransaction.setAccountid(transfer.getAccountfrom()); 
        senderTransaction.setBalance(sendFrom.getWalletbalance()); 
        senderTransaction.setAmount(transfer.getAmount());
        Transaction newTransaction = transactionService.addNewTransaction(senderTransaction);
         //TopUp
         Topup topup = topupService.getbyId(topUpId);
         topup.setTopUpStatus(TopUpStatus.DONE);
         topup.setTransactionid(newTransaction.getId());
         topupService.updateTopup(topUpId, topup);
       
    }

    private void setSenderTransaction(Transfer transfer) {
        // Create a new Transaction and Balnce Update for the sendFrom

       Users sendFrom= usersService.getbyId(transfer.getAccountfrom());
       sendFrom.setWalletbalance(sendFrom.getWalletbalance()-transfer.getAmount());
       usersService.updateUsers(sendFrom.getId(), sendFrom);

        Transaction senderTransaction = new Transaction();
        senderTransaction.setParticular(transfer.getAmount()+" amount has been transferred");
        senderTransaction.setTransactionType(TransactionType.SEND);
        senderTransaction.setAccountid(transfer.getAccountfrom()); 
        senderTransaction.setBalance(sendFrom.getWalletbalance()); 
        senderTransaction.setAmount(transfer.getAmount());
        transactionService.addNewTransaction(senderTransaction);
    }
    
    private void setReceiverTransaction(Transfer transfer) {
        // Create a new Transaction and Balnce Update for the receiveTo

        Users receiveTo= usersService.getbyId(transfer.getAccountto());
        receiveTo.setWalletbalance(receiveTo.getWalletbalance()+transfer.getAmount());
        usersService.updateUsers(receiveTo.getId(), receiveTo);

        Transaction receiverTransaction = new Transaction();
        receiverTransaction.setParticular(transfer.getAmount()+" amount has been received");
        receiverTransaction.setTransactionType(TransactionType.RECEIVE);
        receiverTransaction.setAccountid(transfer.getAccountto()); 
        receiverTransaction.setBalance(receiveTo.getWalletbalance());
        receiverTransaction.setAmount(transfer.getAmount());
        transactionService.addNewTransaction(receiverTransaction);
    }

    
    // public Transfer popUpTransfer(Map<String, Object> payload) {
    //     Transfer transfer = new Transfer();
    //     transfer.setTransfertype(TransferType.TOP_UP);
    //     transfer.setAccountfrom((String) payload.get("accountfrom"));
    //     transfer.setAccountto((String) payload.get("accountto"));
    //     transfer.setAmount((Double) payload.get("amount"));
    //     transfer.setNotes((String) payload.get("notes"));
    //     transfer.setStatus("DONE");
    //     return transferRepository.save(transfer);
    // }

    public void deleteTransfer(String id) {

        transferRepository.deleteById(id);
    }


    @Transactional
    public Transfer updateTransfer(String id, Transfer transfer) {
        Transfer check = transferRepository.findById(id).orElseThrow(() -> new IllegalStateException("NOT_FOUND"));

        if (transfer.getCreateby() != null && transfer.getCreateby().length() > 0
                && !Objects.equals(check.getCreateby(), transfer.getCreateby())) {

            check.setCreateby(transfer.getCreateby());
        }

        if (transfer.getCreateat() != null && !Objects.equals(check.getCreateat(), transfer.getCreateat())) {
            check.setCreateat(transfer.getCreateat());
        }

        if (transfer.getUpdateby() != null && transfer.getUpdateby().length() > 0
                && !Objects.equals(check.getUpdateby(), transfer.getUpdateby())) {

            check.setUpdateby(transfer.getUpdateby());
        }

        if (transfer.getUpdateat() != null && !Objects.equals(check.getUpdateat(), transfer.getUpdateat())) {
            check.setUpdateat(transfer.getUpdateat());
        }

        if (transfer.getAccountfrom() != null && transfer.getAccountfrom().length() > 0
                && !Objects.equals(check.getAccountfrom(), transfer.getAccountfrom())) {

            check.setAccountfrom(transfer.getAccountfrom());
        }

        if (transfer.getAccountto() != null && transfer.getAccountto().length() > 0
                && !Objects.equals(check.getAccountto(), transfer.getAccountto())) {

            check.setAccountto(transfer.getAccountto());
        }

        if (transfer.getAmount() != null && !Objects.equals(check.getAmount(), transfer.getAmount())) {
            check.setAmount(transfer.getAmount());
        }

        if (transfer.getTransferStatus() != null ) {

            check.setTransferStatus(transfer.getTransferStatus());
        }

        if (transfer.getTransfertype() != null ){

            check.setTransfertype(transfer.getTransfertype());
        }

        if (transfer.getNotes() != null && transfer.getNotes().length() > 0
                && !Objects.equals(check.getNotes(), transfer.getNotes())) {

            check.setNotes(transfer.getNotes());
        }

        if (transfer.getOtp() != null && transfer.getOtp().length() > 0
                && !Objects.equals(check.getOtp(), transfer.getOtp())) {

            check.setOtp(transfer.getOtp());
        }

        if (transfer.getOtpexpires() != null && !Objects.equals(check.getOtpexpires(), transfer.getOtpexpires())) {
            check.setOtpexpires(transfer.getOtpexpires());
        }
        return transferRepository.save(check);

    }

    public Transfer getbyId(String id) {
        return transferRepository.findById(id).orElseThrow(() -> new IllegalStateException("NOT_FOUND"));
    }

}
