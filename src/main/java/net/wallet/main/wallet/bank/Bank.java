package net.wallet.main.wallet.bank;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.wallet.main.wallet.config.Auditable;

@Entity
@Getter
@Setter
public class Bank extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String name;
    private String shortName;
    private String logo;
    // getters and setters
}
