package net.wallet.main.wallet.utility.exception;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class GlobalExceptionHandler{
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomeException.class)
    public Map<String, String>  customeException(CustomeException ex){
    //public ResponseEntity<String>  customeException(CustomeException ex, WebRequest request,HttpServletRequest httpRequest) {
      Map<String, String> errors = new HashMap<>();
         errors.put("errors", ex.getMessage());
       return errors;
        
       // return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
/*
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String>  IllegalStateException(CustomeException ex, WebRequest request,HttpServletRequest httpRequest) {
        //Map<String, String> errors = new HashMap<>();
         //errors.put("errors", ex.getMessage());
       //return errors;
        System.out.println("WE ARE HERE ");
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public Map<String, String> handleInvalidArgument2(IllegalStateException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("errors", ex.getMessage());
        // ex.getBindingResult().getFieldErrors().forEach(error -> {
           // errorMap.put(error.getField(), error.getDefaultMessage());
       //});
        return errorMap;
    }
}
