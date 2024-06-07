package br.com.alura.matchscreen;

import br.com.alura.matchscreen.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchscreenApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MatchscreenApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibeMenu();
//		principal.treinoStream();
	}
}
