package net.wallet.main.wallet.config;

//import org.springframework.boot.CommandLineRunner;
import net.wallet.main.wallet.users.UsersRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
public class ApplicationConfig {
    

    private final UsersRepository userRepository;
    private final EntityManager entityManager;
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findUsersByEmail(username)
        .orElseThrow(()-> new UsernameNotFoundException("User not found"));

    }

     

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();

    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl();
    }
    @Bean
public MultipartResolver multipartResolver() {
    return new StandardServletMultipartResolver();
}
/*
@Bean
    public StudentService studentService(){
        System.out.println("****StudentService***");
        return new StudentService(studentRepository,entityManager);
    }
  
    @Bean 
    public FilesStorageService  FileStorageImplementation() {
        return new FilesStorageServiceImpl();
    }
*/
}
