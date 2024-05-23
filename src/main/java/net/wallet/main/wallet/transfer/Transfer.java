package net.wallet.main.wallet.transfer;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT
import lombok.Setter;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import net.wallet.main.wallet.config.Auditable;
import net.wallet.main.wallet.transfer.transfer_enum.TransferStatus;
import net.wallet.main.wallet.transfer.transfer_enum.TransferType;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;
import net.wallet.main.wallet.utility.interfaces.IDataSearch;
import net.wallet.main.wallet.utility.interfaces.IDataUpdate;

@Entity
@Table(name = "transfer")
@NoArgsConstructor
@Getter
@Setter
public class Transfer extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(groups = { IDataAdd.class }, message = "accountfrom required")
    private String accountfrom;

    @NotBlank(groups = { IDataAdd.class }, message = "accountto required")
    private String accountto;

    @NotNull(groups = { IDataAdd.class }, message = "amount required")
    private Double amount;

    @NotBlank(groups = { IDataAdd.class }, message = "status required")
    @Enumerated(EnumType.STRING)
    private TransferStatus transferStatus;

    @NotBlank(groups = { IDataAdd.class }, message = "transfertype required")
     @Enumerated(EnumType.STRING)
    private TransferType transfertype;

    @NotBlank(groups = { IDataAdd.class }, message = "notes required")
    private String notes;

    private String otp;

    @FutureOrPresent(groups = { IDataAdd.class, IDataUpdate.class,
            IDataSearch.class }, message = "otpexpires is not valid date")
    private LocalDateTime otpexpires;

}
