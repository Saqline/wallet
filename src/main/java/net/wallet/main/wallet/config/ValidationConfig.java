package net.wallet.main.wallet.config;

import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
//import net.excode.alifpay.alifpay.validation.ValidDateValidator;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = jakarta.validation.Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        System.out.println(validator.getClass());
        System.out.println("***********************VALIDATOR*********************");
        return validator;
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setProviderClass(org.hibernate.validator.HibernateValidator.class);
        System.out.println("***********************LocalValidatorFactoryBean*********************");
        return bean;
    }
}
