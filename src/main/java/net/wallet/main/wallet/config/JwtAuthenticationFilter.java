package net.wallet.main.wallet.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
//import lombok.var;
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
  
    //private final  UsersRepository usersRepository;
    private final  UserDetailsService userDetailsService2;
    //private final  UserDetails userDetails;
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain
        )
            throws ServletException, IOException {
       final String authHeader =request.getHeader("Authorization");
       final String jwt;
       final String userEmail;

       if(authHeader==null || !authHeader.startsWith("Bearer ")){
        filterChain.doFilter(request, response);
        return;
       }
         
       jwt=authHeader.substring(7);
       if(jwtService.isTokenExpiter(jwt)){
        
        response.sendError(405, "SESSION_EXPIRED");
        //throw new CustomeException("SESSION_EXPIRED",null);
    
       }
       userEmail = jwtService.extractUsername(jwt);
       if(userEmail!=null && SecurityContextHolder.getContext().getAuthentication()==null){
        UserDetails user = this.userDetailsService2.loadUserByUsername(userEmail);
        //Users userDetails = this.usersRepository.findUsersByEmail(userEmail).orElse(null);
        UserDetails userDetails = this.userDetailsService2.loadUserByUsername(userEmail);
        if(jwtService.isTokenValid(jwt, userDetails)){
            UsernamePasswordAuthenticationToken authToken = 
            new UsernamePasswordAuthenticationToken( userDetails, null,user.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            System.out.println(authToken.getDetails());
            System.out.println(authToken.getPrincipal());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            var auth= SecurityContextHolder.getContext().getAuthentication();
           System.out.println(auth.getName());

        }
       }else{
       var auth= SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getName());
       }
       filterChain.doFilter(request, response);
    }
    
}