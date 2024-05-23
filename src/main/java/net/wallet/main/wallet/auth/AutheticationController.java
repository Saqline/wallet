package net.wallet.main.wallet.auth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path="auth")
@RequiredArgsConstructor
public class AutheticationController {

    private final AutheticationService service;

    @PostMapping("/reg")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request) {
        System.out.println(request.getEmail());

        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/verifyOtpSend")
    public void verifyEmailotp(@RequestParam String email) {
        service.createGmailVerifyOTP(email);
    }

    @PostMapping("/verifyEmail")
    public void verifyEmail(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");
        System.out.println("come otp");
        service.verifyEmail(email, otp);
    }

    @PostMapping("/forgotPasswordOtpSend")
    public void setForgotPasswordOTP(@RequestParam String email) {
        service.setForgotPasswordOTP(email);
    }

    @PostMapping("/resetPassword")
    public void resetPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String otp = payload.get("otp");
        String newPassword = payload.get("newPassword");
        service.resetPassword(email, otp, newPassword);
    }

}
