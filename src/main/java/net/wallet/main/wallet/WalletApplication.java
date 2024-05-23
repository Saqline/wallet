
package net.wallet.main.wallet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//import io.github.cdimascio.dotenv.Dotenv;
@SpringBootApplication
public class WalletApplication {

	public static void main(String[] args) {
		//Dotenv dotenv = Dotenv.configure().load();
		SpringApplication.run(WalletApplication.class, args);
	}

}



