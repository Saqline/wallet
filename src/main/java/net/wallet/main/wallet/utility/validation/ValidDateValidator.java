package net.wallet.main.wallet.utility.validation;

import java.time.LocalDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDateValidator implements ConstraintValidator<ValidDate, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
       
        if (value == null || value.isEmpty()) {
            return true;  // Null values are handled by @NotNull or @NotBlank
        }
        try{
             System.out.println("***** I SHOULD PRINT ******");
            LocalDate dt = LocalDate.parse(value);
            return true; 
        }catch(Exception e){
            System.out.println("***** I SHOULD FAILED ******");
            return false; 
        }
       
    }
}
