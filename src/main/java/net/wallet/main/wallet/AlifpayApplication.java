
package net.wallet.main.wallet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
@ComponentScan("net.excode.alifpay.alifpay")
public class AlifpayApplication {

	public static void main(String[] args) {
		//Dotenv dotenv = Dotenv.configure().load();
		SpringApplication.run(AlifpayApplication.class, args);
	}

}



