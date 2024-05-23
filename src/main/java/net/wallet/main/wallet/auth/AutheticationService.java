package net.wallet.main.wallet.auth;

import java.security.SecureRandom;

import net.wallet.main.wallet.email.EmailService;
import net.wallet.main.wallet.users.Users;
import net.wallet.main.wallet.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
//import lombok.var;
import net.wallet.main.wallet.config.JwtService;
import net.wallet.main.wallet.roleEnum.RoleEnum;

@Service
@RequiredArgsConstructor
public class AutheticationService {
    
    private final UsersRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
     private final  UsersRepository usersRepository;
     @Autowired
     EmailService emailService;

    public AuthenticationResponse register(RegisterRequest request) {
        var userCheck = userRepository.findUsersByEmail(request.getEmail());
        if(userCheck.isPresent()){
            throw new IllegalStateException("email_taken");
        }
        System.out.println("come");
        var user = Users.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .mobile(request.getMobile())
        .usertype(1)
        .walletbalance(1000.00)
        .role(RoleEnum.User)
        .build();
        userRepository.save(user);
       var users =this.usersRepository.findUsersByEmail(request.getEmail()).orElse(null);
        var jwtToken = jwtService.generateToken(users);
        return AuthenticationResponse
        .builder()
        .accessToken(jwtToken).build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println("come");
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
        request.getEmail(),
        request.getPassword())
     );
    
       var users =this.usersRepository.findUsersByEmail(request.getEmail()).orElse(null);
     var jwtToken = jwtService.generateToken(users);
        return AuthenticationResponse
        .builder()
        .accessToken(jwtToken).build();
}

     
    public void createGmailVerifyOTP(String email) {
        System.out.println("code come ra1: "+email);
        var user = usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var otpCode = generateRandomOTP(email); // Generate a random OTP code
        var otpExpires = System.currentTimeMillis() + (5 * 60 * 1000); // Set OTP expiration time to 5 minutes from now

        user.setEmailOtpCode(otpCode);
        user.setEmailOtpExpire(otpExpires);
        usersRepository.save(user);
    }

    public void verifyEmail(String email, String otp) {
        var user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getEmailOtpExpire() < System.currentTimeMillis()) {
            throw new IllegalStateException("OTP has expired");
        }

        if (!user.getEmailOtpCode().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        user.setEmailVerified(true);
        usersRepository.save(user);
    }

    public void setForgotPasswordOTP(String email) {
        var user = usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        var otpCode = generateRandomOTP(email); // Generate a random OTP code
        var otpExpires = System.currentTimeMillis() + (5 * 60 * 1000); // Set OTP expiration time to 5 minutes from now

        user.setForgotPassOtpCode(otpCode);
        user.setForgotPassOtpExpire(otpExpires);
        usersRepository.save(user);
    }

    public void resetPassword(String email, String otp, String newPassword) {
        var user = usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getForgotPassOtpCode().equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        if (user.getForgotPassOtpExpire() < System.currentTimeMillis()) {
            throw new IllegalStateException("OTP has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }

    public void changePassword(String email, String oldPassword, String newPassword) {
        System.out.println("email se: " + email);
        var user = usersRepository.findUsersByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }

    private String generateRandomOTP(String email) {
        System.out.println("code come ra2:");
        final String CHARACTERS = "0123456789";
        final int OTP_LENGTH = 6;
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp = new StringBuilder(OTP_LENGTH);

        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }
        System.out.println("code come ra3: "+otp.toString());
        emailService.sendMail(email, "Otp Code-Time Up to 5 min",
                "Your OTP code is: " + otp.toString() + " .Please use this code for verify.");
                System.out.println("code come ra4: "+otp.toString());
        return otp.toString();
    }

}