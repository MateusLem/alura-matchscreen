package br.com.alura.matchscreen;

import br.com.alura.matchscreen.model.DadosSerie;
import br.com.alura.matchscreen.service.ConsumoAPI;
import br.com.alura.matchscreen.service.ConverteDados;
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
		var consumoAPI = new ConsumoAPI();
		var serie = "Gilmore Girls";

		String apikey = "a24a2dc4";
		String address ="https://www.omdbapi.com/?t="+
				serie.replace(" ", "+").toLowerCase()+
				"&apikey="+apikey;

		var json = consumoAPI.obterDados(address);
 		System.out.println("\n"+json);
//		json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
//		System.out.println("\n"+json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println("\n"+dados);

	}
}
