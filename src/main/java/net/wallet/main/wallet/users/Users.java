package net.wallet.main.wallet.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import net.wallet.main.wallet.config.Auditable;
import net.wallet.main.wallet.roleEnum.RoleEnum;
import net.wallet.main.wallet.utility.interfaces.IDataAdd;
import net.wallet.main.wallet.utility.interfaces.IDataUpdate;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users extends Auditable implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull(message = "usertype required")
    private Integer usertype;

    private String firstname;
    @NotBlank(message = "lastname required")
    @Size(min = 1, max = 50, message = "lastname length error: should be between 1 and 50 chars")
    private String lastname;

    @NotBlank(message = "password required")
    //@Size(min = 6, max = 20, message = "password length error: should be between 6 and 20 chars")
    private String password;

    @NotBlank(message = "email required")
    @Email(groups = { IDataAdd.class, IDataUpdate.class }, message = "email should be valid email address")
    private String email;


    @NotBlank(message = "mobile required")
    @Size(min = 8, max = 20, message = "mobile length error: should be between 8 and 20 chars")
    private String mobile;

    private String address;

    private String city;

    private String state;

    private String postcode;

    private String selfyphoto;

    private String country;

    private Double walletbalance;

    private String walletbalanceencrypted;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;


    private Long emailOtpExpire ;

    private String emailOtpCode ;

    private Boolean emailVerified = false;

    private Long forgotPassOtpExpire ;

    private String forgotPassOtpCode ;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority( role.name() ));
        //throw new UnsupportedOperationException("Unimplemented method 'getAuthorities'");

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
